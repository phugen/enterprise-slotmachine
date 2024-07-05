package phugen.slotmachine.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import phugen.slotmachine.dto.Row;
import phugen.slotmachine.service.implementations.WinningConditionDetectorImplCheat;
import phugen.slotmachine.service.implementations.WinningConditionDetectorImplTraditional;
import phugen.slotmachine.service.interfaces.WinningConditionDetector;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

final public class WinningConditionDetectorImplCheatTest {

	@ParameterizedTest
	@MethodSource("getTests")
	public void testIfWinningConditionIsAlwaysDenied(TestCase data) {
		final WinningConditionDetector detector = new WinningConditionDetectorImplCheat();
		final Boolean actual = detector.isWinningConditionMet(data.rows);

		assertEquals(false, actual);
	}

	private static Stream<TestCase> getTests() {
		return Stream.of(
				new TestCase(
						List.of(
							new Row("a", "b", "c"),
							new Row("d", "e", "f"),
							new Row("g", "h", "i")
						),
						"Should return false because there were no matching symbols"
				),
				new TestCase(
						List.of(
								new Row("x", "b", "c"),
								new Row("x", "e", "f"),
								new Row("x", "h", "i")
						),
						"Should return false despite three vertical symbols of a kind in the first column"
				),
				new TestCase(
						List.of(
								new Row("x", "x", "x"),
								new Row("d", "e", "f"),
								new Row("g", "h", "i")
						),
						"Should return false despite three horizontal symbols of a kind in the first row"
				),
				new TestCase(
						List.of(
								new Row("x", "b", "c"),
								new Row("d", "x", "f"),
								new Row("g", "h", "x")
						),
						"Should return false despite three symbols of a kind on the reverse diagonal"
				),
				new TestCase(
						List.of(
								new Row("a", "b", "x"),
								new Row("d", "x", "f"),
								new Row("x", "h", "i")
						),
						"Should return false despite three symbols of a kind on the main diagonal"
				)
		);
	}

	private record TestCase (
			List<Row> rows,
			String description
	) {
		@Override
		public String toString() {
			return this.description;
		}
	}
}
