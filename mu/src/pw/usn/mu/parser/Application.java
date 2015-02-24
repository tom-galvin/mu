package pw.usn.mu.parser;

/**
 * Represents the application of a function with a given argument.
 */
public class Application implements Parsable {
	private Parsable function;
	private Parsable argument;
	
	/**
	 * Initializes a new Application, with the given function to apply and the
	 * value to apply with.
	 * @param function The function to apply in this application.
	 * @param argument The argument to pass to the function.
	 */
	public Application(Parsable function, Parsable argument) {
		this.function = function;
		this.argument = argument;
	}
	
	/**
	 * Gets the function applied in this application.
	 * @return The function applied.
	 */
	public Parsable getFunction() {
		return function;
	}
	
	/**
	 * Gets the argument passed to the applied function in this application.
	 * @return The argument passed to the function being applied.
	 */
	public Parsable getArgument() {
		return argument;
	}
	
	@Override
	public String toSource(int indentationLevel) {
		return String.format("%s %s",
				function.toSource(indentationLevel),
				argument.toSource(indentationLevel));
	}
}
