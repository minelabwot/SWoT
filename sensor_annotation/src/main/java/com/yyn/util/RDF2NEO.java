package com.yyn.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;

public class RDF2NEO {
	//private static final String URL = "115.28.240.210";
	private static final String URL = "localhost";
	
	public static void main(String[] args) {
		RDF2NEO exam = new RDF2NEO();
		//exam.deleteAllLink();
		exam.addLabel();
		exam.deleteAll();
		exam.addSchema();
		
	}
	public void addSchema() {
		generateNode();
		generateRelation();
	}
	//用于给手动创建的owl元素添加label属性
	public void addLabel() {
		String xmlpath="file/Wot.owl";
		SAXBuilder builder=new SAXBuilder();
		try {
			Document doc = builder.build(xmlpath);
			Element root = doc.getRootElement();
			System.out.println(root.getName());
			Namespace nsowl = Namespace.getNamespace("http://www.w3.org/2002/07/owl#");
			Namespace nsrdf = Namespace.getNamespace("http://www.w3.org/1999/02/22-rdf-syntax-ns#");
			Namespace nsrdfs = Namespace.getNamespace("http://www.w3.org/2000/01/rdf-schema#");
//			List<Element> list = root.getChildren("DatatypeProperty",nsowl); //给数据属性添加
//			List<Element> list = root.getChildren("ObjectProperty", nsowl);//给关系属性添加
			List<Element> list = root.getChildren("Class", nsowl);//给概念添加
			System.out.println(list.isEmpty());
			for(Element e : list) {
				String v = e.getAttributeValue("about",nsrdf);
				String label = e.getChildText("label",nsrdfs);
				if(label == null) {
					Element te = new Element("label", nsrdfs);
					te.addContent(v.split("#")[1]);
					e.addContent(te);
				}
				System.out.println(v+"\t\t"+e.getChildText("label",nsrdfs));
			}
			XMLOutputter outputer = new XMLOutputter();
			Format format = Format.getPrettyFormat();
			outputer.setFormat(format);
			outputer.output(doc, new FileOutputStream("file/Wot.owl"));
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void generateNode() {
		Driver driver = GraphDatabase.driver( "bolt://"+URL, AuthTokens.basic( "neo4j", "930208" ) );
		Session session = driver.session();
		
		String xmlpath="file/Wot.owl";
		SAXBuilder builder=new SAXBuilder();
		try {
			Document doc = builder.build(xmlpath);
			Element root = doc.getRootElement();
			System.out.println(root.getName());
			Namespace nsowl = Namespace.getNamespace("http://www.w3.org/2002/07/owl#");
			Namespace nsrdf = Namespace.getNamespace("http://www.w3.org/1999/02/22-rdf-syntax-ns#");
			Namespace nsrdfs = Namespace.getNamespace("http://www.w3.org/2000/01/rdf-schema#");
			List<Element> list = root.getChildren("Class",nsowl);
			System.out.println(list.isEmpty());
			for(Element e : list) {
				String v = e.getAttributeValue("about",nsrdf);
				String label = e.getChildText("label",nsrdfs);
				String statement = "create (:Class {label:'"+label+"',ref:'"+v+"'})";
				session.run(statement);
				System.out.println(v+"\t\t"+label);
			}
			String statement = "create (:Class {label:'Literal',ref:'http://www.w3.org/2000/01/rdf-schema#Literal'})";
			session.run(statement);
			session.close();
			driver.close();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void generateRelation() {
		Driver driver = GraphDatabase.driver( "bolt://"+URL, AuthTokens.basic( "neo4j", "930208" ) );
		Session session = driver.session();
		
		String xmlpath="file/Wot.owl";
		SAXBuilder builder=new SAXBuilder();
		XPathFactory factory = XPathFactory.instance();
		String statement;
		try {
			Document doc = builder.build(xmlpath);
			Element root = doc.getRootElement();

			Namespace nsowl = Namespace.getNamespace("owl","http://www.w3.org/2002/07/owl#");
			Namespace nsrdf = Namespace.getNamespace("rdf","http://www.w3.org/1999/02/22-rdf-syntax-ns#");
			Namespace nsrdfs = Namespace.getNamespace("rdfs","http://www.w3.org/2000/01/rdf-schema#");
			List<Element> list = root.getChildren("Class",nsowl);
			for(Element e : list) { //e是其中每个Class
				String subjectRef = e.getAttributeValue("about", nsrdf);
				List<Element> subclass = e.getChildren("subClassOf", nsrdfs);
				for (Element  ee : subclass) {//ee是每个subClass
					//输出所有subclass关系
					if (ee.getChildren().isEmpty()) {
						String objectRef = ee.getAttributeValue("resource", nsrdf);
						statement = "match (d:Class {ref:'"+subjectRef+"'}),(p:Class {ref: '"+objectRef+"'}) "
								+ "merge (d)-[:subClassOf {label:'subClassOf',ref:'http://www.w3.org/2000/01/rdf-schema#subClassOf'}]->(p)";
						System.out.println("子类关系:\t" + statement);
						session.run(statement);
					}
					//输出所有其他关系
					else {
						Element res = ee.getChild("Restriction", nsowl);
						List<Element> relationship = res.getChildren();
						String rlabel = null;
						String relation = null;
						String objectRef = null;
						for (Element eee : relationship) {//eee是Restriction里的内容
							if(eee.getName().equals("onProperty")) {
								System.out.println("-------------------------------------------");
								relation = eee.getAttribute("resource", nsrdf).getValue();
								if(relation.equals("http://www.w3.org/1999/02/22-rdf-syntax-ns#type")) {
									rlabel = "type";
								} 
								else {
									System.out.println(relation);
									List<Namespace> nss = new ArrayList<Namespace>();
									nss.add(nsowl);nss.add(nsrdf);
									XPathExpression<Element> objs = factory.compile("//owl:ObjectProperty[@rdf:about='"+relation+"']",Filters.element(),null, nss);
									List<Element> result = objs.evaluate(doc);
									if(result.isEmpty()) {
										objs = factory.compile("//owl:DatatypeProperty[@rdf:about='"+relation+"']",Filters.element(),null, nss);
										result = objs.evaluate(doc);
									}
									System.out.println(result.isEmpty());
									rlabel = result.get(0).getChildText("label",nsrdfs);
									System.out.println(rlabel);
								}
							}
							
							else {
								Attribute tmp = eee.getAttribute("resource", nsrdf);
								if(tmp != null) {
									String s = eee.getAttribute("resource", nsrdf).getValue();
									if(s.contains("http://www.w3.org/2001/XMLSchema#")) {
										objectRef = "http://www.w3.org/2000/01/rdf-schema#Literal";
									}
									else 
										objectRef = s;
								}
							}
						}
						statement = "match (d:Class {ref:'"+subjectRef+"'}),(p:Class {ref: '"+objectRef+"'}) "
								+ "merge (d)-[:"+relation.split("#")[1]+" {label:'"+ rlabel  +"',ref:'"+relation+"'}]->(p)";
						System.out.println("非子类关系:\t"+statement);
						session.run(statement);
					}
				}
			}
			session.close();
			driver.close();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void deleteAll() {
		Driver driver = GraphDatabase.driver( "bolt://"+URL, AuthTokens.basic( "neo4j", "930208" ) );
		Session session = driver.session();
		
		String statement = "match (a)-[r]->(b) delete r";
		session.run(statement);
		statement = "match (a) delete a";
		session.run(statement);
		session.close();
		driver.close();
	}
	
	public void deleteAllLink() {
		Driver driver = GraphDatabase.driver( "bolt://"+URL, AuthTokens.basic( "neo4j", "930208" ) );
		Session session = driver.session();
		String statement = "match (d:DBpedia),(p)-[r]->(d) delete r delete d";
		session.run(statement);
		session.close();
		driver.close();
	}

}
