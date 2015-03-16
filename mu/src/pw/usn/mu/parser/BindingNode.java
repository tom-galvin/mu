package pw.usn.mu.parser;

import pw.usn.mu.parser.binding.BindTupleNode;
import pw.usn.mu.tokenizer.Location;
import pw.usn.mu.tokenizer.SymbolTokenType;

/**
 * Represents a binding, wherein a value is bound to a name.
 */
public class BindingNode extends Node {
	private Node bindingStructure;
	private Node value;
	private Node content;
	
	/**
	 * Initializes a new BindingNode with the given name and value, and content which
	 * can refer to the bound value by the given name.
	 * @param location The location of the AST node in a parsed input source.
	 * @param name The structure of the decomposing binding.
	 * @param value The value to bind.
	 * @param content The content of the binding.
	 */
	public BindingNode(Location location, Node bindingStructure, Node value, Node content) {
		super(location);
		this.bindingStructure = bindingStructure;
		this.value = value;
		this.content = content;
	}

	/**
	 * Gets the structure of the binding, describing how to decompose the value bound.
	 * @return The structure of the binding.
	 */
	public Node getBindingStructure() {
		return bindingStructure;
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
	 * refer to the identifier(s) bound in this binding to access the values bound.
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
		try {
			/* this *might* be the structure of a binding so create a lookahead parser to 
			 * tentatively step ahead */
			Parser lookaheadParser = parser.copyState(); /* copy the parser state */
			Node bindingStructure = BindTupleNode.parse(lookaheadParser);
			/* OK, we've parsed the binding structure. Now let's see if there's a <- after it */
			if(lookaheadParser.accept(token -> token.isSymbolToken(SymbolTokenType.BIND))) {
				/* there is, so parse the binding */
				Node value = Node.parse(lookaheadParser);
				lookaheadParser.expect(token -> token.isSymbolToken(SymbolTokenType.SEPARATOR), "Expected semicolon to end binding.");
				Node content = parse(lookaheadParser);
				parser.fastForward(lookaheadParser); // bring the current parser up to the level of the look-ahead parser
				return new BindingNode(bindingStructure.getLocation(), bindingStructure, value, content);
			} else {
				/* this looks like a valid binding but isn't so parse normally */
				return Node.parse(parser);
			}
		} catch(ParserException e) {
			/* not a binding structure so parse as normal */
			return Node.parse(parser);
		}
	}
}
