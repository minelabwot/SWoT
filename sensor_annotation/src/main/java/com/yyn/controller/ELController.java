package com.yyn.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.jena.query.Dataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yyn.model.Device;
import com.yyn.service.DeviceService;

import edu.bupt.linkSystemApplication.Application;

@Controller
@RequestMapping("/EL*")
public class ELController {
	@Autowired
	DeviceService ds;
	@RequestMapping("/ELrun.do")
	public String runEL(HttpServletRequest request,String id,String deviceType,Model model) {
		String elroot = request.getSession().getServletContext().getRealPath("/WEB-INF/ELApp/");
		System.out.println(elroot);
		if(Application.INSTANCE.runApp(elroot)) {
			Properties info = new Properties();
			try {
				info.load(new FileInputStream(elroot+"el_info.properties"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			int table_id = Integer.parseInt(info.getProperty("table_id"))+1;
			System.out.println("新的table_id为"+table_id);
			info.setProperty("table_id", ""+table_id);
			try {
				info.store(new FileOutputStream(elroot+"el_info.properties"),"");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		model.addAttribute("id", id);
		model.addAttribute("deviceType", deviceType);
		Device device = ds.getLink(id);
		//向neo4j中添加
		ds.addLink2Neo_instance(device);
		//向tdb中添加
		Dataset dataset = (Dataset)request.getSession().getServletContext().getAttribute("dataset");
		ds.addELResult(id, dataset, device);
		
		System.out.println(device.getCompany());
		device = ds.getColumnLink(device.getTable_id());
		ds.addLink2Neo_column(device);
		return "forward:/deviceDetail.do";
	}
}
