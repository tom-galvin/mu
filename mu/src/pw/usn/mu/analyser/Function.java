package pw.usn.mu.analyser;

import pw.usn.mu.parser.FunctionNode;
import pw.usn.mu.parser.IdentifierNode;

/**
 * Represents a first-class function in a mu program.
 */
public class Function implements Expression {
	private Value argument;
	private Expression body;
	
	/**
	 * Initializes a new Function.
	 * @param argument The value passed to the function.
	 * @param body The function body of the new function.
	 */
	public Function(Value argument, Expression body) {
		this.argument = argument;
		this.body = body;
	}
	
	/**
	 * Gets the argument of this function.
	 * @return The argument of this function.
	 */
	public Value getArgument() {
		return argument;
	}
	
	/**
	 * Gets the body of this function.
	 * @return The body of this function.
	 */
	public Expression getBody() {
		return body;
	}

	/**
	 * Analyses the given {@code node} in the given context and creates an equivalent
	 * {@link Function}.
	 * @param context The context in which {@code node} resides.
	 * @param node The AST node to analyse.
	 * @return An {@link Function} representing the same program structure as {@code
	 * node} but with all identifiers resolved into references to values.
	 */
	public static Function analyse(ResolutionContext context, FunctionNode node) {
		Value argument = new Value();
		
		ResolutionContext functionContext = new ResolutionContext(context) {
			@Override
			public Reference resolve(IdentifierNode identifier) {
				if(identifier.isUnqualified() &&
						identifier.getName().equals(node.getArgumentName())) {
					return argument.newReference();
				} else {
					return super.resolve(identifier);
				}
			}
		};
		
		return new Function(
				argument,
				Expression.analyse(functionContext, node.getBody()));
	}
}
