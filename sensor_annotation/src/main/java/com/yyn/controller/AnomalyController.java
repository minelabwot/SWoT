package com.yyn.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yyn.service.AnomalyService;
import com.yyn.util.RDFReasoning;

@Controller
public class AnomalyController {
	@Autowired
	AnomalyService as;
	@RequestMapping("/Anomaly_showAllAnomaly.do")
	public String generateDiagnosisModel(HttpServletRequest request,Model model) {
		Dataset ds = (Dataset)request.getServletContext().getAttribute("dataset");
		Map<String,List<String>> causes = new HashMap<>();
		Map<String,String> times = new HashMap<>();
		ds.begin(ReadWrite.READ);
		try {
			String query = StrUtils.strjoinNL(
					"PREFIX wot: <http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#> ",
					"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ",
					"PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> ",
					"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> ",
					"SELECT ?anomaly ?cause ?time",
					"WHERE { ?anomaly a wot:Anomaly.",
					"?anomaly wot:hasPotCause ?cause.",
					"?anomaly ssn:observationSamplingTime ?time. ",
					"?cause ssn:observationSamplingTime ?time. }");
			ResultSet rs = RDFReasoning.selectQuery(query, ds);
			while(rs.hasNext()) {
				QuerySolution qs = rs.next();
				Resource ano = qs.get("anomaly").asResource();
				Resource cau = qs.get("cause").asResource();
				String time = qs.getLiteral("time").getString();
				System.out.println(ano.getURI());
				if(causes.containsKey(ano.getLocalName())){
					causes.get(ano.getLocalName()).add(cau.getLocalName());
				}
				else {
					List<String> list = new ArrayList<String>();
					list.add(cau.getLocalName());
					causes.put(ano.getLocalName(), list);
				}
				times.put(ano.getLocalName(), time.toString());
			}
			
			model.addAttribute("anomalys", causes);
			model.addAttribute("times", times);
			ds.commit();
		} finally { 
			ds.end();
		}
		return "servicePage/anomalyList.jsp";
	}
}
