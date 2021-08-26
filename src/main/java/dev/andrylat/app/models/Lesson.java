package dev.andrylat.app.models;

public class Lesson {
	
	private long lessonId;
	private long classId;
	private String title;
	private String text;

	public long getLessonId() {
		return lessonId;
	}

	public void setLessonId(long lessonId) {
		this.lessonId = lessonId;
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
		return String.format("Class[lesson_id=%d, class_id=%d, title=%d,text='%s']", 
				lessonId, classId, title, text);
	}
	
}
