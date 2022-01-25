package com.projects.classroom.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.projects.classroom.models.Submission;

public class SubmissionMapper implements RowMapper<Submission>{

	private static final String SUBMISSION_ID_LABEL = "submission_id";
	private static final String ASSIGNMENT_ID_LABEL = "assignment_id";
	private static final String STUDENT_ID_LABEL = "student_id";
	private static final String TITLE_LABEL = "title";
	private static final String TEXT_LABEL = "text";
	
	@Override
	public Submission mapRow(ResultSet rs, int rowNum) throws SQLException {
		Submission submission = new Submission();
		submission.setSubmissionId(rs.getLong(SUBMISSION_ID_LABEL));
		submission.setAssignmentId(rs.getLong(ASSIGNMENT_ID_LABEL));
		submission.setStudentId(rs.getLong(STUDENT_ID_LABEL));
		submission.setTitle(rs.getString(TITLE_LABEL));
		submission.setText(rs.getString(TEXT_LABEL));
		return submission;
	}
}
