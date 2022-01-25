package com.projects.classroom.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class User {

    @PositiveOrZero(message = "userId cannot be less than 0")
	private long userId;
    
    @NotNull(message = "username cannot be null")
    @NotBlank(message = "username cannot be blank")
	private String username;
    
    @NotNull(message = "password cannot be null")
    @NotBlank(message = "password cannot be blank")
	private String password;
    
    @NotNull(message = "firstName cannot be null")
    @NotBlank(message = "firstName cannot be blank")
	private String firstName;
    
	private String surname;
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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
	
	public void setUserInfo(User user) {
	    this.userId = user.userId;
	    this.username = user.username;
	    this.password = user.password;
	    this.firstName = user.firstName;
	    this.surname = user.surname;
	}

	@Override
	public String toString() {
		return String.format("User[user_id=%d, username='%s', password='%s', first_name='%s', surname='%s']", 
				userId, username, password, firstName, surname);
	}
}
