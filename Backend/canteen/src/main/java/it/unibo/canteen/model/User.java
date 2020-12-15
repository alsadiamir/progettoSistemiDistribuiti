package it.unibo.canteen.model;


import javax.persistence.*;

import com.sun.istack.NotNull;

import java.io.Serializable;


@Entity
public class User implements Serializable {
	
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column
	private String device;
	
	@Column(unique = true, nullable = false)
	private String mail;
	
	public User() {}
	
	public User(int id) {
		this.id=id;
	}
	
	public User(String mail, String device) {
		this.mail = mail;
		this.device = device;
	}

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
	

}
