package bupt.database;

import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import bupt.tools.IO;

public enum DataTransfer {
	INSTANCE;
	private String outputpath; 
	private DataTransfer() {}
	
	//从数据库中读取数据并输出成文件格式,该路径是EL input table的路径
	public void setOutputpath(String outputpath) {
		this.outputpath = outputpath;
	}

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		INSTANCE.transfer();
	}
	
	public List<String> transfer() {
		List<String> tableId = new ArrayList<String>();
		Connection con = ConnectDB.getConnection();
		System.out.println("是否和远程数据库建立了链接:"+(con != null));
		Statement statement;
		try {
			statement = con.createStatement();
			ResultSet rs = statement.executeQuery("select * from datatransform where status=0");   //获取结果集
			if(!rs.next()) {
				return null;
			}
			String lastIndex = rs.getString("table_id");
			tableId.add(lastIndex);
			BufferedWriter bw = IO.Writer(outputpath+"table_"+lastIndex+".txt");
			List<String> list = new ArrayList<String>();
			String s = getOneDevice(rs);
			list.add(s);
			while(rs.next()) {
				if(rs.getString("table_id").equals(lastIndex)) {
					s = getOneDevice(rs);
					list.add(s);
				}
				else {
					try {
						bw.write(list.size()+"\t5");
						bw.newLine();
						bw.write("sensor_type\tunit\torganization\tlocation\tobservation");
						bw.newLine();
						for(String ss : list) {
							bw.write(ss);
							bw.newLine();
						}
						bw.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					list = new ArrayList<String>();
					lastIndex = rs.getString("table_id");
					tableId.add(lastIndex);
					bw = IO.Writer(outputpath+"table_"+lastIndex+".txt");
					list.add(getOneDevice(rs));
				}
			}
			bw.write(list.size()+"\t5");
			bw.newLine();
			bw.write("sensor_type\tunit\torganization\tlocation\tobservation");
			bw.newLine();
			for(String ss : list) {
				bw.write(ss);
				bw.newLine();
			}
			bw.close();
			rs.close();
			statement.close();
			if(con != null)
				con.close();
		} catch (SQLException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    //获取statement
		
         return tableId;
	}
	
	private String getOneDevice(ResultSet rs) throws SQLException {
		String s = rs.getString("sensorType")+"\t"+
				rs.getString("unit")+"\t"+
				rs.getString("company")+"\t"+
				rs.getString("location")+"\t"+
				rs.getString("observation");
		return s;
	}
	
}
