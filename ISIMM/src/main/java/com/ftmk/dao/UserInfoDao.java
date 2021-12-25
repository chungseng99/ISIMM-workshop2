package com.ftmk.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.ftmk.model.UserInfo;
import com.ftmk.model.UserPersonalDetails;
import com.ftmk.model.UserTableDisplay;

public class UserInfoDao {

	private JdbcTemplate jdbcTemplate;

	public UserInfoDao(DataSource dataSource) {

		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	public List<UserTableDisplay> listUser() {
		//Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT users.user_id, users.username,users.enabled,user_details.name,user_details.ic_number,user_role.role FROM users "
				+ "JOIN user_details ON users.user_id=user_details.user_id JOIN user_role ON users.user_id=user_role.user_id";
		List<UserTableDisplay> list = jdbcTemplate.query(sql, new RowMapper<UserTableDisplay>() {

			@Override
			public UserTableDisplay mapRow(ResultSet rs, int rowNum) throws SQLException {
					
				UserTableDisplay display = new UserTableDisplay();
				display.setUserId(rs.getInt("user_id"));
				display.setUsername(rs.getString("username"));
				display.setIcNumber(rs.getString("ic_number"));
				display.setName(rs.getString("name"));
				display.setRole(rs.getString("role"));
				display.setEnabled(rs.getBoolean("enabled"));
				return display;
			}
		});
		return list;
	}
	
	
	public UserTableDisplay getUserById(Integer userId) {
		//Select certain field from each table to be editted
		String sql="SELECT users.user_id, users.username,users.enabled,user_details.name,user_details.ic_number,user_role.role FROM users JOIN user_details "
				+ "ON users.user_id=user_details.user_id JOIN user_role ON users.user_id=user_role.user_id WHERE users.user_id="+userId;
		return jdbcTemplate.query(sql, new ResultSetExtractor<UserTableDisplay>() {

			@Override
			public UserTableDisplay extractData(ResultSet rs) throws SQLException, DataAccessException {
				
				if(rs.next()) {
					
					UserTableDisplay userDisplay = new UserTableDisplay();
					userDisplay.setUserId(rs.getInt("user_id"));
					userDisplay.setUsername(rs.getString("username"));
					userDisplay.setEmail(rs.getString("username"));
					userDisplay.setIcNumber(rs.getString("ic_number"));
					userDisplay.setName(rs.getString("name"));
					userDisplay.setRole(rs.getString("role"));
					userDisplay.setEnabled(rs.getBoolean("enabled"));
					return userDisplay;
				}
				return null;
			}
			
		});
	}

	
	public int updateUser(UserTableDisplay user) {
		//update the selected field into database
		String sql="UPDATE users,user_role,user_details SET users.username=?,users.enabled=?,user_details.name=?,user_details.email=?"
				+ ",user_details.ic_number=?,user_role.role=? WHERE users.user_id=? AND  user_details.user_id=? AND user_role.user_id=?";
		return jdbcTemplate.update(sql,user.getUsername(),user.isEnabled(),user.getName(),
				user.getEmail(),user.getIcNumber(),user.getRole(),user.getUserId(),user.getUserId(),user.getUserId());
		
	}
	
	
	public List<UserTableDisplay> searchByUsername(String search) {
		//Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT users.user_id, users.username,users.enabled,user_details.name,user_details.ic_number,user_role.role FROM "
				+ "users JOIN user_details ON users.user_id=user_details.user_id JOIN user_role "
				+ "ON users.user_id=user_role.user_id WHERE users.username LIKE '%"+search+"%'";
		List<UserTableDisplay> list = jdbcTemplate.query(sql, new RowMapper<UserTableDisplay>() {

			@Override
			public UserTableDisplay mapRow(ResultSet rs, int rowNum) throws SQLException {
					
				UserTableDisplay display = new UserTableDisplay();
				display.setUserId(rs.getInt("user_id"));
				display.setUsername(rs.getString("username"));
				display.setIcNumber(rs.getString("ic_number"));
				display.setName(rs.getString("name"));
				display.setRole(rs.getString("role"));
				display.setEnabled(rs.getBoolean("enabled"));
				return display;
			}
		});
		return list;
	}
	
	public List<UserTableDisplay> searchByName(String search) {
		//Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT users.user_id, users.username,users.enabled,user_details.name,user_details.ic_number,user_role.role FROM "
				+ "users JOIN user_details ON users.user_id=user_details.user_id JOIN user_role "
				+ "ON users.user_id=user_role.user_id WHERE user_details.name LIKE '%"+search+"%'";
		List<UserTableDisplay> list = jdbcTemplate.query(sql, new RowMapper<UserTableDisplay>() {

			@Override
			public UserTableDisplay mapRow(ResultSet rs, int rowNum) throws SQLException {
					
				UserTableDisplay display = new UserTableDisplay();
				display.setUserId(rs.getInt("user_id"));
				display.setUsername(rs.getString("username"));
				display.setIcNumber(rs.getString("ic_number"));
				display.setName(rs.getString("name"));
				display.setRole(rs.getString("role"));
				display.setEnabled(rs.getBoolean("enabled"));
				return display;
			}
		});
		return list;
	}
	
	public List<UserTableDisplay> searchByIC(String search) {
		//Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT users.user_id, users.username,users.enabled,user_details.name,user_details.ic_number,"
				+ "user_role.role FROM users JOIN user_details ON users.user_id=user_details.user_id "
				+ "JOIN user_role ON users.user_id=user_role.user_id WHERE user_details.ic_number LIKE '%"+search+"%'";
		List<UserTableDisplay> list = jdbcTemplate.query(sql, new RowMapper<UserTableDisplay>() {

			@Override
			public UserTableDisplay mapRow(ResultSet rs, int rowNum) throws SQLException {
					
				UserTableDisplay display = new UserTableDisplay();
				display.setUserId(rs.getInt("user_id"));
				display.setUsername(rs.getString("username"));
				display.setIcNumber(rs.getString("ic_number"));
				display.setName(rs.getString("name"));
				display.setRole(rs.getString("role"));
				display.setEnabled(rs.getBoolean("enabled"));
				return display;
			}
		});
		return list;
	}
	
	public List<UserTableDisplay> searchByRole(String search) {
		//Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT users.user_id, users.username,users.enabled,user_details.name,user_details.ic_number,"
				+ "user_role.role FROM users JOIN user_details ON users.user_id=user_details.user_id "
				+ "JOIN user_role ON users.user_id=user_role.user_id WHERE user_role.role LIKE '%"+search+"%'";
		List<UserTableDisplay> list = jdbcTemplate.query(sql, new RowMapper<UserTableDisplay>() {

			@Override
			public UserTableDisplay mapRow(ResultSet rs, int rowNum) throws SQLException {
					
				UserTableDisplay display = new UserTableDisplay();
				display.setUserId(rs.getInt("user_id"));
				display.setUsername(rs.getString("username"));
				display.setIcNumber(rs.getString("ic_number"));
				display.setName(rs.getString("name"));
				display.setRole(rs.getString("role"));
				display.setEnabled(rs.getBoolean("enabled"));
				return display;
			}
		});
		return list;
	}
	
	public String getUserRoles(String username) {

		String sql = "SELECT role FROM user_role JOIN users ON user_role.user_id=users.user_id WHERE username=?";
		
		return jdbcTemplate.queryForObject(sql, new Object[] {username},String.class);

	}
	
	public int deactivateUser(Integer userId) {
		
		String sql= "UPDATE users SET enabled=false WHERE user_id="+userId;
		return jdbcTemplate.update(sql);
	}
	
	public int deleteUser(Integer userId) {
		
		String sql= "DELETE FROM users WHERE user_id="+userId;
		return jdbcTemplate.update(sql);
	}
	
	public int changePassword(String password,String username) {
		
		String sql="UPDATE users SET users.password=? WHERE users.username=?";
		return jdbcTemplate.update(sql,password,username);
	}

}
