package pw.usn.mu.parser.type;

import pw.usn.mu.parser.Identifier;
import pw.usn.mu.parser.Parser;

/**
 * Represents a reference (with an identifier) to an already defined type.
 */
public class DefinedType implements Type {
	private Identifier identifier;
	
	/**
	 * Initializes a new DefinedType, with the given identifier to which this defined
	 * type refers to.
	 * @param identifier The identifier of the defined type which this DefinedType
	 * refers to.
	 */
	public DefinedType(Identifier identifier) {
		this.identifier = identifier;
	}
	
	/**
	 * Gets the identifier which this defined type refers to.
	 * @return The identifier of this defined type.
	 */
	public Identifier getIdentifier() {
		return identifier;
	}
	
	/**
	 * Parses a defined type reference from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return A {@link DefinedType}, as parsed from the current input.
	 */
	public static DefinedType parse(Parser parser) {
		return new DefinedType(Identifier.parse(parser));
	}
}
