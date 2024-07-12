package phugen.slotmachine.service.interfaces;

import phugen.slotmachine.dto.Row;

import java.util.List;

/**
 * A class that implements this interface provides some sort of way to display
 * Slotmachine data.
 */
public interface SlotmachineDisplay {
	/**
	 * Displays the given rows.
	 *  @param rows The list of rows to be displayed.
	 */
	void display(List<Row> rows);

	/**
	 * Displays a simple message.
	 * @param message The message to be displayed.
	 */
	void displayMessage(String message);
}
