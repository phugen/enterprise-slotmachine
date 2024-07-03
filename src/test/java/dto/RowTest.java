package dto;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import phugen.slotmachine.dto.Row;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RowTest {

	@ParameterizedTest
	@MethodSource("getTests")
	public void testIfRowIsPrintedCorrectly(TestCase data) {
		Row row = new Row(data.rowContents.toArray(new String[0]));

		String actual = row.toString();
		StringBuilder builder = new StringBuilder();
		builder.append("||");
		data.rowContents.forEach ( it -> builder.append(String.format(" %s ", it)));
		builder.append("||");

		String expected = builder.toString();

		assertEquals(expected, actual);
	}

	private static Stream<TestCase> getTests() {
		return Stream.of(
				new TestCase(
						List.of(""),
						"Should nothing but frame because contents were empty"
				),
				new TestCase(
						List.of("\uD83D\uDC80"),
						"Should display one slot: A skull"
				),
				new TestCase(
						List.of("\uD83D\uDCB0", "\uD83D\uDD25", "\uD83D\uDE97"),
						"Should display three slots: A bag of money, a flame and a car"
				)
		);
	}

	private record TestCase (
			List<String> rowContents,
			String description
	) {
		@Override
		public String toString() {
			return this.description;
		}
	}
}
