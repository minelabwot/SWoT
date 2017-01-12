package com.yyn.service;


import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.springframework.stereotype.Service;

import com.yyn.util.RDFReasoning;

@Service
public class AnomalyService {
	private static final String NS_WOT = "http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#";
	
	public void createState(Dataset dataset) {
		String highState = StrUtils.strjoinNL("PREFIX wot: <http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#>",
	    		"PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> ",
	    		"DELETE { ?device wot:hasState ?a.}",
	    		"INSERT { ?device wot:hasState wot:high. }", 
	            "WHERE { ",
	            "?device wot:hasState ?a. ",//用于delete
	            "?device wot:hasValue ?val1. ",//以下用于insert
	            "?device wot:hasType ?sensorType. ",
	            "?sensorType wot:defaultObserved ?properCls. ",
	            "?properCls wot:highThreshold ?val2. ",
	            "FILTER(?val1 > ?val2)}");
		String lowState = StrUtils.strjoinNL("PREFIX wot: <http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#>",
	    		"PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> ",
	    		"DELETE { ?device wot:hasState ?a }",
	    		"INSERT { ?device wot:hasState wot:low }", 
	            "WHERE { ?device wot:hasState ?a. ",//用于匹配删除所有之前的state
	            "?device wot:hasValue ?val1.",
	            "?device wot:hasType ?sensorType. ",
	            "?sensorType wot:defaultObserved ?properCls. ",
	            "?properCls wot:lowThreshold ?val2. ",
	            "FILTER(?val1 < ?val2)}");
		String nomalState = StrUtils.strjoinNL("PREFIX wot: <http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#>",
	    		"PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> ",
	    		"DELETE { ?device wot:hasState ?a }",
	    		"INSERT { ?device wot:hasState wot:nomal }", 
	            "WHERE { ?device wot:hasState ?a. ",//用于匹配删除所有之前的state
	            "?device wot:hasValue ?val1.",
	            "?device wot:hasType ?sensorType. ",
	            "?sensorType wot:defaultObserved ?properCls. ",
	            "?properCls wot:lowThreshold ?val2. ",
	            "?properCls wot:highThreshold ?val3. ",
	            "FILTER(?val1 >= ?val2 && ?val1 <= ?val3)}");
		String[] strings = {highState,lowState,nomalState};
		
		for(int i=0;i<3;++i) {
			try {
				dataset.begin(ReadWrite.WRITE);
				RDFReasoning.updateQuery(strings[i], dataset);
				System.out.println("更新State_"+i+"执行");
				dataset.commit();
			} finally {
				dataset.end();
			}
		}
	}
	
	
	public void generateDiagModel(Dataset ds) {
		generateFoI(ds);
		createMandatoryProp(ds);
		createOptional(ds);
		createProcess(ds);
		combineProcess(ds);
		createCause(ds);
	}
	
	private void generateFoI(Dataset ds) {
		ds.begin(ReadWrite.WRITE);
		//xxx
		String update = StrUtils.strjoinNL(
				"PREFIX wot: <http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#> ",
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ",
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ",
				"PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> ",
				"INSERT { ?uri rdf:type ?foiCls. ",
				"?uri wot:hasSpot ?spot. }",
				"WHERE { ?spot a wot:Spot.",
				"?foiCls rdfs:subClassOf ssn:FeatureOfInterest.",
				"BIND(URI(CONCAT('"+NS_WOT+"',STRAFTER(str(?spot),'#'),'_',STRAFTER(str(?foiCls),'#'))) as ?uri) }");
		RDFReasoning.updateQuery(update, ds);
		ds.commit();
		ds.end();
		
	}
	/**
	 * #1 sparql-update 创建场景FOI的通量属性 如energy airquality等
	 */
	private void createMandatoryProp(Dataset dataset) {
		//这个语句运行之前?feature必须存在,也就是说必须要有FOI的Instance
		dataset.begin(ReadWrite.WRITE);
		String update = StrUtils.strjoinNL(
				"PREFIX wot: <http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#> ",
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ",
				"PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> ",
				"INSERT { ?uri rdf:type ?propClass. ",
				"?feature ssn:hasProperty ?uri.",
				"?uri ssn:isPropertyOf ?feature. } ",
				"WHERE { ?feature a ?featureCls.",
				"?featureCls wot:requiresProperty ?propClass.",
				"BIND(URI(CONCAT('http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#',STRAFTER(str(?feature),'#'),'_',STRAFTER(str(?propClass),'#'))) as ?uri) }");
		RDFReasoning.updateQuery(update, dataset);
		dataset.commit();
		dataset.end();
	}
	
