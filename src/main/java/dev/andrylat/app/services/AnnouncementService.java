package dev.andrylat.app.services;

import java.io.InvalidObjectException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import dev.andrylat.app.daos.AnnouncementDao;
import dev.andrylat.app.exceptions.DatabaseOperationException;
import dev.andrylat.app.models.Announcement;

@Service
public class AnnouncementService {

    @Autowired
    private AnnouncementDao announcementDao;
    
    private static final String ANNOUNCEMENT_ID_ERROR_MESSAGE = "The announcement_id provided doesn't match any columns in the database. Please verify the value and try again";
    private static final String GET_ALL_ERROR_MESSAGE = "Something went wrong when trying to retrieve all of the announcements. Please check the database";
    private static final String SAVE_ERROR_MESSAGE = "Something went wrong when trying to save the announcement object. Please check the database.";
    private static final String UPDATE_ERROR_MESSAGE = "Something went wrong when trying to update the announcement object. Please check the database"
                                                        + "for the announcement with annoucementId=";
    private static final String INVALID_OBJECT_ERROR_MESSAGE = "Announcement object has an invalid state. Check title/text values to make sure they "
                                                                + "aren't empty for the announcement with annoucementId=";;
    private static final String NEW_LINE = "\n";
    
    private void validate(Announcement announcement) throws InvalidObjectException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Announcement>> violations = validator.validate(announcement);
        StringBuilder violationMessages = new StringBuilder();
        
        for(ConstraintViolation<Announcement> violation : violations) {
            violationMessages.append(violation.getMessage() + NEW_LINE);
        }
        if(!violations.isEmpty()) {
            throw new InvalidObjectException(violationMessages.toString() + INVALID_OBJECT_ERROR_MESSAGE + announcement.getAnnouncementId());
        }
    }
    
    public Announcement get(long announcementId) throws DatabaseOperationException, InvalidObjectException{
        Announcement announcement = new Announcement();
        try {
            announcement = announcementDao.get(announcementId);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException(ANNOUNCEMENT_ID_ERROR_MESSAGE);
        }
        
        validate(announcement);
        
        return announcement;
    }
    
    public List<Announcement> getAnnouncementsByClassId(long classId) throws DatabaseOperationException, InvalidObjectException{
  
        List<Announcement> announcements = Collections.EMPTY_LIST;
        
        try {
            announcements = announcementDao.getAnnouncementsByClassId(classId);
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException (ANNOUNCEMENT_ID_ERROR_MESSAGE);
        }
        for(Announcement announcement : announcements) {
            validate(announcement);
        }
        
        return announcements;
    }
    
    public Collection<Announcement> getAll() throws DatabaseOperationException, InvalidObjectException {
        Collection<Announcement> announcements = Collections.EMPTY_LIST;
        
        try {
            announcements = announcementDao.getAll();
        }
        catch(DataAccessException e) {
            throw new DatabaseOperationException(GET_ALL_ERROR_MESSAGE);
        }
        
        for(Announcement announcement : announcements) {
            validate(announcement);
        }
        
        return announcements;
    }
    
    public int save(Announcement announcement) throws InvalidObjectException, DatabaseOperationException {
        int output = 0;
        validate(announcement);
        try {
            output = announcementDao.save(announcement);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(SAVE_ERROR_MESSAGE);
        }
        
        return output;
    }
    
    public void update(Announcement announcement) throws InvalidObjectException, DatabaseOperationException{
        validate(announcement);
        try {
            announcementDao.update(announcement);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(UPDATE_ERROR_MESSAGE + announcement.getAnnouncementId());
        }
    }
    
    public void delete(long announcementId) throws DatabaseOperationException{
        try {
            announcementDao.delete(announcementId);
        }catch(DataAccessException e) {
            throw new DatabaseOperationException(ANNOUNCEMENT_ID_ERROR_MESSAGE);
        }
    }

    
}
