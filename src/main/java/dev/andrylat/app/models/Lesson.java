package dev.andrylat.app.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class Lesson {
	
    @PositiveOrZero(message = "lessonId cannot be less than 0")
	private long lessonId;
    
    @PositiveOrZero(message = "classId cannot be less than 0")
	private long classId;
    
    @NotNull(message = "title cannot be null")
    @NotBlank(message = "title cannot be blank")
	private String title;
    
    @NotNull(message = "text cannot be null")
    @NotBlank(message = "text cannot be blank")
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
