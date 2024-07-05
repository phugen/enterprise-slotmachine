package phugen.slotmachine.service.implementations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import phugen.slotmachine.dto.Row;
import phugen.slotmachine.service.interfaces.WinningConditionDetector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class implements a "traditional" winning condition detection. Traditional means
 * that the winning condition is that symbols of a kind are present
 * either horizontally, vertically or diagonally, left-to-right or right-to-left.
 */
@Service
@Qualifier("playFair")
final public class WinningConditionDetectorImplTraditional implements WinningConditionDetector {
	@Override
	public Boolean isWinningConditionMet(List<Row> rows) {
		final Boolean anyRowHasWinningCondition = doesAnyRowHaveWinningCondition(rows);
		final Boolean anyColumnHasWinningCondition = doesAnyColumnHaveWinningCondition(rows);
		final Boolean anyDiagonalHasWinningCondition = doesAnyDiagonalHaveWinningCondition(rows);

		return anyRowHasWinningCondition || anyColumnHasWinningCondition || anyDiagonalHasWinningCondition;
	}

	private Boolean doesAnyRowHaveWinningCondition(List<Row> rows) {
		return rows.stream()
			.map(it -> areElementsAllTheSame(it.slotContents()))
			.reduce(false, (current, elem) -> current |= elem);
	}

	private Boolean doesAnyColumnHaveWinningCondition(List<Row> rows) {
		final List<Row> columns = rotate90DegreesCCW(rows);
		return doesAnyRowHaveWinningCondition(columns);
	}

	private Boolean doesAnyDiagonalHaveWinningCondition(List<Row> rows) {
		final List<Row> diagonals = List.of(
				extractMainDiagonal(rows),
				extractAntiDiagonal(rows)
		);
		return doesAnyRowHaveWinningCondition(diagonals);
	}

	private Boolean areElementsAllTheSame(String... elements) {
		return Arrays
			.stream(elements)
			.allMatch(it -> it.equals(elements[0]));
	}

	/**
	 * Assuming that the given rows form a matrix where each row is a row
	 * in said matrix, counting from top to bottom, this function rotates
	 * it by 90 degrees counter-clockwise.
	 * This can be useful for operating on columns instead of rows - since
	 * a rotation effectively turns columns into rows and vice-versa.
	 * @param rows a new set of rows after the rotation.
	 */
	private List<Row> rotate90DegreesCCW(List<Row> rows) {
		final List<Row> rotated = new ArrayList<>();
		for (int idx = 0; idx < rows.size(); idx++) {
			final int finalIdx = idx; // index access in lambdas requires final variables; loop variables can't be final.
			final String[] rotatedRowContents = rows
						.stream()
						.map(it -> it.slotContents()[finalIdx])
						.toArray(String[]::new);
			rotated.add(0, new Row(rotatedRowContents));
		}

		return rotated;
	}

	private Row extractMainDiagonal(List<Row> rows) {
		final List<String> diagonalContents = new ArrayList<>();
		for (int idx = 0; idx < rows.size(); idx++) {
			final Row row = rows.get(idx);
			diagonalContents.add(row.slotContents()[idx]);
		}

		return new Row(diagonalContents.toArray(String[]::new));
	}

	private Row extractAntiDiagonal(List<Row> rows) {
		final List<String> diagonalContents = new ArrayList<>();
		for (int idx = 0; idx < rows.size(); idx++) {
			final Row row = rows.get(idx);
			diagonalContents.add(row.slotContents()[(rows.size() - 1) - idx]);
		}

		return new Row(diagonalContents.toArray(String[]::new));
	}
}
