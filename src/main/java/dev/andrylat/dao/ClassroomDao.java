package dev.andrylat.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import dev.andrylat.dao.mappers.ClassroomMapper;
import dev.andrylat.model.Classroom;

@Repository
public class ClassroomDao implements Dao<Classroom>{

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final String SELECT_BY_CLASS_ID_QUERY = "SELECT * FROM classes WHERE class_id=?";
	private static final String SELECT_BY_MAIN_TEACHER_ID_QUERY = "SELECT * FROM classes WHERE main_teacher_id=?";
	private static final String SELECT_ALL_QUERY = "SELECT * FROM classes";
	
	private static final String INSERT_QUERY = "INSERT INTO public.classes (main_teacher_id, name, description) \r\n"
	                                            + "	VALUES(?,?,?);";
	
	private static final String UPDATE_QUERY = "UPDATE classes \r\n"
                                                + "SET main_teacher_id = ?, name = ?, description = ? \r\n"
                                                + "WHERE class_id = ?;";
	
	private static final String DELETE_QUERY = "DELETE FROM classes WHERE class_id = ?;";
	
	@Override
	public Collection<Classroom> getAll() {
		return jdbcTemplate.query(SELECT_ALL_QUERY, new ClassroomMapper());
	}
	
	public List<Classroom> getClassesByMainTeacherId(long mainTeacherId){
		return jdbcTemplate.query(SELECT_BY_MAIN_TEACHER_ID_QUERY, new ClassroomMapper(), mainTeacherId);
	}

	@Override
	public int save(Classroom t) {
		return jdbcTemplate.update(INSERT_QUERY,t.getMainTeacherId(), t.getName(), t.getDescription());
	}

	@Override
	public void update(Classroom t) {
		jdbcTemplate.update(UPDATE_QUERY,t.getMainTeacherId(), t.getName(), t.getDescription(), t.getClassId());
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
