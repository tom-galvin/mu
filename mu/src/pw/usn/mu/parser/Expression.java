package pw.usn.mu.parser;

/**
 * Represents an expression that can be evaluated. This includes such
 * things like function application, functions themselves, and identifiers,
 * but not things like modules.
 */
public interface Expression extends Parsable {
	/**
	 * Parses an expression from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return An expression, as parsed from the current input.
	 */
	public static Expression parse(Parser parser) {
		return Tuple.parse(parser);
	}
}
