package dev.andrylat.app.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class Announcement {

    @PositiveOrZero(message = "announcementId canno be less than 0")
	private long announcementId;
    
    @PositiveOrZero(message = "classId cannot be less than 0")
	private long classId;
	
	@NotNull(message = "title cannot be null")
	@NotBlank(message = "title cannot not be blank")
	private String title;
	
	private String text;

	public long getAnnouncementId() {
		return announcementId;
	}

	public void setAnnouncementId(long announcementId) {
		this.announcementId = announcementId;
	}

	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	@Override
	public String toString() {
		return String.format("Announcement[announcemnt_id=%d, class_id=%d, title='%s', text='%s']", 
				announcementId, classId, title, text);
	}
	
}
