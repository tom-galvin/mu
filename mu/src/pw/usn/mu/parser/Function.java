package pw.usn.mu.parser;

import java.util.Stack;

import pw.usn.mu.parser.type.Type;
import pw.usn.mu.tokenizer.IdentifierToken;
import pw.usn.mu.tokenizer.SymbolTokenType;
import pw.usn.mu.tokenizer.Token;

/**
 * Represents a function which takes an argument and transforms it in some way.
 */
public class Function implements Parsable, Expression {
	private Identifier argumentName;
	private Expression body;
	private Type argType, valType;
	
	/**
	 * Initializes a new Function with the given argument identifier and function
	 * body, and type specifiers for the function argument and return value.
	 * @param argumentName The identifier of the function argument.
	 * @param body The body of the function.
	 * @param argType The type of the function argument, or {@code null} for unspecified.
	 * @param valType The type of the function return value, or {@code null} for unspecified.
	 */
	public Function(Identifier argumentName, Expression body, Type argType, Type valType) {
		this.argumentName = argumentName;
		this.body = body;
		this.argType = argType;
		this.valType = valType;
	}
	
	/**
	 * Initializes a new Function with the given argument identifier and function
	 * body.
	 * @param argumentName The identifier of the function argument.
	 * @param body The body of the function.
	 */
	public Function(Identifier argumentName, Expression body) {
		this(argumentName, body, null, null);
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
	 * Gets the type of the function argument.
	 * @return The type of the function argument, or {@code null} if the function argument
	 * has no specified type.
	 */
	public Type getArgumentType() {
		return argType;
	}
	
	/**
	 * Gets the type of the function return value.
	 * @return The type of the function return value, or {@code null} if the function has
	 * no specified return type.
	 */
	public Type getReturnValurType() {
		return valType;
	}
	
	/**
	 * Parses a function from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return An {@link Function}, as parsed from the current input.
	 */
	public static Function parse(Parser parser) {
		Stack<Identifier> arguments = new Stack<Identifier>();
		Stack<Type> types = new Stack<Type>();
		Type returnType = null;
		parser.expect(token -> token.isSymbolToken(SymbolTokenType.FUNCTION_BEGIN), "Expected beginning of function.");
		if(parser.accept(token -> token.isSymbolToken(SymbolTokenType.PAREN_OPEN))) {
			returnType = Type.parse(parser);
			parser.expect(token -> token.isSymbolToken(SymbolTokenType.PAREN_CLOSE), "Expected closing parenthesis in type specification.");
		}
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
			if(!parser.test(token -> token.isSymbolToken(SymbolTokenType.COMMA))) {
				types.push(Type.parse(parser));
			} else {
				types.push(null);
			}
		} while(parser.accept(token -> token.isSymbolToken(SymbolTokenType.COMMA)));
		parser.expect(token -> token.isSymbolToken(SymbolTokenType.FUNCTION), "Expected forward arrow at end of argument list.");
		Expression content = Binding.parse(parser);
		Function function = new Function(arguments.pop(), content);
		while(!arguments.empty()) {
			function = new Function(arguments.pop(), function, types.pop(), returnType);
			returnType = null;
		}
		return function;
	}
}
