package pw.usn.mu.parser;

import java.util.Stack;

import pw.usn.mu.tokenizer.IdentifierToken;
import pw.usn.mu.tokenizer.Location;
import pw.usn.mu.tokenizer.SymbolTokenType;
import pw.usn.mu.tokenizer.Token;

/**
 * Represents a function which takes an argument and transforms it in some way.
 */
public class FunctionNode extends Node {
	private String argumentName;
	private Node body;
	
	/**
	 * Initializes a new Function with the given argument identifier and function
	 * body.
	 * @param location The location of the AST node in a parsed input source.
	 * @param argumentName The name of the function argument.
	 * @param body The body of the function.
	 */
	public FunctionNode(Location location, String argumentName, Node body) {
		super(location);
		this.argumentName = argumentName;
		this.body = body;
	}
	
	/**
	 * Gets the argument name of the function.
	 * @return The argument name.
	 */
	public String getArgumentName() {
		return argumentName;
	}
	
	/**
	 * Gets the body of the function.
	 * @return The body of the function, which transforms the argument into some
	 * other value.
	 */
	public Node getBody() {
		return body;
	}
	
	/**
	 * Parses a function from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return An {@link FunctionNode}, as parsed from the current input.
	 */
	public static FunctionNode parse(Parser parser) {
		Stack<String> arguments = new Stack<String>();
		Location functionLocation = parser.expect(
				token -> token.isSymbolToken(SymbolTokenType.FUNCTION_DECLARE), 
				"Expected beginning of function.");
		if(parser.test(token -> token.isSymbolToken(SymbolTokenType.SWITCH_DECLARE))) {
			Token switchExpressionToken = parser.current(2);
			SwitchNode switchBody = SwitchNode.parse(parser);
			Node switchBodyExpression = switchBody.getExpression();
			if(switchBodyExpression instanceof IdentifierNode) {
				IdentifierNode argumentName = (IdentifierNode)switchBodyExpression;
				if(argumentName.isUnqualified()) {
					return new FunctionNode(functionLocation, argumentName.getName(), switchBody);
				} else {
					throw new ParserException("Switch function argument must not be qualified.", switchExpressionToken);
				}
			} else {
				throw new ParserException("Switch function argument must be a non-qualified identifier.", switchExpressionToken);
			}
		}
		do {
			Token argumentToken = parser.current(1);
			IdentifierNode argumentName = IdentifierNode.parse(parser);
			if(argumentName.isUnqualified()) {
				arguments.push(argumentName.getName());
			} else {
				throw new ParserException("Function argument must not be qualified.", argumentToken);
			}
		} while(parser.test(token -> token instanceof IdentifierToken));
		parser.expect(token -> token.isSymbolToken(SymbolTokenType.FUNCTION_BEGIN), "Expected forward arrow at end of argument list.");
		Node content = BindingNode.parse(parser);
		FunctionNode function = new FunctionNode(functionLocation, arguments.pop(), content);
		while(!arguments.empty()) {
			function = new FunctionNode(functionLocation, arguments.pop(), function);
		}
		return function;
	}
}
