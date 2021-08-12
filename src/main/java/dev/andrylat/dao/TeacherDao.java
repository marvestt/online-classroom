package dev.andrylat.dao;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import dev.andrylat.dao.mappers.TeacherMapper;
import dev.andrylat.model.Teacher;

@Repository
public class TeacherDao implements Dao<Teacher>{

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	UserDao userDao;
	
	private static final String SELECT_BY_TEACHER_ID_QUERY = "SELECT * FROM teachers INNER JOIN users \n"
															+ "ON users.user_id = teachers.user_id \n"
															+ "WHERE teacher_id=?";
	private static final String SELECT_BY_USER_ID_QUERY = "SELECT * FROM teachers INNER JOIN users \n"
															+ "ON users.user_id = teachers.user_id \n"
															+ "WHERE user_id=?";
	private static final String SELECT_ALL_QUERY = "SELECT * FROM teachers INNER JOIN users "
													+ "ON users.user_id = teachers.user_id";
	
	private static final String INSERT_QUERY = "INSERT INTO public.teachers (user_id, professional_name, description)"
												+ "	VALUES((SELECT user_id FROM users WHERE username=?),"
												+ "?,?);";
	
	private static final String UPDATE_QUERY = "UPDATE teachers \r\n"
												+ "SET professional_name = ?, description = ? \n"
												+ "WHERE teacher_id = ?;";
	
	private static final String DELETE_QUERY = "DELETE FROM teachers WHERE teacher_id = ?;";
	
	@Override
	public Collection<Teacher> getAll() {
		return jdbcTemplate.query(SELECT_ALL_QUERY, new TeacherMapper());
	}
	
	public Teacher getTeacherByUserId(long userId){
		return jdbcTemplate.queryForObject(SELECT_BY_USER_ID_QUERY, new TeacherMapper(), userId);
	}
	
	@Override
	public Teacher get(long teacherId){
		return jdbcTemplate.queryForObject(SELECT_BY_TEACHER_ID_QUERY, new TeacherMapper(), teacherId);
	}

	@Override
	public int save(Teacher t) {
		userDao.save(t);
		return jdbcTemplate.update(INSERT_QUERY,t.getUsername(), t.getProfessionalName(), t.getDescription());
	}

	@Override
	public void update(Teacher t) {
		jdbcTemplate.update(UPDATE_QUERY,t.getProfessionalName(), t.getDescription(), t.getTeacherId());
	}

	@Override
	public void delete(long teacherId) {
		jdbcTemplate.update(DELETE_QUERY, teacherId);
	}

}
