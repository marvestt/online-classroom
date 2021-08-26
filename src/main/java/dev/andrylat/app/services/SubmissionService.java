package dev.andrylat.app.services;

import java.io.InvalidObjectException;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import dev.andrylat.app.daos.SubmissionDao;
import dev.andrylat.app.exceptions.DatabaseOperationException;
import dev.andrylat.app.models.Submission;

public class SubmissionService {

    @Autowired
    private SubmissionDao submissionDao;
    
    private static final String SUBMISSION_ID_ERROR_MESSAGE = "";
    private static final String GET_ALL_ERROR_MESSAGE = "";
    private static final String SAVE_ERROR_MESSAGE = "";
    private static final String UPDATE_ERROR_MESSAGE = "";
    private static final String INVALID_OBJECT_ERROR_MESSAGE = "";
    private static final String NEW_LINE = "";
    
    private void validate(Submission submission) throws InvalidObjectException {
        /*
         * ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
         * Validator validator = factory.getValidator();
         * Set<ConstraintViolation<Announcement>> violations =
         * validator.validate(announcement); StringBuilder violationMessages = new
         * StringBuilder();
         * 
         * for(ConstraintViolation<Announcement> violation : violations) {
         * violationMessages.append(violation.getMessage() + NEW_LINE); }
         * if(!violations.isEmpty()) { throw new
         * InvalidObjectException(violationMessages.toString() +
         * INVALID_OBJECT_ERROR_MESSAGE + announcement.getAnnouncementId()); }
         */
    }
    
    public Submission get(long submissionId) throws DatabaseOperationException, InvalidObjectException{
        /*
         * Announcement announcement = new Announcement(); try { announcement =
         * announcementDao.get(announcementId); } catch(DataAccessException e) { throw
         * new DatabaseOperationException(ANNOUNCEMENT_ID_ERROR_MESSAGE); }
         * 
         * validate(announcement);
         * 
         * return announcement;
         */
        return null;
    }
    
    public List<Submission> getSubmissionsByClassId(long classId) throws DatabaseOperationException, InvalidObjectException{
  
        /*
         * List<Announcement> announcements = Collections.EMPTY_LIST;
         * 
         * try { announcements = announcementDao.getAnnouncementsByClassId(classId); }
         * catch(DataAccessException e) { throw new DatabaseOperationException
         * (ANNOUNCEMENT_ID_ERROR_MESSAGE); } for(Announcement announcement :
         * announcements) { validate(announcement); }
         * 
         * return announcements;
         */
        return null;
    }
    
    public Collection<Submission> getAll() throws DatabaseOperationException, InvalidObjectException {
        /*
         * Collection<Announcement> announcements = Collections.EMPTY_LIST;
         * 
         * try { announcements = announcementDao.getAll(); } catch(DataAccessException
         * e) { throw new DatabaseOperationException(GET_ALL_ERROR_MESSAGE); }
         * 
         * for(Announcement announcement : announcements) { validate(announcement); }
         * 
         * return announcements;
         */
        return null;
    }
    
    public int save(Submission submission) throws InvalidObjectException, DatabaseOperationException {
        /*
         * int output = 0; validate(announcement); try { output =
         * announcementDao.save(announcement); }catch(DataAccessException e) { throw new
         * DatabaseOperationException(SAVE_ERROR_MESSAGE); }
         * 
         * return output;
         */
        return 0;
    }
    
    public void update(Submission submission) throws InvalidObjectException, DatabaseOperationException{
        /*
         * validate(announcement); try { announcementDao.update(announcement);
         * }catch(DataAccessException e) { throw new
         * DatabaseOperationException(UPDATE_ERROR_MESSAGE +
         * announcement.getAnnouncementId()); }
         */
    }
    
    public void delete(long submissionId) throws DatabaseOperationException{
        /*
         * try { announcementDao.delete(announcementId); }catch(DataAccessException e) {
         * throw new DatabaseOperationException(ANNOUNCEMENT_ID_ERROR_MESSAGE); }
         */
    }
}
