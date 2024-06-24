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
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Entity
public class Announcement {

    @Id
    @Column(name = "announcement_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @PositiveOrZero
	private long id;
    
    @ManyToOne
    @JoinColumn(name = "class_id")
	private Classroom classroom;
	
	@NotNull
	@NotBlank
	private String title;
	
	@Lob
	private String text;

	public long getAnnouncementId() {
		return id;
	}

	public void setAnnouncementId(long announcementId) {
		this.id = announcementId;
	}

	public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
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
		return String.format("Announcement[announcemnt_id=%d, class_id=%d, title='%s', text='%s']", 
				id, classroom.getClassroomId(), title, text);
	}
	
}
