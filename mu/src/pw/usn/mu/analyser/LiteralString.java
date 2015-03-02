package pw.usn.mu.analyser;

import pw.usn.mu.parser.LiteralStringNode;

/**
 * Represents an string literal in a mu program.
 */
public class LiteralString implements Expression {
	private String value;
	
	/**
	 * Initializes a new LiteralString with the given value.
	 * @param value The value of this string literal.
	 */
	public LiteralString(String value) {
		this.value = value;
	}
	
	/**
	 * Returns the value of this literal.
	 * @return The value of this string literal.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Analyses the given {@code node} in the given context and creates an equivalent
	 * {@link LiteralString}.
	 * @param context The context in which {@code node} resides.
	 * @param node The AST node to analyse.
	 * @return An {@link LiteralString} representing the same literal string as {@code
	 * node}.
	 */
	public static LiteralString analyse(ResolutionContext context, LiteralStringNode node) { 
		return new LiteralString(node.getValue());
	}
}
