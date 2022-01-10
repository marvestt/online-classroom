package dev.andrylat.app.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import dev.andrylat.app.models.Announcement;

public class AnnouncementMapper implements RowMapper<Announcement> {
    private static final String ANNOUNCEMENT_ID_LABEL = "announcement_id";
    private static final String CLASS_ID_LABEL = "class_id";
    private static final String TITLE_LABEL = "title";
    private static final String TEXT_LABEL = "text";

    @Override
    public Announcement mapRow(ResultSet rs, int rowNum) throws SQLException {
        Announcement announcement = new Announcement();
        announcement.setAnnouncementId(rs.getLong(ANNOUNCEMENT_ID_LABEL));
        announcement.setClassId(rs.getLong(CLASS_ID_LABEL));
        announcement.setTitle(rs.getString(TITLE_LABEL));
        announcement.setText(rs.getString(TEXT_LABEL));
        return announcement;
    }

}