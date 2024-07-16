package phugen.slotmachine.service.implementations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import phugen.slotmachine.dto.Row;
import phugen.slotmachine.model.RoundResult;
import phugen.slotmachine.repository.interfaces.HistoricalResultDataProvider;
import phugen.slotmachine.service.interfaces.SlotmachineDisplay;
import phugen.slotmachine.service.interfaces.Slotmachine;
import phugen.slotmachine.service.interfaces.WinningConditionDetector;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
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
	private final SlotmachineDisplay display;
	private final WinningConditionDetector winningConditionDetector;

	private final HistoricalResultDataProvider history;
	private List<Row> rows;

	public TerminalSlotmachine(
			@Qualifier("standard") SlotmachineDisplay display,
			@Qualifier("playFair") WinningConditionDetector winningConditionDetector,
			HistoricalResultDataProvider history
	) {
		this.display = display;
		this.winningConditionDetector = winningConditionDetector;
		this.history = history;
	}

	@Override
	public void play() {
		Boolean isGameWon = playRound();
		while(!isGameWon) {
			isGameWon = playRound();
		}

		String statisticsMessage = getGamesPlayedMessage();
		display.displayMessage(statisticsMessage);
	}

	private Boolean playRound() {
		this.rows = initializeRowsRandomly(slotMachineRowSize);
		final Boolean isGameWon = winningConditionDetector.isWinningConditionMet(rows);
		final String resultMessage = getResultMessage(isGameWon);

		display.display(rows);
		display.displayMessage(resultMessage);

		this.history.saveResultData(new RoundResult(
				this.rows,
				isGameWon
		));

		return isGameWon;
	}

	private String getResultMessage(Boolean isGameWon) {
		final String positiveMessage = "Congratulations! You won! Your prize is a job offer!";
		final String negativeMessage = "No luck this time. Try again!";

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

	private String getGamesPlayedMessage() {
		List<RoundResult> result = history.getResultDataAfter(LocalDateTime.now().minusHours(1));

		Integer gamesPlayedInLastHour = result.size();
		Integer gamesWonInLastHour = result
				.stream()
				.filter(RoundResult::getWasWin)
				.toList()
				.size();
		Float percentWon = (gamesWonInLastHour.floatValue() / gamesPlayedInLastHour.floatValue()) * 100;

		return "\nYou have played "
				+ gamesPlayedInLastHour
				+ " games in the last hour of which you have won "
				+ gamesWonInLastHour
				+ ". That's "
				+ String.format(percentWon.toString(), new DecimalFormat("#0.00")) + "%!\n\n";
	}
}
