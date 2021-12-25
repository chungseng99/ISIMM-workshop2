package com.ftmk.model;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class UserMapper implements RowMapper<UserInfo>{

	@Override
	public UserInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		String username=rs.getString("username");
		String password=rs.getString("password");
		boolean enabled=rs.getBoolean("enabled");
		
		return new UserInfo(username,password,enabled);
		
	}
	
	

}
