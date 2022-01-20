package dev.andrylat.app.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class Assignment {

    @PositiveOrZero(message = "assignmentId cannot be less than 0")
	private long assignmentId;
    
    @PositiveOrZero(message = "classId cannot be less than 0")
	private long classId;
	
	@NotNull(message = "title cannot be null")
	@NotBlank(message = "title cannot be blank")
	private String title;
	
	private String description;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(long assignmentId) {
		this.assignmentId = assignmentId;
	}

	public long getClassId() {
		return classId;
	}

	public void setClassId(long classId) {
		this.classId = classId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Override
	public String toString() {
		return String.format("Assignment[assignment_id=%d, class_id=%d, title='%s', description='%s']", 
				assignmentId, classId, title,description);
	}
	
}
