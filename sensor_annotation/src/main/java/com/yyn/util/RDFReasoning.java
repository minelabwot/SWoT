package com.yyn.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.tdb.TDBFactory;
import org.apache.jena.update.UpdateExecutionFactory;
import org.apache.jena.update.UpdateFactory;
import org.apache.jena.update.UpdateProcessor;
import org.apache.jena.update.UpdateRequest;
import org.apache.jena.util.FileManager;

public class RDFReasoning {
	public static void main(String[] args) {
		String path = "/Users/yangyunong/javaworkspace/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/sensor_annotation/WEB-INF/RDF_Database/";
		Dataset ds = getDataset(path, "sensor_annotation", "");
		String prefix = StrUtils.strjoinNL(
				"PREFIX wot: <http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#> ",
				"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ",
				"PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> ",
				"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ");
		
		String queryString1 = " select DISTINCT ?s1 ( sql:rnk_scale ( <LONG::IRI_RANK> ( ?s1 ) ) ) as ?rank " + 
				"where {" + 
				"?s1 rdfs:label ?o1 ." + 
				"?o1 bif:contains \"(' "+ "Beijing" + "' )\" ." +
				"Filter regex (str(?s1),\"resource\",\"ontology\") ."+
				"} "+
				"order by desc ( ?rank )";

		String queryString2 = "SELECT DISTINCT ?loc "+
				"WHERE { "+
				"SERVICE <http://dbpedia.org/sparql> { "+
				"<http://dbpedia.org/resource/Beijing> ?rel ?loc. " + 
				"FILTER regex(str(?loc),\"China\") }}";

		ResultSet rs = RDFReasoning.selectQuery(queryString2,ds);
		ResultSetFormatter.out(rs);
	}
	public static void first() {
		String queryString2 = "SELECT DISTINCT ?loc "+
				"WHERE { "+
				"<http://dbpedia.org/resource/Beijing> ?rel ?loc. " + 
				"FILTER regex(str(?loc),\"China\") }";
		QueryEngineHTTP qe = new QueryEngineHTTP("http://dbpedia.org/sparql", queryString2);
		ResultSet rs = qe.execSelect();
		ResultSetFormatter.out(rs);
		qe.close();
	}
	public static Dataset getDataset(String realpath, String databaseName,String fileName) {
		String databasePath = realpath+databaseName;
		File file = new File(realpath+databaseName);
		if(!file.exists()) {
			Dataset ds = TDBFactory.createDataset(databasePath);
			Model model;
			ds.begin(ReadWrite.WRITE);
			try {
			 	model = ds.getDefaultModel();
				FileManager.get().readModel(model, realpath+fileName);
				ds.commit();
			} finally { 
				ds.end(); 
			}
			model.close();
			ds.close();
		}
		return TDBFactory.createDataset(databasePath);
	}
	
	public static ResultSet selectQuery(String queryString,Dataset dataset) {
		QueryExecution qExec = QueryExecutionFactory.create(queryString, dataset);
	    ResultSet rs = qExec.execSelect();
	    //ResultSetFormatter.out(rs);
	    return rs;
	}
	
	public static ResultSet remoteQuery(String queryString) {
		QueryExecution qExec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", queryString);
	    ResultSet rs = qExec.execSelect() ;
		return rs;
	}
	
	public static void updateQuery(String updateString,Dataset dataset) {
	     UpdateRequest request = UpdateFactory.create(updateString) ;
	     UpdateProcessor proc = UpdateExecutionFactory.create(request, dataset) ;
	     proc.execute() ;
	}
	
	public static void output(Dataset ds){
		String rdfRoot = "/Users/yangyunong/temp/";
		FileOutputStream fos = null;
		ds.begin(ReadWrite.READ);
		try{
		fos = new FileOutputStream(rdfRoot+"output.owl");
		Model model = ds.getDefaultModel();
		model.getWriter().write(model, fos,null);
		ds.commit();
		ds.end();
		fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("更新了output.owl");
	}
}
