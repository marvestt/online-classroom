package dev.andrylat.app.daos;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import dev.andrylat.app.dao.mappers.LessonMapper;
import dev.andrylat.app.models.Lesson;

@Repository
public class LessonDao implements Dao<Lesson>{

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final String SELECT_BY_LESSON_ID_QUERY = "SELECT * FROM lessons WHERE lesson_id=?";
	private static final String SELECT_BY_CLASS_ID_QUERY = "SELECT * FROM lessons WHERE class_id=?";
	private static final String SELECT_ALL_QUERY = "SELECT * FROM lessons";
	
	private static final String INSERT_QUERY = "INSERT INTO public.lessons (class_id, title, text) \r\n"
	                                            + "	VALUES(?,?,?);";
	
	private static final String UPDATE_QUERY = "UPDATE lessons \r\n"
                                                + "SET class_id = ?, title = ?, text = ? \r\n"
                                                + "WHERE lesson_id = ?;";
	
	private static final String DELETE_QUERY = "DELETE FROM lessons WHERE lesson_id = ?;";
	
	@Override
	public Collection<Lesson> getAll() {
		return jdbcTemplate.query(SELECT_ALL_QUERY, new LessonMapper());
	}
	
	public List<Lesson> getLessonsByClassId(long classId){
		return jdbcTemplate.query(SELECT_BY_CLASS_ID_QUERY, new LessonMapper(), classId);
	}

	@Override
	public int save(Lesson t) {
		return jdbcTemplate.update(INSERT_QUERY,t.getClassId(), t.getTitle(), t.getText());
	}

	@Override
	public void update(Lesson t) {
		jdbcTemplate.update(UPDATE_QUERY,t.getClassId(), t.getTitle(), t.getText(), t.getLessonId());
	}

	@Override
	public Lesson get(long lessonId) {
		return jdbcTemplate.queryForObject(SELECT_BY_LESSON_ID_QUERY, new LessonMapper(), lessonId);
	}

	@Override
	public void delete(long lessonId) {
		jdbcTemplate.update(DELETE_QUERY, lessonId);
	}
}
