package com.projects.classroom.daos;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.projects.classroom.dao.mappers.ClassroomMembersMapper;

@Repository
public class ClassMembersDao {

    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    private static final Logger logger = LoggerFactory.getLogger(ClassMembersDao.class);
    
    private static final String SELECT_BY_USER_ID = "SELECT * FROM class_members WHERE user_id=?";
    private static final String SELECT_BY_CLASS_ID = "SELECT user_id FROM class_members WHERE class_id=?";
    private static final String INSERT = "INSERT INTO public.class_members (user_id,class_id) "
            + " VALUES(?,?);";
    private static final String DELETE = "DELETE FROM class_members WHERE user_id = ? AND class_id=?;";
    
    public List<Long> getClassroomIdsByUserId(Long userId){
        logger.debug("Running query to retrieve all class ids from class_members table where the provided user id is found");
        return jdbcTemplate.query(SELECT_BY_USER_ID,new ClassroomMembersMapper(),userId);
    }
    
    public List<Long> getUserIdsByClassroomId(Long classId){
        logger.debug("Runnign query to retrieve all user ids from class_members table where the provided class id is found");
        return jdbcTemplate.queryForList(SELECT_BY_CLASS_ID, Long.class, classId);
    }
    
    public void addUserToClassroom(Long userId, Long classId) {
        logger.debug("Running query to save the user into the class_members table along with the corresponding class id");
        jdbcTemplate.update(INSERT,userId,classId);
    }
    
    public void delete(long classId, long userId) {
        logger.debug("Running query to delete Class member with from the following classId: " + classId);
        jdbcTemplate.update(DELETE, userId,classId);
    }
}
