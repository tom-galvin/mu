package pw.usn.mu.analyser;

import pw.usn.mu.analyser.closure.ClosureContext;
import pw.usn.mu.parser.ApplicationNode;
import pw.usn.mu.parser.BindingNode;
import pw.usn.mu.parser.FunctionNode;
import pw.usn.mu.parser.IdentifierNode;
import pw.usn.mu.parser.LiteralIntNode;
import pw.usn.mu.parser.LiteralStringNode;
import pw.usn.mu.parser.LiteralSymbolNode;
import pw.usn.mu.parser.Node;
import pw.usn.mu.parser.SequenceNode;
import pw.usn.mu.parser.SwitchNode;
import pw.usn.mu.parser.TupleNode;
import pw.usn.mu.tokenizer.Location;

/**
 * Represents an expression in a mu program.
 */
public abstract class Expression {
	private Location location;
	
	/**
	 * Initializes a new Expression with the given location in the original parsed source file.
	 */
	public Expression(Location location) {
		this.location = location;
	}
	
	/**
	 * Gets the location of the source code representing this expression.
	 * @return The location of the expression, as defined in a parsed input source.
	 */
	public final Location getLocation() {
		return location;
	}
	
	/**
	 * Lifts all free variables in this expression up to the local scope.
	 * @param context The closure-lifting context to use for determining the correct
	 * values to lift.
	 */
	public abstract void liftClosures(ClosureContext context);
	
	/**
	 * Lifts all free variables in this expression up to the local scope. This makes
	 * {@code this} the top-level context during closure lifting.
	 */
	public void liftClosures() {
		liftClosures(null);
	}
	
	/**
	 * Analyses the given {@code node} in the given context and creates an equivalent
	 * {@link Expression}. The exact behaviour of this function will depend upon the
	 * type of {@code node}, but the expected behaviour is that an analysis tree node
	 * representing the same program structure as the given AST node will be returned,
	 * with all identifiers resolved into concrete references to values bound within
	 * the program. This means that, for example, given this example program:
	 * <pre>{@code
	 * outerFunc <- (\ x
	 *     innerFunc <- (\ x
	 *         x + 2);
	 *     innerFunc x);
	 * }</pre>
	 * The two identifiers named {@code x} will represent different values, and the addition
	 * operation inside {@code innerFunc} will return to the inner-most value named {@code
	 * x}.
	 * @param context The context in which {@code node} resides.
	 * @param node The AST node to analyse.
	 * @return An {@link Expression} representing the same program structure as {@code
	 * node} but with all identifiers resolved into references to values.
	 */
	public static Expression analyse(ResolutionContext context, Node node) {
		if(node instanceof ApplicationNode) {
			return Application.analyse(context, (ApplicationNode)node);
		} else if(node instanceof BindingNode) {
			return Binding.analyse(context, (BindingNode)node);
		} else if(node instanceof FunctionNode) {
			return Function.analyse(context, (FunctionNode)node);
		} else if(node instanceof LiteralIntNode) {
			return LiteralInt.analyse(context, (LiteralIntNode)node);
		} else if(node instanceof LiteralStringNode) {
			return LiteralString.analyse(context, (LiteralStringNode)node);
		} else if(node instanceof LiteralSymbolNode) {
			return LiteralSymbol.analyse(context, (LiteralSymbolNode)node);
		} else if(node instanceof IdentifierNode) {
			return Reference.analyse(context, (IdentifierNode)node);
		} else if(node instanceof TupleNode) {
			return Tuple.analyse(context, (TupleNode)node);
		} else if(node instanceof SequenceNode) {
			return Sequence.analyse(context, (SequenceNode)node);
		} else if(node instanceof SwitchNode) {
			throw new AnalysisErrorException("Switch expressions are currently unsupported.", node.getLocation());
		} else {
			throw new RuntimeException("Unknown AST node type: " + node.getClass().getSimpleName());
		}
	}
}
