package dev.andrylat.app.models;

public class AssignmentGrade {

	private long assignmentGradeId;
	private long studentId;
	private long assignmentId;
	private String grade;
	
	public long getAssignmentGradeId() {
		return assignmentGradeId;
	}

	public void setAssignmentGradeId(long assignmentGradeId) {
		this.assignmentGradeId = assignmentGradeId;
	}

	public long getStudentId() {
		return studentId;
	}

	public void setStudentId(long studentId) {
		this.studentId = studentId;
	}

	public long getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(long assignmentId) {
		this.assignmentId = assignmentId;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}
	
	@Override
	public String toString() {
		return String.format("AssignmentGrade[assignment_grade_id=%d, student_id=%d, assignment_id=%d,grade='%s']", 
				assignmentGradeId, studentId, assignmentId,grade);
	}
}
