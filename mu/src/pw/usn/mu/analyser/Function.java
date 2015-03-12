package pw.usn.mu.analyser;

import java.util.HashMap;
import java.util.Map;

import pw.usn.mu.analyser.closure.ClosureContext;
import pw.usn.mu.analyser.closure.FunctionClosureContext;
import pw.usn.mu.parser.FunctionNode;
import pw.usn.mu.parser.IdentifierNode;
import pw.usn.mu.tokenizer.Location;

/**
 * Represents a first-class function definition in a mu program.
 */
public class Function extends Expression {
	private Value argument;
	private Expression body;
	private Map<Value, Reference> closureContext;
	
	/**
	 * Initializes a new Function.
	 * @param location The original location, in a source, of the code that represents
	 * this expression.
	 * @param argument The value passed to the function.
	 * @param body The function body of the new function.
	 */
	public Function(Location location, Value argument, Expression body) {
		super(location);
		this.argument = argument;
		this.body = body;
		this.closureContext = new HashMap<Value, Reference>();
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
	 * Gets the closure context for this function. This maps local
	 * closure values into references to values outside of this function.
	 * @return The closure context for this function.
	 */
	public Map<Value, Reference> getClosureContext() {
		return closureContext;
	}
	
	@Override
	public void liftClosures(ClosureContext context) {
		FunctionClosureContext functionContext = new FunctionClosureContext(this, context);
		body.liftClosures(functionContext);
		functionContext.getLocalClosure().forEach((v, l) -> {
			closureContext.put(v, l.newReference(getLocation()));
		});
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
		Value argument = new Value(node.getArgumentName());
		
		ResolutionContext functionContext = new ResolutionContext(context) {
			@Override
			public Reference resolve(IdentifierNode identifier) {
				if(identifier.isUnqualified() &&
						identifier.getName().equals(node.getArgumentName())) {
					return argument.newReference(identifier.getLocation());
				} else {
					return super.resolve(identifier);
				}
			}
		};
		
		return new Function(node.getLocation(),
				argument,
				Expression.analyse(functionContext, node.getBody()));
	}
}
