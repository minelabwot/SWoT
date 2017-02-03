# Semantic Web of Things Project<br>
####*A Hybrid Semantic Annotation, Extraction and Reasoning Framework for Cyber Physical System*
## 1.SWoT Architecture
The whole system is composed of four main components: SWoT-O Annotator, EL Annotator, Knowledge Storage and Semantic Reasoner.
 
<img src="./Github_src_readme_files/Paper/1.tif"/>

#### •	SWoT-O Annotator
This building block is designed for generating metadata representations templates and annotating semantics of WoT resources with SWoT-O vocabulary. The SWoT-O is extended from SSN upper ontology and reuses some other IoT ontologies. It describes a Sensor-Observation-Event-Rule-Actuator-Action model that is useful for further reasoning tasks in most of intelligent and automatic CPS applications, and it also describes some common necessary attributes of physical devices (including sensors and actuators), such as Location, Ownership, Unit and DeviceType. The annotator provides a graphical user interface (UI) for service modeler to create domain services.
#### •	EL Annotator
This building block is designed for extracting semantic entities from the metadata of WoT resources and aligning SWoT ontologies with the concepts in the commonsense KB. In our testbed, DBpedia is used for the referent commonsense KB. The input of this module is the annotated WoT instances according to SWoT-O ontology, and the output is the annotated WoT metadata data with linked entities and aligned ontological concepts to DBpedia. The subtasks are divided into Schema Type Extraction & Identification, Candidate Entity Generation & Ranking and Relation Extraction, The extraction model is extended from EL framework that is usually used for semantic extraction and alignment on relational Web data.
#### •	Knowledge Storage
This building block provides common APIs for storing knowledge graph into persistent database. For storage efficiency and scalability, the graph database is proposed to be used. Concepts, properties, entities and relationships in Resource Description Framework (RDF) formats are transferred to graphical data structures. For compatible with existing semantic reasoner, RDF-based knowledge storage is also used. The query of the KB is based on a SPARQL-compatible language which could be also used for graph database.
#### •	Semantic Reasoner
This building block is aimed at providing a semantic reasoning capability based on the linked hybrid knowledge. The reasoning process could be both based on logical rules or statistical method or hybrid method. The query is based on a SPARQL language, and a list of ranked entities that matches the query are returned. The rule is modeled based on Jena’s rule language, and the reasoning process is based on Jena API and Jena Reasoner. 

## 2.SWoT-O Ontology
To provide a uniform ontology of CPS applications, the SWoT base ontology (SWoT-O) is mainly referred to and extended from SSN ontology, as well as reusing other IoT ontologies, such as Semantic Actuator Network (SAN) for actuator, Stream Annotation Ontology (SAO) for streaming data and QUDT ontology for units of measure. 

<img src="./Github_src_readme_files/Paper/2-new.jp2"/>
<br>

According to SWoT-O vocabulary, we then setup a basic domain knowledge base of how these sensors and actuators collaborate with each other via SWoT-O Annotator. The temperature sensor and camera are annotated as ssn:Sensor with :WoTProperty, such as qu:Unit, :Location (:Region and :Spot), :Owner and :EntityType, while the CAC is annotated as san:Actuator with :Action. The ssn:FeatureofInterest is modeled as the target scenario composed with :SensorProperty of temperature sensor, camera and CAC. The :PhysicalProcess is modeled as the causal relation among these there devices with their :SensorProperty as input and output parameters. In this use case, the causal relations are categorized into two kind (:PositiveCorrelationProcess and :NegativeCorrelationProcess) as a reference knowledge for diagnosing the cause of anomaly.
<img src="./Github_src_readme_files/Paper/8-new.jp2"/>

**The SWoT-O ontology could be referred [here](./swot-o.owl)**

## 3.Proof-of-Concept Demostration
The scenarios are composed of a temperature sensor, a camera sensor and a Cooling Air Condition (CAC) deployed in each room of buildings at different locations. The temperature sensor could directly detect the indoor temperature, while the CAC could tune the indoor temperature by turn on/off/up/down of the machine. The camera is used to detect the occupation of the room and the exact number of persons in the room. Our goal is to provide anomaly diagnosis and automatic temperature adjusting services according to indoor temperature anomaly events. 

* According to SWoT-O vocabulary, we then setup a basic domain knowledge base of how these sensors and actuators collaborate with each other via SWoT-O Annotator.
* To link the knowledge base with common knowledge in DBpedia, we run EL Annotator to execute EL task. In this use case, the instances of :Region, qu:Unit, :Onwer and :EntityType are linked to the DBpedia., and the linking relations are stored into both [Jena TDB](./graph1.db) and [Neo4j](./tdb). 
* Based on the hybrid knowledge base, the system will perform semantic search and reasoning tasks. Since devices are created and deployed in a distributed manner, the first step is to query the target devices which could be composed into temperature adjusting and anomaly diagnosis scenario. While, the second step is to inference the cause of the anomaly once anomaly event occurs, and the system will automatically adjust the indoor temperature by controlling CAC.

#### Step 1: UI for Creating New Devices with SWoT-O
<img src="./Github_src_readme_files/1.jp2"/>
<br>

The instances of WoT Entities are created by a editing tools. The tool provide a UI for service modeler to define the descriptive information of a WoT resources. The descriptors are annotated with SWoT-O, such as SensorType, Observations(Propoerty), Unit, Owner, Location／Region and Location/Spot. Once the WoT Entity is initialized, then the data will be stored into the knowledge stotage ([Jena TDB](./graph1.db) and [Neo4j](./tdb))
<br>

