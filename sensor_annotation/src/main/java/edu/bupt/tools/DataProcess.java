package edu.bupt.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public enum DataProcess {
	INSTANCE;
	private int threadNum = 36;
	private String ELROOT;
	private String FILENAME = "huizong";
	private static int rows;
	private static int cols;
	public String[][] table = null;
	
	public static void main(String[] args)  {	
		INSTANCE.queryAndRank();
	}
	
	private DataProcess() {
	}
	
	//这里的fileName是文件的名字
	public void setPara(int num,String fileName,String elroot) {
		this.threadNum = num;
		this.FILENAME = fileName;
		this.ELROOT = elroot;
	}
	
	/**
	 * 对原始输入表格进行处理,查询其在知识库中的候选实体并用svm rank对候选实体进行排序
	 */
	public void queryAndRank() {
		File file1 = new File(ELROOT+"output/origin/" + FILENAME);
		File file2 = new File(ELROOT+"output/ranked/" + FILENAME);
		if(!file1.exists())
			file1.mkdirs();
		if(!file2.exists())
			file2.mkdirs();
		IO.clearFile(ELROOT+"trainDataset/prediction/");
		init();
	}
	
	private void init() {
		long start = System.currentTimeMillis();
		BufferedReader br = IO.Reader(ELROOT+"inputTables/"+FILENAME+".txt");
		// 读取行数和列数
		try {
			String line;
			if ((line = br.readLine()) != null) {
				String[] s = line.split("\t");
				rows = Integer.parseInt(s[0]);
				cols = Integer.parseInt(s[1]);
			}
			br.readLine();
			table = new String[rows][];
			for (int i = 0; i < rows; i++) {
				String line1 = br.readLine();
				if (line1.contains("\t")) {
					table[i] = line1.split("\t");
				}
				else {
					table[i] = new String[1];
					table[i][0] = line1;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//每一列都用一个线程去跑
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(threadNum);
		for (int j = 0; j < cols; j++) {
			for (int i = 0; i < rows; i++) {
				MyRunner r = new MyRunner(j,table,i);
				fixedThreadPool.execute(r);
			}
		}
		fixedThreadPool.shutdown();
		while(true) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(fixedThreadPool.isTerminated()) 
				break;
		}
		long end = System.currentTimeMillis();
		int time = (int)((end-start)/1000);
		System.out.println("程序运行了:"+time+"s");
	}
	
	//给每个context生成rank后的候选实体
	private void searchE(String str, int i, int j) {
		String context = str;
		if(context.toLowerCase().equals("null") || context.toLowerCase().equals("none")) {
			File file = new File(ELROOT+"output/ranked/" + FILENAME + "/e_"+i+j+".txt");
			File fileOri = new File(ELROOT+"output/origin/" + FILENAME + "/e_"+i+j+".txt");
			try {
				file.createNewFile();
				fileOri.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return;
		}
		str = str.replace(","," ");
		str = str.replaceAll(" +", " ");
		Set<String> originSet = new HashSet<String>();//转小写用于去重
		List<String> originList = new ArrayList<String>();//存放原始字符串
		Map<String,Float> feature5 = new HashMap<String,Float>();
		//查询实体
		if (str.contains(" ")) {
			String[] words = str.split(" ");
			str = "";
			for (int q = 0; q < words.length; q++) {
				if (q != words.length - 1)
					str += words[q] + "' AND '";
				else
					str += words[q];
			}
		}
		String queryString = " select DISTINCT ?s1 ( sql:rnk_scale ( <LONG::IRI_RANK> ( ?s1 ) ) ) as ?rank " + 
							"where {" + 
							"?s1 rdfs:label ?o1 ." + 
							"?o1 bif:contains \"(' "+ str + "' )\" ." +
							"Filter regex (str(?s1),\"resource\",\"ontology\") ."+
							"} "+
							"order by desc ( ?rank ) LIMIT 1200";
		QueryEngineHTTP qe = new QueryEngineHTTP("http://dbpedia.org/sparql", queryString);
		ResultSet results = qe.execSelect();
		//将候选实体添加到list中,originList2
		while (results.hasNext()) {
			QuerySolution qs = results.next();
			String s = qs.get("s1").toString();
			if(originSet.add(s.toLowerCase())) {
				originList.add(s);
				feature5.put(s, Float.parseFloat(qs.get("rank").toString().split("\\^")[0]));
			}
		}
		System.out.println("去重后的实体数量:"+originList.size());
		//生成svm rank中的test.dat文件
		List<String> querys = new ArrayList<String>();
		BufferedWriter bw = IO.Writer(ELROOT+"output/origin/" + FILENAME + "/e_"+i+j+".txt");
		try {
			for(String s : originList) {
				bw.write(s);
				bw.newLine();
				String candidate;
				String[] strings = s.split("/");
				candidate = strings[strings.length-1];
				Features f = Features.getInstance(context, candidate);
				float f1 = f.diceWord();
				float f2 = f.editDistance();
				float f3 = f.allEqual();
				float f4 = f.allcontains();
				float f5 = feature5.get(s);
				float f6 = f.stringLength();
				String query = "0 qid:1"+" 1:"+f1+" 2:"+f2+" 3:"+f3+" 4:"+f4+" 5:"+f5+" 6:"+f6;
				querys.add(query);
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("---------------general mark---------------");
		//作为rank的输入数据
		BufferedWriter bw1 =IO.Writer(ELROOT+"trainDataset/query/q_"+i+"_"+j+".txt");
		try {
			for(String q : querys) {
				bw1.write(q);
				bw1.newLine();
			}
			bw1.flush();
			bw1.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		List<String> rankedCandidate = new ArrayList<String>();
		List<Integer> rank = generatePredict(i, j);
		System.out.println("原始长度:"+originList.size()+"--------"+"ranked后:"+rank.size());
		for(int index : rank) {
			rankedCandidate.add(originList.get(index));
		}
		if(rank.size() != originList.size())
		System.out.println("---------------------------------------------------mark--------------------------------------------------------------------");
		System.out.println("输出的排序后实体数量:"+rankedCandidate.size());
		//输出排序后结果
		BufferedWriter bw2 =IO.Writer(ELROOT+"output/ranked/" + FILENAME + "/e_"+i+j+".txt");
		try {
			for(String s : rankedCandidate) {
				//System.out.println(s);
				bw2.write(s);
				bw2.newLine();
			}
			bw2.flush();
			bw2.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		qe.close();
	}
	
	//产生rank并排名
	private List<Integer> generatePredict(int i,int j) {
		File file = new File(ELROOT+"trainDataset/query/q_"+i+"_"+j+".txt");
		if( file.exists() && file.isFile() ) {
			if(file.length() != 0) {
				List<Integer> list = new ArrayList<Integer>();
				Map<Float,Integer> map = new TreeMap<Float,Integer>(Collections.reverseOrder());
				//query文件为trainDataset/query
				String[] commends = {ELROOT+"svm/svm_rank_classify",ELROOT+"trainDataset/query/q_"+i+"_"+j+".txt",
						ELROOT+"trainDataset/model.txt",ELROOT+"trainDataset/prediction/prediction"+i+j+".txt" };
				try {
					Runtime.getRuntime().exec(commends);
					File file2 = new File(ELROOT+"trainDataset/prediction/prediction"+i+j+".txt");
					BufferedReader br;
					while(true) {
						System.out.println("hahahahhaha");
						Thread.sleep(100);
						if(file2.exists()) {
							br = IO.Reader(ELROOT+"trainDataset/prediction/prediction"+i+j+".txt");
							String s = br.readLine();
							if(s != null && !"".equals(s)) {
								map.put(Float.parseFloat(s), 0);
								break;
							}
						}
					}
					System.out.println("generatePre_mark");
					String score;
					int index = 1;
					while((score=br.readLine()) != null) {
						if(!score.equals("")) {
							float sc = Float.parseFloat(score);
							while (map.containsKey(sc)) {
								sc -= (float)0.00001;
							}
							map.put(sc, index++);
						}
					}
					//list中的index就是文档中candidate的index,其值是candidate的排名
					for (float f : map.keySet()) {
						list.add(map.get(f));
					}
				} catch (IOException | InterruptedException  e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return list;
			}
			else {
				return new ArrayList<Integer>();
			}
		}
		else {
			throw new NullPointerException();
		}
		
	}
	
	//清空上次结果
	public void clearAll() {
		//rank用的feature化数据,每个query是一个context的对应
		IO.clearFile(ELROOT+"trainDataset/query");
		//生成的得分结果
		IO.clearFile(ELROOT+"trainDataset/prediction");
		//原始候选集
		IO.clearFile(ELROOT+"output/origin");
		//rank后的候选集
		IO.clearFile(ELROOT+"output/ranked");
	}
	
	private class MyRunner implements Runnable {
		private int colNum;
		private String[][] table;
		private int i;
		
		public MyRunner(int colNum, String[][] table,int i) {
			super();
			this.colNum = colNum;
			this.table = table;
			this.i = i;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
				System.out.println("第"+i+"行"+colNum+"列");
				File f = new File(ELROOT+"output/ranked/" + FILENAME+"/e_"+i+colNum+".txt");
				if(!f.exists()) 
					searchE(table[i][colNum], i, colNum);
			
		}
	}
	
}
