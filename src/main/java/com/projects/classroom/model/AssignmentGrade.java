package com.projects.classroom.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class AssignmentGrade {

    @PositiveOrZero(message = "assignmentGradeId cannot be less than 0")
	private long assignmentGradeId;
    
    @PositiveOrZero(message = "studentId cannot be less than 0")
	private long studentId;
    
    @PositiveOrZero(message = "assignmentId cannot be less than 0")
	private long assignmentId;
    
    @NotNull(message = "grade cannot be null")
    @NotBlank(message = "grade cannot be blank")
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
