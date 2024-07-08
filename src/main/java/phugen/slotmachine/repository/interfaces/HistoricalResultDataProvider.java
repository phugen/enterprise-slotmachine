package phugen.slotmachine.repository.interfaces;

import phugen.slotmachine.model.RoundResult;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A class that implements this interface provides access to historical data about the results
 * of a game played.
 */
public interface HistoricalResultDataProvider {
	/**
	 * @param date A deadline before which all results should be discarded.
	 * @return A list of results for a game.
	 */
	List<RoundResult> getResultDataAfter(LocalDateTime date);

	/**
	 * Stores a result of a game.
	 * @param data The result to be stored.
	 */
	void saveResultData(RoundResult data);
}
