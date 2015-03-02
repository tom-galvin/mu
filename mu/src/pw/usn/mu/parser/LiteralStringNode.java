package pw.usn.mu.parser;

import pw.usn.mu.tokenizer.LiteralStringToken;

/**
 * Represents a string literal in mu source code.
 */
public class LiteralStringNode extends Node {
	/**
	 * The delimiter used to signal the start and end of a literal string in source code.
	 */
	public static final String LITERAL_STRING_DELIMITER = "\"";
	private String value;
	
	/**
	 * Initializes a new LiteralString with the given value.
	 * @param value The value of the string represented by this literal.
	 */
	public LiteralStringNode(String value) {
		this.value = value;
	}
	
	/**
	 * Gets the value of this string literal.
	 * @return The value as a {@link String} that this LiteralString represents.
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Parses a string literal from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return A string literal, as parsed from the current input.
	 */
	public static LiteralStringNode parse(Parser parser) {
		parser.expect(token -> token instanceof LiteralStringToken, "String literal expected.");
		return new LiteralStringNode(((LiteralStringToken)parser.current()).getValue());
	}
}
