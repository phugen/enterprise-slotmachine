package phugen.slotmachine.service.implementations;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import phugen.slotmachine.model.RoundResult;
import phugen.slotmachine.repository.implementations.SlotmachineResultDataProvider;
import phugen.slotmachine.repository.interfaces.HistoricalResultDataProvider;
import phugen.slotmachine.repository.interfaces.RoundResultRepository;

import java.time.LocalDateTime;
import java.util.List;

public class SlotmachineResultDataProviderTest {
		@Test
		public void testIfReturnsHistoryDataAfterDate() {
			final RoundResult entry = new RoundResult();
			entry.setDatePlayed(LocalDateTime.of(2024, 7, 15, 21, 2, 10));
			final List<RoundResult> expected = List.of(entry);

			RoundResultRepository mockRepository = Mockito.mock(RoundResultRepository.class);
			Mockito
					.when(mockRepository.findByDatePlayedGreaterThanEqual(Mockito.any()))
					.thenReturn(expected);

			HistoricalResultDataProvider provider = new SlotmachineResultDataProvider(mockRepository);
			final List<RoundResult> actual = provider.getResultDataAfter(
					LocalDateTime.of(2022, 5, 4, 2, 2, 17)
			);

			Assertions.assertEquals(expected, actual);
		}
}
