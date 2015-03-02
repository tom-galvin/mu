package pw.usn.mu.parser;

import pw.usn.mu.tokenizer.IdentifierToken;
import pw.usn.mu.tokenizer.SymbolTokenType;
import pw.usn.mu.tokenizer.Token;

/**
 * Represents a binding, wherein a value is bound to a name.
 */
public class BindingNode extends Node {
	private String name;
	private Node value;
	private Node content;
	
	/**
	 * Initializes a new Binding with the given name and value, and content which
	 * can refer to the bound value by the given name.
	 * @param name The name of the newly-bound value.
	 * @param value The value to bind.
	 * @param content The content of the binding.
	 */
	public BindingNode(String name, Node value, Node content) {
		this.name = name;
		this.value = value;
		this.content = content;
	}

	/**
	 * Gets the name of the binding.
	 * @return The name to which the value is bound in this binding. For example, if
	 * the value {@code "hello"} is bound to the name {@code myIdentifier}, then this
	 * function returns {@code "myIdentifier"}.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Gets the value bound in this binding.
	 * @return The value bound to the name returned by {@link BindingNode#getName()
	 * getName()} in this binding.
	 */
	public Node getValue() {
		return value;
	}
	
	/**
	 * Gets the content of this binding.
	 * @return The content of this binding. Any expressions within {@code content} can
	 * refer to the identifier returned by {@link BindingNode#getName() getName()} to access
	 * the value bound to the name with this binding.
	 */
	public Node getContent() {
		return content;
	}
	
	/**
	 * Parses a variable number of bindings from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return An expression, as parsed from the current input. This method must
	 * return an {@link Node} rather than a concrete {@link BindingNode} as
	 * there may be zero bindings made, in which case nothing is bound to anything.
	 */
	public static Node parse(Parser parser) {
		/* This bit here is an unbounded lookahead parser
		 * it does this by cloning the parser enumerator state, which can be
		 * returned to if there is no binding arrow (<-) after the potential binding target
		 */
		if(parser.test(token -> token instanceof IdentifierToken)) {
			/* If the expression starts with an IdentifierToken, it *might* be the target
			 * of a binding, so look ahead of the Identifier to check for it
			 */
			Parser lookaheadParser = parser.copyState(); // copy the parser state
			Token identifierToken = lookaheadParser.current(1); // get the identifier token for error reporting
			IdentifierNode identifier = IdentifierNode.parse(lookaheadParser);
			if(identifier.isUnqualified()) {
				/* OK, we've parsed the identifier. Now let's see if there's a <- after it */
				if(lookaheadParser.accept(token -> token.isSymbolToken(SymbolTokenType.BIND))) {
				/* there is, so parse the binding */
					Node value = Node.parse(lookaheadParser);
					lookaheadParser.expect(token -> token.isSymbolToken(SymbolTokenType.SEPARATOR), "Expected semicolon to end binding.");
					Node content = parse(lookaheadParser);
					parser.fastForward(lookaheadParser); // bring the current parser up to the level of the look-ahead parser
					return new BindingNode(identifier.getName(), value, content);
				}
			} else {
				/* binding target can't be qualified with module names so throw a ParserException */
				throw new ParserException("Binding target must not be qualified.", identifierToken);
			}
			/* this wasn't actually a binding, so discard the LookaheadParser and carry on as before */
		}
		return Node.parse(parser);
	}
}
