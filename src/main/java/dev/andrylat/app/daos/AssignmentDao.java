package dev.andrylat.app.daos;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import dev.andrylat.app.dao.mappers.AnnouncementMapper;
import dev.andrylat.app.dao.mappers.AssignmentMapper;
import dev.andrylat.app.models.Announcement;
import dev.andrylat.app.models.Assignment;

@Repository
public class AssignmentDao implements Dao<Assignment>{

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final String SELECT_BY_ASSIGNMENT_ID_QUERY = "SELECT * FROM assignments WHERE assignment_id=?";
	private static final String SELECT_BY_CLASS_ID_QUERY = "SELECT * FROM assignments WHERE class_id=?";
	private static final String SELECT_ALL_QUERY = "SELECT * FROM assignments";
    private static final String LIMIT = " LIMIT ";
    private static final String OFFSET = " OFFSET ";
    private static final String TOTAL_COUNT_QUERY = "SELECT COUNT(*) FROM assignments";
	
	private static final String INSERT_QUERY = 
        "INSERT INTO public.assignments (class_id, title, description)  "
        + "	VALUES(?,?,?);";
	
	private static final String UPDATE_QUERY = 
        "UPDATE assignments  "
        + "SET class_id = ?, title = ?, description = ?  "
        + "WHERE assignment_id = ?;";
	
	private static final String DELETE_QUERY = "DELETE FROM assignments WHERE assignment_id = ?;";
	
    @Override
    public Page<Assignment> getAll(Pageable page) {
        List<Assignment> assignments = jdbcTemplate.query(SELECT_ALL_QUERY + LIMIT + page.getPageSize() + OFFSET + page.getOffset(), 
                new AssignmentMapper());
        
        int totalNumberOfAssignments = jdbcTemplate.queryForObject(TOTAL_COUNT_QUERY, Integer.class);
        
        return new PageImpl<Assignment>(assignments, page, totalNumberOfAssignments);
    }
	
	public List<Assignment> getAssignmentsByClassId(long classId){
	    return jdbcTemplate.query(SELECT_BY_CLASS_ID_QUERY, new AssignmentMapper(), classId);
	}

	@Override
	public int save(Assignment assignment) {
	    return jdbcTemplate.update(INSERT_QUERY,assignment .getClassId(), assignment .getTitle(), assignment .getDescription());
	}

	@Override
	public void update(Assignment assignment) {
	    jdbcTemplate.update(UPDATE_QUERY,assignment.getClassId(),assignment.getTitle(), assignment.getDescription(), assignment.getAssignmentId());
	}

	@Override
	public Assignment get(long assignmentId) {
	    return jdbcTemplate.queryForObject(SELECT_BY_ASSIGNMENT_ID_QUERY, new AssignmentMapper(), assignmentId);
	}

	@Override
	public void delete(long assignmentId) {
	    jdbcTemplate.update(DELETE_QUERY, assignmentId);
	}


}
