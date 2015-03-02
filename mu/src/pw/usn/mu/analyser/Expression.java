package pw.usn.mu.analyser;

import pw.usn.mu.parser.ApplicationNode;
import pw.usn.mu.parser.BindingNode;
import pw.usn.mu.parser.FunctionNode;
import pw.usn.mu.parser.IdentifierNode;
import pw.usn.mu.parser.LiteralIntNode;
import pw.usn.mu.parser.LiteralStringNode;
import pw.usn.mu.parser.Node;

/**
 * Represents an expression in a mu program.
 */
public interface Expression {
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
		} else if(node instanceof IdentifierNode) {
			return Reference.analyse(context, (IdentifierNode)node);
		} else {
			throw new RuntimeException("Unknown AST node type: " + node.getClass().getSimpleName());
		}
	}
}
