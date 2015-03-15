package pw.usn.mu.parser.binding;

import pw.usn.mu.parser.Node;
import pw.usn.mu.parser.Parser;
import pw.usn.mu.tokenizer.IdentifierToken;
import pw.usn.mu.tokenizer.Location;

/**
 * Represents a binding of a value.
 */
public class BindValueNode extends Node {
	private String valueName;
	
	/**
	 * Initializes a new BindValueNode, with the given identifier name to
	 * bind to.
	 * @param location The location of the AST node in a parsed input source.
	 * @param valueName The name of the value to bind to.
	 */
	public BindValueNode(Location location, String valueName) {
		super(location);
		this.valueName = valueName;
	}
	
	/**
	 * Gets the name of the value to bind.
	 * @return The identifier name of the value to bind.
	 */
	public String getValueName() {
		return valueName;
	}
	
	/**
	 * Parses a value binding name from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return A value binding name, as parsed from the current input.
	 */
	public static BindValueNode parse(Parser parser) {
		parser.expect(
				token -> token instanceof IdentifierToken,
				"Expected identifier in binding structure.");
		IdentifierToken token = (IdentifierToken)parser.current();
		return new BindValueNode(token.getLocation(), token.getIdentifier());
	}
}
