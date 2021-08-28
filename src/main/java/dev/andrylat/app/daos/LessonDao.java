package dev.andrylat.app.daos;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import dev.andrylat.app.dao.mappers.ClassroomMapper;
import dev.andrylat.app.dao.mappers.LessonMapper;
import dev.andrylat.app.models.Classroom;
import dev.andrylat.app.models.Lesson;

@Repository
public class LessonDao implements Dao<Lesson>{

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final String SELECT_BY_LESSON_ID_QUERY = "SELECT * FROM lessons WHERE lesson_id=?";
	private static final String SELECT_BY_CLASS_ID_QUERY = "SELECT * FROM lessons WHERE class_id=?";
	private static final String SELECT_ALL_QUERY = "SELECT * FROM lessons";
    private static final String LIMIT = " LIMIT ";
    private static final String OFFSET = " OFFSET ";
    private static final String TOTAL_COUNT_QUERY = "SELECT COUNT(*) FROM lessons";
	
	private static final String INSERT_QUERY = 
        "INSERT INTO public.lessons (class_id, title, text)  "
        + "	VALUES(?,?,?);";
	
	private static final String UPDATE_QUERY = 
        "UPDATE lessons  "
        + "SET class_id = ?, title = ?, text = ?  "
        + "WHERE lesson_id = ?;";
	
	private static final String DELETE_QUERY = "DELETE FROM lessons WHERE lesson_id = ?;";
	
    @Override
    public Page<Lesson> getAll(Pageable page) {
        List<Lesson> lessons = jdbcTemplate.query(SELECT_ALL_QUERY + LIMIT + page.getPageSize() + OFFSET + page.getOffset(), 
                new LessonMapper());
        
        int totalNumberOfLessons = jdbcTemplate.queryForObject(TOTAL_COUNT_QUERY, Integer.class);
        
        return new PageImpl<Lesson>(lessons, page, totalNumberOfLessons);
    }
	
	public List<Lesson> getLessonsByClassId(long classId){
	    return jdbcTemplate.query(SELECT_BY_CLASS_ID_QUERY, new LessonMapper(), classId);
	}

	@Override
	public int save(Lesson lesson) {
	    return jdbcTemplate.update(INSERT_QUERY,lesson.getClassId(), lesson.getTitle(), lesson.getText());
	}

	@Override
	public void update(Lesson lesson) {
	    jdbcTemplate.update(UPDATE_QUERY,lesson.getClassId(), lesson.getTitle(), lesson.getText(), lesson.getLessonId());
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
