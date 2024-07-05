package phugen.slotmachine.service.implementations;

import org.springframework.stereotype.Service;
import phugen.slotmachine.dto.Row;
import phugen.slotmachine.service.interfaces.Displayable;
import phugen.slotmachine.service.interfaces.Slotmachine;
import phugen.slotmachine.service.interfaces.WinningConditionDetector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class represents an emulated slot machine that can
 * run in a computer terminal.
 */
@Service
final public class TerminalSlotmachine implements Slotmachine {

	private final int slotMachineRowSize = 3;
	private final Displayable display;
	private final WinningConditionDetector winningConditionDetector;
	private final List<Row> rows;

	public TerminalSlotmachine(
			Displayable display,
			WinningConditionDetector winningConditionDetector
	) {
		this.display = display;
		this.winningConditionDetector = winningConditionDetector;
		this.rows = initializeRowsRandomly(slotMachineRowSize);
	}

	@Override
	public void play() {
		display.display(rows);
		display.displayFarewellMessage(getFarewellMessage());
	}

	private String getFarewellMessage() {
		final String positiveMessage = "Congratulations! You won! Your prize is a job offer!";
		final String negativeMessage = "No luck this time. Try again!";
		final Boolean isGameWon = winningConditionDetector.isWinningConditionMet(rows);

		if(isGameWon)
			return positiveMessage;
		return negativeMessage;
	}

	private List<Row> initializeRowsRandomly(Integer numRows) {
		List<String> possibleSlotContents = List.of(
			"\uD83D\uDC80", // skull
			"\uD83D\uDC37", // pig
			"\uD83D\uDD25", // fire,
			"\uD83C\uDF40", // four-leaf clover
			"\uD83C\uDF44", // toadstool
			"\uD83C\uDF52", // cherries
			"\uD83C\uDF1F", // star
			"\uD83D\uDCB0" // money bag
		);

		List<Row> initializedRows = new ArrayList<>();
		Random randomNumberGenerator = new Random();

		for (int rowIdx = 0; rowIdx < numRows; rowIdx++) {
			List<String> randomRowContents = randomNumberGenerator.ints(numRows, 0, possibleSlotContents.size())
				.mapToObj(possibleSlotContents::get)
				.toList();
			initializedRows.add(new Row(randomRowContents.toArray(String[]::new)));
		}

		return initializedRows;
	}
}
