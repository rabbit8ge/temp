package com.example.dto;

import java.io.Serializable;

public class UserInfo implements Serializable {

	public String username;

	public String password;

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

	@Override
	public String toString() {
		return "UserInfo [username=" + username + ", password=" + password + "]";
	}

}
