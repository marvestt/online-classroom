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

import com.projects.classroom.dao.mapper.StudentMapper;
import com.projects.classroom.model.Student;

@Repository
public class StudentDao implements Dao<Student> {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    UserDao userDao;

    private static final String SELECT_BY_STUDENT_ID_QUERY = "SELECT * FROM students INNER JOIN users "
            + "ON users.user_id = students.user_id " + "WHERE student_id=?";
    private static final String SELECT_BY_USER_ID_QUERY = "SELECT * FROM students INNER JOIN users "
            + "ON users.user_id = students.user_id " + "WHERE students.user_id=?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM students INNER JOIN users "
            + "ON users.user_id = students.user_id";
    private static final String LIMIT = " LIMIT ";
    private static final String OFFSET = " OFFSET ";
    private static final String TOTAL_COUNT_QUERY = "SELECT COUNT(*) FROM students";

    private static final String INSERT_QUERY = "INSERT INTO public.students (user_id, goals, description)"
            + "	VALUES((SELECT user_id FROM users WHERE username=? LIMIT 1)," + "?,?);";

    private static final String UPDATE_QUERY = "UPDATE students " + "SET goals = ?, description = ? "
            + "WHERE student_id = ?;";

    private static final String DELETE_QUERY = "DELETE FROM students WHERE student_id = ?;";

    private static final Logger logger = LoggerFactory.getLogger(StudentDao.class);

    @Override
    public Page<Student> getAll(Pageable page) {
        logger.debug(
                "Running query to retrieve all Students with provided limit and offset and saving contents into a list");
        List<Student> students = jdbcTemplate
                .query(SELECT_ALL_QUERY + LIMIT + page.getPageSize() + OFFSET + page.getOffset(), new StudentMapper());
        logger.debug("Running query to count the total number of rows in students table");
        int totalNumberOfStudents = jdbcTemplate.queryForObject(TOTAL_COUNT_QUERY, Integer.class);
        logger.debug("Placing all list Student elements into a PageImpl object");
        return new PageImpl<Student>(students, page, totalNumberOfStudents);
    }

    public Student getStudentByUserId(long userId) {
        logger.debug("Running query to retrieve Student object by the following userId: " + userId);
        return jdbcTemplate.queryForObject(SELECT_BY_USER_ID_QUERY, new StudentMapper(), userId);
    }

    @Override
    public Student get(long studentId) {
        logger.debug("Running query to retrieve Student object by studentId: " + studentId);
        return jdbcTemplate.queryForObject(SELECT_BY_STUDENT_ID_QUERY, new StudentMapper(), studentId);
    }

    @Override
    public int save(Student student) {
        userDao.save(student);
        logger.debug("Running query to save the following Student object into the database: " + student);
        return jdbcTemplate.update(INSERT_QUERY, student.getUsername(), student.getGoals(), student.getDescription());
    }

    @Override
    public void update(Student student) {
        logger.debug("Running query to update the following Student object in the database: " + student);
        jdbcTemplate.update(UPDATE_QUERY, student.getGoals(), student.getDescription(), student.getStudentId());
    }

    @Override
    public void delete(long studentId) {
        logger.debug("Running query to delete Student with the following studentId: " + studentId);
        jdbcTemplate.update(DELETE_QUERY, studentId);
    }
}
