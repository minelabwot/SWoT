package bupt.linkSystemApplication;

import java.util.ArrayList;
import java.util.List;

import bupt.database.DataTransfer;
import bupt.semantic.MessagePassing;
import bupt.tools.DataProcess;

public enum Application {
	INSTANCE;
	private String elroot;
	//用于本地测试
	private static String elroot_local = "";
	
	public void setElroot(String elroot) {
		this.elroot = elroot;
	}

	public static void main(String[] args) {
		//从数据库中读取数据并输出成文件格式,该路径是EL input table的路径
		DataTransfer.INSTANCE.setOutputpath(elroot_local+"tables/inputTables/");
		List<String> tableId = DataTransfer.INSTANCE.transfer();  //从数据库中拿到待处理的表格的id,并转成本地文件
		List<String> fileId = new ArrayList<String>();
		if(null != tableId) {
			System.out.println(tableId.size());
			for(String id : tableId) {
				fileId.add(elroot_local+"table_"+id);
			}
			for(String f : fileId) {
				//Application.INSTANCE.implementLink(f);
			}
		}
		else 
			System.out.println("没有需要处理的新数据");
	}
	
	//执行EL算法 的入口
	public void runApp(String elroot) {
		DataTransfer.INSTANCE.setOutputpath(elroot+"inputTables/");
		List<String> tableId = DataTransfer.INSTANCE.transfer();  //从数据库中拿到待处理的表格的id,并转成本地文件
		List<String> fileId = new ArrayList<String>();
		if(null != tableId) {
			System.out.println(tableId.size());
			for(String id : tableId) {
				fileId.add(elroot+"table_"+id);
			}
			for(String f : fileId) {
				implementLink(f);
			}
		}
		else 
			System.out.println("没有需要处理的新数据");
	}
	public void implementLink(String filePath) {
		DataProcess.INSTANCE.setPara(36, filePath);
		DataProcess.INSTANCE.queryAndRank();
		MessagePassing.INSTANCE.setFileName(filePath);
		MessagePassing.INSTANCE.doPassing();
	}
}
