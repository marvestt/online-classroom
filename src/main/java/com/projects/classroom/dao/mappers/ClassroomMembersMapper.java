package com.projects.classroom.dao.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class ClassroomMembersMapper implements RowMapper<Long> {

    private static final String CLASS_ID_LABEL = "class_id";
    
    @Override
    public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong(CLASS_ID_LABEL);
    }

}
