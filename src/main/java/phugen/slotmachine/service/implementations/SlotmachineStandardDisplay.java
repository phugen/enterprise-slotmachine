package phugen.slotmachine.service.implementations;

import org.springframework.stereotype.Service;
import phugen.slotmachine.dto.Row;
import phugen.slotmachine.service.interfaces.SlotmachineDisplay;

import java.util.List;
import java.util.logging.*;

/**
 * This class implements a barebones, logger-based approach to just printing the
 * contents of a slot machine.
 */
@Service
final public class SlotmachineStandardDisplay implements SlotmachineDisplay {

	private final Logger logger = Logger.getLogger(this.getClass().getName());

	public SlotmachineStandardDisplay() {
		configureDisplayFormat();
	}

	@Override
	public void display(List<Row> data) {
		data.forEach( it -> {
			logger.info(it.toString());
			logger.info("\n");
		});
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
}
