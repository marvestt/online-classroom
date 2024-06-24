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
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @PositiveOrZero
	private long id;
    
    @NotNull(message = "Username must me provided")
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 4, max = 15, message = "Username must be between 4-15 characters")
	private String username;
    
    @NotNull(message = "Password must be provided")
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 5, message = "Password must be at least 5 characters long")
	private String password;
    
    @NotNull(message = "First Name must be provided")
    @NotBlank(message = "First Name cannot be blank")
    @Size(min = 2, message = "First Name must be at least 2 characters long")
	private String firstName;
    
    @NotNull(message = "Last Name must be provided")
    @NotBlank(message = "Last Name cannot be blank")
    @Size(min = 2, message = "Last Name must be at least 2 characters long")
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
