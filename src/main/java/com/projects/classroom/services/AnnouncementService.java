package com.projects.classroom.services;

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

import com.projects.classroom.daos.AnnouncementDao;
import com.projects.classroom.exceptions.DatabaseOperationException;
import com.projects.classroom.models.Announcement;
import com.projects.classroom.utilities.Utilities;

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
            + "aren't empty for the announcement with annoucementId=";
    private static final String NEW_LINE = "\n";
    private static final String NULL_ANNOUNCEMENT = "Announcement is not allowed to be null. Please check the object and make sure a value is assigned correctly";

    private static final Logger logger = LoggerFactory.getLogger(AnnouncementService.class);

    private void validate(Announcement announcement) throws InvalidObjectException {
        if (announcement == null) {
            logger.error("Announcement object cannot be null. Invalid state");
            throw new InvalidObjectException(NULL_ANNOUNCEMENT);
        }
        logger.debug("Validating the following Announcement object" + announcement);
        List<String> violations = Utilities.validate(announcement);

        if (!violations.isEmpty()) {
            String violationMessages = violations.stream().collect(Collectors.joining(NEW_LINE));
            logger.error("Validation of the announcement object has failed due the following:\n" + violationMessages);
            throw new InvalidObjectException(
                    violationMessages + INVALID_OBJECT_ERROR_MESSAGE + announcement.getAnnouncementId());
        }
        logger.debug("Announcement object has been validated succesfully");
    }

    public Announcement get(long announcementId) throws DatabaseOperationException, InvalidObjectException {
        Announcement announcement = null;
        try {
            logger.debug("Attempting to retrieve announcement object from the database with an announcementId = "
                    + announcementId);
            announcement = announcementDao.get(announcementId);
        } catch (DataAccessException e) {
            logger.error(
                    "The announcement could not be found in the database. Check the records to make sure the following announcementId is present: "
                            + announcementId);
            throw new DatabaseOperationException(ANNOUNCEMENT_ID_ERROR_MESSAGE);
        }
        return announcement;
    }

    public List<Announcement> getAnnouncementsByClassId(long classId)
            throws DatabaseOperationException, InvalidObjectException {
        List<Announcement> announcements = Collections.EMPTY_LIST;
        try {
            logger.debug("Attempting to retrieve a list of all announcements that have the classId = " + classId);
            announcements = announcementDao.getAnnouncementsByClassId(classId);
        } catch (DataAccessException e) {
            logger.error("No announcements with the classId were found");
            throw new DatabaseOperationException(ANNOUNCEMENT_ID_ERROR_MESSAGE);
        }
        logger.debug("Validating each announcement object in the list");
        for (Announcement announcement : announcements) {
            validate(announcement);
        }

        return announcements;
    }

    public Page<Announcement> getAll(Pageable page) throws DatabaseOperationException, InvalidObjectException {
        Page<Announcement> announcements = new PageImpl<>(Collections.EMPTY_LIST);
        try {
            logger.debug("Attempting to retrieve a page of all of the annoucements");
            announcements = announcementDao.getAll(page);
        } catch (DataAccessException e) {
            logger.error(
                    "Failed to retrieve a page of all annoucnements. Check the announcement table in the database");
            throw new DatabaseOperationException(GET_ALL_ERROR_MESSAGE);
        }
        logger.debug("Validating each announcement object");
        for (Announcement announcement : announcements) {
            validate(announcement);
        }
        return announcements;
    }

    public int save(Announcement announcement) throws InvalidObjectException, DatabaseOperationException {
        int output = 0;
        logger.debug("Attempting to save the following announcement object to the database: " + announcement);
        validate(announcement);
        try {
            output = announcementDao.save(announcement);
        } catch (DataAccessException e) {
            logger.error(
                    "Failed to save announcement. Check the database to make sure the announcements table is properly initialized");
            throw new DatabaseOperationException(SAVE_ERROR_MESSAGE);
        }

        return output;
    }

    public void update(Announcement announcement) throws InvalidObjectException, DatabaseOperationException {
        logger.debug("Attempting to update the following announcement object in the database: " + announcement);
        validate(announcement);
        try {
            announcementDao.update(announcement);
        } catch (DataAccessException e) {
            logger.error("Failed to perform update. Check announcement object and announcements table in the database");
            throw new DatabaseOperationException(UPDATE_ERROR_MESSAGE + announcement.getAnnouncementId());
        }
    }

    public void delete(long announcementId) throws DatabaseOperationException {
        logger.debug("Attempting to delete announcement with announcementId = " + announcementId);
        try {
            announcementDao.delete(announcementId);
        } catch (DataAccessException e) {
            logger.error(
                    "Announcement deletion failed. Check the announcements table in the database and verify the announcemtnID");
            throw new DatabaseOperationException(ANNOUNCEMENT_ID_ERROR_MESSAGE);
        }
    }

}
