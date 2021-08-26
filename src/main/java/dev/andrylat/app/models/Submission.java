package dev.andrylat.app.models;

public class Submission {
	
	private long submissionId;
	private long assignmentId;
	private long studentId;
	private String title;
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
