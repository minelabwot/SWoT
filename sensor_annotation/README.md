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
About maven environmental settings, please Google of Baidu for yourself.
<hr>

