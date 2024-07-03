package phugen.slotmachine.dto;

import java.util.stream.Stream;

/**
 * Identifies one of the rows of a slotmachine.
 *
 * @param slotContent One or more strings that make up the contents of the slots in this row.
 */
public record Row(
		String... slotContent
) {
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("||");
		Stream.of(this.slotContent).forEach(it -> builder.append(
			String.format(" %s ", it))
		);
		builder.append("||");

		return builder.toString();
	}
}
