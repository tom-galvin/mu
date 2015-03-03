package pw.usn.mu.analyser;

import pw.usn.mu.parser.ApplicationNode;
import pw.usn.mu.tokenizer.Location;

/**
 * Represents the application of a function with an argument in a
 * mu program.
 */
public class Application extends Expression {
	private Expression function, argument;
	
	/**
	 * Initializes a new Application with the given applied function
	 * and argument.
	 * @param location The original location, in a source, of the code that represents
	 * this expression.
	 * @param function The function that is applied.
	 * @param argument The argument passed to the function.
	 */
	public Application(Location location, Expression function, Expression argument) {
		super(location);
		this.function = function;
		this.argument = argument;
	}
	
	/**
	 * Gets the function applied in this application.
	 * @return The function applied.
	 */
	public Expression getFunction() {
		return function;
	}
	
	/**
	 * Gets the argument passed in this application.
	 * @return The argument passed to the applied function.
	 */
	public Expression getArgument() {
		return argument;
	}
	
	/**
	 * Analyses the given {@code node} in the given context and creates an equivalent
	 * {@link Application}.
	 * @param context The context in which {@code node} resides.
	 * @param node The AST node to analyse.
	 * @return An {@link Application} representing the same program structure as {@code
	 * node} but with all identifiers resolved into references to values.
	 */
	public static Application analyse(ResolutionContext context, ApplicationNode node) {
		return new Application(node.getLocation(),
				Expression.analyse(context, node.getFunction()),
				Expression.analyse(context, node.getArgument()));
	}
}
