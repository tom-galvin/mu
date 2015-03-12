package pw.usn.mu.analyser;

import pw.usn.mu.analyser.closure.ClosureContext;
import pw.usn.mu.parser.TupleNode;
import pw.usn.mu.tokenizer.Location;

/**
 * Represents a tuple expression in a mu program.
 */
public class Tuple extends Expression {
	private Expression[] elements;
	
	/**
	 * Initializes a new Tuple.
	 * @param location The original location, in a source, of the code that represents
	 * this expression.
	 * @param expressions The expressions within this tuple.
	 */
	public Tuple(Location location, Expression... expressions) {
		super(location);
		this.elements = expressions;
	}
	
	/**
	 * Gets the size of the tuple - that is, the number of sub-expressions contained
	 * within.
	 * @return The size of the tuple.
	 */
	public int getSize() {
		return elements.length;
	}
	
	/**
	 * Gets the element at zero-based index {@code index} in this tuple.
	 * @param index The index in the range <b>[0, {@link #getSize()}]</b>, of the element
	 * to get.
	 * @return The element at index {@code index} in this tuple.
	 */
	public Expression getElement(int index) {
		return elements[index];
	}

	@Override
	public void liftClosures(ClosureContext context) {
		for(int i = 0; i < elements.length; i++) {
			elements[i].liftClosures(context);
		}
	}
	
	/**
	 * Analyses the given {@code node} in the given context and creates an equivalent tuple.
	 * @param context The context in which {@code node} resides.
	 * @param node The AST node to analyse.
	 * @return An expression representing the same tuple as {@code node}.
	 */
	public static Tuple analyse(ResolutionContext context, TupleNode node) {
		Expression[] expressions = new Expression[node.getSize()];
		for(int i = 0; i < expressions.length; i++) {
			expressions[i] = Expression.analyse(context, node.getValue(i));
		}
		return new Tuple(node.getLocation(), expressions);
	}
}
