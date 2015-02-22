package pw.usn.mu.tokenizer;

/**
 * Represents a token for a data-less symbol.
 */
public class SymbolToken extends Token {
	private SymbolTokenType type;
	
	/**
	 * Initializes a new SymbolToken with the given location and identifier string.
	 * @param location The location of the token.
	 * @param type The type of this symbol token.
	 */
	public SymbolToken(Location location, SymbolTokenType type) {
		super(location);
		this.type = type;
	}
	
	/**
	 * Gets the type of symbol represented by this SymbolToken.
	 * @return A {@link SymbolTokenType} representing the type of this symbol token.
	 */
	public SymbolTokenType getType() {
		return type;
	}
	
	@Override
	public String getInformation() {
		return type.toString();
	}
}
