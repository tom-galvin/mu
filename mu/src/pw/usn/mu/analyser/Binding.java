package pw.usn.mu.analyser;

import pw.usn.mu.parser.BindingNode;
import pw.usn.mu.parser.IdentifierNode;

/**
 * Represents the binding of a value in a mu program.
 */
public class Binding implements Expression {
	private Value value;
	private Expression expression, body;
	
	/**
	 * Initializes a new Binding.
	 * @param value The bound-to value.
	 * @param expression The expression bound in the binding.
	 * @param body The body of the new binding.
	 */
	public Binding(Value value, Expression expression, Expression body) {
		this.value = value;
		this.expression = expression;
		this.body = body;
	}
	
	/**
	 * Gets the value to which an expression was bound.
	 * @return The bound-to value.
	 */
	public Value getValue() {
		return value;
	}
	
	/**
	 * Gets the expression bound in this binding.
	 * @return The expression bound.
	 */
	public Expression getExpression() {
		return expression;
	}
	
	/**
	 * Gets the body of this binding.
	 * @return The body of this binding.
	 */
	public Expression getBody() {
		return body;
	}

	/**
	 * Analyses the given {@code node} in the given context and creates an equivalent
	 * {@link Binding}.
	 * @param context The context in which {@code node} resides.
	 * @param node The AST node to analyse.
	 * @return An {@link Binding} representing the same program structure as {@code
	 * node} but with all identifiers resolved into references to values.
	 */
	public static Binding analyse(ResolutionContext context, BindingNode node) {
		Value value = new Value();
		
		ResolutionContext bindingContext = new ResolutionContext(context) {
			@Override
			public Reference resolve(IdentifierNode identifier) {
				if(identifier.isUnqualified() &&
						identifier.getName().equals(node.getName())) {
					return value.newReference();
				} else {
					return super.resolve(identifier);
				}
			}
		};
		
		return new Binding(
				value,
				Expression.analyse(bindingContext, node.getValue()),
				Expression.analyse(bindingContext, node.getContent()));
	}
}
