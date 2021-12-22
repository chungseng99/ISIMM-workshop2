package com.ftmk.dao;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.ftmk.model.Announcement;
import com.ftmk.model.Classroom;
import com.ftmk.model.Fee;
import com.ftmk.model.Payment;
import com.ftmk.model.Subject;
import com.ftmk.model.UserPersonalDetails;
import com.ftmk.model.UserTableDisplay;

public class clerkDashboardDao {

	private JdbcTemplate jdbcTemplate;

	public clerkDashboardDao(DataSource dataSource) {

		this.jdbcTemplate = new JdbcTemplate(dataSource);

	}

	public List<UserPersonalDetails> teacherList() {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT user_details.user_id,user_details.name,user_details.ic_number FROM user_details JOIN user_role ON "
				+ "user_details.user_id=user_role.user_id JOIN users ON user_details.user_id=users.user_id "
				+ "WHERE user_role.role='TEACHER' AND users.enabled=1";
		List<UserPersonalDetails> list = jdbcTemplate.query(sql, new RowMapper<UserPersonalDetails>() {

			@Override
			public UserPersonalDetails mapRow(ResultSet rs, int rowNum) throws SQLException {

				UserPersonalDetails user = new UserPersonalDetails();
				user.setUserId(rs.getInt("user_id"));
				user.setName(rs.getString("name"));
				user.setIcNumber(rs.getString("ic_number"));
				return user;
			}
		});
		return list;
	}

