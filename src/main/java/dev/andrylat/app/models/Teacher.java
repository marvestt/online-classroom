package dev.andrylat.app.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class Teacher extends User{

    @PositiveOrZero(message = "teacherId cannot be less than 0")
	private long teacherId;
    
	private String description;
	private String professionalName;

	public String getProfessionalName() {
		return professionalName;
	}
	public void setProfessionalName(String professionalName) {
		this.professionalName = professionalName;
	}
	public long getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(long teacherId) {
		this.teacherId = teacherId;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
