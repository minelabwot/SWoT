package bupt.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ParagramCompute {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ParagramCompute p = new ParagramCompute();
		try {
			p.tongji1();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void deal() {
		SAXReader sr = new SAXReader();
		File directory = new File("fixedWebTables");
		File[] fileName = directory.listFiles();
		for(int i=300;i<fileName.length;i++) {
			System.out.println(fileName[i].toString());
			dealOneDoc(sr,fileName[i].toString().split("\\\\")[1],i);
		}
	}
	
	private void dealOneDoc(SAXReader sr,String fileName,int j) {
		Document ori = null;
		Document ann = null;
		try {
		ori = sr.read(new File("fixedWebTables/"+fileName));
		ann = sr.read(new File("fixedWebTablesAnnotations/"+fileName));
		@SuppressWarnings("unchecked")
		List<Element> origin = ori.selectNodes("//row");
		@SuppressWarnings("unchecked")
		List<Element> annotation = ann.selectNodes("//row");
		//trimList(origin);
		//trimList(annotation);
		System.out.println(origin.size()+"  "+annotation.size());
		BufferedWriter bw1 = IO.Writer("resultCompare/input/table_"+(j-300)+".txt");
		BufferedWriter bw2 = IO.Writer("resultCompare/ground/table_"+(j-300)+".txt");
		int col=getCol(annotation);
		bw1.write(origin.size()+"\t"+col);
		bw1.newLine();
		bw1.write("column");
		bw1.newLine();
		for(int i=0;i<origin.size();i++) {
			List<Element> list1 = origin.get(i).elements("cell");
			//System.out.println(list1.get(0).element("text").getTextTrim());
			List<Element> list2 = annotation.get(i).elements("entity");
			String writeline1="";
			String writeline2="";
			for(int index=0;index<list1.size();index++) {
				if (!list1.get(index).element("text").getTextTrim().equals("")) {
					String oriS = list1.get(index).element("text").getTextTrim();
					System.out.println(oriS);
					String annS = list2.get(index).getTextTrim();
					writeline1 += oriS+"\t";
					writeline2 += annS+"\t";
				}
			}
			
			if(!writeline1.trim().equals("")) {
				bw1.write(writeline1);
				bw2.write(writeline2);
				bw1.newLine();
				bw2.newLine();
			}
		}
		bw1.close();
		bw2.close();
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void trimList(List<Element> list) {
		for(Iterator<Element> it= list.iterator();it.hasNext();) {
			if( it.next().getTextTrim().equals("") )
				it.remove();
		}
	}
	private int getCol(List<Element> list) {
		int col=0;
		int i=0;
		while (col != 0) {
			for(Element e : (List<Element>)list.get(i).elements("entity")) {
				if(!e.getTextTrim().equals(""))
					col++;
				i++;
			}
		}
		return col;
	}


	//统计WEB_Manual数据里origin rank的准确率
	 public void tongji1() throws IOException {
		 int top1 = 0;
		 int top10 = 0;
		 for(int i=2501;i<2894;i++) {
			 
			 //BufferedReader br = IO.Reader("trainDataset/train/addrankscore/query"+i+".txt");
			 BufferedReader br = IO.Reader("output/ranked/huizong/ee_"+(i-2501)+"0.txt");
			 int index=1;
			 String line;
			 br.readLine();
			 while((line = br.readLine()) != null && index <= 6) {
				 if(Integer.parseInt(line.split("\t")[0]) == 5) {
					break; 
				 }
				 index++;
			 }
			 if(index == 1) {
				 top1++;
				 top10++;
			 }
			 else if(index>1 && index<6) {
				 top10++;
			 }
			 br.close();
		 }
		 System.out.println(top1);
		 System.out.println(top10);
	 }
	 
	 public void tongji2() throws IOException {
		 BufferedWriter bw = IO.Writer("tables/huizong.txt");
		 for(int i=2501;i<2894;i++) {
			 BufferedReader br = IO.Reader("trainDataset/train/addrankscore/query"+i+".txt");
			 bw.write(br.readLine());
			 bw.newLine();
			 br.close();
		 }
		 bw.close();
	}
	 
	public void tongji3() throws IOException {
		BufferedWriter bw = null;
		 for(int i=2501;i<2894;i++) {
			 BufferedReader br1 = IO.Reader("trainDataset/train/addrankscore/query"+i+".txt");
			 BufferedReader br2 = IO.Reader("output/ranked/huizong/e_"+(i-2501)+"0.txt");
			 String ss;
			 List<String> elist = new ArrayList<String>();
			 while((ss = br2.readLine()) != null ) {
				 elist.add(ss);
			 }
			 br2.close();
			 //获取groundtruth
			 String candidate = null;
			 String line;
			 br1.readLine();
			 List<String> list = new ArrayList<String>();
			 while((line = br1.readLine()) != null) {
				 if(Integer.parseInt(line.split("\t")[0]) == 5) {
					candidate = line.split("\t")[1];
					break; 
				 }
			 }
			 br1.close();
			 
			 bw = IO.Writer("output/ranked/huizong/ee_"+(i-2501)+"0.txt");
			 if(candidate ==null) {
				 for(int j=0;j<elist.size();j++) {
					elist.set(j, "1\t"+elist.get(j));
				 }
			 }
			 for(int j=0;j<elist.size();j++) {
				 if(elist.get(j).equals(candidate)) {
					 elist.set(j, "5\t"+elist.get(j));
				 }
				 else
					 elist.set(j, "1\t"+elist.get(j));
			 }
			 
			 bw.write("origin");
			 bw.newLine();
			 for(String s : elist) {
				 System.out.println(s);
				 bw.write(s);
				 bw.newLine();
			 }
			 bw.close();
		 }
	 }
	
}


