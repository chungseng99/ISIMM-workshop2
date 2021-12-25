package com.ftmk.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.ftmk.model.UserConfirmation;
import com.ftmk.model.UserInfo;
import com.ftmk.model.UserPersonalDetails;
import com.ftmk.model.UserRole;

public class RegistrationDao {

	private JdbcTemplate jdbcTemplate;
	
	public RegistrationDao(DataSource dataSource) {
		
		this.jdbcTemplate=new JdbcTemplate(dataSource);
		
	}
	
		
	public int register (UserInfo user) {
		//register user and set enabled to false before confirmation token
		String sql="INSERT INTO users (username,password,enabled) VALUES(?,?,?)";
		return jdbcTemplate.update(sql, user.getUsername(),user.getPassword(),false);
	}
	
	public Integer getUserId(UserInfo user) {
		//get user_id based on username
		String sql="SELECT user_id FROM users WHERE username=?";
		return jdbcTemplate.queryForObject(sql,new Object[] {user.getUsername()},Integer.class);
	}
	
	public int getToken(Integer id,UserConfirmation confirmation) {
		//insert token generated into database
		String sql= "INSERT INTO user_confirmation (confirmation_token,date_created,user_id) VALUES (?,?,?)";
		return jdbcTemplate.update(sql,confirmation.getConfirmationToken(),confirmation.getCreatedDate(),id);
	}
	
	public String tokenValidate(String token) {
		//select token using the parameter of link
		String sql="SELECT confirmation_token FROM user_confirmation WHERE confirmation_token="+"'"+token+"'";
		return jdbcTemplate.query(sql, new ResultSetExtractor<String>() {
	        @Override
	        public String extractData(ResultSet rs) throws SQLException,DataAccessException {
	            return rs.next() ? rs.getString("confirmation_token") : null;
	        }
	    });
	}
	
		
	public Integer getIdByToken(String token) {
		//get user_id by using token
		String sql="SELECT users.user_id FROM users JOIN user_confirmation ON users.user_id=user_confirmation.user_id WHERE confirmation_token=?";
		return jdbcTemplate.queryForObject(sql, new Object[] {token},Integer.class);
	}
	
	public int setEnabled(boolean status,Integer id) {
		//set enabled to true
		String sql ="UPDATE users SET enabled=? WHERE user_id=?";
		return jdbcTemplate.update(sql,status,id);
	}
	
	public String searchUsername(String username) {
		//Search username in database
		
		String sql="SELECT username FROM users WHERE username="+"'"+username+"'";
		return jdbcTemplate.query(sql, new ResultSetExtractor<String>() {
	        @Override
	        public String extractData(ResultSet rs) throws SQLException,DataAccessException {
	            return rs.next() ? rs.getString("username") : null;
	        }
	    });
	}
	
	public String userEmail(String username) {
		
		String sql="SELECT user_details.email FROM user_details JOIN users ON user_details.user_id=users.user_id WHERE username=?";
		return jdbcTemplate.queryForObject(sql, new Object[] {username},String.class);
		
	}
	
	public String searchIC(String icNumber) {
		//Search username in database
		
		String sql="SELECT ic_number FROM user_details WHERE ic_number="+"'"+icNumber+"'";
		return jdbcTemplate.query(sql, new ResultSetExtractor<String>() {
	        @Override
	        public String extractData(ResultSet rs) throws SQLException,DataAccessException {
	            return rs.next() ? rs.getString("ic_number") : null;
	        }
	    });
	}
	
	public String searchICByUsername(String icNumber,String username) {
		//Search username in database
		
		String sql="SELECT user_details.ic_number,users.username FROM user_details JOIN users ON "
				+ "user_details.user_id=users.user_id WHERE ic_number="+"'"+icNumber+"'"+"AND users.username="+"'"+username+"'";
		return jdbcTemplate.query(sql, new ResultSetExtractor<String>() {
	        @Override
	        public String extractData(ResultSet rs) throws SQLException,DataAccessException {
	            return rs.next() ? rs.getString("ic_number") : null;
	        }
	    });
	}
	public int InsertUserDetails(UserPersonalDetails userPersonalDetails,Integer Id) {
		//insert user details into database
		String sql="INSERT INTO user_details (user_id,email,name,ic_number) VALUES (?,?,?,?)";
		return jdbcTemplate.update(sql,Id,userPersonalDetails.getEmail(),userPersonalDetails.getName(),userPersonalDetails.getIcNumber());
		
	}
	
	public int InsertStudentDetails(UserPersonalDetails userPersonalDetails,Integer Id) {
		//insert user details into database
		String sql="INSERT INTO user_details (user_id,email,name,ic_number,phone_number,address,nationality,ethnicity) VALUES (?,?,?,?,?,?,?,?)";
		return jdbcTemplate.update(sql,Id,userPersonalDetails.getEmail(),userPersonalDetails.getName(),userPersonalDetails.getIcNumber(),
				userPersonalDetails.getPhoneNumber(),userPersonalDetails.getAddress(),userPersonalDetails.getNationality(),userPersonalDetails.getEthnicity());
		
	}
	
	public int InsertRole(Integer Id,UserRole userRole) {
		//insert role and user_id
		String sql="INSERT INTO user_role (user_id,role) VALUES (?,?)";
		return jdbcTemplate.update(sql,Id,userRole.getRole());
	}
	
	public int InsertStudentRole(Integer Id,String role) {
		//insert role and user_id
		String sql="INSERT INTO user_role (user_id,role) VALUES (?,?)";
		return jdbcTemplate.update(sql,Id,role);
	}
	
	public int resetPassword(String password,String username) {
		
		String sql="UPDATE users SET users.password=? WHERE users.username=?";
		return jdbcTemplate.update(sql,password,username);
	}
	

}
