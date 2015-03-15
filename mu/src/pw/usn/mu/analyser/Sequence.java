package pw.usn.mu.analyser;

import pw.usn.mu.analyser.closure.ClosureContext;
import pw.usn.mu.parser.SequenceNode;
import pw.usn.mu.tokenizer.Location;

/**
 * Represents a sequence expression in a mu program.
 */
public class Sequence extends Expression {
	private Expression[] elements;
	
	/**
	 * Initializes a new Sequence.
	 * @param location The original location, in a source, of the code that represents
	 * this expression.
	 * @param expressions The expressions within this sequence.
	 */
	public Sequence(Location location, Expression... expressions) {
		super(location);
		this.elements = expressions;
	}
	
	/**
	 * Gets the length of the sequence - that is, the number of elements.
	 * @return The length of the sequence.
	 */
	public int getLength() {
		return elements.length;
	}
	
	/**
	 * Gets the element at zero-based index {@code index} in this sequence.
	 * @param index The index in the range <b>[0, {@link #getLength()} - 1]</b>, of the element
	 * to get.
	 * @return The element at index {@code index} in this sequence.
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
	 * Analyses the given {@code node} in the given context and creates an equivalent sequence
	 * expression.
	 * @param context The context in which {@code node} resides.
	 * @param node The AST node to analyse.
	 * @return An expression representing the same sequence as {@code node}.
	 */
	public static Sequence analyse(ResolutionContext context, SequenceNode node) {
		Expression[] expressions = new Expression[node.getLength()];
		for(int i = 0; i < expressions.length; i++) {
			expressions[i] = Expression.analyse(context, node.getValue(i));
		}
		return new Sequence(node.getLocation(), expressions);
	}
}
