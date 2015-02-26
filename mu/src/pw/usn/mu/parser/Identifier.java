package pw.usn.mu.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import pw.usn.mu.tokenizer.IdentifierToken;
import pw.usn.mu.tokenizer.SymbolTokenType;

/**
 * Represents an (optionally qualified) identifier in mu source code.
 */
public class Identifier implements Parsable, Expression {
	/**
	 * The symbol used to qualify identifiers with module names.
	 */
	public static final String QUALIFIER_SYMBOL = ".";
	private String[] modules;
	private String name;
	
	/**
	 * Initializes a new Identifier with the specified identifier components.
	 * @param names The components (normally separated by {@link
	 * Identifier#QUALIFIER_SYMBOL}) comprising this identifier.
	 */
	public Identifier(String... names) {
		if(names.length == 0) {
			throw new IllegalArgumentException("Must specify at least one component of identifier.");
		} else {
			modules = new String[names.length - 1];
			System.arraycopy(names, 0, modules, 0, modules.length);
			name = names[modules.length];
		}
	}
	
	/**
	 * Initializes a new Identifier with the specified identifier string.
	 * @param identifier The identifier string, as it appears in the source
	 * code, representing this identifier.
	 */
	public Identifier(String identifier) {
		this(identifier.split(Pattern.quote(QUALIFIER_SYMBOL)));
	}
	
	/**
	 * Gets the modules qualifying this identifier.
	 * @return An array of the nested module names qualifying this identifier.
	 * For example, given an identifier {@code mu:test:func}, this would return
	 * an array {@code ["mu", "test"]}.
	 */
	public String[] getModules() {
		return modules;
	}
	
	/**
	 * Gets the name of this identifier.
	 * @return The name of this identifier.
	 * For example, given an identifier {@code mu:test:func}, this would return
	 * a string {@code "func"}.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Parses an identifier from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return An identifier, as parsed from the current input.
	 */
	public static Identifier parse(Parser parser) {
		List<String> identifierParts = new ArrayList<String>();
		do {
			parser.expect(token -> token instanceof IdentifierToken, "Identifier expected.");
			identifierParts.add(((IdentifierToken)parser.current()).getIdentifier());
		} while(parser.accept(token -> token.isSymbolToken(SymbolTokenType.NAMESPACE_QUALIFIER)));
		String[] partsArray = new String[identifierParts.size()];
		identifierParts.toArray(partsArray);
		return new Identifier(partsArray);
	}
	
	@Override
	public int hashCode() {
		int hashCode = 0x633c0b24, prevHashCode = 0x6902a10c;
		for(int i = 0; i < modules.length; i++) {
			int currentHashCode = modules[i].hashCode();
			hashCode ^= currentHashCode * i + (prevHashCode << ((i + currentHashCode) % 3));
			prevHashCode = currentHashCode;
		}
		int currentHashCode = name.hashCode();
		hashCode ^= currentHashCode + modules.length ^ (prevHashCode);
		return hashCode;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof Identifier) {
			Identifier identifier = (Identifier)obj;
			if(!identifier.name.equals(this.name)) {
				return false;
			}
			if(identifier.modules.length != this.modules.length) {
				return false;
			}
			for(int i = 0; i < this.modules.length; i++) {
				if(!this.modules[i].equals(identifier.modules[i])) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
