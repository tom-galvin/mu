package pw.usn.mu.parser;

import pw.usn.mu.tokenizer.SymbolTokenType;

/**
 * Represents a function which takes an argument and transforms it in some way.
 */
public class Function implements Parsable {
	private Identifier argumentName;
	public Parsable body;
	
	/**
	 * Initializes a new Function with the given argument identifier and function
	 * body.
	 * @param argumentName The identifier of the function argument.
	 * @param body The body of the function.
	 */
	public Function(Identifier argumentName, Parsable body) {
		this.argumentName = argumentName;
		this.body = body;
	}
	
	/**
	 * Gets the argument name of the function.
	 * @return The identifier representing the argument name.
	 */
	public Identifier getArgumentName() {
		return argumentName;
	}
	
	/**
	 * Gets the body of the function.
	 * @return The body of the function, which transforms the argument into some
	 * other value.
	 */
	public Parsable getBody() {
		return body;
	}
	
	@Override
	public String toSource(int indentationLevel) {
		if(body instanceof Function) {
			return ((Function)body).toSource(
					indentationLevel,
					String.format("%s%s ",
							argumentName.toSource(indentationLevel),
							SymbolTokenType.COMMA.getDisplayString()));
		} else {
			return toSource(indentationLevel, "");
		}
	}
	
	/**
	 * Gets the equivalent mu source code for this Function.
	 * @param indentationLevel The current indentation level at which to
	 * output the code.
	 * @param previousArguments The string representation of any arguments of
	 * parent functions, such to make the outputted code look nicer.
	 * @return Source code which, when parsed, would be identical to
	 * this function.
	 */
	public String toSource(int indentationLevel, String previousArguments) {
		String openingString = String.format("%s%s",
				SymbolTokenType.PAREN_OPEN.getDisplayString(),
				SymbolTokenType.FUNCTION_BEGIN.getDisplayString());
		String bodyString = body.toSource(indentationLevel + openingString.length());
		boolean newlineNeeded = bodyString.contains("\n");
		
		return String.format("%s%s%s%s%s%s",
				openingString,
				previousArguments,
				argumentName.toSource(indentationLevel),
				newlineNeeded ? "\n" : " ",
				bodyString,
				SymbolTokenType.PAREN_CLOSE.getDisplayString());
	}
}
