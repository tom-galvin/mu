package pw.usn.mu.tokenizer;

/**
 * Represents a token containing an identifier for a function, module or value.
 */
public class IdentifierToken extends Token {
	private String identifier;
	
	/**
	 * Initializes a new IdentifierToken with the given location and identifier string.
	 * @param location The location of the token.
	 * @param identifier The identifier string this token represents.
	 */
	public IdentifierToken(Location location, String identifier) {
		super(location);
		this.identifier = identifier;
	}
	
	/**
	 * Gets the identifier string this token represents.
	 * @return The string identifying a function, module or value that this token represents.
	 */
	public String getIdentifier() {
		return identifier;
	}
	
	@Override
	public String getInformation() {
		return getIdentifier();
	}
}
