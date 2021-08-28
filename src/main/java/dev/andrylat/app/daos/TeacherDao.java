package dev.andrylat.app.daos;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import dev.andrylat.app.dao.mappers.SubmissionMapper;
import dev.andrylat.app.dao.mappers.TeacherMapper;
import dev.andrylat.app.models.Submission;
import dev.andrylat.app.models.Teacher;

@Repository
public class TeacherDao implements Dao<Teacher>{

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	UserDao userDao;
	
	private static final String SELECT_BY_TEACHER_ID_QUERY = 
        "SELECT * FROM teachers INNER JOIN users "
        + "ON users.user_id = teachers.user_id "
        + "WHERE teacher_id=?";
	private static final String SELECT_BY_USER_ID_QUERY = 
        "SELECT * FROM teachers INNER JOIN users "
        + "ON users.user_id = teachers.user_id "
        + "WHERE user_id=?";
	private static final String SELECT_ALL_QUERY = 
        "SELECT * FROM teachers INNER JOIN users "
        + "ON users.user_id = teachers.user_id";
    private static final String LIMIT = " LIMIT ";
    private static final String OFFSET = " OFFSET ";
    private static final String TOTAL_COUNT_QUERY = "SELECT COUNT(*) FROM teachers";
	
	private static final String INSERT_QUERY = 
        "INSERT INTO public.teachers (user_id, professional_name, description)"
        + "	VALUES((SELECT user_id FROM users WHERE username=?),"
        + "?,?);";
	
	private static final String UPDATE_QUERY = 
        "UPDATE teachers "
        + "SET professional_name = ?, description = ? "
        + "WHERE teacher_id = ?;";
	
	private static final String DELETE_QUERY = "DELETE FROM teachers WHERE teacher_id = ?;";
	
    @Override
    public Page<Teacher> getAll(Pageable page) {
        List<Teacher> teachers = jdbcTemplate.query(SELECT_ALL_QUERY + LIMIT + page.getPageSize() + OFFSET + page.getOffset(), 
                new TeacherMapper());
        
        int totalNumberOfTeachers = jdbcTemplate.queryForObject(TOTAL_COUNT_QUERY, Integer.class);
        
        return new PageImpl<Teacher>(teachers, page, totalNumberOfTeachers);
    }
	
	public Teacher getTeacherByUserId(long userId){
	    return jdbcTemplate.queryForObject(SELECT_BY_USER_ID_QUERY, new TeacherMapper(), userId);
	}
	
	@Override
	public Teacher get(long teacherId){
	    return jdbcTemplate.queryForObject(SELECT_BY_TEACHER_ID_QUERY, new TeacherMapper(), teacherId);
	}

	@Override
	public int save(Teacher teacher) {
	    userDao.save(teacher);
	    return jdbcTemplate.update(INSERT_QUERY,teacher.getUsername(), teacher.getProfessionalName(), teacher.getDescription());
	}

	@Override
	public void update(Teacher teacher) {
	    jdbcTemplate.update(UPDATE_QUERY,teacher.getProfessionalName(), teacher.getDescription(), teacher.getTeacherId());
	}

	@Override
	public void delete(long teacherId) {
	    jdbcTemplate.update(DELETE_QUERY, teacherId);
	}

}
