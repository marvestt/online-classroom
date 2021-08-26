package dev.andrylat.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import dev.andrylat.app.daos.AnnouncementDao;

@SpringBootApplication
public class OnlineClassroomApplication{

	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	private AnnouncementDao annoucementDao;
	
	public static void main(String[] args) {
		SpringApplication.run(OnlineClassroomApplication.class, args);
	}
	
}
