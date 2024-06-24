package com.projects.classroom.model;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Submission {
	
    @Id
    @Column(name = "submission_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
    
    @ManyToOne
    @JoinColumn(name = "assignment_id")
	private Assignment assignment;
    
    @ManyToOne
    @JoinColumn(name = "student_id")
	private Student student;
    
    @NotNull
    @NotBlank
	private String title;
    
    @NotNull
    @NotBlank
    @Lob
	private String text;
    
    private String grade;
    
    private boolean graded;

	public long getSubmissionId() {
		return id;
	}

	public void setSubmissionId(long submissionId) {
		this.id = submissionId;
	}
	
	public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }

	public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
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

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public boolean isGraded() {
        return graded;
    }

    public void setGraded(boolean graded) {
        this.graded = graded;
    }

    @Override
	public String toString() {
		return String.format("Submission[submission_id=%d, assignment_id=%d, student_id=%d, title=%s, text=%s, grade=%d]", 
				id, assignment.getAssignmentId(), student.getStudentId(), title, text, grade);
	}
	
	
}
