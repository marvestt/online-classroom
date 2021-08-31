package dev.andrylat.app.daos;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import dev.andrylat.app.dao.mappers.SubmissionMapper;
import dev.andrylat.app.models.Submission;

@Repository
public class SubmissionDao implements Dao<Submission> {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String SELECT_BY_SUBMISSION_ID_QUERY = "SELECT * FROM submissions WHERE submission_id=?";
    private static final String SELECT_BY_STUDENT_ID_QUERY = "SELECT * FROM submissions WHERE student_id=?";
    private static final String SELECT_BY_ASSIGNMENT_ID_QUERY = "SELECT * FROM submissions WHERE assignment_id=?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM submissions";
    private static final String LIMIT = " LIMIT ";
    private static final String OFFSET = " OFFSET ";
    private static final String TOTAL_COUNT_QUERY = "SELECT COUNT(*) FROM submissions";

    private static final String INSERT_QUERY = "INSERT INTO public.submissions (assignment_id, student_id, title, text)"
            + "	VALUES(?,?,?,?);";

    private static final String UPDATE_QUERY = "UPDATE submissions  "
            + "SET assignment_id = ?, student_id = ?, title = ?, text = ?  " + "WHERE submission_id = ?;";

    private static final String DELETE_QUERY = "DELETE FROM submissions WHERE submission_id = ?;";

    private static final Logger logger = LoggerFactory.getLogger(SubmissionDao.class);

    @Override
    public Page<Submission> getAll(Pageable page) {
        logger.debug(
                "Running query to retrieve all Submissions with provided limit and offset and saving contents into a list");
        List<Submission> submissions = jdbcTemplate.query(
                SELECT_ALL_QUERY + LIMIT + page.getPageSize() + OFFSET + page.getOffset(), new SubmissionMapper());
        logger.debug("Running query to count the total number of rows in submissions table");
        int totalNumberOfSubmissons = jdbcTemplate.queryForObject(TOTAL_COUNT_QUERY, Integer.class);
        logger.debug("Placing all list Submission elements into a PageImpl object");
        return new PageImpl<Submission>(submissions, page, totalNumberOfSubmissons);
    }

    public List<Submission> getSubmissionsByStudentId(long studentId) {
        logger.debug("Running query to retrieve list of Submissions by studentId: " + studentId);
        return jdbcTemplate.query(SELECT_BY_STUDENT_ID_QUERY, new SubmissionMapper(), studentId);
    }

    public List<Submission> getSubmissionsByAssignmentId(long assignmentId) {
        logger.debug("Running query to retrieve list of Submissions by assignmentId: " + assignmentId);
        return jdbcTemplate.query(SELECT_BY_ASSIGNMENT_ID_QUERY, new SubmissionMapper(), assignmentId);
    }

    @Override
    public int save(Submission submission) {
        logger.debug("Running query to save the following Submission object into the database: " + submission);
        return jdbcTemplate.update(INSERT_QUERY, submission.getAssignmentId(), submission.getStudentId(),
                submission.getTitle(), submission.getText());
    }

    @Override
    public void update(Submission submission) {
        logger.debug("Running query to update the following Submission object in the database: " + submission);
        jdbcTemplate.update(UPDATE_QUERY, submission.getAssignmentId(), submission.getStudentId(),
                submission.getTitle(), submission.getText(), submission.getSubmissionId());
    }

    @Override
    public Submission get(long submissionId) {
        logger.debug("Running query to retrieve Submission object by submissionId: " + submissionId);
        return jdbcTemplate.queryForObject(SELECT_BY_SUBMISSION_ID_QUERY, new SubmissionMapper(), submissionId);
    }

    @Override
    public void delete(long submissionId) {
        logger.debug("Running query to delete Submission with the following submissionId: " + submissionId);
        jdbcTemplate.update(DELETE_QUERY, submissionId);
    }

}
