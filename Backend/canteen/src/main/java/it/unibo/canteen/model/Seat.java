package it.unibo.canteen.model;

import javax.persistence.*;

import com.sun.istack.NotNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint( name = "room_x_y", columnNames = {"room_id", "x","y"}))
public class Seat {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull
	@OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private Room room;
	
	@Column(nullable = false)
	private int x;
	@Column(nullable = false)
	private int y;
	
	public Seat() {
	}
	
	public Seat(int id) {
		this.id=id;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Room getRoom() {
		return room;
	}
	
	public void setRoom(Room room) {
		this.room = room;
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}

}
