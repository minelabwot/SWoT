package com.yyn.service;

import javax.servlet.ServletContext;

import org.apache.jena.query.Dataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.yyn.util.RDFReasoning;

@Component
public class StartupListener implements ServletContextAware{
	@Autowired
	AnomalyService as;
	
	@Override
	public void setServletContext(ServletContext sc) {
		// TODO Auto-generated method stub
		String tdbRoot = sc.getRealPath("/WEB-INF/RDF_Database/");
		Dataset dataset = RDFReasoning.getDataset(tdbRoot, "sensor_annotation", "Wot.owl");
		sc.setAttribute("dataset", dataset);
		System.out.println("Start up Listener execute, dataset  has been set to context");
		
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true) {
					try {
						Thread.sleep(1000*20);
						as.generateDiagModel(dataset);
						RDFReasoning.output(dataset);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
	}
	
}
