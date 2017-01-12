package com.yyn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yyn.dao.UserLoginDAO;
import com.yyn.model.User;

@Service
public class UserService {
	@Autowired
	UserLoginDAO loginDao;
	
	//login service
	public int insertNewUser(User user) {
		return loginDao.insertNewUser(user);
	}
	
	public User getUser(String name) {
		return loginDao.getUser(name);
	}
	
	public void updatePwd(String username,String pwd) {
		loginDao.updatePwd(username, pwd);
	}
	
	public void updateUser(User user) {
		loginDao.updateUser(user);
	}
}
