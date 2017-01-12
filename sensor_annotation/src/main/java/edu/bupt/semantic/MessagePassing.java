package edu.bupt.semantic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

import edu.bupt.database.ConnectDB;
import edu.bupt.tools.DataProcess;
import edu.bupt.tools.IO;

public enum MessagePassing {
	INSTANCE;
	public static int rows;
	public static int cols;
	Entity[][] e2Array;
	Type[] t1Array;
	Relation[][] r2Array;
	private float unmess;
	private float chmess;
	private static final float Threshold = (float)0;
	private static final int ITERATOR = 5;
	private static final int ThreadNum_MESSAGECHANGE = 36;
	private static final int LIMIT_E = 5;
	private static final float convergence = (float)0.05;
	private String FILENAME = "ELApp/table_1";
	private String ELROOT;
	Properties dictionary = new Properties();
	FileInputStream fis1;
	Set<String> drop = new HashSet<String>();
	public long started ;
	public int[] times = new int[5];
	public static int queryCount = 0;
	public static void main(String args[]) {
		//INSTANCE.started = System.currentTimeMillis();
		MessagePassing.INSTANCE.doPassing();
		//long end = System.currentTimeMillis();
		//long runtime = (end-NSTANCE.started)/1000;
		//System.out.println("程序执行了"+runtime+"s");
	}
	
	public void setProperty(String fileName,String elroot) {
		this.FILENAME = fileName;
		this.ELROOT = elroot;
	}
	
	private MessagePassing() {
	}

