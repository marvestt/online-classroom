package com.projects.classroom.model;

import java.util.ArrayList;
import java.util.List;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Entity
public class Classroom {

    @Id
    @Column(name = "class_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @PositiveOrZero
	private long id;
    
    @ManyToOne
    @JoinColumn(name = "teacher_id")
	private Teacher teacher;
    
    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL)
    private List<Announcement> announcements;
    
    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL)
    private List<Assignment> assignments;
    
    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL)
    private List<Lesson> lessons;
    
    @ManyToMany(mappedBy = "classrooms")
    private List<User> users ;
    
    public Classroom() {
        announcements = new ArrayList<>();
        assignments = new ArrayList<>();
        lessons = new ArrayList<>();
        users = new ArrayList<>();
    }
    
    @NotNull
    @NotBlank
	private String name;
    
    @Lob
	private String description;

	public long getClassroomId() {
		return id;
	}

	public void setClassroomId(long id) {
		this.id = id;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher mainTeacher) {
		this.teacher = mainTeacher;
	}

	public List<Announcement> getAnnouncements() {
        return announcements;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void addAnnouncement(Announcement announcement) {
        announcements.add(announcement);
    }
    
    public void addAssignment (Assignment assignment) {
        assignments.add(assignment);
    }
    
    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public void setAnnouncements(List<Announcement> announcements) {
        this.announcements = announcements;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
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
				id, teacher.getTeacherId(), name,description);
	}

    @Override
    public int hashCode() {
        return Objects.hash(id, description, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Classroom other = (Classroom) obj;
        return id == other.id && Objects.equals(description, other.description)
                && Objects.equals(name, other.name);
    }
	
	
	
	
}
