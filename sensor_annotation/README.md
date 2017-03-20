# This is a semantic annotation system for Web of Things
This web application is constructed based on JSP techniques. It takes SSM (spring + spring-MVC + Mybatis) frameworks with maven as construction tool. The author uses Eclipse Neon1 as IDE and tomcat as the server. So, following contents will take these for examples.
## How to run the web application
This application uses THREE methods to implement the data persistence:

- **Mysql**

	This is a relational database. In this app, it is used  to interact with DBpedia which is a public knowledge base.
- **Jena-TDB**

	This is a file storage system. Its input and output are both in OWL format(*.owl files). You can treat it as a database. But it is really not a database.In this app, it is used to conduct reasoning operations.
- **Neo4j**

	This is a graphic database with a nice presentation at web front end. It does not act as a functional module in this app. It is just for presentation of the nodes and the relations.

So, to run the application, you should first guarantee that your computer is equipped with Mysql and Neo4j and keep them alive before you run the app.If not, go to their official website to download proper version for your operation system.  
<hr>
About maven environmental settings, you can take the following links as reference:

- for [Windows](http://jingyan.baidu.com/article/d8072ac45d3660ec94cefd51.html) 

- for [Ubuntu](http://blog.csdn.net/scorpion_zs/article/details/53128489)

<hr>

About Tomcat settings within eclipse, you can look [link1](http://jingyan.baidu.com/article/ca2d939dd90183eb6d31ce79.html) or [link2](http://blog.csdn.net/yerenyuan_pku/article/details/51830104).

<hr>

To successfully run the application there are something you should pay attention to.

- Right click the project -> properties -> Deployment Assembly. Please make sure that **Maven Dependencies** is in the list. If not, add it.

- Sometimes the process of importing the maven project could be blocked because of the network issues. These two links are useful: 

 	* [Click me~](https://www.oschina.net/code/snippet_151849_49131)
 	* [The content is different from the one above. So, click me!](https://segmentfault.com/q/1010000008178782/a-1020000008178968)

<hr>

VERY IMPORTANT!

VERY IMPORTANT!

VERY IMPORTANT!

THREE TIMES FOR IMPORTANT THINGS!

Database and paths settings:

1. your project -> src -> main -> webapp -> WEB-INF -> config -> jdbc.properties

	Please modify the settings for your Mysql including port, username and password.

2. Enter your Mysql and create a database named **sensor_annotation**:
	>create database sensor_annotation;

	Conduct data migration of Mysql: click **[here](http://blog.chinaunix.net/uid-20761674-id-136275.html)** for reference.

	source: SWoT/mysql.sql

3. your project -> src -> main -> java -> com.yyn.service -> Neo4jConnector.java
your project/src/main/java/com/yyn/util/RDF2NEO.java


	Please modify the settings for your Neo4j including username and password in the above two files.

4. Configure settings for your jena-TDB.


	Find /sensor_annotation/src/main/java/com/yyn/service/StartupListener.java.

	The 21st line : 
	>Dataset dataset = RDFReasoning.getDataset(tdbRoot, "sensor_annotation", "Wot.owl");
	
	This is the input path. You can modify it according your needs. The actual path is 
	>D:\eclipse\exercise\\.metadata\\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\sensor_annotation\WEB-INF\RDF_Database
	
	Find /sensor_annotation/src/main/java/com/yyn/util/RDFReasoning.java

	The 101st line:
	>String rdfRoot = "*******";
	
	This is the output path. You can modify it according your needs.

5. A useful software: Protege

	This is an ontolgy constructing tool which can operate on the *.owl files.

	**[Download](http://protege.stanford.edu/download/protege/4.3/installanywhere/Web_Installers/)** 

6. Example database back up:

	- jena-TDB : SWoT\tdb
	
		target path : D:\eclipse\exercise\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\sensor_annotation\WEB-INF\RDF_Database\sensor_annotation

	- Neo4j : SWoT\graph1.db
	
		target path : D:\Neo4j CE 3.1.2\graphdb

7. /sensor_annotation/src/main/java/com/yyn/service/DeviceService.java

	line 36: It should be 

	>private static final String NS_WOT = "https://raw.githubusercontent.com/minelabwot/SWoT/master/swot-o.owl#";

	to use SWoT/swot-o.owl which is a predifined ontology.

<hr>
Tip: 

When you encounter some puzzling red crosses on the project menu in your eclipse,

try 

> Right click your project name -> refresh

and 

> Right click your project name -> maven -> update project
<hr>
Now , open your brower and enter **http://localhost:8080/sensor_annotation/**.

Enjoy the system~~~

## Some file comment
1. /sensor_annotation/src/main/java/com/yyn/util/RDF2NEO.java

	Clear neo4j database and insert the predefined ontology.

## Manual annotation system 
1. /sensor_annotation/src/main/java/com/yyn/service/StartupListener.java

	Get dataset for Jena-TDB.

2. /sensor_annotation/src/main/java/com/yyn/controller/DeviceController.java

	line 70~75 

	add device data to the three databases.

## Automatic annotation system
1. /sensor_annotation/src/main/java/com/yyn/controller/ELController.java

	EL algorithm.