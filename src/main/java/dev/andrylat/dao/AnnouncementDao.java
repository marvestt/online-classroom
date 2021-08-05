/**
 * 
 */
package dev.andrylat.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import dev.andrylat.model.Announcement;

/**
 * @author lamem
 *
 */
@Repository
public class AnnouncementDao implements Dao<Announcement>{

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	private static final String SELECT_BY_ASSIGNMENT_ID_QUERY = "SELECT * FROM announcements WHERE announcement_id=?";
	private static final String SELECT_BY_CLASS_ID_QUERY = "SELECT * FROM announcements WHERE class_id=?";
	private static final String SELECT_ALL_QUERY = "SELECT * FROM announcements";
	
	private static final String INSERT_QUERY = "INSERT INTO public.announcements (class_id, title, text) \r\n"
												+ "	VALUES(?,?,?);";
	
	private static final String UPDATE_QUERY = "UPDATE table_name\r\n"
													+ "SET class_id = ?, title = ?, text = ? \r\n"
													+ "WHERE announcement_id = ?;";
	
	private static final String DELETE_QUERY = "DELETE FROM announcements WHERE announcement_id = ?;";
	
	private static final String ASSIGNMENT_ID_LABEL = "announcement_id";
	private static final String CLASS_ID_LABEL = "class_id";
	private static final String TITLE_LABEL = "title";
	private static final String TEXT_LABEL = "text";
	
	@Override
	public Announcement get(long announcementId) {
		// TODO Auto-generated method stub
		return jdbcTemplate.queryForObject(SELECT_BY_ASSIGNMENT_ID_QUERY, new AnnouncementMapper(), announcementId);
	}
	
	public List<Announcement> getAnnouncementsByClassId(long classId){
		return jdbcTemplate.query(SELECT_BY_CLASS_ID_QUERY, new AnnouncementMapper(), classId);
	}

	@Override
	public Collection<Announcement> getAll() {
		return jdbcTemplate.query(SELECT_ALL_QUERY, new AnnouncementMapper());
	}

	@Override
	public int save(Announcement t) {
		return jdbcTemplate.update(INSERT_QUERY,t.getClassId(), t.getTitle(), t.getText());
	}

	@Override
	public void update(Announcement t) {
		jdbcTemplate.update(UPDATE_QUERY,t.getClassId(),t.getTitle(), t.getText());
	}

	@Override
	public void delete(long announcementId) {
		jdbcTemplate.update(DELETE_QUERY, announcementId);
		
	}

	private class AnnouncementMapper implements RowMapper<Announcement>{

		@Override
		public Announcement mapRow(ResultSet rs, int rowNum) throws SQLException {
			Announcement announcement = new Announcement();
			announcement.setAnnouncementId(rs.getLong(ASSIGNMENT_ID_LABEL));
			announcement.setClassId(rs.getLong(CLASS_ID_LABEL));
			announcement.setText(rs.getString(TITLE_LABEL));
			announcement.setTitle(rs.getString(TEXT_LABEL));
			return announcement;
		}
		
	}
}
