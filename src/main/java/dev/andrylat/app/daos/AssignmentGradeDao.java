package dev.andrylat.app.daos;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import dev.andrylat.app.dao.mappers.AssignmentGradeMapper;
import dev.andrylat.app.models.AssignmentGrade;

@Repository
public class AssignmentGradeDao implements Dao<AssignmentGrade>{

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final String SELECT_BY_ASSIGNMENT_GRADE_ID_QUERY = "SELECT * FROM assignment_grades WHERE assignment_grade_id=?";
	private static final String SELECT_BY_ASSIGNMENT_ID_QUERY = "SELECT * FROM assignment_grades WHERE assignment_id=?";
	private static final String SELECT_BY_STUDENT_ID_QUERY = "SELECT * FROM assignment_grades WHERE student_id=?";
	private static final String SELECT_BY_ASSIGNMENT_AND_STUDENT_ID_QUERY = "SELECT * FROM assignment_grades WHERE assignment_id=? AND student_id=? ";
	private static final String SELECT_ALL_QUERY = "SELECT * FROM assignment_grades";
    private static final String LIMIT = " LIMIT ";
    private static final String OFFSET = " OFFSET ";
    private static final String TOTAL_COUNT_QUERY = "SELECT COUNT(*) FROM assignment_grades";
	
	private static final String INSERT_QUERY = 
        "INSERT INTO public.assignment_grades (assignment_id, student_id, grade)  "
        + "	VALUES(?,?,?);";
	
	private static final String UPDATE_QUERY = 
        "UPDATE assignment_grades  "
        + "SET assignment_id = ?, student_id = ?, grade = ?  "
        + "WHERE assignment_grade_id = ?;";
	
	private static final Logger logger = LoggerFactory.getLogger(AssignmentGrade.class);
	
	private static final String DELETE_QUERY = "DELETE FROM assignment_grades WHERE assignment_grade_id = ?;";
	
    @Override
    public Page<AssignmentGrade> getAll(Pageable page) {
        logger.debug(
                "Running query to retrieve all AssignmentGrades with provided limit and offset and saving contents into a list");
        List<AssignmentGrade> assignmentGrades = jdbcTemplate.query(SELECT_ALL_QUERY + LIMIT + page.getPageSize() + OFFSET + page.getOffset(), 
            new AssignmentGradeMapper());
        
        logger.debug("Running query to count the total number of rows in assignment_grades table");
        int totalNumberOfAssignmentGrades = jdbcTemplate.queryForObject(TOTAL_COUNT_QUERY, Integer.class);
        logger.debug("Placing all list AssignmentGrade elements into a PageImpl");
        return new PageImpl<AssignmentGrade>(assignmentGrades, page, totalNumberOfAssignmentGrades);
    }
	
	public List<AssignmentGrade> getAssignmentGradesByAssignmentId(long assignmentId){
	    logger.debug("Running query to retrieve list of AssignmentGrades by assignmentId:" + assignmentId);
	    return jdbcTemplate.query(SELECT_BY_ASSIGNMENT_ID_QUERY, new AssignmentGradeMapper(), assignmentId);
	}

	public List<AssignmentGrade> getAssignmentGradesByStudentId(long studentId){
	    logger.debug("Running query to retrieve list of AssignmentGrades by studentId:" + studentId);
	    return jdbcTemplate.query(SELECT_BY_STUDENT_ID_QUERY, new AssignmentGradeMapper(), studentId);
	}
	
	public List<AssignmentGrade> getAssignmentGradesByAssignmentAndStudentId(long assignmentId,long studentId){
	    logger.debug("Running query to retrieve list of AssignmentGrades by assignmentId and studentId: " + assignmentId + " " + studentId );
	    return jdbcTemplate.query(SELECT_BY_ASSIGNMENT_AND_STUDENT_ID_QUERY, new AssignmentGradeMapper(), assignmentId,studentId);
	}
	
	@Override
	public int save(AssignmentGrade assignmentGrade) {
	    logger.debug("Running query to save the following AssignmentGrade object into the database: " + assignmentGrade);
	    return jdbcTemplate.update(INSERT_QUERY,assignmentGrade.getAssignmentId(), assignmentGrade.getStudentId(), assignmentGrade.getGrade());
	}

	@Override
	public void update(AssignmentGrade assignmentGrade) {
	    logger.debug("Running query to update the following AssignmentGrade object in the database: " + assignmentGrade);
	    jdbcTemplate.update(UPDATE_QUERY,assignmentGrade.getAssignmentId(), assignmentGrade.getStudentId(), assignmentGrade.getGrade(), assignmentGrade.getAssignmentGradeId());
	}

	@Override
	public AssignmentGrade get(long assignmentGradeId) {
	    logger.debug("Running query to retrieve AssignmentGrade object by announcementId: " + assignmentGradeId);
	    return jdbcTemplate.queryForObject(SELECT_BY_ASSIGNMENT_GRADE_ID_QUERY, new AssignmentGradeMapper(), assignmentGradeId);
	}

	@Override
	public void delete(long assignmentGradeId) {
	    logger.debug("Running query to delete the AssignmentGrade with the following assignmentGradeId: " + assignmentGradeId);
	    jdbcTemplate.update(DELETE_QUERY, assignmentGradeId);
	}



}
