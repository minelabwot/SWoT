# Semantic Web of Things Project<br>
###*A Hybrid Semantic Annotation, Extraction and Reasoning Framework for Cyber-Physical System*

> Please cite these papers in your publications<br>
> [1] Wu Z, Xu Y, Yang Y, Zhang C, Zhu X, Ji Y. Towards a Semantic Web of Things: A Hybrid Semantic Annotation, Extraction, and Reasoning Framework for Cyber-Physical System. Sensors. 2017; 17(2):403.<br>
> [2] Wu Z, Xu Y, Zhang C, et al. Towards Semantic Web of Things: From Manual to Semi-automatic Semantic Annotation on Web of Things[M]// Big Data Computing and Communications. Springer International Publishing, 2016.<br>
> [3] Wu Z, Itälä T, Tang T, et al. A web-based two-layered integration framework for smart devices[J]. EURASIP Journal on Wireless Communications and Networking, 2012, 2012(1):150.<br>
> [4] Xu, Y.; Zhang, C.; Ji, Y. An Upper-Ontology-Based Approach for Automatic Construction of IOT Ontology. Int. J. Distribu. Sensor Netw. 2014, 2014, 594782.<br>
> [5] Chunhong Zhang, Miao Zhou, Xiao Han, Zheng Hu and Yang Ji, Knowledge Graph Embedding for Hyper-relational Data，TSINGHUA SCIENCE AND TECHNOLOGY，2017 Vol 2.<br>
> [6] Zhang, Chunhong，Fu, Wei，Sun, Tingting，Ji, Yang，Resolving Web Services Mismatch in Mashup，Wireless Personal Communications，2016，86（4）: 1781-1796<br>
> [7] Fengzi Wang，Xinning Zhu，Jiansong Miao，Semantic Trajectories Based Social Relationships Discovery Using WiFi Monitors，Personal and Ubiquitous Computing， 2016.11.15，1~12.  
> [8] Yang, Yunong, Z. Wu, and X. Zhu. "Semi-automatic metadata annotation of Web of Things with knowledge base." IEEE International Conference on Network Infrastructure and Digital Content IEEE, 2017:124-129.  
> [9] Wu, Z.; Guo, Y.; Lin, W.; Yu, S.; Ji, Y. A Weighted Deep Representation Learning Model for Imbalanced Fault Diagnosis in Cyber-Physical Systems. Sensors 2018, 18, 1096.  
> [10] Wu, Zhenyu, W. Lin, and Y. Ji. "An Integrated Ensemble Learning Model for Imbalanced Fault Diagnostics and Prognostics." IEEE Access 6(2018):8394-8402.   
> [11] Zhenyu Wu, Hao Luo, Yunong Yang, Xinning Zhu, Xiaofeng Qiu. An Unsupervised Degradation Estimation Framework for Diagnostics and Prognostics in Cyber-Physical System. In Proceedings of the 2018 IEEE World Forum on Internet of Things (WF-IoT '18), Singapore, 2018.  
> [12] Guo, Yang, Z. Wu, and Y. Ji. "A Hybrid Deep Representation Learning Model for Time Series Classification and Prediction." International Conference on Big Data Computing and Communications IEEE Computer Society, 2017:226-231.  
> [13] Luo, Hao, Z. Wu, and X. Zhu. "A Cluster-Based Hidden Markov Model for High-Level State Discovery from Time Series." International Conference on Big Data Computing and Communications IEEE Computer Society, 2017:218-225.

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

* **Sensor-Observation-State-Event**: Sensors observes some objects with SensorProperty which has low-level raw data, and high-level state could be extracted from these observing raw data. The observed system runs and switches among certain states, and when the internal state of the observed system is changed, an event will be generated and published. The high-level state could be extracted by pattern discovery and prediction method based on streaming mining algorithms, such as unsupervised cluster model, Hidden Markov Model (HMM) or deep learning model with temporal feature representations. In the figure, the processing of raw streaming data into high-level state and event are represented with dotted line，and the line does not mean the exact predicts/relations between the entities but only describes reference methods of how data stream could be transformed into states or events. The SAO ontology is reused to annotate streaming sensory data for further high-level state mining and reasoning with other prior knowledge as proposed.* **Event-Rule-Actuator-Action**:  Since events were generated from sensory observations, the rule will be defined by service providers/developers which describes which event to subscribe to and what action should be triggered by actuators in some conditions.  The rule is defined for further semantic reasoning by combining forward knowledge with events (ssn:Sensors :hasState :State and :generates ssn:Stimulus) and backward knowledge with actions (san:Actuator :triggers :Action). In an automatic controlling CPS application, the action could change the current state of system to another one. To better describe the actions performed by actuators, the SAN ontology is reused to annotate actuators or controllers. IoT-O could be a reference reusable ontology as well.* **WoTProperty**: WoTProperty describes some basic properties of WoT resources, including Location, EntityType, Owner and Unit, and SensorProperty is inherited from WoTProperty. WoTProperty contains more commonsense knowledge and facts, which could be linked to entities and concepts in existing global KBs. In this paper, DBpedia is used as the background KB.* **FeatureofInterests**: Feature of Interest (FoI) defines the CPS scenario which is composed of related sensors or actuators. It includes the relations between devices which is interlinked with predefined sets of rules. The rule defines which WoTProperty of devices are considered in the scenario and which SensorProperty of device should be acts as the filtering condition of the scenario. In SWoT framework, FoI will be used as a set of rules to automatically composing related devices as certain CPS scenarios. * **PhysicalProcess**: The properties of many real world features observed by sensors are related by physical processes. The Physical Process models this as directed relationship of a source sensorProperty (ssn:hasInput) that influences a target sensorProperty (ssn:hasOutput) via itermediateProperty. It represents causal relationship between source property of one device and target property of another device. For example, in building automation system, the state of cooling machine or number of people in the room both influence the indoor energy (intermediateProperty), while energy influence the indoor temperature. Hence, by modeling the process chain between devices with their properties and generated events, it could be used for causal reasoning tasks, such as anomaly diagnosis in building automation systems or predictive maintenance for industrial machines. 


According to SWoT-O vocabulary, we then setup a basic domain knowledge base of how these sensors and actuators collaborate with each other via SWoT-O Annotator. The temperature sensor and camera are annotated as ssn:Sensor with :WoTProperty, such as qu:Unit, :Location (:Region and :Spot), :Owner and :EntityType, while the CAC is annotated as san:Actuator with :Action. The ssn:FeatureofInterest is modeled as the target scenario composed with :SensorProperty of temperature sensor, camera and CAC. The :PhysicalProcess is modeled as the causal relation among these there devices with their :SensorProperty as input and output parameters. In this use case, the causal relations are categorized into two kind (:PositiveCorrelationProcess and :NegativeCorrelationProcess) as a reference knowledge for diagnosing the cause of anomaly.
<img src="./Github_src_readme_files/Paper/8-new.jp2"/>

**The SWoT-O ontology could be referred [here](./swot-o.owl)**

## 3.Proof-of-Concept Demonstration
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
#### Step 4: Initialize Anomaly Diagnosis Model and Exectue Reasoning Rules
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

## Future Works

1. Deep Learning for Pattern/State Recognition and Prediction of Time Series ([link](https://github.com/minelabwot/DeepLearning_WoT))
2. Semantic State Finder for Time Series via Cluster and Hidden Markov model ([link](https://github.com/minelabwot/Semantic_State_Finder_Time_Series))