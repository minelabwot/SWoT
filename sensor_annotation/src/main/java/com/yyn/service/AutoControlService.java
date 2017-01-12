package com.yyn.service;


import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.springframework.stereotype.Service;

import com.yyn.util.RDFReasoning;

@Service
public class AutoControlService {
	private String prefix = StrUtils.strjoinNL(
			"PREFIX wot: <http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#> ",
			"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ",
			"PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> ");
	
	public void generateControlModel(Dataset ds) {
		createSubscribe(ds);
		generateAction(ds);
	}
	
	private void createSubscribe(Dataset ds) {
		ds.begin(ReadWrite.WRITE);
		try {
			String updateString = StrUtils.strjoinNL( prefix,
					"DELETE{ ?actuator wot:subscribe ?event.}",
					"INSERT { ?actuator wot:subscribe ?anomaly. }",
					"WHERE { ?actuator a wot:Actuator. ",
					"?feature wot:hasDevice ?actuator. ",
					"?sensor a ssn:Sensor. ",
					"?feature wot:hasDevice ?sensor. ",
					"?sensor wot:generate ?anomaly. ",
					"?anomaly a wot:Anomaly. ",
					"?actuator ?rel ?event. }"
					);
			RDFReasoning.updateQuery(updateString, ds);
			ds.commit();
		} finally {
			ds.end();
		}
	}
	
	private void generateAction(Dataset ds) {
		ds.begin(ReadWrite.WRITE);
		
		String delete = "DELETE{ ?uri ssn:observationSamplingTime ?val. "
				+ "?actuator wot:triggers ?event. }";
		String part1 = StrUtils.strjoinNL("?uri ssn:observationSamplingTime ?time. ",
				"?actuator wot:triggers ?uri. }",
				"WHERE { ?actuator ssn:forProperty ?prop1. ",
				"?actuator wot:subscribe ?anomaly. ",
				"?anomaly ssn:observationSamplingTime ?time. ",
				"?sensor wot:generate ?anomaly. ",
				"?sensor ssn:forProperty ?prop2. ");
		String part2 = StrUtils.strjoinNL("?proc ssn:hasInput ?prop1. ",
				"?proc ssn:hasOutput ?prop2. ");
		String part3 = StrUtils.strjoinNL("?sub ssn:observationSamplingTime ?val. ",
				"?actuator ?rel ?event. ");
		try {
			String negHigh = StrUtils.strjoinNL(prefix,
					delete,
					"INSERT { ?uri rdf:type wot:TurnUp. ",//dute
					part1,
					"?proc a wot:NegativeCorrelationProcess. ",//dute
					part2,
					"?sensor wot:hasState wot:high. ",//dute
					part3,
					"BIND(URI(CONCAT(str(?actuator),'_action_up')) as ?uri ) }"
					);
			String negLow = StrUtils.strjoinNL(prefix,
					delete,
					"INSERT { ?uri rdf:type wot:TurnDown. ",//dute
					part1,
					"?proc a wot:NegativeCorrelationProcess. ",//dute
					part2,
					"?sensor wot:hasState wot:low. ",//dute
					part3,
					"BIND(URI(CONCAT(str(?actuator),'_action_down')) as ?uri ) }"
					);
			String posHigh = StrUtils.strjoinNL(prefix,
					delete,
					"INSERT { ?uri rdf:type wot:TurnDown. ",//dute
					part1,
					"?proc a wot:PositiveCorrelationProcess. ",//dute
					part2,
					"?sensor wot:hasState wot:high. ",//dute
					part3,
					"BIND(URI(CONCAT(str(?actuator),'_action_down')) as ?uri ) }"
					);
			String posLow = StrUtils.strjoinNL(prefix,
					delete,
					"INSERT { ?uri rdf:type wot:TurnUp. ",//dute
					part1,
					"?proc a wot:PositiveCorrelationProcess. ",//dute
					part2,
					"?sensor wot:hasState wot:low. ",//dute
					part3,
					"BIND(URI(CONCAT(str(?actuator),'_action_up')) as ?uri ) }"
					);
			RDFReasoning.updateQuery(negHigh, ds);
			RDFReasoning.updateQuery(negLow, ds);
			RDFReasoning.updateQuery(posHigh, ds);
			RDFReasoning.updateQuery(posLow, ds);
			ds.commit();
		} finally {
			ds.end();
		}
	}
}
