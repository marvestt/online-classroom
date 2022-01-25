package com.projects.classroom.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.projects.classroom.models.User;

public class UserMapper implements RowMapper<User>{
	
	private static final String USER_ID_LABEL = "user_id";
	private static final String USERNAME_LABEL = "username";
	private static final String PASSWORD_LABEL = "password";
	private static final String FIRST_NAME_LABEL = "first_name";
	private static final String SURNAME_LABEL = "surname";
	
	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		User user = new User();
		user.setUserId(rs.getLong(USER_ID_LABEL));
		user.setUsername(rs.getString(USERNAME_LABEL));
		user.setPassword(rs.getString(PASSWORD_LABEL));
		user.setFirstName(rs.getString(FIRST_NAME_LABEL));
		user.setSurname(rs.getString(SURNAME_LABEL));
		return user;
	}

}
