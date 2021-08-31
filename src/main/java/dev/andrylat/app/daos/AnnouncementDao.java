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

import dev.andrylat.app.dao.mappers.AnnouncementMapper;
import dev.andrylat.app.models.Announcement;

@Repository
public class AnnouncementDao implements Dao<Announcement> {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String SELECT_BY_ANNOUNCEMENT_ID_QUERY = "SELECT * FROM announcements WHERE announcement_id=?";
    private static final String SELECT_BY_CLASS_ID_QUERY = "SELECT * FROM announcements WHERE class_id=?";
    private static final String SELECT_ALL_QUERY = "SELECT * FROM announcements";
    private static final String LIMIT = " LIMIT ";
    private static final String OFFSET = " OFFSET ";
    private static final String TOTAL_COUNT_QUERY = "SELECT COUNT(*) FROM announcements";

    private static final String INSERT_QUERY = "INSERT INTO public.announcements (class_id, title, text) "
            + "	VALUES(?,?,?);";

    private static final String UPDATE_QUERY = "UPDATE announcements " + "SET class_id = ?, title = ?, text = ?  "
            + "WHERE announcement_id = ?;";

    private static final String DELETE_QUERY = "DELETE FROM announcements WHERE announcement_id = ?;";

    private static final Logger logger = LoggerFactory.getLogger(AnnouncementDao.class);

    @Override
    public Announcement get(long announcementId) {
        logger.debug("Running query to retrieve announcement object by announcementId: " + announcementId);
        return jdbcTemplate.queryForObject(SELECT_BY_ANNOUNCEMENT_ID_QUERY, new AnnouncementMapper(), announcementId);
    }

    public List<Announcement> getAnnouncementsByClassId(long classId) {
        logger.debug("Running query to retrieve list of announcements by class_id:" + classId);
        return jdbcTemplate.query(SELECT_BY_CLASS_ID_QUERY, new AnnouncementMapper(), classId);
    }

    @Override
    public Page<Announcement> getAll(Pageable page) {
        logger.debug(
                "Running query to retrieve all announcements with provided limit and offset and saving contents into a list");
        List<Announcement> announcements = jdbcTemplate.query(
                SELECT_ALL_QUERY + LIMIT + page.getPageSize() + OFFSET + page.getOffset(), new AnnouncementMapper());
        logger.debug("Running query to count the total number of rows in announcements table");
        int totalNumberOfAnnouncements = jdbcTemplate.queryForObject(TOTAL_COUNT_QUERY, Integer.class);
        logger.debug("Placing all list announcement elements into a PageImpl object");
        return new PageImpl<Announcement>(announcements, page, totalNumberOfAnnouncements);
    }

    @Override
    public int save(Announcement announcement) {
        logger.debug("Running query to save the following announcement object into the database: " + announcement);
        return jdbcTemplate.update(INSERT_QUERY, announcement.getClassId(), announcement.getTitle(),
                announcement.getText(), announcement.getAnnouncementId());
    }

    @Override
    public void update(Announcement announcement) {
        logger.debug("Running query to update the following announcement object in the database: " + announcement);
        jdbcTemplate.update(UPDATE_QUERY, announcement.getClassId(), announcement.getTitle(), announcement.getText());
    }

    @Override
    public void delete(long announcementId) {
        logger.debug("Running query to delete announcement with the following announcementId: " + announcementId);
        jdbcTemplate.update(DELETE_QUERY, announcementId);
    }

}
