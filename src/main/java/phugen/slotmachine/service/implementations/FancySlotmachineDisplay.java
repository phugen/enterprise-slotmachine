package phugen.slotmachine.service.implementations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import phugen.slotmachine.dto.Row;
import phugen.slotmachine.service.interfaces.SlotmachineDisplay;

import java.util.List;
import java.util.Map;
import java.util.logging.*;

/**
 * This class is for when a standard display is not beautiful enough.
 * This enables colored text and fancy text in the display.
 */
@Service
@Qualifier("pretty")
public class FancySlotmachineDisplay implements SlotmachineDisplay {

	private final Logger logger = Logger.getLogger(this.getClass().getName());

	public FancySlotmachineDisplay() {
		configureDisplayFormat();
	}

	@Override
	public void display(List<Row> rows) {
		rows.forEach( it -> {
			final String fancyRow = addFancySymbolsToString(it.toString());
			logger.info(fancyRow);
			logger.info("\n");
		});
	}

	private String addFancySymbolsToString(String input) {
		final String fancyString = "×º°”˜`”°º×";
		return fancyString
				.concat(input)
				.concat(fancyString);
	}

	@Override
	public void displayMessage(String message) {
		final String fancyMessage = swapLettersForFancyLetters(message);
		final String fancyColoredMessage = colorStringYellow(fancyMessage);
		logger.info(fancyColoredMessage);
		logger.info("\n");
	}

	private String colorStringYellow(String input) {
		final String yellowAnsiCode = "\u001B[33m";
		final String resetAnsiCode = "\u001B[0m";

		return yellowAnsiCode
				.concat(input)
				.concat(resetAnsiCode);
	}

	private String swapLettersForFancyLetters(String input) {
		final Map<String, String> lookupTable = Map.ofEntries(
				Map.entry("a", "А"),
				Map.entry("b", "в"),
				Map.entry("c", "ℂ"),
				Map.entry("d", "д"),
				Map.entry("e", "ℇ"),
				Map.entry("f", "f"),
				Map.entry("g", "ℊ"),
				Map.entry("h", "ℏ"),
				Map.entry("i", "ℐ"),
				Map.entry("j", "ℑ"),
				Map.entry("k", "к"),
				Map.entry("l", "ℓ"),
				Map.entry("m", "м"),
				Map.entry("n", "ℕ"),
				Map.entry("o", "ø"),
				Map.entry("p", "℘"),
				Map.entry("q", "ℚ"),
				Map.entry("r", "я"),
				Map.entry("s", "s"),
				Map.entry("t", "t"),
				Map.entry("u", "u"),
				Map.entry("v", "℣"),
				Map.entry("w", "ш"),
				Map.entry("x", "x"),
				Map.entry("y", "ℽ"),
				Map.entry("z", "ℤ")
		);
		final String inputLowerCase = input.toLowerCase();
		StringBuilder builder = new StringBuilder();

		for (String it: inputLowerCase.split("")) {
			final String fancyReplacement = lookupTable.get(it);
			if (fancyReplacement != null)
				builder.append(fancyReplacement);
			else
				builder.append(it);
		}

		return builder.toString();
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
