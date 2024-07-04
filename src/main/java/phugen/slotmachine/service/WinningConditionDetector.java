package phugen.slotmachine.service;

import phugen.slotmachine.dto.Row;

import java.util.List;

/**
 * A class that implements this interface can determine whether a winning
 * condition for a number of rows has been met or not.
 */
public interface WinningConditionDetector {
	/**
	 * @param rows the rows of a slot machine.
	 * @return True if the given rows indicate a winning condition, false otherwise.
	 */
	Boolean isWinningConditionMet(List<Row> rows);
}
