package dev.andrylat.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import dev.andrylat.model.Student;

public class StudentMapper implements RowMapper<Student> {

	private static final String STUDENT_ID_LABEL = "student_id";
	private static final String USER_ID_LABEL = "user_id";
	private static final String GOALS_LABEL = "goals";
	private static final String DESCRIPTION_LABEL = "description";
	private static final String USERNAME_LABEL = "username";
	private static final String PASSWORD_LABEL = "password";
	private static final String FIRST_NAME_LABEL = "first_name";
	private static final String SURNAME_LABEL = "surname";
	
	
	@Override
	public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
		Student student = new Student();
		student.setStudentId(rs.getLong(STUDENT_ID_LABEL));
		student.setUserId(rs.getLong(USER_ID_LABEL));
		student.setGoals(rs.getString(GOALS_LABEL));
		student.setDescription(rs.getString(DESCRIPTION_LABEL));
		student.setUsername(rs.getString(USERNAME_LABEL));
		student.setPassword(rs.getString(PASSWORD_LABEL));
		student.setFirstName(rs.getString(FIRST_NAME_LABEL));
		student.setSurname(rs.getString(SURNAME_LABEL));
		return student;
	}

}
