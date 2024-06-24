package com.projects.classroom.model;

import java.util.ArrayList;
import java.util.List;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @PositiveOrZero
	private long id;
    
    @NotNull
    @NotBlank
	private String username;
    
    @NotNull
    @NotBlank
	private String password;
    
    @NotNull
    @NotBlank
	private String firstName;
    
    @NotNull
    @NotBlank
	private String surname;
    
    @ManyToMany
    @JoinTable(
            name = "user_classroom",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "classroom_id"))
    private List<Classroom> classrooms;
	
    public User() {
        classrooms = new ArrayList<>();
    }
    
	public long getUserId() {
		return id;
	}

	public void setUserId(long userId) {
		this.id = userId;
	}

    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}
	
	public List<Classroom> getClassrooms() {
        return classrooms;
    }

    public void setClassrooms(List<Classroom> classrooms) {
        this.classrooms = classrooms;
    }

    public void setUserInfo(User user) {
	    this.id = user.id;
	    this.username = user.username;
	    this.password = user.password;
	    this.firstName = user.firstName;
	    this.surname = user.surname;
	}

	@Override
	public String toString() {
		return String.format("User[user_id=%d, username='%s', password='%s', first_name='%s', surname='%s']", 
				id, username, password, firstName, surname);
	}
}
