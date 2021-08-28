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
import dev.andrylat.app.dao.mappers.AssignmentMapper;
import dev.andrylat.app.models.Assignment;
import dev.andrylat.app.models.AssignmentGrade;

@Repository
public class AssignmentGradeDao implements Dao<AssignmentGrade>{

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final String SELECT_BY_ASSIGNMENT_GRADE_ID_QUERY = "SELECT * FROM assignment_grades WHERE assignment_grade_id=?";
	private static final String SELECT_BY_ASSIGNMENT_ID_QUERY = "SELECT * FROM assignment_grades WHERE assignment_id=?";
	private static final String SELECT_BY_STUDENT_ID_QUERY = "SELECT * FROM assignment_grades WHERE student_id=?";
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
	
	private static final String DELETE_QUERY = "DELETE FROM assignment_grades WHERE assignment_grade_id = ?;";
	
    @Override
    public Page<AssignmentGrade> getAll(Pageable page) {
        List<AssignmentGrade> assignmentGrades = jdbcTemplate.query(SELECT_ALL_QUERY + LIMIT + page.getPageSize() + OFFSET + page.getOffset(), 
                new AssignmentGradeMapper());
        
        int totalNumberOfAssignmentGrades = jdbcTemplate.queryForObject(TOTAL_COUNT_QUERY, Integer.class);
        
        return new PageImpl<AssignmentGrade>(assignmentGrades, page, totalNumberOfAssignmentGrades);
    }
	
	public List<AssignmentGrade> getAssignmentGradesByAssignmentId(long assignmentId){
	    return jdbcTemplate.query(SELECT_BY_ASSIGNMENT_ID_QUERY, new AssignmentGradeMapper(), assignmentId);
	}

	public List<AssignmentGrade> getAssignmentGradesByStudentId(long studentId){
	    return jdbcTemplate.query(SELECT_BY_STUDENT_ID_QUERY, new AssignmentGradeMapper(), studentId);
	}
	
	@Override
	public int save(AssignmentGrade assignmentGrade) {
	    return jdbcTemplate.update(INSERT_QUERY,assignmentGrade.getAssignmentId(), assignmentGrade.getStudentId(), assignmentGrade.getGrade());
	}

	@Override
	public void update(AssignmentGrade assignmentGrade) {
	    jdbcTemplate.update(UPDATE_QUERY,assignmentGrade.getAssignmentId(), assignmentGrade.getStudentId(), assignmentGrade.getGrade(), assignmentGrade.getAssignmentGradeId());
	}

	@Override
	public AssignmentGrade get(long assignmentGradeId) {
	    return jdbcTemplate.queryForObject(SELECT_BY_ASSIGNMENT_GRADE_ID_QUERY, new AssignmentGradeMapper(), assignmentGradeId);
	}

	@Override
	public void delete(long assignmentGradeId) {
	    jdbcTemplate.update(DELETE_QUERY, assignmentGradeId);
	}



}
