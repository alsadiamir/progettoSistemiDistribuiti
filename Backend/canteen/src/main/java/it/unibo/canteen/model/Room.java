package it.unibo.canteen.model;

import java.time.LocalTime;

import javax.persistence.*;

import com.sun.istack.NotNull;

@Entity
public class Room {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(nullable = false)
	private String name;
	@Column(nullable = false)
	private String address;
	@Column(nullable = false)
	private LocalTime openingTime;
	@Column(nullable = false)
	private LocalTime closingTime;
	
	public Room() {
	}
	
	public Room(int id) {
		this.id=id;
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
	
	public LocalTime getOpeningTime() {
		return openingTime;
	}
	
	public void setOpeningTime(LocalTime openingTime) {
		this.openingTime = openingTime;
	}
	
	public LocalTime getClosingTime() {
		return closingTime;
	}
	
	public void setClosingTime(LocalTime closingTime) {
		this.closingTime = closingTime;
	}

}