<img src="./Github_src_readme_files/13.jp2"/>
#### Step 2: View constructed domain KB via protege
The annotated WoT resources are stored in the Jebn TDB, and protege could be used to view the domain KB. The picture below illustrates the domain knoweldge constructed based on SWoT-O ontology, according to the anomaly diagnosis and automatic temperature adjusting services scenario. 

<img src="./Github_src_readme_files/Paper/8-2.tif"/>
<br>

The contents of "hasType", "hasLocation", "hasSpot", "hasUnit", "defaultObserved' and "isOwnedBy" are filled by service modeler in Step 1.

<img src="./Github_src_readme_files/2.jp2"/>

The observed features are labled by axiom annotations in Protege. For instance, the temperature sensor is labled with "Temperature" as "defaultObserved" feature.
<br>
Since domain knowledge are linked to commonsense knowledge via EL model, for example the instances of Region, SensorType, Owner and Unit are linked and aligned to entities of Location, Organization and UnitofMeasurement in DBpedia, the common relationships are inherited to the domain knowledge as well. The enriched knowledge could be used to facilitate searching for semantic entities which has common relationships. To annotate the linking relationship between domain and commonsense knowledge, the "linkTo" property is used to represent the linkage.

<img src="./Github_src_readme_files/3.jp2"/>

#### Step 3: Entity Linking for domain WoT Knowledge

There is an Entity Linking function at the device infobox web page. By clicking on the "EL" button, the system will automatically run the [EL algorithm](./Message_Passing_Algorithm_11_14), and instances of Region, SensorType, Owner and Unit are linked and aligned to entities of Location, Organization and UnitofMeasurement in DBpedia.
<br>
To annotate the linking relationship between domain and commonsense knowledge, the "linkTo" property is used to represent the linkage. The EL results will be:
<br>
1. updated in the Neo4j for KB display
<br>
2. updateed in Jena TDB for further semantic search and reasoning via Jena Reasoner

<img src="./Github_src_readme_files/5.jp2"/>
<br>
<img src="./Github_src_readme_files/6.jp2"/>
<br>

The EL results are stored in Neo4i graph database. The blue circles represent the linked entities and types to DBpedia. It is annotated as “linkTo” property

<img src="./Github_src_readme_files/Paper/16.jp2"/>
<br>
<img src="./Github_src_readme_files/Paper/16-2.jp2"/>
#### Step 4: Initialize Aomaly Diagnosis Model and Exectue Reasoning Rules
In our case, it is assumed that the anomaly events have been detected via anomaly detection algorithms and annotated with SWoT-O ontology. By triggering a “High_Temperature_Anomaly_Event”, the reasoner will infer that the Occupation of the room and the State of the CAC have either positive or negative correlations with the high temperature Event, thus to adjust the temperature to normal state, the “Turn-down” operation of CAC will be actioned automatically. 

The system provides APIs to syncronize sensory data and a simulator could also be used for generating simulating sensor data for experiments.

<img src="./Github_src_readme_files/7.jp2"/>
<br>


A high temperature anomaly event (temp_high_237) is modeled via protege before searching and reasoning.

<img src="./Github_src_readme_files/8.jp2"/>
<br>

In our systems, there are lots of WoT devices. To compose related devices into our proposed scenario, a semantic fuzzy search is executed based on the hybrid KB (domain and linked commonsense knowledge). There is a search UI to edit the search parameters. For instance, Beijing and Shanghai are both a city of China, and these knowledge have already been stored in the DBpedia already. If a query of “Searching the sensors located in China”, then the commonsense knowledge could be used for inference the correlations among sensors have similar properties. The inference process could be divided into two steps that one is to search for the domain Region instances which has linkTo properties with entities in DBpedia and the other is to search these linked entities which meets the queried relations (located in China). The SPARQL query is illustrated below:

	SELECT DISTINTC ?deviceID
	WHERE{
	?device swot:deviceID ?deviceID.
	?device dul:hasLocation ?loc_local.
	?loc_local swot:linkTo ?loc_el.
	BIND(URI(?loc_el) as ?loc_el_uri).
	?loc_local a swot:Region.
		Service <http://dbpedia.org/sparql> {
			?loc_el_uri ?rel ?loc_db.
			FILTER regex(str(?loc_db), "China")
		}
	}
	
The Three figures below illustrate fuzzy search according to the Location and Owner of the WoT devices. Matched resources could be composed as the proposed building automation scenarios. 

<img src="./Github_src_readme_files/10.jp2"/>
<br>
<img src="./Github_src_readme_files/11.jp2"/>
<br>
<img src="./Github_src_readme_files/12.jp2"/>

After the Anomaly Diagnosis Model is initialized, the system will execute reasoning rules via Jena Reasoner, according to the predefined semantic rules (modeled as a set of SPARUL statements). Once anomaly events are detected, the system will deduce the cause of the anomalies and the diagnosis results will be recorded. A semantic fuzzy search could be used to lists the anaomaly events with their causal devices. For instance, the temp_high_237 anomaly event is caused by device2 (Camera-Occupation of the room is high) and CAC (the state of the CAC is turned off).  

<img src="./Github_src_readme_files/9.jp2"/>
