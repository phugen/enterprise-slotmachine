package phugen.slotmachine.service.implementations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import phugen.slotmachine.dto.Row;
import phugen.slotmachine.service.interfaces.Displayable;
import phugen.slotmachine.service.interfaces.Slotmachine;
import phugen.slotmachine.service.interfaces.WinningConditionDetector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import static java.lang.System.exit;

/**
 * This class represents an emulated slot machine that can
 * run in a computer terminal.
 */
@Service
final public class TerminalSlotmachine implements Slotmachine {

	private final int slotMachineRowSize = 3;
	private final Displayable display;
	private final WinningConditionDetector winningConditionDetector;
	private List<Row> rows;

	public TerminalSlotmachine(
			Displayable display,
			@Qualifier("playFair") WinningConditionDetector winningConditionDetector
	) {
		this.display = display;
		this.winningConditionDetector = winningConditionDetector;
	}

	@Override
	public void play() {
		Boolean isGameWon = playRound();
		while(!isGameWon) {
			if (!doesUserWantToContinue(getUserInput()))
				break;
			isGameWon = playRound();
		}
	}

	private Boolean playRound() {
		this.rows = initializeRowsRandomly(slotMachineRowSize);

		final Boolean isGameWon = winningConditionDetector.isWinningConditionMet(rows);
		final String resultMessage = getResultMessage(isGameWon);

		display.display(rows);
		display.displayMessage(resultMessage);

		return isGameWon;
	}

	private String getUserInput() {
		Scanner scanner = new Scanner(System.in);
		return scanner.nextLine();
	}

	private Boolean doesUserWantToContinue (String input) {
		if (input.equals("c"))
			return false;
		return true;
	}

	private String getResultMessage(Boolean isGameWon) {
		final String positiveMessage = "Congratulations! You won! Your prize is a job offer!";
		final String negativeMessage = "No luck this time. Try again by pressing ENTER, or stop by pressing C!";

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
