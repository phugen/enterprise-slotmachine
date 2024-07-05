package phugen.slotmachine.service.implementations;

import org.springframework.stereotype.Service;
import phugen.slotmachine.dto.Row;
import phugen.slotmachine.service.interfaces.Displayable;

import java.util.List;
import java.util.logging.*;

/**
 * This class implements a barebones, logger-based approach to just printing the
 * contents of a slot machine.
 */
@Service
final public class SlotmachineStandardDisplay implements Displayable {

	private final Logger logger = Logger.getLogger(this.getClass().getName());

	public SlotmachineStandardDisplay() {
		configureDisplayFormat();
	}

	@Override
	public <T> void display(List<T> data) {
		final String errorMessage = "can't display non-slotmachine row data";
		if (!(hasValidDataFormat(data)))
			throw new IllegalArgumentException(errorMessage);

		showDataInTerminal(data);
	}

	@Override
	public void displayMessage(String message) {
		logger.info(message);
		logger.info("\n");
	}

	private void configureDisplayFormat() {
		ConsoleHandler handler = new ConsoleHandler();
		Formatter formatter = new Formatter() {
			@Override
			public String format(LogRecord record) {
				return record.getMessage();
			}
		};
		handler.setFormatter(formatter);

		logger.addHandler(handler);
		logger.setLevel(Level.ALL);
	}

	private <T> Boolean hasValidDataFormat(List<T> data) {
		if(data.isEmpty())
			return true;

		if (!(data.get(0) instanceof Row))
			return false;

		return true;
	}

	private <T> void showDataInTerminal(List<T> rows) {
		rows.forEach( it -> {
			logger.info(it.toString());
			logger.info("\n");
		});
	}
}
