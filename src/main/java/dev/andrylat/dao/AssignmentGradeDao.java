package dev.andrylat.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import dev.andrylat.dao.mappers.AssignmentGradeMapper;
import dev.andrylat.model.AssignmentGrade;

@Repository
public class AssignmentGradeDao implements Dao<AssignmentGrade>{

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final String SELECT_BY_ASSIGNMENT_GRADE_ID_QUERY = "SELECT * FROM assignment_grades WHERE assignment_grade_id=?";
	private static final String SELECT_BY_ASSIGNMENT_ID_QUERY = "SELECT * FROM assignment_grades WHERE assignment_id=?";
	private static final String SELECT_BY_STUDENT_ID_QUERY = "SELECT * FROM assignment_grades WHERE student_id=?";
	private static final String SELECT_ALL_QUERY = "SELECT * FROM assignment_grades";
	
	private static final String INSERT_QUERY = "INSERT INTO public.assignment_grades (assignment_id, student_id, grade) \r\n"
												+ "	VALUES(?,?,?);";
	
	private static final String UPDATE_QUERY = "UPDATE assignment_grades \r\n"
												+ "SET assignment_id = ?, student_id = ?, grade = ? \r\n"
												+ "WHERE assignment_grade_id = ?;";
	
	private static final String DELETE_QUERY = "DELETE FROM assignment_grades WHERE assignment_grade_id = ?;";
	
	@Override
	public Collection<AssignmentGrade> getAll() {
		return jdbcTemplate.query(SELECT_ALL_QUERY, new AssignmentGradeMapper());
	}
	
	public List<AssignmentGrade> getAssignmentGradesByAssignmentId(long assignmentId){
		return jdbcTemplate.query(SELECT_BY_ASSIGNMENT_ID_QUERY, new AssignmentGradeMapper(), assignmentId);
	}

	public List<AssignmentGrade> getAssignmentGradesByStudentId(long studentId){
		return jdbcTemplate.query(SELECT_BY_STUDENT_ID_QUERY, new AssignmentGradeMapper(), studentId);
	}
	
	@Override
	public int save(AssignmentGrade t) {
		return jdbcTemplate.update(INSERT_QUERY,t.getAssignmentId(), t.getStudentId(), t.getGrade());
	}

	@Override
	public void update(AssignmentGrade t) {
		jdbcTemplate.update(UPDATE_QUERY,t.getAssignmentId(), t.getStudentId(), t.getGrade(), t.getAssignmentGradeId());
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
