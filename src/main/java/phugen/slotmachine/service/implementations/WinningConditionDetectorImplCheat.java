package phugen.slotmachine.service.implementations;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import phugen.slotmachine.dto.Row;
import phugen.slotmachine.service.interfaces.WinningConditionDetector;

import java.util.List;

/**
 * A completely unfair winning condition checker implementation that always
 * returns false, no matter what.
 */
@Service
@Qualifier("startCheating")
public class WinningConditionDetectorImplCheat implements WinningConditionDetector {
	@Override
	public Boolean isWinningConditionMet(List<Row> rows) {
		return false;
	}
}
