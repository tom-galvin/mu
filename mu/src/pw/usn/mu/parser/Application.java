package pw.usn.mu.parser;

/**
 * Represents the application of a function with a given argument.
 */
public class Application implements Parsable, Expression {
	private Expression function;
	private Expression argument;
	
	/**
	 * Initializes a new Application, with the given function to apply and the
	 * value to apply with.
	 * @param function The function to apply in this application.
	 * @param argument The argument to pass to the function.
	 */
	public Application(Expression function, Expression argument) {
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
	 * Gets the argument passed to the applied function in this application.
	 * @return The argument passed to the function being applied.
	 */
	public Expression getArgument() {
		return argument;
	}
	
	/**
	 * Parses a variable number of applications from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return An expression, as parsed from the current input. This method must
	 * return an {@link Expression} rather than a concrete {@link Application} as
	 * there may be zero applications made, in which case there will be no
	 * application of anything.
	 */
	public static Expression parse(Parser parser) { 
		Expression left = Grammar.parseAtomic(parser);
		while(parser.test(token -> token.isAtomicToken())) {
			left = new Application(left, Grammar.parseAtomic(parser));
		}
		return left;
	}
}
