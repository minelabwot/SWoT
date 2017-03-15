-- MySQL dump 10.13  Distrib 5.7.17, for Linux (x86_64)
--
-- Host: localhost    Database: sensor_annotation
-- ------------------------------------------------------
-- Server version	5.7.17-0ubuntu0.16.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `column_type`
--

DROP TABLE IF EXISTS `column_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `column_type` (
  `table_id` varchar(45) NOT NULL,
  `company` text,
  `unit` text,
  `sensorType` text,
  `property` text,
  `region` text,
  PRIMARY KEY (`table_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `column_type`
--

LOCK TABLES `column_type` WRITE;
/*!40000 ALTER TABLE `column_type` DISABLE KEYS */;
INSERT INTO `column_type` VALUES ('1','http://www.w3.org/2002/07/owl#Thing','http://dbpedia.org/class/yago/Abstraction100002137','http://www.w3.org/2002/07/owl#Thing','http://www.w3.org/2002/07/owl#Thing','http://dbpedia.org/class/yago/YagoPermanentlyLocatedEntity');
/*!40000 ALTER TABLE `column_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `datatransform`
--

DROP TABLE IF EXISTS `datatransform`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `datatransform` (
  `device_id` int(11) NOT NULL,
  `provider` varchar(45) DEFAULT NULL,
  `type` varchar(45) DEFAULT NULL,
  `name` varchar(45) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  `property` varchar(45) DEFAULT NULL,
  `sensorType` varchar(45) DEFAULT NULL,
  `unit` varchar(45) DEFAULT NULL,
  `region` varchar(45) DEFAULT NULL,
  `spot` varchar(45) DEFAULT NULL,
  `company` varchar(45) DEFAULT NULL,
  `property_url` text,
  `company_url` text,
  `region_url` text,
  `unit_url` text,
  `sensorType_url` text,
  `status` int(11) DEFAULT '0',
  `table_id` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`device_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `datatransform`
--

LOCK TABLES `datatransform` WRITE;
/*!40000 ALTER TABLE `datatransform` DISABLE KEYS */;
INSERT INTO `datatransform` VALUES (65,'xiaobai','Sensor','device1','for temp','Temperature','TemperatureSensor','degree','Beijing','Room_612','BUPT','http://dbpedia.org/resource/Temperature,http://dbpedia.org/resource/Category:Temperature,http://dbpedia.org/resource/Operating_temperature,http://dbpedia.org/resource/Orders_of_magnitude_(temperature),http://dbpedia.org/resource/Color_temperature','http://dbpedia.org/resource/BUPT','http://dbpedia.org/resource/Beijing,http://dbpedia.org/resource/Beijing_Guoan_F.C.,http://dbpedia.org/resource/Beijing_Renhe_F.C.,http://dbpedia.org/resource/Beijing_Subway,http://dbpedia.org/resource/Category:Beijing_Subway_stations','http://dbpedia.org/resource/Master\'s_degree,http://dbpedia.org/resource/Bachelor\'s_degree,http://dbpedia.org/resource/Honorary_degree,http://dbpedia.org/resource/Degree,http://dbpedia.org/resource/Category:Recipients_of_the_Order_of_St._George_of_the_Fourth_Degree','',1,'1'),(72,'xiaobai','Sensor','device2','for cooling','Cooling','CoolingAC','degree','Beijing','Room_612','BUPT','http://dbpedia.org/resource/Water_cooling,http://dbpedia.org/resource/Category:Cooling_technology,http://dbpedia.org/resource/Cooling,http://dbpedia.org/resource/Computer_cooling,http://dbpedia.org/resource/Air_cooling','http://dbpedia.org/resource/BUPT','http://dbpedia.org/resource/Beijing,http://dbpedia.org/resource/Beijing_Guoan_F.C.,http://dbpedia.org/resource/Beijing_Renhe_F.C.,http://dbpedia.org/resource/Beijing_Subway,http://dbpedia.org/resource/Category:Beijing_Subway_stations','http://dbpedia.org/resource/Master\'s_degree,http://dbpedia.org/resource/Bachelor\'s_degree,http://dbpedia.org/resource/Honorary_degree,http://dbpedia.org/resource/Degree,http://dbpedia.org/resource/Category:Recipients_of_the_Order_of_St._George_of_the_Fourth_Degree','',1,'1'),(75,'xiaobai','Sensor','device3','for population','Occupation','Camera','frame','Beijing','Room_612','BUPT','http://dbpedia.org/resource/Allied-occupied_Germany,http://dbpedia.org/resource/Music_artist_(occupation),http://dbpedia.org/resource/Occupation_of_Japan,http://dbpedia.org/resource/Category:People_by_occupation,http://dbpedia.org/resource/German_occupation_of_Czechoslovakia','http://dbpedia.org/resource/BUPT','http://dbpedia.org/resource/Beijing,http://dbpedia.org/resource/Beijing_Guoan_F.C.,http://dbpedia.org/resource/Beijing_Renhe_F.C.,http://dbpedia.org/resource/Beijing_Subway,http://dbpedia.org/resource/Category:Beijing_Subway_stations','http://dbpedia.org/resource/Numeric_std,http://dbpedia.org/resource/Numeric_pi,http://dbpedia.org/resource/Numeric,http://dbpedia.org/resource/Numeric_pad,http://dbpedia.org/resource/Numeric_data','http://dbpedia.org/resource/Camera,http://dbpedia.org/resource/Chamber_music,http://dbpedia.org/resource/Upper_house,http://dbpedia.org/resource/House_of_Commons_of_Canada,http://dbpedia.org/resource/Lok_Sabha',1,'1'),(79,'xiaobai','Actuator','device4','for air cleaning','AirCleaner','AirCleaner','PPM','Beijing','Room_612','BUPT','','http://dbpedia.org/resource/BUPT','http://dbpedia.org/resource/Beijing,http://dbpedia.org/resource/Beijing_Guoan_F.C.,http://dbpedia.org/resource/Beijing_Renhe_F.C.,http://dbpedia.org/resource/Beijing_Subway,http://dbpedia.org/resource/Category:Beijing_Subway_stations','http://dbpedia.org/resource/Ppm,http://dbpedia.org/resource/.ppm,http://dbpedia.org/resource/550_ppm,http://dbpedia.org/resource/PPM_58291,http://dbpedia.org/resource/PPM_66283','',1,'1'),(82,'xiaobai','Actuator','device5','for cooling','Cooling','CoolingAC','degree','Beijing','Room_612','BUPT','http://dbpedia.org/resource/Water_cooling,http://dbpedia.org/resource/Category:Cooling_technology,http://dbpedia.org/resource/Cooling,http://dbpedia.org/resource/Computer_cooling,http://dbpedia.org/resource/Air_cooling','http://dbpedia.org/resource/BUPT','http://dbpedia.org/resource/Beijing,http://dbpedia.org/resource/Beijing_Guoan_F.C.,http://dbpedia.org/resource/Beijing_Renhe_F.C.,http://dbpedia.org/resource/Beijing_Subway,http://dbpedia.org/resource/Category:Beijing_Subway_stations','http://dbpedia.org/resource/Master\'s_degree,http://dbpedia.org/resource/Bachelor\'s_degree,http://dbpedia.org/resource/Honorary_degree,http://dbpedia.org/resource/Degree,http://dbpedia.org/resource/Category:Recipients_of_the_Order_of_St._George_of_the_Fourth_Degree','',1,'1'),(83,'xiaobai','Sensor','device6','for PM		','PM','PM_dector','PPM','Beijing','Room_612','BUPT','http://dbpedia.org/resource/Am/pm,http://dbpedia.org/resource/Pm,http://dbpedia.org/resource/Distances_shorter_than_1_pm,http://dbpedia.org/resource/Toyota_concept_vehicles,_2000–09,http://dbpedia.org/resource/Pm.','http://dbpedia.org/resource/BUPT','http://dbpedia.org/resource/Beijing,http://dbpedia.org/resource/Beijing_Guoan_F.C.,http://dbpedia.org/resource/Beijing_Renhe_F.C.,http://dbpedia.org/resource/Beijing_Subway,http://dbpedia.org/resource/Category:Beijing_Subway_stations','http://dbpedia.org/resource/Ppm,http://dbpedia.org/resource/.ppm,http://dbpedia.org/resource/550_ppm,http://dbpedia.org/resource/PPM_58291,http://dbpedia.org/resource/PPM_66283','',1,'1'),(86,'xiaobai','Sensor','device7','for temp','Temperature','TemperatureSensor','degree','Los_Angeles','Room_303','UCLA','http://dbpedia.org/resource/Temperature,http://dbpedia.org/resource/Category:Temperature,http://dbpedia.org/resource/Operating_temperature,http://dbpedia.org/resource/Orders_of_magnitude_(temperature),http://dbpedia.org/resource/Color_temperature','http://dbpedia.org/resource/UCLA,http://dbpedia.org/resource/UCLA_Bruins,http://dbpedia.org/resource/Category:UCLA_Bruins_football_players,http://dbpedia.org/resource/Category:UCLA_Film_School_alumni,http://dbpedia.org/resource/Category:UCLA_Bruins_men\'s_basketball_players','http://dbpedia.org/resource/Los_Angeles,http://dbpedia.org/resource/Los_Angeles_Kings,http://dbpedia.org/resource/Los_Angeles_Rams,http://dbpedia.org/resource/Los_Angeles_Lakers,http://dbpedia.org/resource/LA_Galaxy','http://dbpedia.org/resource/Master\'s_degree,http://dbpedia.org/resource/Bachelor\'s_degree,http://dbpedia.org/resource/Honorary_degree,http://dbpedia.org/resource/Degree,http://dbpedia.org/resource/Category:Recipients_of_the_Order_of_St._George_of_the_Fourth_Degree','',1,'1'),(90,'xiaobai','Actuator','device8','for cooling','Cooling','CoolingAC','degree','Los Angeles','Room_303','UCLA','http://dbpedia.org/resource/Water_cooling,http://dbpedia.org/resource/Category:Cooling_technology,http://dbpedia.org/resource/Cooling,http://dbpedia.org/resource/Computer_cooling,http://dbpedia.org/resource/Air_cooling','http://dbpedia.org/resource/UCLA,http://dbpedia.org/resource/UCLA_Bruins,http://dbpedia.org/resource/Category:UCLA_Bruins_football_players,http://dbpedia.org/resource/Category:UCLA_Film_School_alumni,http://dbpedia.org/resource/Category:UCLA_Bruins_men\'s_basketball_players','http://dbpedia.org/resource/Los_Angeles,http://dbpedia.org/resource/Los_Angeles_Kings,http://dbpedia.org/resource/Los_Angeles_Rams,http://dbpedia.org/resource/Los_Angeles_Lakers,http://dbpedia.org/resource/LA_Galaxy','http://dbpedia.org/resource/Master\'s_degree,http://dbpedia.org/resource/Bachelor\'s_degree,http://dbpedia.org/resource/Honorary_degree,http://dbpedia.org/resource/Degree,http://dbpedia.org/resource/Category:Recipients_of_the_Order_of_St._George_of_the_Fourth_Degree','',1,'1'),(92,'xiaobai','Sensor','device9','for population','Occupation','Camera','Numeric','Los Angeles','Room_303','UCLA','http://dbpedia.org/resource/Allied-occupied_Germany,http://dbpedia.org/resource/Music_artist_(occupation),http://dbpedia.org/resource/Occupation_of_Japan,http://dbpedia.org/resource/Category:People_by_occupation,http://dbpedia.org/resource/German_occupation_of_Czechoslovakia','http://dbpedia.org/resource/UCLA,http://dbpedia.org/resource/UCLA_Bruins,http://dbpedia.org/resource/Category:UCLA_Bruins_football_players,http://dbpedia.org/resource/Category:UCLA_Film_School_alumni,http://dbpedia.org/resource/Category:UCLA_Bruins_men\'s_basketball_players','http://dbpedia.org/resource/Los_Angeles,http://dbpedia.org/resource/Los_Angeles_Kings,http://dbpedia.org/resource/Los_Angeles_Rams,http://dbpedia.org/resource/Los_Angeles_Lakers,http://dbpedia.org/resource/LA_Galaxy','http://dbpedia.org/resource/Numeric_std,http://dbpedia.org/resource/Numeric_pi,http://dbpedia.org/resource/Numeric,http://dbpedia.org/resource/Numeric_pad,http://dbpedia.org/resource/Numeric_data','http://dbpedia.org/resource/Camera,http://dbpedia.org/resource/Chamber_music,http://dbpedia.org/resource/Upper_house,http://dbpedia.org/resource/House_of_Commons_of_Canada,http://dbpedia.org/resource/Lok_Sabha',1,'1'),(94,'xiaobai','Sensor','device10','for temp','Temperature','TemperatureSensor','degree','Aarhus,Denmark','Room_107_Teaching_Building_4','Aarhus University','http://dbpedia.org/resource/Temperature,http://dbpedia.org/resource/Category:Temperature,http://dbpedia.org/resource/Operating_temperature,http://dbpedia.org/resource/Orders_of_magnitude_(temperature),http://dbpedia.org/resource/Color_temperature','http://dbpedia.org/resource/Aarhus_University,http://dbpedia.org/resource/Category:Aarhus_University_alumni,http://dbpedia.org/resource/Category:Aarhus_University,http://dbpedia.org/resource/Category:Aarhus_University_faculty,http://dbpedia.org/resource/Aarhus_University_Press','http://dbpedia.org/resource/Aarhus,_Denmark','http://dbpedia.org/resource/Master\'s_degree,http://dbpedia.org/resource/Bachelor\'s_degree,http://dbpedia.org/resource/Honorary_degree,http://dbpedia.org/resource/Degree,http://dbpedia.org/resource/Category:Recipients_of_the_Order_of_St._George_of_the_Fourth_Degree','',1,'1'),(98,'xiaobai','Sensor','device11','for pupulation','Occupation','Camera','Numeric','Aarhus,Denmark','Room_107_Teaching_Building_4','Aarhus University','http://dbpedia.org/resource/Allied-occupied_Germany,http://dbpedia.org/resource/Music_artist_(occupation),http://dbpedia.org/resource/Occupation_of_Japan,http://dbpedia.org/resource/Category:People_by_occupation,http://dbpedia.org/resource/German_occupation_of_Czechoslovakia','http://dbpedia.org/resource/Aarhus_University,http://dbpedia.org/resource/Category:Aarhus_University_alumni,http://dbpedia.org/resource/Category:Aarhus_University,http://dbpedia.org/resource/Category:Aarhus_University_faculty,http://dbpedia.org/resource/Aarhus_University_Press','http://dbpedia.org/resource/Aarhus,_Denmark','http://dbpedia.org/resource/Numeric_std,http://dbpedia.org/resource/Numeric_pi,http://dbpedia.org/resource/Numeric,http://dbpedia.org/resource/Numeric_pad,http://dbpedia.org/resource/Numeric_data','http://dbpedia.org/resource/Camera,http://dbpedia.org/resource/Chamber_music,http://dbpedia.org/resource/Upper_house,http://dbpedia.org/resource/House_of_Commons_of_Canada,http://dbpedia.org/resource/Lok_Sabha',1,'1'),(99,'xiaobai','Sensor','device12','Vehicle Traffic','Congestion','Camera','frame','Aarhus,Denmark','Volden street','COWI','http://dbpedia.org/resource/Congestion,http://dbpedia.org/resource/Network_congestion,http://dbpedia.org/resource/Nasal_congestion,http://dbpedia.org/resource/Traffic_congestion,http://dbpedia.org/resource/TCP_congestion','http://dbpedia.org/resource/COWI,http://dbpedia.org/resource/COWI_A/S','http://dbpedia.org/resource/Aarhus,_Denmark','http://dbpedia.org/resource/Fatal_Frame,http://dbpedia.org/resource/FRAME,http://dbpedia.org/resource/Roddy_Frame,http://dbpedia.org/resource/I-frame,http://dbpedia.org/resource/P-frame','http://dbpedia.org/resource/Camera,http://dbpedia.org/resource/Chamber_music,http://dbpedia.org/resource/Upper_house,http://dbpedia.org/resource/House_of_Commons_of_Canada,http://dbpedia.org/resource/Lok_Sabha',1,'1'),(103,'xiaobai','Sensor','device13','Weather Data','Humidity','Humidity_Sensor','percentage','Aarhus,Denmark','Room_107_Teaching_Building_4','CCSR','http://dbpedia.org/resource/Humidity,http://dbpedia.org/resource/Air_humidity,http://dbpedia.org/resource/Humidity_meter,http://dbpedia.org/resource/Humidity_probe,http://dbpedia.org/resource/Percent_humidity','','http://dbpedia.org/resource/Aarhus,_Denmark','http://dbpedia.org/resource/Body_fat_percentage,http://dbpedia.org/resource/Winning_percentage,http://dbpedia.org/resource/Percentage,http://dbpedia.org/resource/Fat_percentage,http://dbpedia.org/resource/Win_percentage','',1,'1'),(108,'xiaobai','Sensor','device14','Weather Data','Pressure','Barometer','mBar','Aarhus,Denmark','Volden street','CCSR','http://dbpedia.org/resource/Pressure,http://dbpedia.org/resource/Blood_pressure,http://dbpedia.org/resource/Category:Pressure,http://dbpedia.org/resource/Category:Political_pressure_groups_of_the_United_Kingdom,http://dbpedia.org/resource/Pressure_measurement','','http://dbpedia.org/resource/Aarhus,_Denmark','http://dbpedia.org/resource/Mbar,http://dbpedia.org/resource/Aere_Mbar','http://dbpedia.org/resource/Barometer,http://dbpedia.org/resource/Arab_Barometer,http://dbpedia.org/resource/Leech_Barometer,http://dbpedia.org/resource/Daily_barometer,http://dbpedia.org/resource/Barometer_light',1,'1'),(112,'xiaobai','Sensor','device15','Weather Data','Wind_direction','Wind_Direction_dector','degrees','Aarhus,Denmark','Volden street','CCSR','','','http://dbpedia.org/resource/Aarhus,_Denmark','http://dbpedia.org/resource/98_Degrees,http://dbpedia.org/resource/Degrees,http://dbpedia.org/resource/Category:Master\'s_degrees,http://dbpedia.org/resource/Six_Degrees_Records,http://dbpedia.org/resource/Category:Bachelor\'s_degrees','',1,'1'),(116,'xiaobai','Sensor','device16','Weather Data','Wind_speed','Wind_Speed_dector','kph','Aarhus','Volden street','CCSR','http://dbpedia.org/resource/Wind_speed_profile,http://dbpedia.org/resource/Wind_Speed,http://dbpedia.org/resource/Gradient_wind_speed,http://dbpedia.org/resource/Wind_speed_gradient,http://dbpedia.org/resource/Tropical_Cyclone_Wind_Speed_Climatology','','http://dbpedia.org/resource/Aarhus,http://dbpedia.org/resource/Aarhus_Gymnastikforening,http://dbpedia.org/resource/Aarhus_Fremad,http://dbpedia.org/resource/Aarhus_Municipality,http://dbpedia.org/resource/Category:Aarhus_Gymnastikforening_players','http://dbpedia.org/resource/ISO_639:kph,http://dbpedia.org/resource/KPH_Radio,http://dbpedia.org/resource/Kph,http://dbpedia.org/resource/KPH_(radio),http://dbpedia.org/resource/30_kph_zone','',1,'1'),(121,'xiaobai','Sensor','device17','Parking lot and Transportation','Congestion','Parking Sensor','percentage','Skolebakken,Aarhus','Parking lot','CCSR,Surrey,UK','http://dbpedia.org/resource/Congestion,http://dbpedia.org/resource/Network_congestion,http://dbpedia.org/resource/Nasal_congestion,http://dbpedia.org/resource/Traffic_congestion,http://dbpedia.org/resource/TCP_congestion','','','http://dbpedia.org/resource/Body_fat_percentage,http://dbpedia.org/resource/Winning_percentage,http://dbpedia.org/resource/Percentage,http://dbpedia.org/resource/Fat_percentage,http://dbpedia.org/resource/Win_percentage','',1,'1'),(126,'xiaobai','Sensor','device18','Air Pollution','Carbon_Monoxide','carbon_monoxide_dector','PPM','Aarhus, Denmark','Aarhus city','CCSR','http://dbpedia.org/resource/Carbon_Monoxide,http://dbpedia.org/resource/Category:Suicides_by_carbon_monoxide_poisoning,http://dbpedia.org/resource/Carbon_monoxide_poisoning,http://dbpedia.org/resource/Category:Carbon_monoxide,http://dbpedia.org/resource/Category:Deaths_from_carbon_monoxide_poisoning','','http://dbpedia.org/resource/Aarhus,_Denmark','http://dbpedia.org/resource/Ppm,http://dbpedia.org/resource/.ppm,http://dbpedia.org/resource/550_ppm,http://dbpedia.org/resource/PPM_58291,http://dbpedia.org/resource/PPM_66283','',1,'1'),(131,'xiaobai','Sensor','device19','Air Quality Index','Nitrogen_Dioxide','nitrogen_dioxide_dector','percentage','Aarhus,Denmark','Aarhus city','CCSR','http://dbpedia.org/resource/Nitrogen_Dioxide,http://dbpedia.org/resource/Nitrogen_dioxide_poisoning','','http://dbpedia.org/resource/Aarhus,_Denmark','http://dbpedia.org/resource/Body_fat_percentage,http://dbpedia.org/resource/Winning_percentage,http://dbpedia.org/resource/Percentage,http://dbpedia.org/resource/Fat_percentage,http://dbpedia.org/resource/Win_percentage','',1,'1'),(134,'xiaobai','Sensor','device20','environmental measurements in a meeting room','Carbon_Dioxide','Carbon_Dioxide_dector','PPM','Kista','meeting room 311','Ericsson','http://dbpedia.org/resource/Carbon_dioxide,http://dbpedia.org/resource/Category:Carbon_dioxide,http://dbpedia.org/resource/Carbon_dioxide_ice,http://dbpedia.org/resource/Carbon_dioxide_equivalent,http://dbpedia.org/resource/Carbon_dioxide_removal','http://dbpedia.org/resource/Ericsson,http://dbpedia.org/resource/Pär_Ericsson,http://dbpedia.org/resource/Category:Ericsson,http://dbpedia.org/resource/Xperia_Play,http://dbpedia.org/resource/Category:Sony_Ericsson_mobile_phones','http://dbpedia.org/resource/Kista,http://dbpedia.org/resource/Rinkeby-Kista_borough,http://dbpedia.org/resource/Kista_(disambiguation),http://dbpedia.org/resource/Kista_Rock,http://dbpedia.org/resource/Kista_Torn','http://dbpedia.org/resource/Ppm,http://dbpedia.org/resource/.ppm,http://dbpedia.org/resource/550_ppm,http://dbpedia.org/resource/PPM_58291,http://dbpedia.org/resource/PPM_66283','',1,'1'),(140,'xiaobai','Actuator','device21','Solar Controller','Temperature','Solar Heating','degree','Morpeth,UK','Daniel house','Thingspeak','http://dbpedia.org/resource/Temperature,http://dbpedia.org/resource/Category:Temperature,http://dbpedia.org/resource/Operating_temperature,http://dbpedia.org/resource/Orders_of_magnitude_(temperature),http://dbpedia.org/resource/Color_temperature','http://dbpedia.org/resource/ThingSpeak','http://dbpedia.org/resource/Morpeth_(UK_Parliament_constituency)','http://dbpedia.org/resource/Master\'s_degree,http://dbpedia.org/resource/Bachelor\'s_degree,http://dbpedia.org/resource/Honorary_degree,http://dbpedia.org/resource/Degree,http://dbpedia.org/resource/Category:Recipients_of_the_Order_of_St._George_of_the_Fourth_Degree','http://dbpedia.org/resource/Solar_heating,http://dbpedia.org/resource/Solar_water_heating,http://dbpedia.org/resource/Active_solar_heating,http://dbpedia.org/resource/Solar_heating_system,http://dbpedia.org/resource/Passive_solar_heating',1,'1'),(145,'xiaobai','Sensor','device22','Wind Speed','Wind_speed','anemometer','mps','Madrid','','Thingspeak','http://dbpedia.org/resource/Wind_speed_profile,http://dbpedia.org/resource/Wind_Speed,http://dbpedia.org/resource/Gradient_wind_speed,http://dbpedia.org/resource/Wind_speed_gradient,http://dbpedia.org/resource/Tropical_Cyclone_Wind_Speed_Climatology','http://dbpedia.org/resource/ThingSpeak','http://dbpedia.org/resource/Madrid,http://dbpedia.org/resource/Europe/Madrid,http://dbpedia.org/resource/Madrid_Metro,http://dbpedia.org/resource/Atlético_Madrid,http://dbpedia.org/resource/Real_Madrid_C','http://dbpedia.org/resource/MPs,http://dbpedia.org/resource/MPS_Records,http://dbpedia.org/resource/Category:UK_MPs_1974,http://dbpedia.org/resource/Category:UK_MPs_1910,http://dbpedia.org/resource/Category:UK_MPs_1910–18','http://dbpedia.org/resource/Anemometer,http://dbpedia.org/resource/Cup-anemometer,http://dbpedia.org/resource/Cup_anemometer,http://dbpedia.org/resource/Vane_anemometer,http://dbpedia.org/resource/Anemometer_Hill',1,'1'),(150,'xiaobai','Sensor','device23','Fridge temperature','Temperature','Fridge','Degrees_Celsius','Brattberg,Los,Sweden','kitchen','Thingspeak','http://dbpedia.org/resource/Temperature,http://dbpedia.org/resource/Category:Temperature,http://dbpedia.org/resource/Operating_temperature,http://dbpedia.org/resource/Orders_of_magnitude_(temperature),http://dbpedia.org/resource/Color_temperature','http://dbpedia.org/resource/ThingSpeak','','http://dbpedia.org/resource/Degrees_Celsius,http://dbpedia.org/resource/7_Degrees_Celsius','http://dbpedia.org/resource/Fridge,http://dbpedia.org/resource/Fridge_(band),http://dbpedia.org/resource/RV_Fridge,http://dbpedia.org/resource/Bar_Fridge,http://dbpedia.org/resource/Fridge_Day',1,'1'),(155,'xiaobai','Sensor','device24','Indoor air quality','AirCleaner','Air_purifier','PPM','United States','garage','BMW','','http://dbpedia.org/resource/BMW,http://dbpedia.org/resource/Category:BMW_Open,http://dbpedia.org/resource/BMW_in_Formula_One,http://dbpedia.org/resource/BMW_M3,http://dbpedia.org/resource/Category:BMW','http://dbpedia.org/resource/United_States,http://dbpedia.org/resource/United_States_Army,http://dbpedia.org/resource/United_States_Navy,http://dbpedia.org/resource/Union_Army,http://dbpedia.org/resource/Village_(United_States)','http://dbpedia.org/resource/Ppm,http://dbpedia.org/resource/.ppm,http://dbpedia.org/resource/550_ppm,http://dbpedia.org/resource/PPM_58291,http://dbpedia.org/resource/PPM_66283','',1,'1'),(160,'xiaobai','Sensor','device25','In-lake temperature environment','Temperature','TemperatureSensor','Celsius','Llyn_Conwy','none','CEH,UK','http://dbpedia.org/resource/Temperature,http://dbpedia.org/resource/Category:Temperature,http://dbpedia.org/resource/Operating_temperature,http://dbpedia.org/resource/Orders_of_magnitude_(temperature),http://dbpedia.org/resource/Color_temperature','','http://dbpedia.org/resource/Llyn_conwy','http://dbpedia.org/resource/Celsius,http://dbpedia.org/resource/4169_Celsius,http://dbpedia.org/resource/Celsius_scale,http://dbpedia.org/resource/Minus_Celsius,http://dbpedia.org/resource/Celsius,_Anders','',1,'1'),(165,'xiaobai','Actuator','device26','Pump Controller','','Arduino_Motor','none','Stockholm','factory_room_01','Ericsson','','http://dbpedia.org/resource/Ericsson,http://dbpedia.org/resource/Pär_Ericsson,http://dbpedia.org/resource/Category:Ericsson,http://dbpedia.org/resource/Xperia_Play,http://dbpedia.org/resource/Category:Sony_Ericsson_mobile_phones','http://dbpedia.org/resource/Stockholm,http://dbpedia.org/resource/Europe/Stockholm,http://dbpedia.org/resource/Stockholm_County,http://dbpedia.org/resource/Stockholm_University,http://dbpedia.org/resource/Stockholm_Records','','',1,'1'),(169,'xiaobai','Actuator','device27','for temp','Cooling','CoolingAC','Celsius degree','Beijing','Room_613','BUPT','http://dbpedia.org/resource/Water_cooling,http://dbpedia.org/resource/Category:Cooling_technology,http://dbpedia.org/resource/Cooling,http://dbpedia.org/resource/Computer_cooling,http://dbpedia.org/resource/Air_cooling','http://dbpedia.org/resource/BUPT','http://dbpedia.org/resource/Beijing,http://dbpedia.org/resource/Beijing_Guoan_F.C.,http://dbpedia.org/resource/Beijing_Renhe_F.C.,http://dbpedia.org/resource/Beijing_Subway,http://dbpedia.org/resource/Category:Beijing_Subway_stations','http://dbpedia.org/resource/Celsius_degree,http://dbpedia.org/resource/Degree_Celsius,http://dbpedia.org/resource/100_Degree_Celsius','',1,'1'),(230,'xiaobai','Sensor','dfg','dfg		','Temperature','dfg','dfg','dfg','dfg','dfg',NULL,NULL,NULL,NULL,NULL,0,'2'),(231,'sdf','Sensor','sdf','		sdf','Temperature','sdf','sdf','sdf','sdf','sdf',NULL,NULL,NULL,NULL,NULL,0,'1'),(233,'ch','Sensor','sdf','gdfg','Temperature','fdgdfg','dfh','c','cv','ch',NULL,NULL,NULL,NULL,NULL,0,'1');
/*!40000 ALTER TABLE `datatransform` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `historydata`
--

DROP TABLE IF EXISTS `historydata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `historydata` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `deviceId` varchar(45) DEFAULT NULL,
  `samplingTime` timestamp NULL DEFAULT NULL,
  `value` float DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `historydata`
--

LOCK TABLES `historydata` WRITE;
/*!40000 ALTER TABLE `historydata` DISABLE KEYS */;
INSERT INTO `historydata` VALUES (1,'65','2017-01-18 08:16:44',33),(2,'72','2017-01-18 08:18:07',31),(3,'82','2017-01-18 08:19:06',31),(4,'75','2017-01-18 08:19:34',37);
/*!40000 ALTER TABLE `historydata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_information`
--

DROP TABLE IF EXISTS `user_information`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_information` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_information`
--

LOCK TABLES `user_information` WRITE;
/*!40000 ALTER TABLE `user_information` DISABLE KEYS */;
INSERT INTO `user_information` VALUES (1,'xiaobai','930208');
/*!40000 ALTER TABLE `user_information` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-03-16 14:05:26
