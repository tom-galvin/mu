package pw.usn.mu.parser;

import pw.usn.mu.tokenizer.LiteralSymbolToken;
import pw.usn.mu.tokenizer.Location;

/**
 * Represents a symbol literal in mu source code.
 */
public class LiteralSymbolNode extends Node {
	/**
	 * The delimiter used to signal the start and end of a literal symbol in source code.
	 */
	public static final String LITERAL_SYMBOL_DELIMITER = "\'";
	private String value;
	
	/**
	 * Initializes a new LiteralSymbol with the given value.
	 * @param location The location of the AST node in a parsed input source.
	 * @param value The value of the symbol represented by this literal.
	 */
	public LiteralSymbolNode(Location location, String value) {
		super(location);
		this.value = value;
	}
	
	/**
	 * Gets the value of this symbol literal.
	 * @return The value as a {@link String} that this LiteralSymbol represents.
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Parses a symbol literal from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return A symbol literal, as parsed from the current input.
	 */
	public static LiteralSymbolNode parse(Parser parser) {
		Location location = parser.expect(token -> token instanceof LiteralSymbolToken, "Symbol literal expected.");
		LiteralSymbolToken current = (LiteralSymbolToken)parser.current();
		return new LiteralSymbolNode(location, current.getValue());
	}
}
