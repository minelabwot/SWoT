package bupt.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


public class GenerateTrainDataset  {
	BufferedWriter bw = null;
	//格式处理后的,用于DataProcess环节中的traindata
	
	public static void main(String[] args) {
		GenerateTrainDataset gtd = new GenerateTrainDataset();
		//每次执行一个
		//gtd.dealWithLimayeDataset(); //first,用originText.txt文件
		//gtd.formatHandle();//second
		gtd.generateOwnTrainSet();
	}
	
	/**
	 * 该函数用于处理Limaye的数据
	 */
	public void dealWithLimayeDataset() {
		SAXReader sr = new SAXReader();
		//每次执行该程序，清空文件中原来的内容
		bw = IO.Writer("trainDataset/train/originText.txt");
		try {
			bw.write("");
			bw.flush();
			bw.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//写入文件
		bw = IO.Writer("trainDataset/train/originText.txt", true);
		File directory = new File("fixedWebTables");
		File[] fileName = directory.listFiles();
		for(int i=0;i<fileName.length;i++) {
			System.out.println(fileName[i].toString());
			dealOneDoc(sr,fileName[i].toString().split("\\\\")[1]);
		}
		try {
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void dealOneDoc(SAXReader sr,String fileName) {
		Document ori = null;
		Document ann = null;
		try {
			ori = sr.read(new File("fixedWebTables/"+fileName));
			ann = sr.read(new File("fixedWebTablesAnnotations/"+fileName));
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		@SuppressWarnings("unchecked")
		List<Element> origin = ori.selectNodes("//text");
		@SuppressWarnings("unchecked")
		List<Element> annotaiton = ann.selectNodes("//entity");
		System.out.println(origin.size()+"  "+annotaiton.size());
		for(int i=0;i<origin.size();i++) {
			String oriS = origin.get(i).getTextTrim();
			String annS = annotaiton.get(i).getTextTrim();
			if(!"".equals(oriS) && !"".equals(annS)) {
				try {
					bw.write(oriS+"\t");
					bw.write(annS);
					bw.newLine();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 该函数用于处理我并得到我们自己的数据,需要之前调用preprocess程序处理一遍Limaye的数据格式
	 */
	public void formatHandle() {
		BufferedReader br = IO.Reader("trainDataset/train/originText.txt");
		BufferedWriter bw = IO.Writer("trainDataset/train/originText0430.txt");
		String line;
		try {
			while((line = br.readLine()) != null) {
				String[] lines = line.split("\t");
				line = lines[0];
				if(!line.contains(":") && !lines[1].equals("NULL")) {
					line = line.replace("&apos;", "'"); //替换符号实体
					line = line.replace("&nbsp;", " ");
					line = line.replace("&lt;", "<");
					line = line.replace("&amp;", "&");
					line = line.replace("&quot;", "\"");
					line = line.replace("&apos;", "'");
					line = line.replace("-", " ");//除去符号-
					line = line.replace(" .", ".");
					line = line.replace("'s", "");//去掉's
					line = line.replace(" ,", "");
					line = line.replace("!", "");
					line = line.replace("\"", "");
					line = line.replace("'", "");
					line = line.replace("&", "");
					line = line.replace("(","");
					line = line.replace(")","");
					line = line.replaceAll(" +", " ");//除去多个空格
					bw.write(line+"\t"+lines[1]);
					bw.newLine();
				}
			}
			bw.flush();
			bw.close();
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void generateOwnTrainSet() {
		BufferedReader br = IO.Reader("trainDataset/train/originText0430.txt");
		String line;
		int index = 2057;
		try {
			while((line = br.readLine()) != null ) {
				File file = new File("trainDataset/train/addrankscore/query"+index+".txt");
				if(!file.exists()) {
					line = line.replace("[", "");
					line = line.replace("]", "");
					line = line.replaceAll(" +", " ");
					line = line.replace("/ ", "");
					String[] group = line.split("\t");
					String str = group[0];
					String originText = str;
					//search candidate
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
							"?o1 bif:contains \"('"+ str + "' )\" ." +
							"Filter regex (str(?s1),\"resource\",\"ontology\") ."+
							"} "+
							"order by desc ( ?rank )";
					System.out.println(index);
					//转成GB2312编码-------
					byte[] b1 = queryString.getBytes("GB2312");
					queryString = new String(b1);
					System.out.println(queryString);
					System.out.println(IO.getEncoding(queryString));
					//--------------
					QueryEngineHTTP qe = new QueryEngineHTTP("http://dbpedia.org/sparql", queryString);
					ResultSet results = qe.execSelect();
					if(!results.hasNext()) {
						continue;
					}
					BufferedWriter bw = IO.Writer("trainDataset/train/addrankscore/query"+index+".txt");
					bw.write(originText);
					bw.newLine();
					while(results.hasNext()) {
						QuerySolution qs = results.nextSolution();
						String url = qs.get("s1").toString().trim();
						String urlRepresent = url.split("/")[url.split("/").length-1];
						System.out.println(urlRepresent + qs.get("rank").toString().split("\\^")[0]);
						if (group[1].equals(urlRepresent)) 
							bw.write("5\t"+url+"\t"+qs.get("rank").toString().split("\\^")[0]);
						else
							bw.write("1\t"+url+"\t"+qs.get("rank").toString().split("\\^")[0]);
						bw.newLine();
						
					}
					qe.close();
					bw.flush();
					bw.close();
				}
				index++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
