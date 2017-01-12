package com.yyn.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.jena.atlas.lib.StrUtils;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.ReadWrite;
import org.apache.jena.query.ResultSet;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Record;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyn.dao.DataCollectionDAO;
import com.yyn.model.Device;
import com.yyn.model.Relation;
import com.yyn.model.Rule;
import com.yyn.model.Struct;
import com.yyn.util.RDFReasoning;

@Service
public class DeviceService {

	private static final String NS_WOT = "http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#";
	private String prefix = StrUtils.strjoinNL(
			"PREFIX wot: <http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#> ",
			"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ",
			"PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> ",
			"PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> ",
			"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> ",
			"PREFIX dul: <http://www.loa-cnr.it/ontologies/DUL.owl#> ");
	
	private List<String> unDisplayProperty = new ArrayList<String>();
	@Autowired
	private DataCollectionDAO dc;
	//public static final String PREFIX = "http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#";
	@Autowired
	Neo4jConnector connector;
	
	public DeviceService() {
		unDisplayProperty.add("type");
		unDisplayProperty.add("ref");
	}
	//向关系型数据库添加设备信息
	public void addDevice2mysql(Device device) {
		dc.insertNewDevice(device);
	}
	
	//添加数据属性
	public int addDataProperty(String deviceType, String owner, String name, String desc) {
		Driver driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "930208" ) );
		Session session = driver.session();
		String statement = "match (a:Class {label:\""+deviceType+"\"}) merge (p:Device {name:\""+name+"\",description:\""+desc
		+"\",user:\""+owner+"\",ref:\""+NS_WOT+name+"\"}) merge (p)-[:NamedIndividual {ref:\"http://www.w3.org/2002/07/owl#NamedIndividual\",label:\"NamedIndividual\"}]->(a) return id(p)";
		try {
			StatementResult sr =  session.run(statement);
			if(sr.hasNext())
				
			return sr.next().get("id(p)").asInt();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				session.close();
				driver.close();
			}
		return -1;
	}
	
	//ver 2016 11,11
	public void addObjectProperty(Map<String,String> map,int id) {
		Driver driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "930208" ) );
		Session session = driver.session();
		String temp = "";
		String temp2 = "";
		int index = 0;
		for(String s : map.keySet()) {
			System.out.println(s);
			switch(s) {
				case "hasType":
					temp2 += ",(c"+index+":Class {label:\"EntityType\"})";
					temp += " merge (p"+index+":Instance {label:\""+map.get(s)+"\"}) "
						+ "merge (d)-[:"+s+"]->(p"+index+") merge (p"+index+")-[:NamedIndividual]->(c"+index+")";
					++index;
					break;
				case "hasUnit": 
					temp2 += ",(c"+index+":Class {label:\"Unit\"})";
					temp += " merge (p"+index+":Instance {label:\""+map.get(s)+"\"}) "
						+ "merge (d)-[:"+s+"]->(p"+index+") merge (p"+index+")-[:NamedIndividual]->(c"+index+")";
					++index;
					break;
				case "hasLocation": 
					temp2 += ",(c"+index+":Class {label:\"Region\"})";
					temp += " merge (p"+index+":Instance {label:\""+map.get(s)+"\"}) "
						+ "merge (d)-[:"+s+"]->(p"+index+") merge (p"+index+")-[:NamedIndividual]->(c"+index+")";
					++index;
					break;
				case "hasSpot": 
					temp2 += ",(c"+index+":Class {label:\"Spot\"})";
					temp += " merge (p"+index+":Instance {label:\""+map.get(s)+"\"}) "
						+ "merge (d)-[:"+s+"]->(p"+index+") merge (p"+index+")-[:NamedIndividual]->(c"+index+")";
					++index;
					break;
				case "isOwnedBy": 
					temp2 += ",(c"+index+":Class {label:\"Owner\"})";
					temp += " merge (p"+index+":Instance {label:\""+map.get(s)+"\"}) "
						+ "merge (d)-[:"+s+"]->(p"+index+") merge (p"+index+")-[:NamedIndividual]->(c"+index+")";
					++index;
					break;
				case "forProperty": 
					temp2 += ",(c"+index+":Class {label:\"SensorProperty\"})";
					temp += " merge (p"+index+":Instance {label:\""+map.get(s)+"\"}) "
						+ "merge (d)-[:"+s+"]->(p"+index+") merge (p"+index+")-[:NamedIndividual]->(c"+index+")";
					++index;
					break;
//				case "detects": 
//					temp2 += ",(c"+index+":Class {label:\"Event\"})";
//					temp += " merge (p"+index+":Instance {label:\""+map.get(s)+"\"}) "
//						+ "merge (d)-[:"+s+"]->(p"+index+") merge (p"+index+")-[:NamedIndividual]->(c"+index+")";
//					++index;
//					break;
//				case "subscribe":
//					temp += " merge (p"+index+":Instance {label:\""+map.get(s)+"\"}) "
//							+ "merge (d)-[:"+s+"]->(p"+index+")";
//						++index;
//						break;
//				case "triggers":
//					temp2 += ",(c"+index+":Class {label:\"Action\"})";
//					temp += " merge (p"+index+":Instance {label:\""+map.get(s)+"\"}) "
//							+ "merge (d)-[:"+s+"]->(p"+index+") merge (p"+index+")-[:NamedIndividual]->(c"+index+")";
//						++index;
//						break;
			}
		}
		
		String statement = "match (d)"+temp2+"where id(d)="+id+temp;
		try {
			session.run(statement);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				session.close();
				driver.close();
			}
	}
	
	public List<Device> showAllDevice(String owner,String type) {
		Driver driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "930208" ) );
		Session session = driver.session();
		List<Device> list = new ArrayList<Device>();
		
		try {
			String statement;
			if(owner.equals("allUser"))
				statement = "match (d:Device)-[:NamedIndividual]->(:Class {label:\""+type+"\"})  return d,id(d)";
			else
				statement = "match (d:Device {user:\""+owner+"\"})-[:NamedIndividual]->(:Class {label:\""+type+"\"})  return d,id(d)";
			StatementResult result = session.run(statement);
			while (result.hasNext()) {
				Record r = result.next();
				int id = r.get("id(d)").asInt();
				Map<String,Object> map = r.get("d").asMap();
				Device d = new Device(id, map.get("name").toString(), map.get("description").toString());
				list.add(d);
			}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				session.close();
				driver.close();
			}
		System.out.println(list.size());
		return list;
	}
	
	public Device getDeviceInfo(String device_id) {
		return dc.getDeviceInfo(device_id);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List showDetail(String id,Map<String,String> resultmap) {
		Driver driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "930208" ) );
		Session session = driver.session();

		List list = new ArrayList();
		try {
			String statement = "match (d) where id(d)="+id+" return d";
			StatementResult result = session.run(statement);
			while (result.hasNext()) {
				Record r = result.next();
				Map<String,Object> map = r.get("d").asMap();
				for(String s :map.keySet()) {
					if(!unDisplayProperty.contains(s)) 
						resultmap.put(s, map.get(s).toString());
				}
			}
			
			statement = "match (d)-[r]->(o) where id(d)="+id+" return type(r),o";
			result = session.run(statement);
			while(result.hasNext()) {
				Record r = result.next();
				String rName = r.get("type(r)").asString();
				Map<String,Object> proper = r.get("o").asMap();
				if(!proper.get("label").equals("rule"))
					resultmap.put(rName, proper.get("label").toString());
			}

			statement = "match (o)-[r]->(d) where id(d)="+id+" return type(r),o";
			result = session.run(statement);
			while(result.hasNext()) {
				Record r = result.next();
				String rName = r.get("type(r)").asString();
				Map<String,Object> proper = r.get("o").asMap();
				resultmap.put(rName, proper.get("label").toString());
			}
			list.add(events_actions(Integer.parseInt(id)));
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				session.close();
				driver.close();
			}
		return list;
	}
	public List<List<String>> events_actions(int id) {
		Driver driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "930208" ) );
		Session session = driver.session();
		List<List<String>> list = new ArrayList<List<String>>();
		List<String> events = new ArrayList<String>();
		List<String> actions = new ArrayList<String>();
		String statement = "match (d)-[:triggers]->(o) where id(d)="+id+" return o.label";
		try {
			StatementResult sr = session.run(statement);
			while(sr.hasNext()) {
				Record r = sr.next();
				actions.add(r.get("o.label").asString());
			}
			statement = "match (d)-[:subscribe]->(o) where id(d)="+id+" return o.label";
			sr = session.run(statement);
			while(sr.hasNext()) {
				Record r = sr.next();
				events.add(r.get("o.label").asString());
			}
			list.add(events);
			list.add(actions);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
			driver.close();
		}
		return list;
	}
	public Map<String,String> showAllProperty(String deviceType,String state) {
		Driver driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "930208" ) );
		Session session = driver.session();
		Map<String,String> map = new HashMap<String,String>();
		String statement;
		if("active".equals(state))
			statement = "MATCH (:Class {label:\""+deviceType+"\"})-[:subClassOf *1..]->(p),(p)-[r]->(o) return r.ref,o.ref";
		else
			statement = "MATCH (:Class {label:\""+deviceType+"\"})-[:subClassOf *1..]->(p),(o)-[r]->(p) return r.ref,o.ref";
		try {
			StatementResult result = session.run(statement);
			while (result.hasNext()) {
				Record r = result.next();
				String label = r.get("r.ref").asString().split("#")[1];
				if(!"subClassOf".equals(label) && !"linkTo".equals(label)) {
					String tmp = r.get("o.ref").asString();
					map.put(label,tmp.split("#")[1]);
				}
			}
			if("active".equals(state))
				statement = "MATCH (:Class {label:\""+deviceType+"\"})-[r]->(o) return r.ref,o.ref";
			else
				statement = "MATCH (o)-[r]->(:Class {label:\""+deviceType+"\"}) return r.ref,o.ref";
			result = session.run(statement);
			while (result.hasNext()) {
				Record r = result.next();
				String rLabel = r.get("r.ref").asString().split("#")[1];
				if(!"subClassOf".equals(rLabel)  && !"NamedIndividual".equals(rLabel)) {
					map.put(rLabel,r.get("o.ref").asString().split("#")[1]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
			driver.close();
		}
		return map;
	}
	
	public void addRule(String id,String[] events, String[] actions) {
		Driver driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "930208" ) );
		Session session = driver.session();
		String tmp = "";
		int ei=0,ai=0;
		for(String event : events) {
			tmp += "merge (e"+ei+":Event {label:\""+event+"\"}) merge (t)-[:hasInput]->(e"+ei+") ";
			++ei;
		}
		for(String action : actions) {
			tmp += "merge (a"+ai+":Action {label:\""+action+"\"}) merge (t)-[:hasOutput]->(a"+ai+") ";
			++ai;
		}
		
		String statement = "start d=node("+id+") create (d)-[:hasRule]->(t:TriggerRule {label:\"rule\"}) "+tmp;
		session.run(statement);
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
			driver.close();
		}
	}
	
	public List<Rule> showRule(String id) {
		Driver driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "930208" ) );
		Session session = driver.session();
		List<Rule> rules = new ArrayList<Rule>();
		String statement = "start n=node("+id+") match (n)-[:hasRule]->(r) return id(r)";
		try {
			StatementResult result = session.run(statement);
			List<String> ids = new ArrayList<String>();
			while(result.hasNext()) 
				ids.add(""+result.next().get("id(r)").asInt());
			for(String tmpId : ids) {
				statement = "start n=node("+tmpId+") "
						+ "match (n)-[:hasInput]->(in),(n)-[:hasOutput]->(out)  return in.label as in,out.label as out";
				result = session.run(statement);
				Set<String> events = new HashSet<String>();
				Set<String> actions = new HashSet<String>();
				while(result.hasNext()) {
					Record r = result.next();
					events.add(r.get("in").asString());
					actions.add(r.get("out").asString());
				}
				rules.add(new Rule(events,actions));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
			driver.close();
		}
		return rules;		
	}
	
	
	public Device getLink(String id) {
		return dc.getLink(id);
	}
	public void addLink2Neo_instance(Device device) {
		Driver driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "930208" ) );
		Session session = driver.session();
		String location = device.getRegion().split(",")[0];
		String statement = "start d=node("+device.getId()+")"
				+ " match (d)-[:hasLocation]->(l), (d)-[:hasUnit]->(u),(d)-[:isOwnedBy]->(c)"
				+ ",(d)-[:type]->(t),(o)-[:observedBy]->(d) "
				+ "merge (l1:DBpedia {link:\""+location+"\"}) "
				+ "merge (u1:DBpedia {link:\""+device.getUnit().split(",")[0]+"\"}) "
				+ "merge (c1:DBpedia {link:\""+device.getCompany().split(",")[0]+"\"}) "
				+ "merge (t1:DBpedia {link:\""+device.getSensorType().split(",")[0]+"\"}) "
				+ "merge (o1:DBpedia {link:\""+device.getProperty().split(",")[0]+"\"}) "
				+ "merge (l)-[:linkTo]->(l1) "
				+ "merge (u)-[:linkTo]->(u1) "
				+ "merge (c)-[:linkTo]->(c1) "
				+ "merge (t)-[:linkTo]->(t1) "
				+ "merge (o)-[:linkTo]->(o1)";
		try {
			session.run(statement);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
			driver.close();
		}
	}
	
	public Device getColumnLink(String id) {
		return dc.getColumn(id);
	}
	public void addLink2Neo_column(Device device) {
		Driver driver = GraphDatabase.driver( "bolt://localhost", AuthTokens.basic( "neo4j", "930208" ) );
		Session session = driver.session();
		String location = device.getRegion();
		String observation = device.getProperty();
		String type = device.getSensorType();
		String company = device.getCompany();
		String unit = device.getUnit();
		String statement = "match (l:Class {label:\"Location\"}), (u:Class {label:\"Unit\"}),(c:Class {label:\"Owner\"})"
				+ ",(t:Class {label:\"EntityType\"}),(o:Class {label:\"Observation\"}) ";
		if(!location.equals("init"))
				statement += "merge (l1:DBpedia {link:\""+location+"\"}) "
						+ "merge (l)-[:equalsTo]->(l1) ";
		if(!unit.equals("init"))
				statement += "merge (u1:DBpedia {link:\""+unit+"\"}) "
						+ "merge (u)-[:equalsTo]->(u1) ";
		if(!company.equals("init"))
				statement += "merge (c1:DBpedia {link:\""+company+"\"}) "
						+ "merge (c)-[:equalsTo]->(c1) ";
		if(!type.equals("init"))
				statement += "merge (t1:DBpedia {link:\""+type+"\"}) "
						+ "merge (t)-[:equalsTo]->(t1) ";
		if(!observation.equals("init"))
				statement +=  "merge (o1:DBpedia {link:\""+observation+"\"}) "
						+ "merge (o)-[:equalsTo]->(o1)";
		statement += " return l";
		try {
			session.run(statement);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
			driver.close();
		}
	}
	
	//TDB method
	public void add2TDB(int id,HttpServletRequest request,Dataset ds,Map<String,String> metadata_avp,String deviceType) {
		ds.begin(ReadWrite.WRITE);
		String device_type = null;
		if("Sensor".equals(deviceType))
			device_type = "ssn:"+deviceType;
		else
			device_type = "wot:"+deviceType;

		try {
			String update = StrUtils.strjoinNL(
					"PREFIX wot: <http://www.semanticweb.org/yangyunong/ontologies/2016/7/WoT_domain#> ",
					"PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> ",
					"PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> ",
					"PREFIX dul: <http://www.loa-cnr.it/ontologies/DUL.owl#> ",
					"PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> ",
					"INSERT { ?device rdf:type "+device_type+". ",
					"?device wot:deviceID \""+id+"\"^^xsd:string.",
					"?device wot:hasState wot:nomal. ",
					//region
					"?region rdf:type wot:Region. ",
					"?device dul:hasLocation ?region. ",
					//spot
					"?spot rdf:type wot:Spot. ",
					"?device wot:hasSpot ?spot. ",
					//unit
					"?unit rdf:type wot:Unit. ",
					"?device wot:hasUnit ?unit. ",
					//entityType
					"?type rdf:type wot:EntityType. ",
					"?device wot:hasType ?type. ",
					"?type wot:defaultObserved wot:"+metadata_avp.get("forProperty")+". ",
					//owner
					"?owner rdf:type wot:Owner. ",
					"?device wot:isOwnedBy ?owner. }",
					"WHERE {",
					"BIND(URI(CONCAT(\""+NS_WOT+"\",\""+metadata_avp.get("name")+"\")) as ?device). ",
					"BIND(URI(CONCAT(\""+NS_WOT+"\",\""+metadata_avp.get("hasLocation")+"\")) as ?region). ",
					"BIND(URI(CONCAT(\""+NS_WOT+"\",\""+metadata_avp.get("hasSpot")+"\")) as ?spot). ",
					"BIND(URI(CONCAT(\""+NS_WOT+"\",\""+metadata_avp.get("hasUnit")+"\")) as ?unit). ",
					"BIND(URI(CONCAT(\""+NS_WOT+"\",\""+metadata_avp.get("isOwnedBy")+"\")) as ?owner). ",
					"BIND(URI(CONCAT(\""+NS_WOT+"\",\""+metadata_avp.get("hasType")+"\")) as ?type) }");
			RDFReasoning.updateQuery(update, ds);
			ds.commit();
		} finally { 
			ds.end();
		}
		RDFReasoning.output(ds);
	}
	
	public void addELResult(String id,Dataset ds,Device device) {
		ds.begin(ReadWrite.WRITE);
		try {
			String update = StrUtils.strjoinNL(prefix,
					"INSERT {",
					"?region wot:linkTo \""+ device.getRegion().split(",")[0] +"\"^^xsd:string. ",
					"?owner wot:linkTo \""+ device.getCompany().split(",")[0] +"\"^^xsd:string. ",
					"?unit wot:linkTo \""+ device.getUnit().split(",")[0] +"\"^^xsd:string. ",
					"?propCls wot:linkTo \""+ device.getProperty().split(",")[0] +"\"^^xsd:string. ",
					"?type wot:linkTo \""+ device.getSensorType().split(",")[0] +"\"^^xsd:string. }",
					"WHERE { ?device wot:deviceID \""+id+"\"^^xsd:string. ",
					"?device dul:hasLocation ?region. ",
					"?deivce wot:isOwnedBy ?owner. ",
					"?device wot:hasUnit ?unit. ",
					"?device wot:hasType ?type. ",
					"?type wot:defaultObserved ?propCls. }");
			RDFReasoning.updateQuery(update, ds);
			ds.commit();
		} finally {
			ds.end();
		}
		RDFReasoning.output(ds);
	}
	
	public ResultSet getResult(Dataset ds,String searchType,String firkey,String firval,String seckey,String secval) {
		ds.begin(ReadWrite.READ);
		String location_local ="";
		String location_db = "";
		if("Region".equals(firkey)) {
			location_local += StrUtils.strjoinNL(
					"?device dul:hasLocation ?loc_local. ",
					"?loc_local wot:linkTo ?loc_el. ",
					"BIND(URI(?loc_el) as ?loc_el_uri). ",
					"?loc_local a wot:Region. ");
			location_db += StrUtils.strjoinNL("?loc_el_uri ?rel ?loc_db. ",
					"FILTER regex(str(?loc_db),\""+firval+"\") ");
		}
		else if("Region".equals(seckey)) {
			location_local += StrUtils.strjoinNL(
					"?device dul:hasLocation ?loc_local. ",
					"?loc_local wot:linkTo ?loc_el. ",
					"BIND(URI(?loc_el) as ?loc_el_uri). ",
					"?loc_local a wot:Region. ");
			location_db += StrUtils.strjoinNL("?loc_el ?rel ?loc_db. ",
					"FILTER regex(str(?loc_db),\""+secval+"\"). ");
		}
		
		String owner_local = "";
		String owner_db = "";
		if("Owner".equals(firkey)) {
			owner_local += StrUtils.strjoinNL(
					"?device wot:isOwnedBy ?owner_local. ",
					"?owner_local wot:linkTo ?owner_el. ",
					"BIND(URI(?owner_el) as ?owner_el_uri). ",
					"?owner_local a wot:Owner. ");
			owner_db += StrUtils.strjoinNL("?owner_el_uri ?rel ?owner_db. ",
					"FILTER regex(str(?owner_db),\""+firval+"\") ");
		}
		else if("Owner".equals(seckey)) {
			owner_local += StrUtils.strjoinNL(
					"?device wot:isOwnedBy ?owner_local. ",
					"?owner_local wot:linkTo ?owner_el. ",
					"BIND(URI(?owner_el) as ?owner_el_uri). ",
					"?owner_local a wot:Owner. ");
			owner_db += StrUtils.strjoinNL("?owner_el_uri ?rel ?owner_db. ",
					"FILTER regex(str(?owner_db),\""+secval+"\") ");
		}
		try {
			String query = null;
			if("Device".equals(searchType)) {
				query = StrUtils.strjoinNL(prefix,
						"SELECT DISTINCT ?deviceID ",
						"WHERE { ",
						"?device wot:deviceID ?deviceID. ",
						owner_local,
						location_local,
						"SERVICE <http://dbpedia.org/sparql> { ",
						owner_db,
						location_db,
						"}",
						"}");
			}
			else if("Anomaly".equals(searchType)) {
				query = StrUtils.strjoinNL(prefix,
						"SELECT DISTINCT ?deviceID ?anomaly ?cause ?time ?device ",
						"WHERE { ",
						"?device wot:deviceID ?deviceID. ",
						"?device wot:generate ?anomaly. ",
						"?anomaly wot:hasPotCause ?cause. ",
						"?anomaly ssn:observationSamplingTime ?time. ",
						"?cause ssn:observationSamplingTime ?time. ",
						owner_local,
						location_local,
						"SERVICE <http://dbpedia.org/sparql> { ",
						owner_db,
						location_db,
						"}",
						"}");
			}
			System.out.println(query);
			ResultSet rs = RDFReasoning.selectQuery(query,ds);
			//ResultSetFormatter.out(rs);
			ds.commit();
			return rs;
		} finally {
			ds.end();
		}
	}
	
}







class StrutsComparator implements Comparator<Struct> {
	@Override
	public int compare(Struct o1, Struct o2) {
		// TODO Auto-generated method stub
		return o1.getrName().compareTo(o2.getrName());
		
	}
}
class RelationComparator implements Comparator<Relation> {
	@Override
	public int compare(Relation o1, Relation o2) {
		// TODO Auto-generated method stub
		return o1.getName().compareTo(o2.getName());
		
	}
}