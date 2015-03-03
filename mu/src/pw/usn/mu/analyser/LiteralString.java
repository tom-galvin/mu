package pw.usn.mu.analyser;

import pw.usn.mu.parser.LiteralStringNode;
import pw.usn.mu.tokenizer.Location;

/**
 * Represents an string literal in a mu program.
 */
public class LiteralString extends Expression {
	private String value;
	
	/**
	 * Initializes a new LiteralString with the given value.
	 * @param location The original location, in a source, of the code that represents
	 * this expression.
	 * @param value The value of this string literal.
	 */
	public LiteralString(Location location, String value) {
		super(location);
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
		return new LiteralString(node.getLocation(), node.getValue());
	}
}
