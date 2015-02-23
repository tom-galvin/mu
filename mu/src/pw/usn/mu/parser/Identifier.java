package pw.usn.mu.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import pw.usn.mu.tokenizer.IdentifierToken;
import pw.usn.mu.tokenizer.SymbolTokenType;

/**
 * Represents an (optionally qualified) identifier in mu source code.
 */
public class Identifier implements Parsable {
	/**
	 * The symbol used to qualify identifiers with module names.
	 */
	public static final String QUALIFIER_SYMBOL = ":";
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

	@Override
	public String toSource() {
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < modules.length; i++) {
			builder.append(modules[i]);
			builder.append(QUALIFIER_SYMBOL);
		}
		builder.append(name);
		return builder.toString();
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
}
