package dev.andrylat.app.daos;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import dev.andrylat.app.dao.mappers.UserMapper;
import dev.andrylat.app.models.User;

@Repository
public class UserDao implements Dao<User>{

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final String SELECT_BY_USER_ID_QUERY = "SELECT * FROM users WHERE user_id=?";
	private static final String SELECT_BY_USERNAME_QUERY = "SELECT * FROM users WHERE username=?";
	private static final String SELECT_ALL_QUERY = "SELECT * FROM users";
	
	private static final String INSERT_QUERY = "INSERT INTO public.users (username, password, first_name, surname) \r\n"
	                                            + "	VALUES(?,?,?,?);";
	
	private static final String UPDATE_QUERY = "UPDATE users \r\n"
                                                + "SET username = ?, password = ?, first_name = ?, surname = ? \r\n"
                                                + "WHERE user_id = ?;";
	
	private static final String DELETE_QUERY = "DELETE FROM users WHERE user_id = ?;";
	
	@Override
	public Collection<User> getAll() {
		return jdbcTemplate.query(SELECT_ALL_QUERY, new UserMapper());
	}
	
	public User getUserByUsername(String username){
		return jdbcTemplate.queryForObject(SELECT_BY_USERNAME_QUERY, new UserMapper(), username);
	}

	@Override
	public int save(User t) {
		return jdbcTemplate.update(INSERT_QUERY,t.getUsername(), t.getPassword(), t.getFirstName(), t.getSurname());
	}

	@Override
	public void update(User t) {
		jdbcTemplate.update(UPDATE_QUERY,t.getUsername(), t.getPassword(), t.getFirstName(), t.getSurname(), t.getUserId());
	}

	@Override
	public User get(long userId) {
		return jdbcTemplate.queryForObject(SELECT_BY_USER_ID_QUERY, new UserMapper(), userId);
	}

	@Override
	public void delete(long userId) {
		jdbcTemplate.update(DELETE_QUERY, userId);
	}


}
