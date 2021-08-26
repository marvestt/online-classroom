package dev.andrylat.app.daos;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import dev.andrylat.app.dao.mappers.AssignmentMapper;
import dev.andrylat.app.models.Assignment;

@Repository
public class AssignmentDao implements Dao<Assignment>{

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final String SELECT_BY_ASSIGNMENT_ID_QUERY = "SELECT * FROM assignments WHERE assignment_id=?";
	private static final String SELECT_BY_CLASS_ID_QUERY = "SELECT * FROM assignments WHERE class_id=?";
	private static final String SELECT_ALL_QUERY = "SELECT * FROM assignments";
	
	private static final String INSERT_QUERY = "INSERT INTO public.assignments (class_id, title, description) \r\n"
	                                            + "	VALUES(?,?,?);";
	
	private static final String UPDATE_QUERY = "UPDATE assignments \r\n"
                                                + "SET class_id = ?, title = ?, description = ? \r\n"
                                                + "WHERE assignment_id = ?;";
	
	private static final String DELETE_QUERY = "DELETE FROM assignments WHERE assignment_id = ?;";
	
	@Override
	public Collection<Assignment> getAll() {
		return jdbcTemplate.query(SELECT_ALL_QUERY, new AssignmentMapper());
	}
	
	public List<Assignment> getAssignmentsByClassId(long classId){
		return jdbcTemplate.query(SELECT_BY_CLASS_ID_QUERY, new AssignmentMapper(), classId);
	}

	@Override
	public int save(Assignment t) {
		return jdbcTemplate.update(INSERT_QUERY,t.getClassId(), t.getTitle(), t.getDescription());
	}

	@Override
	public void update(Assignment t) {
		jdbcTemplate.update(UPDATE_QUERY,t.getClassId(),t.getTitle(), t.getDescription(), t.getAssignmentId());
	}

	@Override
	public Assignment get(long assignmentId) {
		// TODO Auto-generated method stub
		return jdbcTemplate.queryForObject(SELECT_BY_ASSIGNMENT_ID_QUERY, new AssignmentMapper(), assignmentId);
	}

	@Override
	public void delete(long assignmentId) {
		jdbcTemplate.update(DELETE_QUERY, assignmentId);
		
	}


}
