package dev.andrylat.app.daos;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import dev.andrylat.app.dao.mappers.TeacherMapper;
import dev.andrylat.app.dao.mappers.UserMapper;
import dev.andrylat.app.models.Teacher;
import dev.andrylat.app.models.User;

@Repository
public class UserDao implements Dao<User>{

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final String SELECT_BY_USER_ID_QUERY = "SELECT * FROM users WHERE user_id=?";
	private static final String SELECT_BY_USERNAME_QUERY = "SELECT * FROM users WHERE username=?";
	private static final String SELECT_ALL_QUERY = "SELECT * FROM users";
    private static final String LIMIT = " LIMIT ";
    private static final String OFFSET = " OFFSET ";
    private static final String TOTAL_COUNT_QUERY = "SELECT COUNT(*) FROM users";
	
	private static final String INSERT_QUERY = 
        "INSERT INTO public.users (username, password, first_name, surname)  "
        + "	VALUES(?,?,?,?);";

	private static final String UPDATE_QUERY = 
        "UPDATE users  "
        + "SET username = ?, password = ?, first_name = ?, surname = ?  "
        + "WHERE user_id = ?;";
	
	private static final String DELETE_QUERY = "DELETE FROM users WHERE user_id = ?;";
	
    @Override
    public Page<User> getAll(Pageable page) {
        List<User> users = jdbcTemplate.query(SELECT_ALL_QUERY + LIMIT + page.getPageSize() + OFFSET + page.getOffset(), 
                new UserMapper());
        
        int totalNumberOfUsers = jdbcTemplate.queryForObject(TOTAL_COUNT_QUERY, Integer.class);
        
        return new PageImpl<User>(users, page, totalNumberOfUsers);
    }
	
	public User getUserByUsername(String username){
	    return jdbcTemplate.queryForObject(SELECT_BY_USERNAME_QUERY, new UserMapper(), username);
	}

	@Override
	public int save(User user) {
	    return jdbcTemplate.update(INSERT_QUERY,user.getUsername(), user.getPassword(), user.getFirstName(), user.getSurname());
	}

	@Override
	public void update(User user) {
	    jdbcTemplate.update(UPDATE_QUERY,user.getUsername(), user.getPassword(), user.getFirstName(), user.getSurname(), user.getUserId());
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
