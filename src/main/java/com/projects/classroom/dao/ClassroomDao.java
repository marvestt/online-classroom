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

import com.projects.classroom.dao.mapper.ClassroomMapper;
import com.projects.classroom.model.Classroom;

@Repository
public class ClassroomDao implements Dao<Classroom> {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String SELECT_BY_CLASS_ID_QUERY = "SELECT * FROM classes WHERE class_id=?";
    private static final String SELECT_BY_MAIN_TEACHER_ID_QUERY = "SELECT * FROM classes WHERE main_teacher_id=?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM classes";
    private static final String LIMIT = " LIMIT ";
    private static final String OFFSET = " OFFSET ";
    private static final String TOTAL_COUNT_QUERY = "SELECT COUNT(*) FROM classes";

    private static final String INSERT_QUERY = "INSERT INTO public.classes (main_teacher_id, name, description)  "
            + "	VALUES(?,?,?) RETURNING class_id;";

    private static final String UPDATE_QUERY = "UPDATE classes  "
            + "SET main_teacher_id = ?, name = ?, description = ?  " + "WHERE class_id = ?;";

    private static final String SEARCH_QUERY = "SELECT class_id,main_teacher_id,classes.name,classes.description,teachers.professional_name "
            + "FROM public.classes JOIN public.teachers on classes.main_teacher_id=teachers.teacher_id "
            + "WHERE to_tsvector(classes.name) @@ plainto_tsquery(?) "
            + "OR to_tsvector(classes.description) @@ plainto_tsquery(?) "
            + "OR to_tsvector(teachers.professional_name) @@ plainto_tsquery(?)";
    
    private static final String DELETE_QUERY = "DELETE FROM classes WHERE class_id = ?;";
    
    private static final Logger logger = LoggerFactory.getLogger(ClassroomDao.class);

    @Override
    public Page<Classroom> getAll(Pageable page) {
        logger.debug(
                "Running query to retrieve all Classrooms with provided limit and offset and saving contents into a list");
        List<Classroom> classrooms = jdbcTemplate.query(
                SELECT_ALL_QUERY + LIMIT + page.getPageSize() + OFFSET + page.getOffset(), new ClassroomMapper());
        logger.debug("Running query to count the total number of rows in classes table");
        int totalNumberOfClassrooms = jdbcTemplate.queryForObject(TOTAL_COUNT_QUERY, Integer.class);
        logger.debug("Placing all list Classroom elements into a PageImpl object");
        return new PageImpl<Classroom>(classrooms, page, totalNumberOfClassrooms);
    }

    public List<Classroom> getClassesByMainTeacherId(long mainTeacherId) {
        return jdbcTemplate.query(SELECT_BY_MAIN_TEACHER_ID_QUERY, new ClassroomMapper(), mainTeacherId);
    }
    
    public List<Classroom> searchClassrooms(String searchText){
        return jdbcTemplate.query(SEARCH_QUERY, new ClassroomMapper(), searchText,searchText,searchText);
    }

    @Override
    public int save(Classroom classroom) {
        logger.debug("Running query to save the following Classroom object into the database: " + classroom);
        return jdbcTemplate.queryForObject(INSERT_QUERY, Integer.class, classroom.getMainTeacherId(), classroom.getName(),
                classroom.getDescription());
    }

    @Override
    public void update(Classroom classroom) {
        logger.debug("Running query to update the following Classroom object in the database: " + classroom);
        jdbcTemplate.update(UPDATE_QUERY, classroom.getMainTeacherId(), classroom.getName(), classroom.getDescription(),
                classroom.getClassId());
    }

    @Override
    public Classroom get(long classId) {
        logger.debug("Running query to retrieve Classroom object by classId: " + classId);
        return jdbcTemplate.queryForObject(SELECT_BY_CLASS_ID_QUERY, new ClassroomMapper(), classId);
    }

    @Override
    public void delete(long classId) {
        logger.debug("Running query to delete Classroom with the following classId: " + classId);
        jdbcTemplate.update(DELETE_QUERY, classId);
    }

}
