package dev.andrylat.dao;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import dev.andrylat.dao.mappers.StudentMapper;
import dev.andrylat.model.Student;

@Repository
public class StudentDao implements Dao<Student>{

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	UserDao userDao;
	
	private static final String SELECT_BY_STUDENT_ID_QUERY = "SELECT * FROM students INNER JOIN users \n"
                                                            + "ON users.user_id = students.user_id \n"
                                                            + "WHERE student_id=?";
	private static final String SELECT_BY_USER_ID_QUERY = "SELECT * FROM students INNER JOIN users \n"
                                                            + "ON users.user_id = students.user_id \n"
                                                            + "WHERE user_id=?";
	private static final String SELECT_ALL_QUERY = "SELECT * FROM students INNER JOIN users "
                                                    + "ON users.user_id = students.user_id";
	
	private static final String INSERT_QUERY = "INSERT INTO public.students (user_id, goals, description)"
                                                + "	VALUES((SELECT user_id FROM users WHERE username=?),"
                                                + "?,?);";
	
	private static final String UPDATE_QUERY = "UPDATE students \r\n"
                                                + "SET goals = ?, description = ? \n"
                                                + "WHERE student_id = ?;";
	
	private static final String DELETE_QUERY = "DELETE FROM students WHERE student_id = ?;";
	
	@Override
	public Collection<Student> getAll() {
		return jdbcTemplate.query(SELECT_ALL_QUERY, new StudentMapper());
	}
	
	public Student getStudentByUserId(long userId){
		return jdbcTemplate.queryForObject(SELECT_BY_USER_ID_QUERY, new StudentMapper(), userId);
	}
	
	@Override
	public Student get(long studentId){
		return jdbcTemplate.queryForObject(SELECT_BY_STUDENT_ID_QUERY, new StudentMapper(), studentId);
	}

	@Override
	public int save(Student t) {
		userDao.save(t);
		return jdbcTemplate.update(INSERT_QUERY,t.getUsername(), t.getGoals(), t.getDescription());
	}

	@Override
	public void update(Student t) {
		jdbcTemplate.update(UPDATE_QUERY,t.getGoals(), t.getDescription(), t.getStudentId());
	}

	@Override
	public void delete(long studentId) {
		jdbcTemplate.update(DELETE_QUERY, studentId);
	}
}
