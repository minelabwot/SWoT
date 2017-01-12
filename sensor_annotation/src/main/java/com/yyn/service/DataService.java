package com.yyn.service;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyn.dao.DataCollectionDAO;

@Service
public class DataService {
	@Autowired
	private DataCollectionDAO dc;
	
	public void update_sensordata(String id,Timestamp time,float value) {
		dc.updateSensorData(id, time,value);
	}
}
