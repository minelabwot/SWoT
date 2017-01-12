package com.yyn.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
//import org.apache.ibatis.annotations.Result;
//import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
//import org.apache.ibatis.type.JdbcType;
import org.springframework.stereotype.Component;

import com.yyn.model.User;

@Component
public interface UserLoginDAO {
	//新用户注册
	@Insert("insert into user_information (name,password,authority,"
			+ "phone,email) values (#{name},#{password},"
			+ "#{authority},#{phone},#{email})")
	int insertNewUser(User user);
	
	@Update("update user_information set password=#{pwd} where name=#{name}")
	void updatePwd(@Param("name")String username,@Param("pwd")String password);
	
	@Update("update user_information set alias=#{alias},birthday=#{birthday},sex=#{sex}"
			+ " where name=#{name}")
	void updateUser(User user);
	//登录验证
//	@Results({
//		@Result(property="name",column="user_username",jdbcType=JdbcType.VARCHAR),
//		@Result(property="password",column="user_password",jdbcType=JdbcType.VARCHAR),
//		@Result(property="userid",column="user_userid",jdbcType=JdbcType.VARCHAR)
//		})
	@Select("select * from user_information where name=#{name}")
	User getUser(String name);
}
