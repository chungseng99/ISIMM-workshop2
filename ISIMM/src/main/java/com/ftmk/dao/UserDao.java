package com.ftmk.dao;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ftmk.model.UserInfo;
import com.ftmk.model.UserMapper;

@Service
@Transactional
public class UserDao extends JdbcDaoSupport {

	@Autowired
	public UserDao(DataSource dataSource) {

		this.setDataSource(dataSource);

	}

	public UserInfo findUser(String username) {

		String sql = "SELECT username,password FROM users WHERE username=?";

		Object[] params = new Object[] { username };

		UserMapper mapper = new UserMapper();
		try {
			UserInfo userInfo = this.getJdbcTemplate().queryForObject(sql, params, mapper);
			return userInfo;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public List<String> getUserRoles(String username) {

		String sql = "SELECT role FROM user_role JOIN users ON user_role.user_id=users.user_id WHERE username=?";

		Object[] params = new Object[] { username };

		List<String> roles = this.getJdbcTemplate().queryForList(sql, params, String.class);

		return roles;

	}

}
