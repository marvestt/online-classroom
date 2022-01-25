package com.projects.classroom.daos;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.projects.classroom.dao.mappers.AssignmentMapper;
import com.projects.classroom.models.Assignment;

@Repository
public class AssignmentDao implements Dao<Assignment> {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String SELECT_BY_ASSIGNMENT_ID_QUERY = "SELECT * FROM assignments WHERE assignment_id=?";
    private static final String SELECT_BY_CLASS_ID_QUERY = "SELECT * FROM assignments WHERE class_id=?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM assignments";
    private static final String LIMIT = " LIMIT ";
    private static final String OFFSET = " OFFSET ";
    private static final String TOTAL_COUNT_QUERY = "SELECT COUNT(*) FROM assignments";

    private static final String INSERT_QUERY = "INSERT INTO public.assignments (class_id, title, description)  "
            + "	VALUES(?,?,?);";

    private static final String UPDATE_QUERY = "UPDATE assignments  " + "SET class_id = ?, title = ?, description = ?  "
            + "WHERE assignment_id = ?;";

    private static final String DELETE_QUERY = "DELETE FROM assignments WHERE assignment_id = ?;";

    private static final Logger logger = LoggerFactory.getLogger(AssignmentDao.class);
    
    @Override
    public Page<Assignment> getAll(Pageable page) {
        logger.debug(
                "Running query to retrieve all assignments with provided limit and offset and saving contents into a list");
        List<Assignment> assignments = jdbcTemplate.query(
                SELECT_ALL_QUERY + LIMIT + page.getPageSize() + OFFSET + page.getOffset(), new AssignmentMapper());
        
        logger.debug("Running query to count the total number of rows in assignments table");
        int totalNumberOfAssignments = jdbcTemplate.queryForObject(TOTAL_COUNT_QUERY, Integer.class);
        logger.debug("Placing all list assignment elements into a PageImpl object");
        return new PageImpl<Assignment>(assignments, page, totalNumberOfAssignments);
    }

    public List<Assignment> getAssignmentsByClassId(long classId) {
        logger.debug("Running query to retrieve list of Assignments by class_id:" + classId);
        return jdbcTemplate.query(SELECT_BY_CLASS_ID_QUERY, new AssignmentMapper(), classId);
    }

    @Override
    public int save(Assignment assignment) {
        logger.debug("Running query to save the following Assignment object into the database: " + assignment);
        return jdbcTemplate.update(INSERT_QUERY, assignment.getClassId(), assignment.getTitle(),
                assignment.getDescription());
    }

    @Override
    public void update(Assignment assignment) {
        logger.debug("Running query to update the following Assignment object in the database: " + assignment);
        jdbcTemplate.update(UPDATE_QUERY, assignment.getClassId(), assignment.getTitle(), assignment.getDescription(),
                assignment.getAssignmentId());
    }

    @Override
    public Assignment get(long assignmentId) {
        logger.debug("Running query to retrieve Assignment object by announcemntId: " + assignmentId);
        return jdbcTemplate.queryForObject(SELECT_BY_ASSIGNMENT_ID_QUERY, new AssignmentMapper(), assignmentId);
    }

    @Override
    public void delete(long assignmentId) {
        logger.debug("Running query to delete Assignment with the following assignmentId: " + assignmentId);
        jdbcTemplate.update(DELETE_QUERY, assignmentId);
    }

}
