package pw.usn.mu.tokenizer;

/**
 * Represents a token indicating matched data that should be ignored by the tokenizer.
 */
public final class IgnoreToken extends Token {
	/**
	 * Initializes a new IgnoreToken with the given location.
	 * @param location The location of the token.
	 */
	public IgnoreToken(Location location) {
		super(location);
	}
}
