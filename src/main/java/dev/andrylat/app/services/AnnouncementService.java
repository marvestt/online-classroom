package dev.andrylat.app.services;

import java.io.InvalidObjectException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dev.andrylat.app.daos.AnnouncementDao;
import dev.andrylat.app.exceptions.DatabaseOperationException;
import dev.andrylat.app.models.Announcement;
import dev.andrylat.app.utilities.Utilities;

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
    
    private static final Logger logger = LoggerFactory.getLogger(AnnouncementService.class);

    private static final String VALIDATING_ANNOUNCEMENT = "Validating announcement object with annoucnementId = ";

    private static final String VALIDATION_SUCCESSFUL = "Announcement object has been validated succesfully";

    private static final String VALIDATION_FAILED  = "Validation of the announcement object has failed due the following:\n";

    private static final String RETRIEVING_ANNOUNCEMENT = "Attempting to retrieve announcement object from the database with an announcementId = ";

    private static final String ANNOUNCEMENT_RETRIEVAL_FAILED = "The announcement could not be foudn in the database";

    private static final String RETRIEVING_LIST_OF_ANNOUCNEMENTS_BY_CLASS_ID = "Attempting to retrieve a list of all announcements that have the classId = ";

    private static final String ANNOUNCEMENTS_RETRIEVAL_BY_CLASS_ID_FAILED = "No announcements with the classId were found";

    private static final String VALIDATING_EACH_ANNOUNCEMENT = "Validating each announcement object";

    private static final String RETRIEVING_PAGE_OF_ALL_ANNOUNCEMENTS = "Attempting to retrieve a page of all of the annoucements";

    private static final String ALL_ANNOUNCEMENTS_RETRIEVAL_FAILED = "Failed to retrieve a list of all annoucnements. Check the announcement table in the database";

    private static final String SAVING_ANNOUNCEMENT = "Attempting to save announcement with announcementId = ";

    private static final String ANNOUNCEMENT_SAVE_FAILED = "Failed to save announcement. Check the database to make sure the announcements table is properly initialized";

    private static final String UPDATING_ANNOUNCEMENT = "Attempting to update announcement with announcementId = ";

    private static final String UPDATING_ANNOUNCEMENT_FAILED = "Failed to perform update. Check announcement object and announcements table in the database";

    private static final String DELETING_ANNOUNCEMENT = "Attempting to delete announcement with announcementId = ";

    private static final String DELETING_ANNOUNCEMENT_FAILED = "Announcement deletion failed. Check the announcements table in the database and enter the correct announcemtnID";
    
    private void validate(Announcement announcement) throws InvalidObjectException {
        logger.debug(VALIDATING_ANNOUNCEMENT + announcement.getAnnouncementId());
        List<String> violations = Utilities.validate(announcement);
        
        if(!violations.isEmpty()) {
            String violationMessages = violations
                .stream()
                .collect(Collectors.joining(NEW_LINE));
            logger.error(VALIDATION_FAILED + violationMessages);
            throw new InvalidObjectException(violationMessages + INVALID_OBJECT_ERROR_MESSAGE + announcement.getAnnouncementId());
        }
        logger.debug(VALIDATION_SUCCESSFUL);
    }
    
    public Announcement get(long announcementId) throws DatabaseOperationException, InvalidObjectException{
        Announcement announcement = new Announcement();
        try {
            logger.debug(RETRIEVING_ANNOUNCEMENT + announcementId);
            announcement = announcementDao.get(announcementId);
        }
        catch(DataAccessException e) {
            logger.debug(ANNOUNCEMENT_RETRIEVAL_FAILED);
            throw new DatabaseOperationException(ANNOUNCEMENT_ID_ERROR_MESSAGE);
        }
        
        validate(announcement);
        return announcement;
    }
    
    public List<Announcement> getAnnouncementsByClassId(long classId) throws DatabaseOperationException, InvalidObjectException{
        List<Announcement> announcements = Collections.EMPTY_LIST;
        try {
            logger.debug(RETRIEVING_LIST_OF_ANNOUCNEMENTS_BY_CLASS_ID + classId );
            announcements = announcementDao.getAnnouncementsByClassId(classId);
        }
        catch(DataAccessException e) {
            logger.error(ANNOUNCEMENTS_RETRIEVAL_BY_CLASS_ID_FAILED);
            throw new DatabaseOperationException (ANNOUNCEMENT_ID_ERROR_MESSAGE);
        }
        logger.debug(VALIDATING_EACH_ANNOUNCEMENT);
        for(Announcement announcement : announcements) {
            validate(announcement);
        }
        
        return announcements;
    }
    
    public Page<Announcement> getAll(Pageable page) throws DatabaseOperationException, InvalidObjectException {
        Page<Announcement> announcements = new PageImpl<>(Collections.EMPTY_LIST);
        
        try {
            logger.debug(RETRIEVING_PAGE_OF_ALL_ANNOUNCEMENTS);
            announcements = announcementDao.getAll(page);
        }
        catch(DataAccessException e) {
            logger.error(ALL_ANNOUNCEMENTS_RETRIEVAL_FAILED);
            throw new DatabaseOperationException(GET_ALL_ERROR_MESSAGE);
        }
        logger.debug(VALIDATING_EACH_ANNOUNCEMENT);
        for(Announcement announcement : announcements) {
            validate(announcement);
        }
        
        return announcements;
    }
    
    public int save(Announcement announcement) throws InvalidObjectException, DatabaseOperationException {
        int output = 0;
        logger.debug(SAVING_ANNOUNCEMENT + announcement.getAnnouncementId());
        validate(announcement);
        try {
            output = announcementDao.save(announcement);
        }catch(DataAccessException e) {
            logger.error(ANNOUNCEMENT_SAVE_FAILED);
            throw new DatabaseOperationException(SAVE_ERROR_MESSAGE);
        }
        
        return output;
    }
    
    public void update(Announcement announcement) throws InvalidObjectException, DatabaseOperationException{
        logger.debug(UPDATING_ANNOUNCEMENT + announcement.getAnnouncementId());
        validate(announcement);
        try {
            announcementDao.update(announcement);
        }catch(DataAccessException e) {
            logger.error(UPDATING_ANNOUNCEMENT_FAILED);
            throw new DatabaseOperationException(UPDATE_ERROR_MESSAGE + announcement.getAnnouncementId());
        }
    }
    
    public void delete(long announcementId) throws DatabaseOperationException{
        logger.debug(DELETING_ANNOUNCEMENT + announcementId);
        try {
            announcementDao.delete(announcementId);
        }catch(DataAccessException e) {
            logger.error(DELETING_ANNOUNCEMENT_FAILED);
            throw new DatabaseOperationException(ANNOUNCEMENT_ID_ERROR_MESSAGE);
        }
    }
    
}
