package pw.usn.mu.analyser;

import pw.usn.mu.parser.LiteralIntNode;
import pw.usn.mu.tokenizer.Location;

/**
 * Represents an integer literal in a mu program.
 */
public class LiteralInt extends Expression {
	private int value;
	
	/**
	 * Initializes a new LiteralInt with the given value.
	 * @param location The original location, in a source, of the code that represents
	 * this expression.
	 * @param value The value of this string literal.
	 */
	public LiteralInt(Location location, int value) {
		super(location);
		this.value = value;
	}
	
	/**
	 * Returns the value of this literal.
	 * @return The value of this integer literal.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Analyses the given {@code node} in the given context and creates an equivalent
	 * {@link LiteralInt}.
	 * @param context The context in which {@code node} resides.
	 * @param node The AST node to analyse.
	 * @return An {@link LiteralInt} representing the same literal integer as {@code node}.
	 */
	public static LiteralInt analyse(ResolutionContext context, LiteralIntNode node) { 
		return new LiteralInt(node.getLocation(), node.getValue());
	}
}
