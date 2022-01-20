package dev.andrylat.app.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import dev.andrylat.app.models.AssignmentGrade;

public class AssignmentGradeMapper implements RowMapper<AssignmentGrade>{
	private static final String ASSIGNMENT_GRADE_ID_LABEL = "assignment_grade_id";
	private static final String ASSIGNMENT_ID = "assignment_id";
	private static final String STUDENT_ID = "student_id";
	private static final String GRADE_LABEL = "grade";
	
	@Override
	public AssignmentGrade mapRow(ResultSet rs, int rowNum) throws SQLException {
		AssignmentGrade assignmentGrade = new AssignmentGrade();
		assignmentGrade.setAssignmentGradeId(rs.getLong(ASSIGNMENT_GRADE_ID_LABEL));
		assignmentGrade.setAssignmentId(rs.getLong(ASSIGNMENT_ID));
		assignmentGrade.setStudentId(rs.getLong(STUDENT_ID));
		assignmentGrade.setGrade(rs.getString(GRADE_LABEL));	
		return assignmentGrade;
	}
}