package dev.andrylat.app.daos;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import dev.andrylat.app.dao.mappers.AssignmentGradeMapper;
import dev.andrylat.app.dao.mappers.ClassroomMapper;
import dev.andrylat.app.models.AssignmentGrade;
import dev.andrylat.app.models.Classroom;

@Repository
public class ClassroomDao implements Dao<Classroom>{

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final String SELECT_BY_CLASS_ID_QUERY = "SELECT * FROM classes WHERE class_id=?";
	private static final String SELECT_BY_MAIN_TEACHER_ID_QUERY = "SELECT * FROM classes WHERE main_teacher_id=?";
	private static final String SELECT_ALL_QUERY = "SELECT * FROM classes";
    private static final String LIMIT = " LIMIT ";
    private static final String OFFSET = " OFFSET ";
    private static final String TOTAL_COUNT_QUERY = "SELECT COUNT(*) FROM classes";
	
	private static final String INSERT_QUERY = 
        "INSERT INTO public.classes (main_teacher_id, name, description)  "
        + "	VALUES(?,?,?);";
	
	private static final String UPDATE_QUERY = 
        "UPDATE classes  "
        + "SET main_teacher_id = ?, name = ?, description = ?  "
        + "WHERE class_id = ?;";
	
	private static final String DELETE_QUERY = "DELETE FROM classes WHERE class_id = ?;";
	
    @Override
    public Page<Classroom> getAll(Pageable page) {
        List<Classroom> classrooms = jdbcTemplate.query(SELECT_ALL_QUERY + LIMIT + page.getPageSize() + OFFSET + page.getOffset(), 
                new ClassroomMapper());
        
        int totalNumberOfClassrooms = jdbcTemplate.queryForObject(TOTAL_COUNT_QUERY, Integer.class);
        
        return new PageImpl<Classroom>(classrooms, page, totalNumberOfClassrooms);
    }
	
	public List<Classroom> getClassesByMainTeacherId(long mainTeacherId){
	    return jdbcTemplate.query(SELECT_BY_MAIN_TEACHER_ID_QUERY, new ClassroomMapper(), mainTeacherId);
	}

	@Override
	public int save(Classroom classroom) {
	    return jdbcTemplate.update(INSERT_QUERY,classroom.getMainTeacherId(), classroom.getName(), classroom.getDescription());
	}

	@Override
	public void update(Classroom classroom) {
	    jdbcTemplate.update(UPDATE_QUERY,classroom.getMainTeacherId(), classroom.getName(), classroom.getDescription(), classroom.getClassId());
	}

	@Override
	public Classroom get(long classId) {
	    return jdbcTemplate.queryForObject(SELECT_BY_CLASS_ID_QUERY, new ClassroomMapper(), classId);
	}

	@Override
	public void delete(long classId) {
	    jdbcTemplate.update(DELETE_QUERY, classId);
	}



}
