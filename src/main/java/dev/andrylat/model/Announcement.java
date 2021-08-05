package dev.andrylat.model;

public class Announcement {

	private long announcementId;
	private long classId;
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
