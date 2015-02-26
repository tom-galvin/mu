package pw.usn.mu.parser.type;

import pw.usn.mu.parser.Parsable;
import pw.usn.mu.parser.Parser;

/**
 * Represents a type in mu source code.
 */
public interface Type extends Parsable {
	/**
	 * Parses a type definition from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return A {@link Type}, as parsed from the current input.
	 */
	public static Type parse(Parser parser) {
		return DefinedType.parse(parser);
	}
}
