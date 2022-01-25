package com.projects.classroom.dao;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.projects.classroom.dao.mapper.LessonMapper;
import com.projects.classroom.model.Lesson;

@Repository
public class LessonDao implements Dao<Lesson> {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String SELECT_BY_LESSON_ID_QUERY = "SELECT * FROM lessons WHERE lesson_id=?";
    private static final String SELECT_BY_CLASS_ID_QUERY = "SELECT * FROM lessons WHERE class_id=?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM lessons";
    private static final String LIMIT = " LIMIT ";
    private static final String OFFSET = " OFFSET ";
    private static final String TOTAL_COUNT_QUERY = "SELECT COUNT(*) FROM lessons";

    private static final String INSERT_QUERY = "INSERT INTO public.lessons (class_id, title, text)  "
            + "	VALUES(?,?,?);";

    private static final String UPDATE_QUERY = "UPDATE lessons  " + "SET class_id = ?, title = ?, text = ?  "
            + "WHERE lesson_id = ?;";

    private static final String DELETE_QUERY = "DELETE FROM lessons WHERE lesson_id = ?;";

    private static final Logger logger = LoggerFactory.getLogger(LessonDao.class);

    @Override
    public Page<Lesson> getAll(Pageable page) {
        logger.debug(
                "Running query to retrieve all Lessons with provided limit and offset and saving contents into a list");
        List<Lesson> lessons = jdbcTemplate
                .query(SELECT_ALL_QUERY + LIMIT + page.getPageSize() + OFFSET + page.getOffset(), new LessonMapper());
        logger.debug("Running query to count the total number of rows in lessons table");
        int totalNumberOfLessons = jdbcTemplate.queryForObject(TOTAL_COUNT_QUERY, Integer.class);
        logger.debug("Placing all list Lesson elements into a PageImpl object");
        return new PageImpl<Lesson>(lessons, page, totalNumberOfLessons);
    }

    public List<Lesson> getLessonsByClassId(long classId) {
        logger.debug("Running query to retrieve list of Lessons by classId:" + classId);
        return jdbcTemplate.query(SELECT_BY_CLASS_ID_QUERY, new LessonMapper(), classId);
    }

    @Override
    public int save(Lesson lesson) {
        logger.debug("Running query to save the following Lesson object into the database: " + lesson);
        return jdbcTemplate.update(INSERT_QUERY, lesson.getClassId(), lesson.getTitle(), lesson.getText());
    }

    @Override
    public void update(Lesson lesson) {
        logger.debug("Running query to update the following Lesson object in the database: " + lesson);
        jdbcTemplate.update(UPDATE_QUERY, lesson.getClassId(), lesson.getTitle(), lesson.getText(),
                lesson.getLessonId());
    }

    @Override
    public Lesson get(long lessonId) {
        logger.debug("Running query to retrieve the Lesson object by lessonId: " + lessonId);
        return jdbcTemplate.queryForObject(SELECT_BY_LESSON_ID_QUERY, new LessonMapper(), lessonId);
    }

    @Override
    public void delete(long lessonId) {
        logger.debug("Running query to delete the Lesson with the following lessonId: " + lessonId);
        jdbcTemplate.update(DELETE_QUERY, lessonId);
    }
}
