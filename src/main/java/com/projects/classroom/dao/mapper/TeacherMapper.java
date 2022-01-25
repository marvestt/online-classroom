package com.projects.classroom.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.projects.classroom.model.Teacher;

public class TeacherMapper implements RowMapper<Teacher> {
	
	private static final String TEACHER_ID_LABEL = "teacher_id";
	private static final String USER_ID_LABEL = "user_id";
	private static final String PROFESSIONAL_NAME_LABEL = "professional_name";
	private static final String DESCRIPTION_LABEL = "description";
	private static final String USERNAME_LABEL = "username";
	private static final String PASSWORD_LABEL = "password";
	private static final String FIRST_NAME_LABEL = "first_name";
	private static final String SURNAME_LABEL = "surname";
	
	@Override
	public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
		Teacher teacher = new Teacher();
		teacher.setTeacherId(rs.getLong(TEACHER_ID_LABEL));
		teacher.setUserId(rs.getLong(USER_ID_LABEL));
		teacher.setProfessionalName(rs.getString(PROFESSIONAL_NAME_LABEL));
		teacher.setDescription(rs.getString(DESCRIPTION_LABEL));
		teacher.setUsername(rs.getString(USERNAME_LABEL));
		teacher.setPassword(rs.getString(PASSWORD_LABEL));
		teacher.setFirstName(rs.getString(FIRST_NAME_LABEL));
		teacher.setSurname(rs.getString(SURNAME_LABEL));
		return teacher;
	}
}
