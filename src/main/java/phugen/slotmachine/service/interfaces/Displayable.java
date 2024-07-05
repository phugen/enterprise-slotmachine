package phugen.slotmachine.service.interfaces;

import java.util.List;

/**
 * A class that implements this interface provides some sort of way to be displayed
 * in an appealing manner.
 */
public interface Displayable {
	/**
	 * Displays the given objects.
	 */
	<T> void display(List<T> toDisplay);

	/**
	 * Displays a simple message.
	 * @param message The message to be displayed.
	 */
	void displayMessage(String message);
}
