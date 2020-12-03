package it.unibo.canteen.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;


@Entity
@Table(uniqueConstraints = @UniqueConstraint( name = "user_resDate_firstBlock_blocksRes_seat", columnNames = {"user_id","reservationDate","firstBlockReserved","blocksReserved","seat_id"}) 
							//@UniqueConstraint( name = "user_resDate_firstBlock_seat", columnNames = {"user_id","reservationDate","firstBlockReserved","seat_id"})
	  )
public class Reservation {
	
	@PrePersist
	  protected void onCreate() {
	    this.createdAt = LocalDateTime.now();
	  }
	
	@PreUpdate
	  protected void onUpdate() {
	    this.modifiedAt = LocalDateTime.now();
	  }
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@NotNull
	@OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;
	
	@NotNull
	@OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "seat_id", referencedColumnName = "id")
	private Seat seat;
	
	private int previousReservationId;
	
	@Column(nullable = false)
	private LocalDateTime createdAt;
	
	private LocalDateTime modifiedAt;
	private LocalDateTime eliminatedAt;
	
	@Column(nullable = false)
	@JsonFormat(pattern = "dd/MM/yyyy")
	private LocalDate reservationDate;
	
	@Column(nullable = false)
	private int firstBlockReserved;
	
	@Column(nullable = false)
	private int blocksReserved;
	
	public Reservation() {
	}
	
	public Reservation(int id) {
		this.id=id;
	}
	

	public Reservation(User user, Seat seat, LocalDate reservationDate, int firstBlockReserved, int blocksReserved) {
		this.user = user;
		this.seat = seat;
		this.reservationDate = reservationDate;
		this.firstBlockReserved = firstBlockReserved;
		this.blocksReserved = blocksReserved;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Seat getSeat() {
		return seat;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
	}

	public int getPreviousReservationId() {
		return previousReservationId;
	}

	public void setPreviousReservation(int previousReservationId) {
		this.previousReservationId = previousReservationId;
	}

	public LocalDateTime getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(LocalDateTime modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public LocalDateTime getEliminatedAt() {
		return eliminatedAt;
	}

	public void setEliminatedAt(LocalDateTime eliminatedAt) {
		this.eliminatedAt = eliminatedAt;
	}

	public int getFirstBlockReserved() {
		return firstBlockReserved;
	}

	public void setFirstBlockReserved(int firstBlockReserved) {
		this.firstBlockReserved = firstBlockReserved;
	}

	public int getBlocksReserved() {
		return blocksReserved;
	}

	public void setBlocksReserved(int blocksReserved) {
		this.blocksReserved = blocksReserved;
	}


	public LocalDateTime getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}


	public LocalDate getReservationDate() {
		return reservationDate;
	}


	public void setReservationDate(LocalDate reservationDate) {
		this.reservationDate = reservationDate;
	}

}
