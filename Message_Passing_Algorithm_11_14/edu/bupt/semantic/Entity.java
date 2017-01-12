package bupt.semantic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public class Entity {
	List<String> Candidate = new ArrayList<String>();
	String currentValues = null;
	Map<String, List<String>> Emap = new ConcurrentHashMap<String, List<String>>();
	//List<Message> mesList = new ArrayList<Message>();
	Vector<Message> mesList = new Vector<Message>();
	Iterator<String> iterator;
	
	public void getMessage(boolean change, float score, String type ,String top) {
		mesList.add(new Message(change, score, type , top));
	}
	
	public String getNext() {
		String type = null;
		String rel = null;
		for (Message m : mesList) {
		if(m.type.equals("type") && m.change)
				type = m.top;
			if(m.type.equals("rel") && m.change)
				rel = m.top;
		}
		
		while(iterator.hasNext()) {
			++MessagePassing.INSTANCE.queryCount;
			String can = iterator.next();
			String queryString = "SELECT ?a "+//count(?a) as ?num "+
					"WHERE {"+ 	
					"<"+can+">" + "rdf:type ?a ."+
					//" Filter regex (str(?a),\""+type+"\")."+
					"}";
			QueryEngineHTTP qe = new QueryEngineHTTP("http://dbpedia.org/sparql", queryString);
			ResultSet results = qe.execSelect();
			if(results.hasNext()) {
				while(results.hasNext()) {	
					String result = results.next().get("a").toString();
					if(result.equals(type)) {
						qe.close();
						mesList = new Vector<Message>();
						return can;
					}	
				}
				qe.close();
			}
			else {
				++MessagePassing.INSTANCE.queryCount;
				String queryString1 = "SELECT DISTINCT ?t ?e "+
						"WHERE {"+ 	
						"<"+can+">" + " <http://dbpedia.org/ontology/wikiPageRedirects> ?e."+
						"?e rdf:type ?t ."+
						"} LIMIT 25 ";	
				QueryEngineHTTP qe1 = new QueryEngineHTTP("http://dbpedia.org/sparql", queryString1);
				ResultSet results1 = qe1.execSelect();
				while(results1.hasNext()) {
					QuerySolution qs = results1.next();
					if(qs.get("t").toString().equals(type)) {
						qe1.close();
						mesList = new Vector<Message>();
						return can;//qs.get("e").toString();  //如果他的重定向满足,也认为他对
					}
				}
				qe1.close();
			}
		}
		mesList = new Vector<Message>();
		return "no-annotation";
	}
}

class Message {
	boolean change;
	float score;
	String type;
	String top;

	public Message(boolean change, float score, String top, String type) {
		this.change = change;
		this.score = score;
		this.type = type;
		this.top = top;
	}
}