	public String checkClassName(String className) {
		// Search username in database

		String sql = "SELECT class_name FROM classroom WHERE class_name=" + "'" + className + "'";
		return jdbcTemplate.query(sql, new ResultSetExtractor<String>() {
			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				return rs.next() ? rs.getString("class_name") : null;
			}
		});
	}

	public String checkClassNameById(Integer classroomId) {

		String sql = "SELECT class_name FROM classroom WHERE classroom_id =" + classroomId;
		return jdbcTemplate.query(sql, new ResultSetExtractor<String>() {
			@Override
			public String extractData(ResultSet rs) throws SQLException, DataAccessException {
				return rs.next() ? rs.getString("class_name") : null;
			}
		});

	}

	public int createClassroom(Classroom classroom) {

		String sql = "INSERT INTO classroom (class_name,form,class_teacher_name,maximum_participant) VALUES (?,?,?,?)";
		return jdbcTemplate.update(sql, classroom.getClassName(), classroom.getForm(), classroom.getTeacherName(),
				classroom.getMaxParticipant());

	}

	public List<Classroom> classroomList() {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT classroom.*,(classroom.maximum_participant- (SELECT COUNT(classroom_id) FROM "
				+ "class_participant WHERE classroom.classroom_id=class_participant.classroom_id)) "
				+ "AS slot FROM classroom ORDER BY class_name";
		List<Classroom> list = jdbcTemplate.query(sql, new RowMapper<Classroom>() {

			@Override
			public Classroom mapRow(ResultSet rs, int rowNum) throws SQLException {

				Classroom classroom = new Classroom();
				classroom.setClassroomId(rs.getInt("classroom_id"));
				classroom.setClassName(rs.getString("class_name"));
				classroom.setForm(rs.getInt("form"));
				classroom.setTeacherName(rs.getString("class_teacher_name"));
				classroom.setMaxParticipant(rs.getInt("maximum_participant"));
				classroom.setSlot(rs.getInt("slot"));
				return classroom;
			}
		});
		return list;
	}

	public Classroom getClassroomById(Integer classroomId) {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT classroom.*,(classroom.maximum_participant- (SELECT COUNT(classroom_id) FROM "
				+"class_participant WHERE classroom.classroom_id=class_participant.classroom_id)) "
				+"AS slot FROM classroom WHERE classroom_id=" + "'" + classroomId + "'";

		return jdbcTemplate.query(sql, new ResultSetExtractor<Classroom>() {

			@Override
			public Classroom extractData(ResultSet rs) throws SQLException, DataAccessException {

				if (rs.next()) {

					Classroom classroom = new Classroom();
					classroom.setClassroomId(rs.getInt("classroom_id"));
					classroom.setClassName(rs.getString("class_name"));
					classroom.setForm(rs.getInt("form"));
					classroom.setTeacherName(rs.getString("class_teacher_name"));
					classroom.setMaxParticipant(rs.getInt("maximum_participant"));
					classroom.setSlot(rs.getInt("slot"));
					return classroom;
				}
				return null;

			}

		});
	}

	public int updateClassroom(Classroom classroom) {

		String sql = "UPDATE classroom SET class_name=?, form=?, class_teacher_name=?, maximum_participant=? WHERE classroom_id=?";
		return jdbcTemplate.update(sql, classroom.getClassName(), classroom.getForm(), classroom.getTeacherName(),
				classroom.getMaxParticipant(), classroom.getClassroomId());

	}

	public int deleteClassroom(Integer classroomId) {

		String sql = "DELETE FROM classroom WHERE classroom_id=?";
		return jdbcTemplate.update(sql, classroomId);

	}

	public int addStudentToClass(Integer classroomId, Integer userId) {

		String sql = "INSERT INTO class_participant (classroom_id,user_id) VALUES (?,?)";
		return jdbcTemplate.update(sql, classroomId, userId);

	}

	public List<UserPersonalDetails> unassignedStudentList() {

		String sql = "SELECT user_details.* FROM user_details JOIN user_role ON "
				+ "user_details.user_id=user_role.user_id JOIN users ON user_details.user_id=users.user_id  "
				+ "WHERE user_details.user_id NOT IN (SELECT class_participant.user_id FROM class_participant) AND "
				+ "user_role.role='STUDENT' AND users.enabled=1";

		List<UserPersonalDetails> list = jdbcTemplate.query(sql, new RowMapper<UserPersonalDetails>() {

			@Override
			public UserPersonalDetails mapRow(ResultSet rs, int rowNum) throws SQLException {

				UserPersonalDetails user = new UserPersonalDetails();
				user.setUserId(rs.getInt("user_id"));
				user.setName(rs.getString("name"));
				user.setIcNumber(rs.getString("ic_number"));
				user.setEmail(rs.getString("email"));
				user.setPhoneNumber(rs.getString("phone_number"));
				user.setAddress(rs.getString("address"));
				user.setNationality(rs.getString("nationality"));
				user.setEthnicity(rs.getString("ethnicity"));
				user.setPicture(rs.getBlob("picture"));
				return user;
			}
		});
		return list;
	}

	public List<UserPersonalDetails> StudentList() {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT user_details.* FROM user_details JOIN user_role ON user_details.user_id=user_role.user_id JOIN "
				+ "users ON user_details.user_id=users.user_id WHERE user_role.role='STUDENT' AND users.enabled=1";

		List<UserPersonalDetails> list = jdbcTemplate.query(sql, new RowMapper<UserPersonalDetails>() {

			@Override
			public UserPersonalDetails mapRow(ResultSet rs, int rowNum) throws SQLException {

				UserPersonalDetails user = new UserPersonalDetails();
				user.setUserId(rs.getInt("user_id"));
				user.setName(rs.getString("name"));
				user.setIcNumber(rs.getString("ic_number"));
				user.setEmail(rs.getString("email"));
				user.setPhoneNumber(rs.getString("phone_number"));
				user.setAddress(rs.getString("address"));
				user.setNationality(rs.getString("nationality"));
				user.setEthnicity(rs.getString("ethnicity"));
				user.setPicture(rs.getBlob("picture"));
				return user;
			}
		});
		return list;
	}

	public UserPersonalDetails getStudentById(Integer userId) {
		String sql = "SELECT * FROM user_details WHERE user_id=" + "'" + userId + "'";

		return jdbcTemplate.query(sql, new ResultSetExtractor<UserPersonalDetails>() {

			@Override
			public UserPersonalDetails extractData(ResultSet rs) throws SQLException, DataAccessException {

				if (rs.next()) {

					UserPersonalDetails user = new UserPersonalDetails();
					user.setUserId(rs.getInt("user_id"));
					user.setName(rs.getString("name"));
					user.setIcNumber(rs.getString("ic_number"));
					user.setEmail(rs.getString("email"));
					user.setPhoneNumber(rs.getString("phone_number"));
					user.setAddress(rs.getString("address"));
					user.setNationality(rs.getString("nationality"));
					user.setEthnicity(rs.getString("ethnicity"));
					user.setPicture(rs.getBlob("picture"));
					return user;
				}
				return null;

			}

		});
	}
	
	public UserPersonalDetails getUserByUsername(String username) {
		String sql = "SELECT user_details.* FROM user_details JOIN users ON "
				+ "user_details.user_id=users.user_id WHERE users.username=" + "'" + username + "'";

		return jdbcTemplate.query(sql, new ResultSetExtractor<UserPersonalDetails>() {

			@Override
			public UserPersonalDetails extractData(ResultSet rs) throws SQLException, DataAccessException {

				if (rs.next()) {

					UserPersonalDetails user = new UserPersonalDetails();
					user.setUserId(rs.getInt("user_id"));
					user.setName(rs.getString("name"));
					user.setIcNumber(rs.getString("ic_number"));
					user.setEmail(rs.getString("email"));
					user.setPhoneNumber(rs.getString("phone_number"));
					user.setAddress(rs.getString("address"));
					user.setNationality(rs.getString("nationality"));
					user.setEthnicity(rs.getString("ethnicity"));
					user.setPicture(rs.getBlob("picture"));
					user.setMaritalStatus(rs.getString("marital_status"));
					user.setOccupation(rs.getString("occupation"));
					return user;
				}
				return null;

			}

		});
	}
	
	public int updateProfile(UserPersonalDetails user, Integer userId, MultipartFile photo) throws IOException {
		
		byte[] photoBytes = photo.getBytes();
		String sql="UPDATE user_details SET email=?, name=?, ic_number=?, phone_number=?,"
				+ "address=?, nationality=?, ethnicity=?,occupation=?, marital_status=?, picture=? "
				+ "WHERE user_id=?";
		return jdbcTemplate.update(sql,new Object[] {user.getEmail(),user.getName(),user.getIcNumber(),
				user.getPhoneNumber(),user.getAddress(),user.getNationality(),user.getEthnicity()
				,user.getOccupation(),user.getMaritalStatus(),photoBytes,userId});
		
	}

	public Blob getPhotoById(int userId) {

		String query = "SELECT picture FROM user_details where user_id=?";

		Blob photo = jdbcTemplate.queryForObject(query, new Object[] { userId }, Blob.class);

		return photo;
	}

	public List<UserPersonalDetails> searchStudentByName(String search) {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT user_details.* FROM user_details JOIN user_role ON user_details.user_id=user_role.user_id JOIN "
				+ "users ON user_details.user_id=users.user_id WHERE user_role.role='STUDENT' AND "
				+ "users.enabled=1 AND user_details.name LIKE " + "'%" + search + "%'";

		List<UserPersonalDetails> list = jdbcTemplate.query(sql, new RowMapper<UserPersonalDetails>() {

			@Override
			public UserPersonalDetails mapRow(ResultSet rs, int rowNum) throws SQLException {

				UserPersonalDetails user = new UserPersonalDetails();
				user.setUserId(rs.getInt("user_id"));
				user.setName(rs.getString("name"));
				user.setIcNumber(rs.getString("ic_number"));
				user.setEmail(rs.getString("email"));
				user.setPhoneNumber(rs.getString("phone_number"));
				user.setAddress(rs.getString("address"));
				user.setNationality(rs.getString("nationality"));
				user.setEthnicity(rs.getString("ethnicity"));
				user.setPicture(rs.getBlob("picture"));
				return user;
			}
		});
		return list;
	}

	public List<UserPersonalDetails> searchStudentByIC(String search) {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT user_details.* FROM user_details JOIN user_role ON user_details.user_id=user_role.user_id JOIN "
				+ "users ON user_details.user_id=users.user_id WHERE user_role.role='STUDENT' AND "
				+ "users.enabled=1 AND user_details.ic_number LIKE " + "'%" + search + "%'";

		List<UserPersonalDetails> list = jdbcTemplate.query(sql, new RowMapper<UserPersonalDetails>() {

			@Override
			public UserPersonalDetails mapRow(ResultSet rs, int rowNum) throws SQLException {

				UserPersonalDetails user = new UserPersonalDetails();
				user.setUserId(rs.getInt("user_id"));
				user.setName(rs.getString("name"));
				user.setIcNumber(rs.getString("ic_number"));
				user.setEmail(rs.getString("email"));
				user.setPhoneNumber(rs.getString("phone_number"));
				user.setAddress(rs.getString("address"));
				user.setNationality(rs.getString("nationality"));
				user.setEthnicity(rs.getString("ethnicity"));
				user.setPicture(rs.getBlob("picture"));
				return user;
			}
		});
		return list;
	}

	public List<UserPersonalDetails> searchStudentByEmail(String search) {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT user_details.* FROM user_details JOIN user_role ON user_details.user_id=user_role.user_id JOIN "
				+ "users ON user_details.user_id=users.user_id WHERE user_role.role='STUDENT' AND "
				+ "users.enabled=1 AND user_details.email LIKE " + "'%" + search + "%'";

		List<UserPersonalDetails> list = jdbcTemplate.query(sql, new RowMapper<UserPersonalDetails>() {

			@Override
			public UserPersonalDetails mapRow(ResultSet rs, int rowNum) throws SQLException {

				UserPersonalDetails user = new UserPersonalDetails();
				user.setUserId(rs.getInt("user_id"));
				user.setName(rs.getString("name"));
				user.setIcNumber(rs.getString("ic_number"));
				user.setEmail(rs.getString("email"));
				user.setPhoneNumber(rs.getString("phone_number"));
				user.setAddress(rs.getString("address"));
				user.setNationality(rs.getString("nationality"));
				user.setEthnicity(rs.getString("ethnicity"));
				user.setPicture(rs.getBlob("picture"));
				return user;
			}
		});
		return list;
	}

	public int countUnassignedStudent() {

		String sql = "SELECT COUNT(user_details.user_id) FROM user_details JOIN user_role ON user_details.user_id=user_role.user_id JOIN "
				+ "users ON user_details.user_id=users.user_id WHERE user_role.role='STUDENT' AND "
				+ "user_details.user_id NOT IN (SELECT user_id FROM class_participant) AND users.enabled=1";
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	public int totalStudent() {

		String sql = "SELECT COUNT(role) FROM user_role JOIN users ON user_role.user_id=users.user_id "
				+ "WHERE user_role.role='STUDENT' AND users.enabled=1";
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}
	
	
	public List<UserPersonalDetails> getTeacherList() {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT user_details.* FROM user_details JOIN user_role ON user_details.user_id=user_role.user_id JOIN "
				+ "users ON user_details.user_id=users.user_id WHERE user_role.role='TEACHER' AND users.enabled=1";

		List<UserPersonalDetails> list = jdbcTemplate.query(sql, new RowMapper<UserPersonalDetails>() {

			@Override
			public UserPersonalDetails mapRow(ResultSet rs, int rowNum) throws SQLException {

				UserPersonalDetails user = new UserPersonalDetails();
				user.setUserId(rs.getInt("user_id"));
				user.setName(rs.getString("name"));
				user.setIcNumber(rs.getString("ic_number"));
				user.setEmail(rs.getString("email"));
				user.setPhoneNumber(rs.getString("phone_number"));
				user.setAddress(rs.getString("address"));
				user.setNationality(rs.getString("nationality"));
				user.setEthnicity(rs.getString("ethnicity"));
				user.setPicture(rs.getBlob("picture"));
				return user;
			}
		});
		return list;
	}

	public UserPersonalDetails getTeacherById(Integer userId) {
		String sql = "SELECT * FROM user_details WHERE user_id=" + "'" + userId + "'";

		return jdbcTemplate.query(sql, new ResultSetExtractor<UserPersonalDetails>() {

			@Override
			public UserPersonalDetails extractData(ResultSet rs) throws SQLException, DataAccessException {

				if (rs.next()) {

					UserPersonalDetails user = new UserPersonalDetails();
					user.setUserId(rs.getInt("user_id"));
					user.setName(rs.getString("name"));
					user.setIcNumber(rs.getString("ic_number"));
					user.setEmail(rs.getString("email"));
					user.setPhoneNumber(rs.getString("phone_number"));
					user.setAddress(rs.getString("address"));
					user.setNationality(rs.getString("nationality"));
					user.setEthnicity(rs.getString("ethnicity"));
					user.setPicture(rs.getBlob("picture"));
					return user;
				}
				return null;

			}

		});
	}
	
	public List<UserPersonalDetails> searchTeacherByName(String search) {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT user_details.* FROM user_details JOIN user_role ON user_details.user_id=user_role.user_id JOIN "
				+ "users ON user_details.user_id=users.user_id WHERE user_role.role='TEACHER' AND "
				+ "users.enabled=1 AND user_details.name LIKE " + "'%" + search + "%'";

		List<UserPersonalDetails> list = jdbcTemplate.query(sql, new RowMapper<UserPersonalDetails>() {

			@Override
			public UserPersonalDetails mapRow(ResultSet rs, int rowNum) throws SQLException {

				UserPersonalDetails user = new UserPersonalDetails();
				user.setUserId(rs.getInt("user_id"));
				user.setName(rs.getString("name"));
				user.setIcNumber(rs.getString("ic_number"));
				user.setEmail(rs.getString("email"));
				user.setPhoneNumber(rs.getString("phone_number"));
				user.setAddress(rs.getString("address"));
				user.setNationality(rs.getString("nationality"));
				user.setEthnicity(rs.getString("ethnicity"));
				user.setPicture(rs.getBlob("picture"));
				return user;
			}
		});
		return list;
	}

	public List<UserPersonalDetails> searchTeacherByIC(String search) {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT user_details.* FROM user_details JOIN user_role ON user_details.user_id=user_role.user_id JOIN "
				+ "users ON user_details.user_id=users.user_id WHERE user_role.role='TEACHER' AND "
				+ "users.enabled=1 AND user_details.ic_number LIKE " + "'%" + search + "%'";

		List<UserPersonalDetails> list = jdbcTemplate.query(sql, new RowMapper<UserPersonalDetails>() {

			@Override
			public UserPersonalDetails mapRow(ResultSet rs, int rowNum) throws SQLException {

				UserPersonalDetails user = new UserPersonalDetails();
				user.setUserId(rs.getInt("user_id"));
				user.setName(rs.getString("name"));
				user.setIcNumber(rs.getString("ic_number"));
				user.setEmail(rs.getString("email"));
				user.setPhoneNumber(rs.getString("phone_number"));
				user.setAddress(rs.getString("address"));
				user.setNationality(rs.getString("nationality"));
				user.setEthnicity(rs.getString("ethnicity"));
				user.setPicture(rs.getBlob("picture"));
				return user;
			}
		});
		return list;
	}

	public List<UserPersonalDetails> searchTeacherByEmail(String search) {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT user_details.* FROM user_details JOIN user_role ON user_details.user_id=user_role.user_id JOIN "
				+ "users ON user_details.user_id=users.user_id WHERE user_role.role='TEACHER' AND "
				+ "users.enabled=1 AND user_details.email LIKE " + "'%" + search + "%'";

		List<UserPersonalDetails> list = jdbcTemplate.query(sql, new RowMapper<UserPersonalDetails>() {

			@Override
			public UserPersonalDetails mapRow(ResultSet rs, int rowNum) throws SQLException {

				UserPersonalDetails user = new UserPersonalDetails();
				user.setUserId(rs.getInt("user_id"));
				user.setName(rs.getString("name"));
				user.setIcNumber(rs.getString("ic_number"));
				user.setEmail(rs.getString("email"));
				user.setPhoneNumber(rs.getString("phone_number"));
				user.setAddress(rs.getString("address"));
				user.setNationality(rs.getString("nationality"));
				user.setEthnicity(rs.getString("ethnicity"));
				user.setPicture(rs.getBlob("picture"));
				return user;
			}
		});
		return list;
	}

	public int totalClassroom() {

		String sql = "SELECT COUNT(classroom_id) FROM classroom";
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	public int emptyClassroomNum() {

		String sql = "SELECT COUNT(classroom.classroom_id) FROM classroom WHERE "
				+ "classroom.classroom_id NOT IN (SELECT classroom_id FROM class_participant)";
		return jdbcTemplate.queryForObject(sql, Integer.class);

	}

	public List<Classroom> emptyClassroom() {

		String sql = "SELECT classroom.class_name FROM classroom WHERE classroom.classroom_id NOT IN (SELECT classroom_id FROM class_participant)";
		List<Classroom> list = jdbcTemplate.query(sql, new RowMapper<Classroom>() {

			@Override
			public Classroom mapRow(ResultSet rs, int rowNum) throws SQLException {
				Classroom classroom = new Classroom();
				classroom.setClassName(rs.getString("class_name"));
				return classroom;
			}
		});
		return list;
	}


	public List<UserPersonalDetails> participantList(Integer classroomId) {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT user_details.user_id,user_details.name,user_details.ic_number,user_details.email FROM user_details "
				+ "JOIN class_participant ON user_details.user_id=class_participant.user_id " + "WHERE classroom_id="
				+ classroomId;

		List<UserPersonalDetails> list = jdbcTemplate.query(sql, new RowMapper<UserPersonalDetails>() {

			@Override
			public UserPersonalDetails mapRow(ResultSet rs, int rowNum) throws SQLException {

				UserPersonalDetails user = new UserPersonalDetails();
				user.setUserId(rs.getInt("user_id"));
				user.setName(rs.getString("name"));
				user.setIcNumber(rs.getString("ic_number"));
				user.setEmail(rs.getString("email"));
				return user;
			}
		});
		return list;
	}

	public int removeParticipant(Integer userId) {

		String sql = "DELETE FROM class_participant WHERE user_id=?";
		return jdbcTemplate.update(sql, userId);

	}

	public int getParticipantClassId(Integer userId) {

		String sql = "SELECT class_participant.classroom_id FROM class_participant WHERE user_id=?";
		return jdbcTemplate.queryForObject(sql, new Object[] { userId }, Integer.class);

	}

	public List<Classroom> searchClassroomByName(String search) {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT classroom.*,(classroom.maximum_participant- (SELECT COUNT(classroom_id) FROM "
				+ "class_participant WHERE classroom.classroom_id=class_participant.classroom_id))"
				+ "AS slot FROM classroom WHERE classroom.class_name LIKE " + "'%" + search + "%'";
		List<Classroom> list = jdbcTemplate.query(sql, new RowMapper<Classroom>() {

			@Override
			public Classroom mapRow(ResultSet rs, int rowNum) throws SQLException {

				Classroom classroom = new Classroom();
				classroom.setClassroomId(rs.getInt("classroom_id"));
				classroom.setClassName(rs.getString("class_name"));
				classroom.setForm(rs.getInt("form"));
				classroom.setTeacherName(rs.getString("class_teacher_name"));
				classroom.setMaxParticipant(rs.getInt("maximum_participant"));
				classroom.setSlot(rs.getInt("slot"));
				return classroom;
			}
		});
		return list;
	}

	public List<Classroom> searchClassroomByTeacherName(String search) {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT classroom.*,(classroom.maximum_participant- (SELECT COUNT(classroom_id) FROM "
				+ "class_participant WHERE classroom.classroom_id=class_participant.classroom_id))"
				+ "AS slot FROM classroom WHERE classroom.class_teacher_name LIKE " + "'%" + search + "%'";
		List<Classroom> list = jdbcTemplate.query(sql, new RowMapper<Classroom>() {

			@Override
			public Classroom mapRow(ResultSet rs, int rowNum) throws SQLException {

				Classroom classroom = new Classroom();
				classroom.setClassroomId(rs.getInt("classroom_id"));
				classroom.setClassName(rs.getString("class_name"));
				classroom.setForm(rs.getInt("form"));
				classroom.setTeacherName(rs.getString("class_teacher_name"));
				classroom.setMaxParticipant(rs.getInt("maximum_participant"));
				classroom.setSlot(rs.getInt("slot"));
				return classroom;
			}
		});
		return list;
	}

	public List<Classroom> searchClassroomByForm(String search) {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT classroom.*,(classroom.maximum_participant- (SELECT COUNT(classroom_id) FROM "
				+ "class_participant WHERE classroom.classroom_id=class_participant.classroom_id))"
				+ "AS slot FROM classroom WHERE classroom.form LIKE " + "'%" + search + "%'";
		List<Classroom> list = jdbcTemplate.query(sql, new RowMapper<Classroom>() {

			@Override
			public Classroom mapRow(ResultSet rs, int rowNum) throws SQLException {

				Classroom classroom = new Classroom();
				classroom.setClassroomId(rs.getInt("classroom_id"));
				classroom.setClassName(rs.getString("class_name"));
				classroom.setForm(rs.getInt("form"));
				classroom.setTeacherName(rs.getString("class_teacher_name"));
				classroom.setMaxParticipant(rs.getInt("maximum_participant"));
				classroom.setSlot(rs.getInt("slot"));
				return classroom;
			}
		});
		return list;
	}

	public List<Announcement> announcementList() {

		String sql = "SELECT announcement.*,user_details.name FROM announcement JOIN user_details "
				+ "ON announcement.user_id=user_details.user_id";

		List<Announcement> list = jdbcTemplate.query(sql, new RowMapper<Announcement>() {

			@Override
			public Announcement mapRow(ResultSet rs, int rowNum) throws SQLException {

				Announcement announcement = new Announcement();
				announcement.setAnnouncementId(rs.getInt("announcement_id"));
				announcement.setTitle(rs.getString("announcement_title"));
				announcement.setDescription(rs.getString("announcement_description"));
				announcement.setDateCreated(rs.getDate("announcement_time"));
				announcement.setName(rs.getString("name"));
				announcement.setUserId(rs.getInt("user_id"));
				return announcement;
			}
		});
		return list;
	}

	public int getUserIdByUsername(String username) {

		String sql = "SELECT user_id FROM users WHERE username=?";
		return jdbcTemplate.queryForObject(sql, new Object[] { username }, Integer.class);
	}

	public int createAnnouncement(Announcement announcement, Integer userId) {

		String sql = "INSERT INTO announcement (announcement_title,announcement_description,announcement_time,user_id) VALUES (?,?,?,?)";
		return jdbcTemplate.update(sql, announcement.getTitle(), announcement.getDescription(),
				announcement.getDateCreated(), userId);

	}

	public Announcement getAnnouncementById(Integer announcementId) {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT * FROM announcement WHERE announcement_id=" + "'" + announcementId + "'";

		return jdbcTemplate.query(sql, new ResultSetExtractor<Announcement>() {

			@Override
			public Announcement extractData(ResultSet rs) throws SQLException, DataAccessException {

				if (rs.next()) {

					Announcement announcement = new Announcement();
					announcement.setAnnouncementId(rs.getInt("announcement_id"));
					announcement.setTitle(rs.getString("announcement_title"));
					announcement.setDescription(rs.getString("announcement_description"));
					announcement.setDateCreated(rs.getDate("announcement_time"));
					announcement.setUserId(rs.getInt("user_id"));
					return announcement;
				}
				return null;

			}

		});
	}

	public int udpateAnnouncement(Announcement announcement, Integer userId) {

		String sql = "UPDATE announcement SET announcement_title=?, announcement_description=?,announcement_time=?,user_id=? WHERE announcement_id=?";
		return jdbcTemplate.update(sql, announcement.getTitle(), announcement.getDescription(),
				announcement.getDateCreated(), userId, announcement.getAnnouncementId());

	}

	public int deleteAnnouncement(Integer announcementId) {

		String sql = "DELETE FROM announcement WHERE announcement_id=?";
		return jdbcTemplate.update(sql, announcementId);

	}

	public List<String> emails() {

		String sql = "SELECT email FROM user_details JOIN user_role ON user_details.user_id=user_role.user_id "
				+ "WHERE user_role.role='STUDENT' OR user_role.role='PARENT'";
		List<String> list = jdbcTemplate.query(sql, new RowMapper<String>() {

			@Override
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {

				String email = rs.getString("email");
				return email;
			}
		});
		return list;

	}

	public List<Announcement> searchByTitle(String search) {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT announcement.*,user_details.name FROM announcement JOIN user_details ON "
				+ "announcement.user_id=user_details.user_id WHERE announcement_title LIKE " + "'%" + search + "%'";
		List<Announcement> list = jdbcTemplate.query(sql, new RowMapper<Announcement>() {

			@Override
			public Announcement mapRow(ResultSet rs, int rowNum) throws SQLException {

				Announcement announcement = new Announcement();
				announcement.setAnnouncementId(rs.getInt("announcement_id"));
				announcement.setTitle(rs.getString("announcement_title"));
				announcement.setDescription(rs.getString("announcement_description"));
				announcement.setDateCreated(rs.getDate("announcement_time"));
				announcement.setUserId(rs.getInt("user_id"));
				announcement.setName(rs.getString("name"));
				return announcement;

			}
		});
		return list;
	}

	public List<Announcement> searchByCreator(String search) {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT announcement.*,user_details.name FROM announcement JOIN user_details ON announcement.user_id=user_details.user_id"
				+ " WHERE user_details.name LIKE  " + "'%" + search + "%'";
		List<Announcement> list = jdbcTemplate.query(sql, new RowMapper<Announcement>() {

			@Override
			public Announcement mapRow(ResultSet rs, int rowNum) throws SQLException {

				Announcement announcement = new Announcement();
				announcement.setAnnouncementId(rs.getInt("announcement_id"));
				announcement.setTitle(rs.getString("announcement_title"));
				announcement.setDescription(rs.getString("announcement_description"));
				announcement.setDateCreated(rs.getDate("announcement_time"));
				announcement.setUserId(rs.getInt("user_id"));
				announcement.setName(rs.getString("name"));
				return announcement;

			}
		});
		return list;
	}

	public List<Announcement> searchByDateCreated(String search) {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT announcement.*,user_details.name FROM announcement JOIN user_details ON announcement.user_id=user_details.user_id"
				+ " WHERE announcement_time LIKE  " + "'%" + search + "%'";
		List<Announcement> list = jdbcTemplate.query(sql, new RowMapper<Announcement>() {

			@Override
			public Announcement mapRow(ResultSet rs, int rowNum) throws SQLException {

				Announcement announcement = new Announcement();
				announcement.setAnnouncementId(rs.getInt("announcement_id"));
				announcement.setTitle(rs.getString("announcement_title"));
				announcement.setDescription(rs.getString("announcement_description"));
				announcement.setDateCreated(rs.getDate("announcement_time"));
				announcement.setUserId(rs.getInt("user_id"));
				announcement.setName(rs.getString("name"));
				return announcement;

			}
		});
		return list;
	}

	public int createFee(Fee fee, Integer userId) {

		String sql = "INSERT INTO fee (fee_name,fee_description,fee_amount,fee_date_created,user_id) VALUES (?,?,?,?,?)";
		return jdbcTemplate.update(sql, fee.getFeeName(), fee.getFeeDescription(), fee.getFeeAmount(),
				fee.getDateCreated(), userId);

	}

	public List<Fee> feeList() {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT fee.*,user_details.name FROM fee JOIN user_details ON user_details.user_id=fee.user_id";
		List<Fee> list = jdbcTemplate.query(sql, new RowMapper<Fee>() {

			@Override
			public Fee mapRow(ResultSet rs, int rowNum) throws SQLException {

				Fee fee = new Fee();
				fee.setFeeId(rs.getInt("fee_id"));
				fee.setFeeName(rs.getString("fee_name"));
				fee.setFeeDescription(rs.getString("fee_description"));
				fee.setDateCreated(rs.getDate("fee_date_created"));
				fee.setFeeAmount(rs.getDouble("fee_amount"));
				fee.setUserId(rs.getInt("user_id"));
				fee.setName(rs.getString("name"));
				return fee;
			}
		});
		return list;
	}

	public Fee getFeeById(Integer feeId) {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT fee.*,user_details.name FROM fee JOIN user_details ON "
				+ "fee.user_id=user_details.user_id WHERE fee_id=" + "'" + feeId + "'";

		return jdbcTemplate.query(sql, new ResultSetExtractor<Fee>() {

			@Override
			public Fee extractData(ResultSet rs) throws SQLException, DataAccessException {

				if (rs.next()) {

					Fee fee = new Fee();
					fee.setFeeId(rs.getInt("fee_id"));
					fee.setFeeName(rs.getString("fee_name"));
					fee.setFeeDescription(rs.getString("fee_description"));
					fee.setDateCreated(rs.getDate("fee_date_created"));
					fee.setFeeAmount(rs.getDouble("fee_amount"));
					fee.setUserId(rs.getInt("user_id"));
					fee.setName(rs.getString("name"));
					return fee;

				}
				return null;

			}

		});
	}

	public int updateFee(Fee fee, Integer userId) {

		String sql = "UPDATE fee SET fee_name=?, fee_description=?, fee_amount=?, fee_date_created=?, user_id=? WHERE fee_id=?";
		return jdbcTemplate.update(sql, fee.getFeeName(), fee.getFeeDescription(), fee.getFeeAmount(),
				fee.getDateCreated(), userId, fee.getFeeId());

	}

	public int deleteFee(Integer feeId) {

		String sql = "DELETE FROM fee WHERE fee_id=?";
		return jdbcTemplate.update(sql, feeId);

	}

	public List<Fee> searchByFeeTitle(String search) {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT fee.*,user_details.name FROM fee JOIN user_details ON"
				+ " fee.user_id=user_details.user_id WHERE fee_name LIKE " + "'%" + search + "%'";
		List<Fee> list = jdbcTemplate.query(sql, new RowMapper<Fee>() {

			@Override
			public Fee mapRow(ResultSet rs, int rowNum) throws SQLException {

				Fee fee = new Fee();
				fee.setFeeId(rs.getInt("fee_id"));
				fee.setFeeName(rs.getString("fee_name"));
				fee.setFeeDescription(rs.getString("fee_description"));
				fee.setDateCreated(rs.getDate("fee_date_created"));
				fee.setFeeAmount(rs.getDouble("fee_amount"));
				fee.setUserId(rs.getInt("user_id"));
				fee.setName(rs.getString("name"));
				return fee;

			}
		});
		return list;
	}

	public List<Fee> searchByFeeDate(String search) {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT fee.*,user_details.name FROM fee JOIN user_details ON"
				+ " fee.user_id=user_details.user_id WHERE fee_date_created LIKE " + "'%" + search + "%'";
		List<Fee> list = jdbcTemplate.query(sql, new RowMapper<Fee>() {

			@Override
			public Fee mapRow(ResultSet rs, int rowNum) throws SQLException {

				Fee fee = new Fee();
				fee.setFeeId(rs.getInt("fee_id"));
				fee.setFeeName(rs.getString("fee_name"));
				fee.setFeeDescription(rs.getString("fee_description"));
				fee.setDateCreated(rs.getDate("fee_date_created"));
				fee.setFeeAmount(rs.getDouble("fee_amount"));
				fee.setUserId(rs.getInt("user_id"));
				fee.setName(rs.getString("name"));
				return fee;

			}
		});
		return list;
	}

	public List<Fee> searchByFeeCreator(String search) {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT fee.*,user_details.name FROM fee JOIN user_details ON"
				+ " fee.user_id=user_details.user_id WHERE user_details.name LIKE " + "'%" + search + "%'";
		List<Fee> list = jdbcTemplate.query(sql, new RowMapper<Fee>() {

			@Override
			public Fee mapRow(ResultSet rs, int rowNum) throws SQLException {

				Fee fee = new Fee();
				fee.setFeeId(rs.getInt("fee_id"));
				fee.setFeeName(rs.getString("fee_name"));
				fee.setFeeDescription(rs.getString("fee_description"));
				fee.setDateCreated(rs.getDate("fee_date_created"));
				fee.setFeeAmount(rs.getDouble("fee_amount"));
				fee.setUserId(rs.getInt("user_id"));
				fee.setName(rs.getString("name"));
				return fee;

			}
		});
		return list;
	}

	public Double totalPaymentReceived() {

		String sql = "SELECT SUM(payment_amount) AS total FROM payment JOIN fee ON fee.fee_id=payment.fee_id "
				+ "WHERE YEAR(fee.fee_date_created)=YEAR(CURDATE()) AND payment.status='accepted'";
		return jdbcTemplate.queryForObject(sql, Double.class);
	}

	public int numberOfAcceptedStatus() {

		String sql = "SELECT COUNT(status) FROM payment JOIN fee ON fee.fee_id=payment.fee_id"
				+ " WHERE YEAR(fee.fee_date_created)=YEAR(CURDATE()) AND payment.status='accepted'";
		return jdbcTemplate.queryForObject(sql, Integer.class);

	}

	public int numberOfPendingStatus() {

		String sql = "SELECT COUNT(status) FROM payment JOIN fee ON fee.fee_id=payment.fee_id"
				+ " WHERE YEAR(fee.fee_date_created)=YEAR(CURDATE()) AND payment.status='pending'";
		return jdbcTemplate.queryForObject(sql, Integer.class);

	}

	public int numberOfRejectedStatus() {

		String sql = "SELECT COUNT(status) FROM payment JOIN fee ON fee.fee_id=payment.fee_id"
				+ " WHERE YEAR(fee.fee_date_created)=YEAR(CURDATE()) AND payment.status='rejected'";
		return jdbcTemplate.queryForObject(sql, Integer.class);

	}

	public List<Payment> paymentList() {

		String sql = "SELECT payment.*,user_details.name FROM payment JOIN user_details ON payment.user_id=user_details.user_id";
		List<Payment> list = jdbcTemplate.query(sql, new RowMapper<Payment>() {

			@Override
			public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {

				Payment payment = new Payment();
				payment.setPaymentId(rs.getInt("payment_id"));
				payment.setPaymentAmount(rs.getDouble("payment_amount"));
				payment.setPaymentDate(rs.getDate("payment_date"));
				payment.setStatus(rs.getString("status"));
				payment.setFeeId(rs.getInt("fee_id"));
				payment.setUserId(rs.getInt("user_id"));
				payment.setName(rs.getString("name"));
				return payment;
			}
		});
		return list;
	}

	public Payment getPaymentById(Integer paymentId) {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT payment.*,user_details.name FROM payment JOIN user_details ON "
				+ "payment.user_id=user_details.user_id WHERE payment_id=" + "'" + paymentId + "'";

		return jdbcTemplate.query(sql, new ResultSetExtractor<Payment>() {

			@Override
			public Payment extractData(ResultSet rs) throws SQLException, DataAccessException {

				if (rs.next()) {

					Payment payment = new Payment();
					payment.setPaymentId(rs.getInt("payment_id"));
					payment.setPaymentAmount(rs.getDouble("payment_amount"));
					payment.setPaymentDate(rs.getDate("payment_date"));
					payment.setStatus(rs.getString("status"));
					payment.setFeeId(rs.getInt("fee_id"));
					payment.setUserId(rs.getInt("user_id"));
					payment.setName(rs.getString("name"));
					return payment;

				}
				return null;

			}

		});

	}

	public Blob getProofById(int paymentId) {

		String query = "SELECT payment_proof FROM payment where payment_id=?";

		Blob photo = jdbcTemplate.queryForObject(query, new Object[] { paymentId }, Blob.class);

		return photo;
	}

	public int acceptPayment(Integer paymentId) {

		String sql = "UPDATE payment SET status='accepted' WHERE payment_id=" + paymentId;
		return jdbcTemplate.update(sql);

	}

	public int rejectPayment(Integer paymentId) {

		String sql = "UPDATE payment SET status='rejected' WHERE payment_id=" + paymentId;
		return jdbcTemplate.update(sql);

	}

	public String getEmailById(Integer userId) {

		String sql = "SELECT email FROM user_details WHERE user_id=?";
		return jdbcTemplate.queryForObject(sql, new Object[] { userId }, String.class);

	}

	public List<Payment> searchPaymentByName(String search) {

		String sql = "SELECT payment.*,user_details.name FROM db_sis.payment JOIN user_details ON "
				+ "payment.user_id=user_details.user_id WHERE user_details.name LIKE " + "'%" + search + "%'";
		List<Payment> list = jdbcTemplate.query(sql, new RowMapper<Payment>() {

			@Override
			public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {

				Payment payment = new Payment();
				payment.setPaymentId(rs.getInt("payment_id"));
				payment.setPaymentAmount(rs.getDouble("payment_amount"));
				payment.setPaymentDate(rs.getDate("payment_date"));
				payment.setStatus(rs.getString("status"));
				payment.setFeeId(rs.getInt("fee_id"));
				payment.setUserId(rs.getInt("user_id"));
				payment.setName(rs.getString("name"));
				return payment;
			}
		});
		return list;
	}

	public List<Payment> searchPaymentByStatus(String search) {

		String sql = "SELECT payment.*,user_details.name FROM db_sis.payment JOIN user_details ON "
				+ "payment.user_id=user_details.user_id WHERE payment.status LIKE " + "'%" + search + "%'";
		List<Payment> list = jdbcTemplate.query(sql, new RowMapper<Payment>() {

			@Override
			public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {

				Payment payment = new Payment();
				payment.setPaymentId(rs.getInt("payment_id"));
				payment.setPaymentAmount(rs.getDouble("payment_amount"));
				payment.setPaymentDate(rs.getDate("payment_date"));
				payment.setStatus(rs.getString("status"));
				payment.setFeeId(rs.getInt("fee_id"));
				payment.setUserId(rs.getInt("user_id"));
				payment.setName(rs.getString("name"));
				return payment;
			}
		});
		return list;
	}

	public List<Payment> searchPaymentByFeeId(String search) {

		String sql = "SELECT payment.*,user_details.name FROM db_sis.payment JOIN user_details ON "
				+ "payment.user_id=user_details.user_id WHERE fee_id LIKE " + "'%" + search + "%'";
		List<Payment> list = jdbcTemplate.query(sql, new RowMapper<Payment>() {

			@Override
			public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {

				Payment payment = new Payment();
				payment.setPaymentId(rs.getInt("payment_id"));
				payment.setPaymentAmount(rs.getDouble("payment_amount"));
				payment.setPaymentDate(rs.getDate("payment_date"));
				payment.setStatus(rs.getString("status"));
				payment.setFeeId(rs.getInt("fee_id"));
				payment.setUserId(rs.getInt("user_id"));
				payment.setName(rs.getString("name"));
				return payment;
			}
		});
		return list;
	}
	
	public List<Subject> subjectList(){
		
		String sql="SELECT * FROM subject";
		List<Subject> list = jdbcTemplate.query(sql, new RowMapper<Subject>() {

			@Override
			public Subject mapRow(ResultSet rs, int rowNum) throws SQLException {

				Subject subject = new Subject();
				subject.setSubjectId(rs.getInt("subject_id"));
				subject.setSubjectName(rs.getString("subject_name"));
				return subject;
			}
		});
		return list;
		
	}
	
	public int deleteSubject(Integer subjectId) {

		String sql = "DELETE FROM subject WHERE subject_id=?";
		return jdbcTemplate.update(sql, subjectId);

	}
	
	public int AddSubject(Subject subject) {
		
		String sql="INSERT INTO subject (subject_name) VALUES (?)";
		return jdbcTemplate.update(sql,subject.getSubjectName());
		
	}
	
	
}
