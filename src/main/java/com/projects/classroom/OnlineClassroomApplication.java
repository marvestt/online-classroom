package com.projects.classroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import com.projects.classroom.dao.AnnouncementDao;

@SpringBootApplication
public class OnlineClassroomApplication{
	
	public static void main(String[] args) {
		SpringApplication.run(OnlineClassroomApplication.class, args);
	}
	
}
