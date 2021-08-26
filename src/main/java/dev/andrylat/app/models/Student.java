package dev.andrylat.app.models;

public class Student extends User{
	
	private long studentId;
	private String description;
	private String goals;

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGoals() {
		return goals;
	}

	public void setGoals(String goals) {
		this.goals = goals;
	}
	
	@Override
	public String toString() {
		return String.format("Class[student_id=%d, description=%d, goals=%d]", 
				studentId, description, goals);
	}
	
}
