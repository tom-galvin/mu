package pw.usn.mu.analyser;

import pw.usn.mu.analyser.closure.ClosureContext;
import pw.usn.mu.parser.LiteralSymbolNode;
import pw.usn.mu.tokenizer.Location;

/**
 * Represents an symbol literal in a mu program.
 */
public class LiteralSymbol extends Expression {
	private String value;
	
	/**
	 * Initializes a new LiteralSymbol with the given value.
	 * @param location The original location, in a source, of the code that represents
	 * this expression.
	 * @param value The value of this symbol literal.
	 */
	public LiteralSymbol(Location location, String value) {
		super(location);
		this.value = value;
	}
	
	/**
	 * Returns the value of this literal.
	 * @return The value of this symbol literal.
	 */
	public String getValue() {
		return value;
	}
	
	@Override
	public void liftClosures(ClosureContext context) {
		/* Nothing to do! */
	}

	/**
	 * Analyses the given {@code node} in the given context and creates an equivalent
	 * {@link LiteralSymbol}.
	 * @param context The context in which {@code node} resides.
	 * @param node The AST node to analyse.
	 * @return An {@link LiteralSymbol} representing the same literal symbol as {@code
	 * node}.
	 */
	public static LiteralSymbol analyse(ResolutionContext context, LiteralSymbolNode node) { 
		return new LiteralSymbol(node.getLocation(), node.getValue());
	}
}
