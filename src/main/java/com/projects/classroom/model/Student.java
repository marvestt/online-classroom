package com.projects.classroom.model;

import java.util.ArrayList;
import java.util.List;


import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

@Entity
public class Student extends User{
    
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List <Submission> submissions;
    
    @Lob
	private String description;
	private String goals;

	public Student() {
	    submissions = new ArrayList<>();
	}
	
	public long getStudentId() {
		return getUserId();
	}

	public void setStudentId(long studentId) {
		setUserId(studentId);
	}
    
    public List<Submission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<Submission> submissions) {
        this.submissions = submissions;
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
		return String.format("Student[student_id=%d, description=%s, goals=%s]", 
				getUserId(), description, goals);
	}
	
}
