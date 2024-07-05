package phugen.slotmachine.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import phugen.slotmachine.dto.Row;
import phugen.slotmachine.service.implementations.WinningConditionDetectorImplTraditional;
import phugen.slotmachine.service.interfaces.WinningConditionDetector;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

final public class WinningConditionDetectorImplTraditionalTest {

	@ParameterizedTest
	@MethodSource("getTests")
	public void testIfWinningConditionIsRecognizedCorrectly(TestCase data) {
		final WinningConditionDetector detector = new WinningConditionDetectorImplTraditional();
		final Boolean actual = detector.isWinningConditionMet(data.rows);

		assertEquals(data.isWinningCondition, actual);
	}

	private static Stream<TestCase> getTests() {
		return Stream.of(
				new TestCase(
						List.of(
							new Row("a", "b", "c"),
							new Row("d", "e", "f"),
							new Row("g", "h", "i")
						),
						"Should return false because there were no three symbols of a kind",
						false
				),
				new TestCase(
						List.of(
								new Row("x", "b", "c"),
								new Row("x", "e", "f"),
								new Row("x", "h", "i")
						),
						"Should return true because there were three vertical symbols of a kind in the first column",
						true
				),
				new TestCase(
						List.of(
								new Row("a", "x", "c"),
								new Row("d", "x", "f"),
								new Row("g", "x", "i")
						),
						"Should return true because there were three vertical symbols of a kind in the second column",
						true
				),
				new TestCase(
						List.of(
								new Row("a", "b", "x"),
								new Row("d", "e", "x"),
								new Row("g", "h", "x")
						),
						"Should return true because there were three vertical symbols of a kind in the third column",
						true
				),
				new TestCase(
						List.of(
								new Row("x", "x", "x"),
								new Row("d", "e", "f"),
								new Row("g", "h", "i")
						),
						"Should return true because there were three horizontal symbols of a kind in the first row",
						true
				),
				new TestCase(
						List.of(
								new Row("a", "b", "c"),
								new Row("x", "x", "x"),
								new Row("g", "h", "i")
						),
						"Should return true because there were three horizontal symbols of a kind in the second row",
						true
				),
				new TestCase(
						List.of(
								new Row("a", "b", "c"),
								new Row("d", "e", "f"),
								new Row("x", "x", "x")
						),
						"Should return true because there were three horizontal symbols of a kind in the third row",
						true
				),
				new TestCase(
						List.of(
								new Row("x", "b", "c"),
								new Row("d", "x", "f"),
								new Row("g", "h", "x")
						),
						"Should return true because there were three symbols of a kind on the reverse diagonal",
						true
				),
				new TestCase(
						List.of(
								new Row("a", "b", "x"),
								new Row("d", "x", "f"),
								new Row("x", "h", "i")
						),
						"Should return true because there were three symbols of a kind on the main diagonal",
						true
				),
				new TestCase(
						List.of(
								new Row("a", "b", "c", "x"),
								new Row("e", "f", "x", "h"),
								new Row("i", "x", "k", "l"),
								new Row("x", "n", "o", "p")
						),
						"Should return true because there were four symbols of a kind on the main diagonal",
						true
				)
		);
	}

	private record TestCase (
			List<Row> rows,
			String description,
			Boolean isWinningCondition
	) {
		@Override
		public String toString() {
			return this.description;
		}
	}
}
