package dev.andrylat.app.daos;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import dev.andrylat.app.dao.mappers.LessonMapper;
import dev.andrylat.app.dao.mappers.StudentMapper;
import dev.andrylat.app.models.Lesson;
import dev.andrylat.app.models.Student;

@Repository
public class StudentDao implements Dao<Student>{

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	UserDao userDao;
	
	private static final String SELECT_BY_STUDENT_ID_QUERY = 
        "SELECT * FROM students INNER JOIN users "
        + "ON users.user_id = students.user_id "
        + "WHERE student_id=?";
	private static final String SELECT_BY_USER_ID_QUERY = 
        "SELECT * FROM students INNER JOIN users "
        + "ON users.user_id = students.user_id "
        + "WHERE user_id=?";
	private static final String SELECT_ALL_QUERY = 
        "SELECT * FROM students INNER JOIN users "
        + "ON users.user_id = students.user_id";
    private static final String LIMIT = " LIMIT ";
    private static final String OFFSET = " OFFSET ";
    private static final String TOTAL_COUNT_QUERY = "SELECT COUNT(*) FROM students";
	
	private static final String INSERT_QUERY = 
        "INSERT INTO public.students (user_id, goals, description)"
        + "	VALUES((SELECT user_id FROM users WHERE username=?),"
        + "?,?);";
	
	private static final String UPDATE_QUERY = 
        "UPDATE students "
        + "SET goals = ?, description = ? "
        + "WHERE student_id = ?;";
	
	private static final String DELETE_QUERY = "DELETE FROM students WHERE student_id = ?;";
	
    @Override
    public Page<Student> getAll(Pageable page) {
        List<Student> students = jdbcTemplate.query(SELECT_ALL_QUERY + LIMIT + page.getPageSize() + OFFSET + page.getOffset(), 
            new StudentMapper());
        
        int totalNumberOfStudents = jdbcTemplate.queryForObject(TOTAL_COUNT_QUERY, Integer.class);
        
        return new PageImpl<Student>(students, page, totalNumberOfStudents);
    }
	
	public Student getStudentByUserId(long userId){
	    return jdbcTemplate.queryForObject(SELECT_BY_USER_ID_QUERY, new StudentMapper(), userId);
	}
	
	@Override
	public Student get(long studentId){
	    return jdbcTemplate.queryForObject(SELECT_BY_STUDENT_ID_QUERY, new StudentMapper(), studentId);
	}

	@Override
	public int save(Student student) {
	    userDao.save(student);
	    return jdbcTemplate.update(INSERT_QUERY,student.getUsername(), student.getGoals(), student.getDescription());
	}

	@Override
	public void update(Student student) {
	    jdbcTemplate.update(UPDATE_QUERY,student.getGoals(), student.getDescription(), student.getStudentId());
	}

	@Override
	public void delete(long studentId) {
	    jdbcTemplate.update(DELETE_QUERY, studentId);
	}
}
