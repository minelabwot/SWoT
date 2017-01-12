package com.yyn.model;

public class Device {
	private int id;
	private String provider;
	private String type;
	private String name;
	private String description;
	private String property;
	private String sensorType;
	private String unit;
	private String region;
	private String spot;
	private String company;
	private String table_id;
	
	public Device(Integer id,String name,String property,String sensorType,String spot,String company,String region,String description) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.property = property;
		this.sensorType = sensorType;
		this.spot = spot;
		this.region = region;
		this.company = company;
	}
	public Device(Integer id,String property, String sensorType, String unit,
			String region, String company,String table_id) {
		this.id = id;
		this.property = property;
		this.sensorType = sensorType;
		this.unit = unit;
		this.region = region;
		this.company = company;
		this.table_id = table_id;
	}
	public Device(String table_id,String property, String sensorType, String unit,
			String region, String company) {
		this.table_id = table_id;
		this.property = property;
		this.sensorType = sensorType;
		this.unit = unit;
		this.region = region;
		this.company = company;
	}
	//从edit界面创建新设备记录到mysql
	public Device(int id, String type,String name, String description, String property, String sensorType, String unit,
			String region, String  spot, String company,String provider,String table_id) {
		super();
		this.id = id;
		this.type = type;
		this.name = name;
		this.description = description;
		this.property = property;
		this.sensorType = sensorType;
		this.unit = unit;
		this.region = region;
		this.spot = spot;
		this.company = company;
		this.provider = provider;
		this.table_id = table_id;
	}
	public Device(int id,String name,String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
	public String getTable_id() {
		return table_id;
	}
	public void setTable_id(String table_id) {
		this.table_id = table_id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSensorType() {
		return sensorType;
	}
	public void setSensorType(String sensorType) {
		this.sensorType = sensorType;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getSpot() {
		return spot;
	}
	public void setSpot(String spot) {
		this.spot = spot;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getProvider() {
		return provider;
	}
	public void setProvider(String provider) {
		this.provider = provider;
	}
	
	
	
}
