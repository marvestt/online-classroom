package com.projects.classroom.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class Classroom {

    @PositiveOrZero(message = "classId cannot be less than 0")
	private long classId;
    
    @PositiveOrZero(message = "mainTeacherId cannot be less than 0")
	private long mainTeacherId;
    
    @NotNull(message = "name cannot be null")
    @NotBlank(message = "name cannot be blank")
	private String name;
    
	private String description;


	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public long getMainTeacherId() {
		return mainTeacherId;
	}

	public void setMainTeacherId(long mainTeacherId) {
		this.mainTeacherId = mainTeacherId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return String.format("Class[class_id=%d, main_teacher_id=%d, name=%s,description='%s']", 
				classId, mainTeacherId, name,description);
	}
	
	
}