	private void init_on() {
		try {
			fis1 = new FileInputStream(ELROOT+"dictionary/type_gran.properties");
			dictionary.load(fis1);
			BufferedReader br = IO.Reader(ELROOT+"dictionary/drop.dic");
			String line;
			while( (line=br.readLine()) != null) {
				drop.add(line);
			}
			br.close();
		}  catch (IOException e) {
			e.printStackTrace();
		}
	
		BufferedReader br = IO.Reader(ELROOT+"inputTables/"+FILENAME+".txt");
		// 读取行数和列数
		try {
			String line;
			if ((line = br.readLine()) != null) {
				String[] s = line.split("\t");
				rows = Integer.parseInt(s[0]);
				cols = Integer.parseInt(s[1]);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		e2Array = new Entity[rows][cols];
		t1Array = new Type[cols];
		r2Array = new Relation[cols][cols];
		// 读取每行每列的实体和其候选type
		for (int j = 0; j < cols; j++) {
			t1Array[j] = new Type();
			t1Array[j].currentValues = "init";
			for (int i = 0; i < rows; i++) {
				System.out.println(i+" "+j);
				e2Array[i][j] = new Entity();
				getE(i,j,LIMIT_E);
				//searchE(table[i][j], columnName[j],i, j, LIMIT_E);
				e2Array[i][j].iterator = e2Array[i][j].Candidate.iterator();
				if(e2Array[i][j].iterator.hasNext())
					e2Array[i][j].currentValues = e2Array[i][j].iterator.next();
				else {
					e2Array[i][j].currentValues = "no-annotation";
				}
			}
			for(int i=j+1;i<cols;i++) {
				r2Array[j][i] = new Relation();
				r2Array[j][i].currentValues="init";
			}
		}
	}
	
	private void getE(int i,int j,int limit){
		BufferedReader br = IO.Reader(ELROOT+"output/ranked/"+FILENAME+"/e_"+i+j+".txt");
		String line;
		int k=0;
		try {
			while((line=br.readLine()) != null && k<limit) {
				e2Array[i][j].Candidate.add(line);
				k++;
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void doPassing() {
		init_on();
		started = System.currentTimeMillis();
		for (int a = 0; a < ITERATOR; a++) {
			chmess = 0;
			unmess = 0;
			//annotating
			//started = System.currentTimeMillis();
			ExecutorService fixedThreadPool = Executors.newFixedThreadPool(cols);
			for (int j = 0; j < cols; j++) {
				DoPass rd = new DoPass(j);
				fixedThreadPool.execute(rd);
			}
			//等待上一步操作完成
			waitToDone(fixedThreadPool);
			
			System.out.println("mark");
			//message change
			fixedThreadPool = Executors.newFixedThreadPool(ThreadNum_MESSAGECHANGE);
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					MessageChange ms = new MessageChange(i, j);
					fixedThreadPool.execute(ms);
				}
			}
			//等待线程执行完毕
			waitToDone(fixedThreadPool);
			
			System.out.println("--------------------第"+a+"次循环-------------------------");
			System.out.println("不变消息:"+unmess+"\t"+"改变消息:"+chmess);
			long end = System.currentTimeMillis();
			long runtime = (end-started)/1000;
			System.out.println("程序执行了"+runtime+"s");
			times[a] = (int)runtime;
			displayAnnotatedTable();
			if (chmess / (chmess + unmess) < convergence) {
				System.out.println("低于门限");
				break;
			}
		}
		
		try {
			updateURL();
			System.out.println(queryCount);
			if(fis1 != null)
				fis1.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void updateURL() {
		Connection con = ConnectDB.getConnection();
		String[] column = {"sensorType","property","unit","company","region"};
		try {
			con.setAutoCommit(false);
			Statement statement = con.createStatement();
			//上传列标注
			String sql = "insert column_type (table_id,sensorType,property,unit,company,region) values ("+FILENAME.split("_")[1]+",";
			for(int j=0;j<cols;++j) {
				if(j!=cols-1)
					sql += "\""+t1Array[j].currentValues+"\",";
				else
					sql += "\""+t1Array[j].currentValues+"\") ";
				
			}
			System.out.println(sql);
			statement.addBatch(sql);
			//上传cell text标注
			for(int i=0;i<rows;++i) {
				for(int j=0;j<cols;++j) {
					//将目标url放在candidate第一个
					if(e2Array[i][j].currentValues.equals("no-annotation")) {
						String sql1 = "update datatransform set "+column[j]+"_url=\"\" where device_id="
								+DataProcess.INSTANCE.table[i][5];
						System.out.println(sql1);
						statement.addBatch(sql1);
						continue;
					}
					List<String> candidate = e2Array[i][j].Candidate;
					String temp = candidate.get(0);
					int index;
					for(index=0;index<candidate.size();++index) {
						if(candidate.get(index).equals(e2Array[i][j].currentValues))
							break;
					}
//					System.out.println("候选值为"+e2Array[i][j].currentValues);
//					for(String s : candidate) {
//						System.out.println(s);
//					}
					candidate.set(0, candidate.get(index));
					candidate.set(index,temp);
					String url = "";
					for (int k=0;k<candidate.size();++k) {
						if(k == candidate.size()-1)
							url += candidate.get(k);
						else
							url += candidate.get(k)+",";
					}
					//写入数据库的数据库语句
					String sql1 = "update datatransform set "+column[j]+"_url=\""+url+
							"\" where device_id="+DataProcess.INSTANCE.table[i][5];
					System.out.println(sql1);
					statement.addBatch(sql1);
				}
				statement.addBatch("update datatransform set status=1 where table_id="+
				FILENAME.split("_")[1]);
			}
			statement.executeBatch();
			con.commit();
			con.setAutoCommit(true);
			statement.close();
			con.close();
		} catch (SQLException e) {
			System.out.println("上传失败,回滚");
			e.printStackTrace();
			if(con != null) {
				try {
					con.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					System.out.println("回滚失败");
					e1.printStackTrace();
				}
			}
		}
		
	}
	
	public int searchGranularity(String type) {
		String key = type.split("//")[1];
		if(dictionary.containsKey(key)) {
			System.out.println("词典中有"+key);  //shuchu_是否在细粒度词典中已经有了
			return Integer.parseInt(dictionary.getProperty(key).toString());
		}
		else {
			String queryString = "SELECT DISTINCT count(?s) as ?num " + 
								"WHERE { ?s rdf:type <" + type + ">. } ";
			QueryEngineHTTP qe = new QueryEngineHTTP("http://dbpedia.org/sparql", queryString);
			ResultSet results = qe.execSelect();
			String result = results.next().get("num").toString();
			String s = result.split("\\^")[0];
			System.out.println("个数:"+s);
			//int index = result.indexOf("http");
			//String s = result.substring(0, index - 2);
			//System.out.println(s); //shuchu2
			BufferedWriter bw = IO.Writer(ELROOT+"dictionary/type_gran.properties",true);
			try {
				bw.write(type.split("//")[1] + " = " + s);
				bw.newLine();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			qe.close();
			return Integer.parseInt(s);
		}
	}

	public void displayAnnotatedTable() {
		for (int j = 0; j < cols; j++) {
			System.out.println("第" + (j + 1) + "列的类型为:");
			System.out.println(t1Array[j].currentValues);
			for (int i = 0; i < rows; i++) {
				System.out.println("第" + (i + 1) + "行的实体为:");
				System.out.println(e2Array[i][j].currentValues);
			}
			for (int i = j + 1; i < cols; i++) {
				if (r2Array[j][i].currentValues != null) {
					System.out.println("-------------------------------------------------------");
					System.out.println("第" + (j + 1) + "列与第" + (i + 1) + "列的关系为:");
					System.out.println(r2Array[j][i].currentValues);
					System.out.println("-------------------------------------------------------");
				}
			}
		}
		for(int i=0;i<5;i++) {
			System.out.println("第"+i+"次循环执行时间:"+times[i]);
		}
	}
	
	private boolean hasDropType(String type) {
		if(drop.contains(type))
			return true;
		else
			return false;
	}
	
	private void waitToDone(ExecutorService es) {
		//等待上一步操作完成
		es.shutdown();
		while(true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(es.isTerminated())
				break;
		}
	}
	
	//开线程
	class DoPass implements Runnable {
		private int colNum;
		
		public DoPass(int col) {
			// TODO Auto-generated constructor stub
			this.colNum = col;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			ExecutorService fixedThreadPool = Executors.newFixedThreadPool(cols+1);
			AnotateColumn rCol = new AnotateColumn(colNum);
			fixedThreadPool.execute(rCol);
			for (int i = (colNum + 1); i < cols; i++) {
				AnotateRel rRel	;
				if (r2Array[colNum][i] != null) {
					rRel  = new AnotateRel(colNum,i);
					fixedThreadPool.execute(rRel);
				}
			}
			//等待线程执行
			waitToDone(fixedThreadPool);
		}
	}
	//开线程,threadnum
	class AnotateColumn implements Runnable {
		private static final int ThreadNum = 3;
		private int col;
		
		public AnotateColumn(int col) {
			// TODO Auto-generated constructor stub
			this.col = col;
		}

		public void run() {
			anotateColumn_on(col, Threshold);
		}

		// fai3,algorithm2,开线程计算
		private void anotateColumn_on(int j, float threshold) {
			// 统计type出现次数
			System.out.println("运行到annoCol的第"+j+"列");
			Map<String, Integer> KBColumnType = new ConcurrentHashMap<String, Integer>();
			ExecutorService   fixedThreadPool = Executors.newFixedThreadPool(ThreadNum);
			
			for(int i=0;i<rows;i++) {
//				if(!e2Array[i][j].currentValues.equals("no-annotation")) {
//					List<String> l = searchT(e2Array[i][j].currentValues,i,j,LIMIT_T,KBColumnType);
//					System.out.println("---------"+e2Array[i][j].currentValues+"----------");
//					for(String s : l) {
//						System.out.println(s+" ");
//					}
//					e2Array[i][j].Emap.put(e2Array[i][j].currentValues,l);
//				}
				MyThread_anotateCol r = new MyThread_anotateCol(j,KBColumnType,i);
				fixedThreadPool.execute(r);
			}
			waitToDone(fixedThreadPool);
			
			// sort
			int maxVotes = 0;
			String topType = null;
			Set<Entry<String, Integer>> entry = KBColumnType.entrySet(); 
			//System.out.println(entry.size());
			Iterator<Entry<String, Integer>> it = entry.iterator();
			Entry<String, Integer> en;
			while (it.hasNext()) {
				en = it.next();
				if( hasDropType(en.getKey()) ) {
					it.remove();
				}
				else if (en.getValue() > maxVotes) {
					maxVotes = en.getValue();
					// topType = en.getKey();
				} else if (en.getValue() < maxVotes) {
					it.remove();
				}
			}
			//过滤掉所有不满足maxVotes的候选
			it = entry.iterator();
			
			float topScore = ((float) maxVotes / rows);
			if (!it.hasNext()) {
				t1Array[j].currentValues = "no-annotation";
				for (int i = 0; i < rows; i++) {
					e2Array[i][j].getMessage(false, topScore, "LOW_CONFIDENCE","type");
				}
				return;
			}
			while(it.hasNext()) {
				en = it.next();
				if(en.getValue() != maxVotes) {
					it.remove();
				}
				else {
					System.out.println(en.getKey() + " "+en.getValue());
				}
			}
			// 细粒度排名
			it = entry.iterator();
			en = it.next();
			int gran_tmp = searchGranularity(en.getKey());//tmp中存的是当前最细粒度的type的实体数
			topType = en.getKey();
			int gran;
			while (it.hasNext()) { 
				en = it.next();
				gran = searchGranularity(en.getKey());
				if (gran <= gran_tmp) {
					topType = en.getKey();
					gran_tmp = gran;
				}
			}
			//System.out.println(topType+"  "+gran_tmp+"  "+maxVotes); //shuchu_type信息
			if (topScore <= threshold) {
				t1Array[j].currentValues = "no-annotation";
				for (int i = 0; i < rows; i++) {
					e2Array[i][j].getMessage(false, topScore, "LOW_CONFIDENCE","type");
				}
				return;
			} else {
				t1Array[j].currentValues = topType;
				System.out.println("第"+j+"列的type:"+topType);
			}
			// 反代,检查哪些实体不满足type的约束
			for (int i = 0; i < rows; i++) {
				String e = e2Array[i][j].currentValues;
				if(!e.equals("no-annotation")) {
					List<String> t = e2Array[i][j].Emap.get(e);
					// decide contains topy??
					if (t.contains(topType)) {
						e2Array[i][j].getMessage(false, topScore, topType,"type");
					}
					else {
						e2Array[i][j].getMessage(true, topScore, topType,"type");
					}
				}
			}
		}
	}
	
	//计算一个候选的type,---一次
	class MyThread_anotateCol implements Runnable {
		private int row;
		private int col;
		private Map<String,Integer> KBMap;
		
		public MyThread_anotateCol(int col, Map<String, Integer> kBMap, int row) {
			super();
			this.row = row;
			this.col = col;
			this.KBMap = kBMap;
		}

		@Override
		public void run() {
			if(!e2Array[row][col].currentValues.equals("no-annotation")) {
				List<String> l = searchT(e2Array[row][col].currentValues,row,col,KBMap);
				System.out.println("---------"+e2Array[row][col].currentValues+"----------");
				for(String s : l) {
					System.out.println(s+" ");
				}
				e2Array[row][col].Emap.put(e2Array[row][col].currentValues,l);
			}
		}
		
		private List<String> searchT(String entity,int i,int j,Map<String, Integer> map) {
			////++queryCount;
			List<String> list = new ArrayList<String>();
			String queryString = "SELECT DISTINCT ?t "+
								"WHERE {"+ 	
								"<"+entity+">" + "rdf:type ?t ."+
								"}";	
			QueryEngineHTTP qe = new QueryEngineHTTP("http://dbpedia.org/sparql", queryString);
			long start = System.currentTimeMillis();
			ResultSet results = qe.execSelect();
			System.out.println("一次http请求执行了:"+((double)(System.currentTimeMillis()-start))/1000);
			if(results.hasNext()) {
				while (results.hasNext()) {
					String type = results.next().get("t").toString();
					list.add(type);
					if(!map.containsKey(type)) {
						map.put(type,1);
					}
					else {
						map.put(type, map.get(type)+1);
					}
				}
				qe.close();
			}
			else if(!results.hasNext()) {
				//++queryCount;
				String queryString1 = "SELECT DISTINCT ?t ?e "+
						"WHERE {"+ 	
						"<"+entity+">" + " <http://dbpedia.org/ontology/wikiPageRedirects> ?e."+
						"?e rdf:type ?t ."+
						"}";	
				QueryEngineHTTP qe1 = new QueryEngineHTTP("http://dbpedia.org/sparql", queryString1);
				ResultSet results1 = qe1.execSelect();
				if(results1.hasNext()) {
					while(results1.hasNext()) {
						QuerySolution qs = results1.next();
						String type = qs.get("t").toString();
						//e2Array[i][j].currentValues = qs.get("e").toString();
						list.add(type);
						if(!map.containsKey(type)) {
							map.put(type,1);
						}
						else {
							map.put(type, map.get(type)+1);
						}
					}
				}
				else {
					list.add("http://www.w3.org/2002/07/owl#Thing");
				}
				qe1.close();
			}
			return list;
		}
	}
	
	//开线程,threadnum
	class AnotateRel implements Runnable {
		private static final int ThreadNum = 3;
		private int colpre;
		private int colback;
		
		public AnotateRel(int colpre,int colback) {
			// TODO Auto-generated constructor stub
			this.colpre = colpre;
			this.colback = colback;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			anotateRelation_on(colpre, colback, Threshold);
		}
		
		private void anotateRelation_on(int i, int j, float threshold) {//i小,j大
			Map<String, Integer> KBRelation = new ConcurrentHashMap<String, Integer>();
			Vector<String> existRel = null;
			existRel = searchR(i, j, KBRelation);//i,j列之间所有的s p o三元组
			existRel.addAll(searchR(j, i, KBRelation));
			// 排序选出highest votes的relation
			int maxVotes = 0;
			String topRel = null;
			Iterator<Entry<String, Integer>> it = KBRelation.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, Integer> en = it.next();
				if (en.getValue() > maxVotes) {
					topRel = en.getKey();
					maxVotes = en.getValue();
				}
			}
			//System.out.println(topRel);  //shuchu_Relation
			float topScore = ((float) maxVotes / rows);
			if (topScore <= threshold) {
				r2Array[i][j].currentValues = "no-annotation";
				for (int k = 0; k < rows; k++) {
					e2Array[k][i].getMessage(false, topScore, "LOW_CONFIDENCE","rel");
					e2Array[k][j].getMessage(false, topScore, "LOW_CONFIDENCE","rel");
				}
				return;
			} else {
				r2Array[i][j].currentValues = topRel;
				for (int k = 0; k < rows; k++) {
					String ei = e2Array[k][i].currentValues;
					String ej = e2Array[k][j].currentValues;
					String rel = ei + "," + topRel + "," + ej;
					if (existRel.contains(rel)) {
						e2Array[k][i].getMessage(false, topScore, topRel,"rel");
						e2Array[k][j].getMessage(false, topScore, topRel,"rel");
					} else {
						e2Array[k][i].getMessage(true, topScore, topRel,"rel");
						e2Array[k][j].getMessage(true, topScore, topRel,"rel");
					}
				}
			}
		}
		//开线程计算
		private Vector<String> searchR(int i,int j, Map<String, Integer> map) {
			//++queryCount;
			Vector<String> list = new Vector<String>();
			ExecutorService fixedThreadPool = Executors.newFixedThreadPool(ThreadNum);
			for(int k=0;k<rows;k++) {
				MyThread_searchR rs = new MyThread_searchR(k, i, j, map, list);
				fixedThreadPool.execute(rs);
			}
			//等待线程结束
			waitToDone(fixedThreadPool);
			return list;
		}
	}
	
	//计算一行内  两列之间的关系--一次
	class MyThread_searchR implements Runnable {
		
		private int k;
		private int i;
		private int j;
		private Map<String,Integer> map;
		private Vector<String> list;

		public MyThread_searchR(int k, int i, int j, Map<String, Integer> map, Vector<String> list) {
			super();
			this.k = k;
			this.i = i;
			this.j = j;
			this.map = map;
			this.list = list;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if(!e2Array[k][i].currentValues.equals("no-annotation") && !e2Array[k][j].currentValues.equals("no-annotation") ) {
				String subject = e2Array[k][i].currentValues;
				String object = e2Array[k][j].currentValues;
				String queryString = "SELECT DISTINCT ?r "+
									"WHERE {"+ 	
									"<" + subject + "> ?r <"+ object +">. }"; 
				QueryEngineHTTP qe = new QueryEngineHTTP("http://dbpedia.org/sparql", queryString);
				long start = System.currentTimeMillis();
				ResultSet results = qe.execSelect();
				System.out.println("rel:"+((double)(System.currentTimeMillis()-start))/1000);
				while (results.hasNext()) {
					String relation = results.next().get("r").toString();
					//System.out.println(subject+"  "+relation+"  "+object);  //shuchu_知识库中存在的三元组
					list.add(subject+","+relation+","+object);
					if(!map.containsKey(relation)) {
						map.put(relation,1);
					}
					else {
						map.put(relation, map.get(relation)+1);
					}	
				}
				qe.close();
			}
		}
		
	}


	//message部分
	class MessageChange implements Runnable {
		private int i;
		private int j;
		
		public MessageChange(int i, int j) {
			this.i = i;
			this.j = j;
		}
		
		private synchronized void plus(boolean state) {
			if(state)
				chmess++;
			else
				unmess++;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			//float unSum = 0, chanSum = 0, unCount = 0, chanCount = 0;
			float unMax=0, chMax=0, unCount = 0, chanCount = 0;
			if(!e2Array[i][j].currentValues.equals("no-annotation")) {
				for (Message m : e2Array[i][j].mesList) {
					System.out.println("第"+i+"行"+j+"列,"+m.type+"-"+m.score+"-"+m.top);
					/*if (m.change) {
						chanSum += m.score;
						chanCount++;
					} else {
						unSum += m.score;
						unCount++;
					} */
					
					if (m.change && m.score >= chMax) {
						chMax = m.score;
						chanCount++;
					} else if(!m.change && m.score >= unMax){
						unMax = m.score;
						unCount++;
					}//*/
					
				}
				//System.out.println("ch:"+chanSum +"_"+chanCount+"  " +"un"+ unSum+"_"+unCount);  //shuchu_置信分数
				System.out.println("ch:"+chMax +"_"+chanCount+"  " +"un"+ unMax+"_"+unCount);
				if (unCount == 0 && chanCount == 0) {
					throw new IndexOutOfBoundsException();// 应该自定义异常
				} else if (unCount == 0 && chanCount != 0) {
					if(e2Array[i][j].iterator.hasNext()) {
						//chmess++;
						plus(true);
						e2Array[i][j].currentValues = e2Array[i][j].getNext();
						//e2Array[i][j].currentValues = e2Array[i][j].iterator.next();
						//System.out.println("第"+i+"行"+j+"列的替换实体为"+e2Array[i][j].currentValues);
					}
					else {
						e2Array[i][j].currentValues = "no-annotation";
						//unmess++;
						plus(false);
					}
				} //else if (unCount != 0 && chanCount != 0 && unSum / unCount < chanSum / chanCount) {
				else if(unCount != 0 && chanCount != 0 && chMax > unMax) {	
					System.out.println("第" + i + j + "执行了");
					if(e2Array[i][j].iterator.hasNext()) {
						e2Array[i][j].currentValues = e2Array[i][j].getNext();
						//e2Array[i][j].currentValues = e2Array[i][j].iterator.next();
						//System.out.println("第"+i+"行"+j+"列的替换实体为"+e2Array[i][j].currentValues);
						//chmess++;
						plus(true);
					}
					else {
						e2Array[i][j].currentValues = "no-annotation";
						//unmess++;
						plus(false);
					}
				} 
				else {
					//unmess++;
					plus(false);
				}
			}
			else
				plus(false);
		}
	}
}