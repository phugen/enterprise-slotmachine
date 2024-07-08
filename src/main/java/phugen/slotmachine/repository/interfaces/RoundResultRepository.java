package phugen.slotmachine.repository.interfaces;

import org.springframework.data.repository.CrudRepository;
import phugen.slotmachine.model.RoundResult;

import java.time.LocalDateTime;
import java.util.List;

public interface RoundResultRepository extends CrudRepository<RoundResult, Integer> {
	List<RoundResult> findByDatePlayedGreaterThanEqual(LocalDateTime date);
}