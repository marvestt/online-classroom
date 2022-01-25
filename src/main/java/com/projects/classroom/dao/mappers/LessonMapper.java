package com.projects.classroom.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.projects.classroom.models.Lesson;

public class LessonMapper implements RowMapper<Lesson>{
	private static final String LESSON_ID_LABEL = "lesson_id";
	private static final String CLASS_ID_LABEL = "class_id";
	private static final String TITLE_LABEL = "title";
	private static final String TEXT_LABEL = "text";
	
	@Override
	public Lesson mapRow(ResultSet rs, int rowNum) throws SQLException {
		Lesson lesson = new Lesson();
		lesson.setLessonId(rs.getLong(CLASS_ID_LABEL));
		lesson.setClassId(rs.getLong(CLASS_ID_LABEL));
		lesson.setTitle(rs.getString(TITLE_LABEL));
		lesson.setText(rs.getString(TEXT_LABEL));
		return lesson;
	}
}