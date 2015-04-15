package it.smartcommunitylab.parking.management.web.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserSetting implements Serializable {
	
	private static final long serialVersionUID = -5310088564875457204L;
	private String id;
	private String username;
    private String password;
    private String appId;
    private String name;
    private String surname;
    private String mail;

    
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * @return the user
	 */
	public String getUsername() {
		return username;
	}
	
	/**
	 * @param user: the user to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	
	/**
	 * @param password: the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}


	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getMail() {
		return mail;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	@Override
	public String toString() {
		return "UserSetting [id=" + id + ", username=" + username + ", password="
				+ password + ", appId=" + appId + ", name=" + name
				+ ", surname=" + surname + ", mail=" + mail + "]";
	}

	
}	
