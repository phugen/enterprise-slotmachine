package phugen.slotmachine.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import phugen.slotmachine.dto.Row;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This represents a row in the round_results database which records a round of a game
 * played at some point in time.
 */
@Entity
@Table(name = "round_results")
public class RoundResult {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@CreationTimestamp
	private LocalDateTime datePlayed;

	@Embedded
	@ElementCollection
	private List<Row> rows;
	private Boolean wasWin;

	public RoundResult() {}

	public RoundResult(List<Row> rows, Boolean wasWin) {
		this.rows = rows;
		this.wasWin = wasWin;
	}

	public Integer getId() {
		return id;
	}

	public void setDatePlayed(LocalDateTime dateTime) { this.datePlayed = dateTime; }

	public LocalDateTime getDatePlayed() {
		return datePlayed;
	}

	public List<Row> getRows() {
		return rows;
	}

	public Boolean getWasWin() {
		return wasWin;
	}
}
