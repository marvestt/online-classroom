package dev.andrylat.app.models;

public class User {

	private long userId;
	private String username;
	private String password;
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

	@Override
	public String toString() {
		return String.format("Class[user_id=%d, username='%s', password='%s', first_name='%s', surname='%s']", 
				userId, username, password, firstName, surname);
	}
}
