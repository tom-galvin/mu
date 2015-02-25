package pw.usn.mu.parser;

import java.util.Stack;

import pw.usn.mu.tokenizer.IdentifierToken;
import pw.usn.mu.tokenizer.SymbolTokenType;
import pw.usn.mu.tokenizer.Token;

/**
 * Represents a function which takes an argument and transforms it in some way.
 */
public class Function implements Parsable, Expression {
	private Identifier argumentName;
	private Expression body;
	
	/**
	 * Initializes a new Function with the given argument identifier and function
	 * body.
	 * @param argumentName The identifier of the function argument.
	 * @param body The body of the function.
	 */
	public Function(Identifier argumentName, Expression body) {
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
	public Expression getBody() {
		return body;
	}
	
	/**
	 * Parses a function from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return An {@link Function}, as parsed from the current input.
	 */
	public static Function parse(Parser parser) {
		Stack<Identifier> arguments = new Stack<Identifier>();
		parser.expect(token -> token.isSymbolToken(SymbolTokenType.FUNCTION_BEGIN), "Expected beginning of function.");
		do {
			Token argumentToken = parser.current(1);
			if(!parser.test(token -> token instanceof IdentifierToken)) {
				throw new ParserException("Expected function argument.", argumentToken);
			}
			Identifier argumentName = Identifier.parse(parser);
			if(argumentName.getModules().length == 0) {
				arguments.push(argumentName);
			} else {
				throw new ParserException("Function argument must not be qualified.", argumentToken);
			}
		} while(parser.accept(token -> token.isSymbolToken(SymbolTokenType.COMMA)));
		Expression content = Binding.parse(parser);
		Function function = new Function(arguments.pop(), content);
		while(!arguments.empty()) {
			function = new Function(arguments.pop(), function);
		}
		return function;
	}
}
