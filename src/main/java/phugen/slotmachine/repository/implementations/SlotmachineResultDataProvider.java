package phugen.slotmachine.repository.implementations;

import org.springframework.stereotype.Repository;
import phugen.slotmachine.model.RoundResult;
import phugen.slotmachine.repository.interfaces.RoundResultRepository;
import phugen.slotmachine.repository.interfaces.HistoricalResultDataProvider;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This class allows access to historical result data showing the outcomes
 * of previously played Slotmachine games.
 */
@Repository
public class SlotmachineResultDataProvider implements HistoricalResultDataProvider {

	private final RoundResultRepository resultRepository;

	public SlotmachineResultDataProvider(
			RoundResultRepository resultRepository
	) {
		this.resultRepository = resultRepository;
	}

	@Override
	public List<RoundResult> getResultDataAfter(LocalDateTime date) {
		return resultRepository.findByDatePlayedGreaterThanEqual(date);
	}

	@Override
	public void saveResultData(RoundResult data) {
		resultRepository.save(data);
	}
}
