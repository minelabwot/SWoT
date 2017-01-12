package edu.bupt.linkSystemApplication;

import java.util.ArrayList;
import java.util.List;

import edu.bupt.database.DataTransfer;
import edu.bupt.semantic.MessagePassing;
import edu.bupt.tools.DataProcess;

public enum Application {
	INSTANCE;
	//用于本地测试
	private static String elroot_local = "/Users/yangyunong/javaworkspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/sensor_annotation/WEB-INF/ELApp/";

	public static void main(String[] args) {
		//从数据库中读取数据并输出成文件格式,该路径是EL input table的路径
		DataTransfer.INSTANCE.setOutputpath(elroot_local+"inputTables/");
		List<String> tableId = DataTransfer.INSTANCE.transfer();  //从数据库中拿到待处理的表格的id,并转成本地文件
		List<String> fileId = new ArrayList<String>();
		if(null != tableId) {
			System.out.println(tableId.size());
			for(String id : tableId) {
				fileId.add("table_"+id);
			}
			for(String f : fileId) {
				Application.INSTANCE.implementLink(f,elroot_local);
			}
		}
		else 
			System.out.println("没有需要处理的新数据");
	}
	
	//执行EL算法 的入口
	public boolean runApp(String elroot) {
		DataTransfer.INSTANCE.setOutputpath(elroot+"inputTables/");
		List<String> tableId = DataTransfer.INSTANCE.transfer();  //从数据库中拿到待处理的表格的id,并转成本地文件
		List<String> fileId = new ArrayList<String>();
		if(null != tableId) {
			System.out.println(tableId.size());
			for(String id : tableId) {
				fileId.add("table_"+id);
			}
			for(String f : fileId) {
				implementLink(f,elroot);
			}
			return true;
		}
		else {
			System.out.println("没有需要处理的新数据");
			return false;
		}
	}
	//fileName用于确定当前操作的是哪个input table     elroot用于指定el相关文件的根目录
	public void implementLink(String fileName,String elroot) {
		DataProcess.INSTANCE.setPara(24, fileName,elroot);
		DataProcess.INSTANCE.queryAndRank();
		MessagePassing.INSTANCE.setProperty(fileName,elroot);
		MessagePassing.INSTANCE.doPassing();
	}
}
