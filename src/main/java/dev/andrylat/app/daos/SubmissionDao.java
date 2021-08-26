package dev.andrylat.app.daos;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import dev.andrylat.app.dao.mappers.SubmissionMapper;
import dev.andrylat.app.models.Submission;

@Repository
public class SubmissionDao implements Dao<Submission>{

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final String SELECT_BY_SUBMISSION_ID_QUERY = "SELECT * FROM submissions WHERE submission_id=?";
	private static final String SELECT_BY_STUDENT_ID_QUERY = "SELECT * FROM submissions WHERE student_id=?";
	private static final String SELECT_BY_ASSIGNMENT_ID_QUERY = "SELECT * FROM submissions WHERE assignment_id=?";
	private static final String SELECT_ALL_QUERY = "SELECT * FROM submissions";
	
	private static final String INSERT_QUERY = "INSERT INTO public.submissions (assignment_id, student_id, title, text) \r\n"
	                                            + "	VALUES(?,?,?,?);";
	
	private static final String UPDATE_QUERY = "UPDATE submissions \r\n"
                                                + "SET assignment_id = ?, student_id = ?, title = ?, text = ? \r\n"
                                                + "WHERE submission_id = ?;";
	
	private static final String DELETE_QUERY = "DELETE FROM submissions WHERE submission_id = ?;";
	
	@Override
	public Collection<Submission> getAll() {
		return jdbcTemplate.query(SELECT_ALL_QUERY, new SubmissionMapper());
	}
	
	public List<Submission> getSubmissionsByStudentId(long studentId){
		return jdbcTemplate.query(SELECT_BY_STUDENT_ID_QUERY, new SubmissionMapper(), studentId);
	}
	
	public List<Submission> getSubmissionsByAssignmentId(long assignmentId){
		return jdbcTemplate.query(SELECT_BY_ASSIGNMENT_ID_QUERY, new SubmissionMapper(), assignmentId);
	}

	@Override
	public int save(Submission t) {
		return jdbcTemplate.update(INSERT_QUERY,t.getAssignmentId(), t.getStudentId(), t.getTitle(), t.getText());
	} 

	@Override
	public void update(Submission t) {
		jdbcTemplate.update(UPDATE_QUERY,t.getAssignmentId(), t.getStudentId(), t.getTitle(), t.getText(), t.getSubmissionId());
	}

	@Override
	public Submission get(long submissionId) {
		return jdbcTemplate.queryForObject(SELECT_BY_SUBMISSION_ID_QUERY, new SubmissionMapper(), submissionId);
	}

	@Override
	public void delete(long submissionId) {
		jdbcTemplate.update(DELETE_QUERY, submissionId);
	}

}
