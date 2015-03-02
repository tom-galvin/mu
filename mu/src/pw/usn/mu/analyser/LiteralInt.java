package pw.usn.mu.analyser;

import pw.usn.mu.parser.LiteralIntNode;

/**
 * Represents an integer literal in a mu program.
 */
public class LiteralInt implements Expression {
	private int value;
	
	/**
	 * Initializes a new LiteralInt with the given value.
	 * @param value The value of this string literal.
	 */
	public LiteralInt(int value) {
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
		return new LiteralInt(node.getValue());
	}
}
