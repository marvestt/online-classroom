package dev.andrylat.service;

import java.io.InvalidObjectException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import dev.andrylat.dao.AnnouncementDao;
import dev.andrylat.model.Announcement;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementDao announcementDao;
    
    private String ANNOUNCEMENT_ID_ERROR_MESSAGE = "The assignment_id provided doesn't match any columns in the database. Please verify the value and try again";
    private String GET_ALL_ERROR_MESSAGE = "Something went wrong when trying to retrieve all of the assignments. Please check the database.";
    private String SAVE_ERROR_MESSAGE = "Something went wrong when trying to save the announcement object. Please check the database.";
    private String UPDATE_ERROR_MESSAGE = "Something went wrong when trying to update the announcement object. Please check the database.";
    private String INVALID_OBJECT_ERROR_MESSAGE = "Announcement object has an invalid state. Check title/text to make sure they aren't empty.";
    
    private void validate(Announcement announcement) throws InvalidObjectException {
        String title = announcement.getTitle();
        String text = announcement.getText();
        boolean validTitle = title != null && !title.isEmpty() ;
        boolean validText = text != null && !title.isEmpty();
        if(!validTitle || !validText) {
            throw new InvalidObjectException(INVALID_OBJECT_ERROR_MESSAGE);
        }
    }
    
    public Announcement get(long announcementId) throws SQLException, InvalidObjectException{
        Announcement announcement = null;
        try {
            announcement = announcementDao.get(announcementId);
        }
        catch(DataAccessException e) {
            throw new SQLException(ANNOUNCEMENT_ID_ERROR_MESSAGE);
        }
        
        validate(announcement);
        
        return announcement;
    }
    
    public List<Announcement> getAnnouncementsByClassId(long classId) throws SQLException, InvalidObjectException{
  
        List<Announcement> announcements = null;
        
        try {
            announcements = announcementDao.getAnnouncementsByClassId(classId);
        }
        catch(DataAccessException e) {
            throw new SQLException(ANNOUNCEMENT_ID_ERROR_MESSAGE);
        }
        for(Announcement announcement : announcements) {
            validate(announcement);
        }
        
        return announcements;
    }
    
    public Collection<Announcement> getAll() throws SQLException, InvalidObjectException {
        Collection<Announcement> announcements = null;
        
        try {
            announcements = announcementDao.getAll();
        }
        catch(DataAccessException e) {
            throw new SQLException(GET_ALL_ERROR_MESSAGE);
        }
        
        for(Announcement announcement : announcements) {
            validate(announcement);
        }
        
        return announcements;
    }
    
    public int save(Announcement t) throws InvalidObjectException, SQLException {
        int output = 0;
        validate(t);
        try {
            output = announcementDao.save(t);
        }catch(DataAccessException e) {
            throw new SQLException(SAVE_ERROR_MESSAGE);
        }
        
        return output;
    }
    
    public void update(Announcement t) throws InvalidObjectException, SQLException{
        validate(t);
        try {
            announcementDao.update(t);
        }catch(DataAccessException e) {
            throw new SQLException(UPDATE_ERROR_MESSAGE);
        }
    }
    
    public void delete(long announcementId) throws SQLException{
        try {
            announcementDao.delete(announcementId);
        }catch(DataAccessException e) {
            throw new SQLException(ANNOUNCEMENT_ID_ERROR_MESSAGE);
        }
    }

    
}