	/**
	 * #2 sparql-update 创建场景FOI的传感器属性,只有存在于与FOI同一spot的传感器才会被添加进来
	 * @param dataset
	 */
	private void createOptional(Dataset dataset) {
		dataset.begin(ReadWrite.WRITE);
		String update = StrUtils.strjoinNL(
				"PREFIX wot: <http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#> ",
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ",
				"PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> ",
				"INSERT { ?uri rdf:type ?propCls.",
				"?feature ssn:hasProperty ?uri.",
				"?uri ssn:isPropertyOf ?feature. ",
				"?sensor ssn:forProperty ?uri. ",
				"?feature wot:hasDevice ?sensor }", //构建设备
				"WHERE {", //FOI是基于地点Spot的,所以设备必须满足出现在该Spot
				"?sensor wot:hasType ?entityType. ",
				"?entityType wot:defaultObserved ?propCls. ",
				"?feature a ?featureCls. ",
				"?featureCls wot:optionalProp ?propCls. ",
				"?sensor wot:hasSpot ?loc. ",
				"?feature wot:hasSpot ?loc.",
				"BIND(URI(CONCAT('http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#',STRAFTER(str(?feature),'#'),'_',STRAFTER(str(?propCls),'#'))) as ?uri) }");
		RDFReasoning.updateQuery(update, dataset);
		dataset.commit();
		dataset.end();
	}
	
	/**
	 * #3 sparql-update 在同一FOI的Property和通量之间建立相关关系
	 * @param dataset
	 */
	private void createProcess(Dataset dataset) {
		dataset.begin(ReadWrite.WRITE);
		String update = StrUtils.strjoinNL(
				"PREFIX wot: <http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#> ",
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ",
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ",
				"PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> ",
				"INSERT { ?uri rdf:type ?proc.",
				"?uri ssn:hasInput ?prop1.",
				"?uri ssn:hasOutput ?prop2. } ",
				"WHERE {", 
				"?annoProc rdfs:subPropertyOf wot:hasIntInfl. ",
				"?annoProc wot:equalsProcess ?proc. ",
				"?propCls1 ?annoProc ?propCls2. ",
				"?prop1 a ?propCls1. ",
				"?prop1 ssn:isPropertyOf ?feature. ",
				"?feature ssn:hasProperty ?prop2. ",
				"?prop2 a ?propCls2. ",
				"BIND(URI(CONCAT('http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#',STRAFTER(str(?prop1),'#'),'_',STRAFTER(str(?prop2),'#'))) as ?uri) }");
		RDFReasoning.updateQuery(update, dataset);
		dataset.commit();
		dataset.end();
	}
	
	/**
	 * #4 sparql-update 在同一FOI下的sensor Property之间利用通量建立直接相关关系
	 * @param dataset
	 */
	private void combineProcess(Dataset dataset) {
		dataset.begin(ReadWrite.WRITE);
		//负正->负
		String updateNP = StrUtils.strjoinNL(
				"PREFIX wot: <http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#> ",
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ",
				"PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> ",
				"INSERT { ?uri rdf:type wot:NegativeCorrelationProcess.",
				"?uri ssn:hasInput ?prop1.",
				"?uri ssn:hasOutput ?prop3. } ",
				"WHERE {", 
				"?proc1 a wot:NegativeCorrelationProcess. ",
				"?proc2 a wot:PositiveCorrelationProcess. ",
				"?proc1 ssn:hasInput ?prop1. ",
				"?proc1 ssn:hasOutput ?prop2. ",
				"?proc2 ssn:hasInput ?prop2. ",
				"?proc2 ssn:hasOutput ?prop3. ",
				"FILTER NOT EXISTS {?prop2 ssn:forProperty ?sensor}",//prop2必须是通量,也就是没有对应的sensor
				"BIND(URI(CONCAT('http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#',STRAFTER(str(?prop1),'#'),'_',STRAFTER(str(?prop3),'#'))) as ?uri) }");
		RDFReasoning.updateQuery(updateNP, dataset);
		//正正->正
		String updatePP = StrUtils.strjoinNL(
				"PREFIX wot: <http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#> ",
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ",
				"PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> ",
				"INSERT { ?uri rdf:type wot:PositiveCorrelationProcess.",
				"?uri ssn:hasInput ?prop1.",
				"?uri ssn:hasOutput ?prop3. } ",
				"WHERE {", 
				"?proc1 a wot:PositiveCorrelationProcess. ",
				"?proc2 a wot:PositiveCorrelationProcess. ",
				"?proc1 ssn:hasInput ?prop1. ",
				"?proc1 ssn:hasOutput ?prop2. ",
				"?proc2 ssn:hasInput ?prop2. ",
				"?proc2 ssn:hasOutput ?prop3. ",
				"FILTER NOT EXISTS {?prop2 ssn:forProperty ?sensor}",
				"BIND(URI(CONCAT('http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#',STRAFTER(str(?prop1),'#'),'_',STRAFTER(str(?prop3),'#'))) as ?uri) }");
		RDFReasoning.updateQuery(updatePP, dataset);
		//正负->负
		String updatePN = StrUtils.strjoinNL(
				"PREFIX wot: <http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#> ",
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ",
				"PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> ",
				"INSERT { ?uri rdf:type wot:NegativeCorrelationProcess.",
				"?uri ssn:hasInput ?prop1.",
				"?uri ssn:hasOutput ?prop3. } ",
				"WHERE {", 
				"?proc1 a wot:PositiveCorrelationProcess. ",
				"?proc2 a wot:NegativeCorrelationProcess. ",
				"?proc1 ssn:hasInput ?prop1. ",
				"?proc1 ssn:hasOutput ?prop2. ",
				"?proc2 ssn:hasInput ?prop2. ",
				"?proc2 ssn:hasOutput ?prop3. ",
				"FILTER NOT EXISTS {?prop2 ssn:forProperty ?sensor}",
				"BIND(URI(CONCAT('http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#',STRAFTER(str(?prop1),'#'),'_',STRAFTER(str(?prop3),'#'))) as ?uri) }");
		RDFReasoning.updateQuery(updatePN, dataset);
		//负负->正
		String updateNN = StrUtils.strjoinNL(
				"PREFIX wot: <http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#> ",
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ",
				"PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> ",
				"INSERT { ?uri rdf:type wot:PositiveCorrelationProcess.",
				"?uri ssn:hasInput ?prop1.",
				"?uri ssn:hasOutput ?prop3. } ",
				"WHERE {", 
				"?proc1 a wot:NegativeCorrelationProcess. ",
				"?proc2 a wot:NegativeCorrelationProcess. ",
				"?proc1 ssn:hasInput ?prop1. ",
				"?proc1 ssn:hasOutput ?prop2. ",
				"?proc2 ssn:hasInput ?prop2. ",
				"?proc2 ssn:hasOutput ?prop3. ",
				"FILTER NOT EXISTS {?prop2 ssn:forProperty ?sensor}",
				"BIND(URI(CONCAT('http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#',STRAFTER(str(?prop1),'#'),'_',STRAFTER(str(?prop3),'#'))) as ?uri) }");
		RDFReasoning.updateQuery(updateNN, dataset);
		dataset.commit();
		dataset.end();
	}
	
