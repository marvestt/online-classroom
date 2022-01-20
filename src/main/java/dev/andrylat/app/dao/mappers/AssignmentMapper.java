package dev.andrylat.app.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import dev.andrylat.app.models.Assignment;

public class AssignmentMapper implements RowMapper<Assignment>{
	private static final String ASSIGNMENT_ID_LABEL = "assignment_id";
	private static final String CLASS_ID_LABEL = "class_id";
	private static final String TITLE_LABEL = "title";
	private static final String DESCRIPTION_LABEL = "description";
	
	@Override
	public Assignment mapRow(ResultSet rs, int rowNum) throws SQLException {
		Assignment assignment = new Assignment();
		assignment.setAssignmentId(rs.getLong(ASSIGNMENT_ID_LABEL));
		assignment.setClassId(rs.getLong(CLASS_ID_LABEL));
		assignment.setTitle(rs.getString(TITLE_LABEL));
		assignment.setDescription(rs.getString(DESCRIPTION_LABEL));
		return assignment;
	}
}
