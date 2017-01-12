package com.yyn.dao;

import java.sql.Timestamp;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import com.yyn.model.Device;

@Component
public interface DataCollectionDAO {
	@Insert("insert into datatransform (device_id,type,name,description,property,sensorType,unit,region,spot,company,provider,table_id)"
			+ " values (#{id},#{type},#{name},#{description},#{property},#{sensorType},#{unit},#{region},#{spot},#{company},#{provider},#{table_id})")
	void insertNewDevice(Device device);
	
	@Results({
		@Result(property="property",column="property_url",javaType=String.class,jdbcType=JdbcType.VARCHAR),
		@Result(property="sensorType",column="sensorType_url",javaType=String.class,jdbcType=JdbcType.VARCHAR),
		@Result(property="unit",column="unit_url",javaType=String.class,jdbcType=JdbcType.VARCHAR),
		@Result(property="region",column="region_url",javaType=String.class,jdbcType=JdbcType.VARCHAR),
		@Result(property="company",column="company_url",javaType=String.class,jdbcType=JdbcType.VARCHAR),
		@Result(property="id",column="device_id",javaType=Integer.class,jdbcType=JdbcType.VARCHAR),
		@Result(property="table_id",column="table_id",javaType=String.class,jdbcType=JdbcType.VARCHAR)
		})
	@Select("select device_id,property_url,sensorType_url,unit_url,company_url,region_url,table_id from datatransform where device_id=#{id}")
	public Device getLink(String id);
	
	//get all info by device_id
	@Results({
		@Result(property="property",column="property",javaType=String.class,jdbcType=JdbcType.VARCHAR),
		@Result(property="name",column="name",javaType=String.class,jdbcType=JdbcType.VARCHAR),
		@Result(property="sensorType",column="sensorType",javaType=String.class,jdbcType=JdbcType.VARCHAR),
		@Result(property="spot",column="spot",javaType=String.class,jdbcType=JdbcType.VARCHAR),
		@Result(property="region",column="region",javaType=String.class,jdbcType=JdbcType.VARCHAR),
		@Result(property="company",column="company",javaType=String.class,jdbcType=JdbcType.VARCHAR),
		@Result(property="id",column="device_id",javaType=Integer.class,jdbcType=JdbcType.VARCHAR),
		@Result(property="description",column="description",javaType=String.class,jdbcType=JdbcType.VARCHAR)
		})
	@Select("select device_id,name,property,sensorType,spot,company,region,description from datatransform where device_id=#{device_id}")
	public Device getDeviceInfo(String device_id);
	
	@Results({
		@Result(property="property",column="property",javaType=String.class,jdbcType=JdbcType.VARCHAR),
		@Result(property="sensorType",column="sensorType",javaType=String.class,jdbcType=JdbcType.VARCHAR),
		@Result(property="unit",column="unit",javaType=String.class,jdbcType=JdbcType.VARCHAR),
		@Result(property="region",column="region",javaType=String.class,jdbcType=JdbcType.VARCHAR),
		@Result(property="company",column="company",javaType=String.class,jdbcType=JdbcType.VARCHAR),
		@Result(property="table_id",column="table_id",javaType=String.class,jdbcType=JdbcType.VARCHAR)
		})
	@Select("select table_id,property,sensorType,unit,region,company from column_type where table_id=#{table_id}")
	public Device getColumn(String table_id);
	
	@Insert("insert into historydata (deviceId,samplingTime,value) values (#{id},#{time},#{value})")
	public void updateSensorData(@Param("id")String id,@Param("time")Timestamp time,@Param("value")float value);
}