	private void createCause(Dataset dataset) {
		dataset.begin(ReadWrite.WRITE);
		String part1 = StrUtils.strjoinNL(
				"PREFIX wot: <http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#> ",
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ",
				"PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> ",
				"DELETE {?uri ssn:observationSamplingTime ?t1. }",
				"INSERT { ?uri rdf:type wot:ObservedCause.",
				"?anomaly wot:hasPotCause ?uri.",
				"?uri ssn:observationSamplingTime ?time. ",
				"?sensor2 wot:generate ?uri. } ",
				"WHERE {", 
				"?sub ssn:observationSamplingTime ?t1. ",
				"?anomaly a wot:Anomaly. ",
				"?sensor1 wot:generate ?anomaly. ",
				"?anomaly ssn:observationSamplingTime ?time. ",
				"?sensor1 ssn:forProperty ?prop1. ");
		
		String part2 = StrUtils.strjoinNL("?proc ssn:hasOutput ?prop1. ",
				"?proc ssn:hasInput ?prop2. ",
				"?sensor2 ssn:forProperty ?prop2.");
		String part3 = "BIND(URI(CONCAT('http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#',STRAFTER(str(?sensor2),'#'),'_','Cause')) as ?uri) }";
		String updateHighPos = StrUtils.strjoinNL(
				part1,
				"?sensor1 wot:hasState wot:high. ",
				"?proc a wot:PositiveCorrelationProcess. ",
				part2,
				"?sensor2 wot:hasState wot:high.",
				part3);
		String updateLowPos = StrUtils.strjoinNL(
				part1,
				"?sensor1 wot:hasState wot:low. ",
				"?proc a wot:PositiveCorrelationProcess. ",
				part2,
				"?sensor2 wot:hasState wot:low.",
				part3);
		String updateHighNeg = StrUtils.strjoinNL(
				part1,
				"?sensor1 wot:hasState wot:high. ",
				"?proc a wot:NegativeCorrelationProcess. ",
				part2,
				"?sensor2 wot:hasState wot:high.",
				part3);
		String updateLowNeg = StrUtils.strjoinNL(
				part1,
				"?sensor1 wot:hasState wot:low. ",
				"?proc a wot:NegativeCorrelationProcess. ",
				part2,
				"?sensor2 wot:hasState wot:low.",
				part3);
		RDFReasoning.updateQuery(updateHighPos, dataset);
		RDFReasoning.updateQuery(updateHighNeg, dataset);
		RDFReasoning.updateQuery(updateLowPos, dataset);
		RDFReasoning.updateQuery(updateLowNeg, dataset);
		dataset.commit();
		dataset.end();
	}
}
