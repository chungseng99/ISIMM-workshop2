package com.ftmk.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import com.ftmk.model.Announcement;
import com.ftmk.model.Classroom;
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
				+ "user_details.user_id=user_role.user_id WHERE user_role.role='TEACHER'";
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
		String sql = "SELECT * FROM classroom ORDER BY class_name";
		List<Classroom> list = jdbcTemplate.query(sql, new RowMapper<Classroom>() {

			@Override
			public Classroom mapRow(ResultSet rs, int rowNum) throws SQLException {

				Classroom classroom = new Classroom();
				classroom.setClassroomId(rs.getInt("classroom_id"));
				classroom.setClassName(rs.getString("class_name"));
				classroom.setForm(rs.getInt("form"));
				classroom.setTeacherName(rs.getString("class_teacher_name"));
				classroom.setMaxParticipant(rs.getInt("maximum_participant"));
				return classroom;
			}
		});
		return list;
	}

	public Classroom getClassroomById(Integer classroomId) {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT * FROM classroom WHERE classroom_id=" + "'" + classroomId + "'";

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
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT user_details.user_id,user_details.name,user_details.ic_number FROM user_details "
				+ "JOIN user_role ON user_details.user_id=user_role.user_id"
				+ " WHERE user_details.user_id NOT IN (SELECT class_participant.user_id FROM class_participant) AND user_role.role='STUDENT'";

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
	
	public int countUnassignedStudent() {
		
		String sql="SELECT COUNT(user_details.user_id) FROM user_details JOIN user_role ON user_details.user_id=user_role.user_id "
				+ "WHERE user_role.role='STUDENT' AND user_details.user_id NOT IN (SELECT user_id FROM class_participant)";
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}
	
	public int totalStudent() {
		
		String sql="SELECT COUNT(role) FROM user_role WHERE user_role.role='STUDENT'";
		return jdbcTemplate.queryForObject(sql,Integer.class);
	}
	
	public int totalClassroom() {
		
		String sql="SELECT COUNT(classroom_id) FROM classroom";
		return jdbcTemplate.queryForObject(sql,Integer.class);
	}
	
	public int emptyClassroomNum() {
		
		String sql="SELECT COUNT(classroom.classroom_id) FROM classroom WHERE "
				+ "classroom.classroom_id NOT IN (SELECT classroom_id FROM class_participant)";
		return jdbcTemplate.queryForObject(sql, Integer.class);
		
	}
	
	public List<Classroom> emptyClassroom(){
		
		String sql="SELECT classroom.class_name FROM classroom WHERE classroom.classroom_id NOT IN (SELECT classroom_id FROM class_participant)";
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

	public int getStudentCount(Integer classroomId) {

		String sql = "SELECT COUNT(classroom_id) FROM class_participant WHERE classroom_id=?";
		return jdbcTemplate.queryForObject(sql, new Object[] { classroomId }, Integer.class);

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
		String sql = "SELECT * FROM classroom WHERE classroom.class_name LIKE " + "'%" + search + "%'";
		List<Classroom> list = jdbcTemplate.query(sql, new RowMapper<Classroom>() {

			@Override
			public Classroom mapRow(ResultSet rs, int rowNum) throws SQLException {

				Classroom classroom = new Classroom();
				classroom.setClassroomId(rs.getInt("classroom_id"));
				classroom.setClassName(rs.getString("class_name"));
				classroom.setForm(rs.getInt("form"));
				classroom.setTeacherName(rs.getString("class_teacher_name"));
				classroom.setMaxParticipant(rs.getInt("maximum_participant"));
				return classroom;
			}
		});
		return list;
	}

	public List<Classroom> searchClassroomByTeacherName(String search) {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT * FROM classroom WHERE classroom.class_teacher_name LIKE " + "'%" + search + "%'";
		List<Classroom> list = jdbcTemplate.query(sql, new RowMapper<Classroom>() {

			@Override
			public Classroom mapRow(ResultSet rs, int rowNum) throws SQLException {

				Classroom classroom = new Classroom();
				classroom.setClassroomId(rs.getInt("classroom_id"));
				classroom.setClassName(rs.getString("class_name"));
				classroom.setForm(rs.getInt("form"));
				classroom.setTeacherName(rs.getString("class_teacher_name"));
				classroom.setMaxParticipant(rs.getInt("maximum_participant"));
				return classroom;
			}
		});
		return list;
	}

	public List<Classroom> searchClassroomByForm(String search) {
		// Select certain field from each table to be displayed in admin dashboard
		String sql = "SELECT * FROM classroom WHERE classroom.form LIKE " + "'%" + search + "%'";
		List<Classroom> list = jdbcTemplate.query(sql, new RowMapper<Classroom>() {

			@Override
			public Classroom mapRow(ResultSet rs, int rowNum) throws SQLException {

				Classroom classroom = new Classroom();
				classroom.setClassroomId(rs.getInt("classroom_id"));
				classroom.setClassName(rs.getString("class_name"));
				classroom.setForm(rs.getInt("form"));
				classroom.setTeacherName(rs.getString("class_teacher_name"));
				classroom.setMaxParticipant(rs.getInt("maximum_participant"));
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
		return jdbcTemplate.update(sql, announcement.getTitle(), announcement.getDescription(),announcement.getDateCreated(), userId,announcement.getAnnouncementId());

	}
	
	public int deleteAnnouncement(Integer announcementId) {
		
			String sql = "DELETE FROM announcement WHERE announcement_id=?";
			return jdbcTemplate.update(sql, announcementId);

	}
	
	public List<String> emails(){
		
		String sql="SELECT email FROM user_details JOIN user_role ON user_details.user_id=user_role.user_id "
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
	
	
}
