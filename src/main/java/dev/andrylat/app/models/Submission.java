package dev.andrylat.app.models;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class Submission {
	
    @PositiveOrZero(message = "submissioId cannot be less than 0")
	private long submissionId;
    
    @PositiveOrZero(message = "assignmentId cannot be less than 0")
	private long assignmentId;
    
    @PositiveOrZero(message = "studentId cannot be less than 0")
	private long studentId;
    
    @NotNull(message = "title cannot be null")
    @NotBlank(message = "title cannot be blank")
	private String title;
    
    @NotNull(message = "text cannot be null")
    @NotBlank(message = "text cannot be blank")
	private String text;

	public long getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(long submissionId) {
		this.submissionId = submissionId;
	}

	public long getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(long assignmentId) {
		this.assignmentId = assignmentId;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
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
		return String.format("Class[submission_id=%d, assignment_id=%d, student_id=%d, title=%d, text=%d]", 
				submissionId, assignmentId, studentId, title, text);
	}
	
	
}
