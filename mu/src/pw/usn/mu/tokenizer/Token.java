package pw.usn.mu.tokenizer;

import pw.usn.mu.Source;

/**
 * Represents a single syntax token in a Mu source file.
 */
public abstract class Token {
	private Location location;
	
	/**
	 * Initializes a new Token read from the given {@link Source} at the given location.
	 * @param location The location of the token.
	 * the start of each row is column 0.
	 */
	public Token(Location location) {
		this.location = location;
	}
	
	/**
	 * Gets the location of the token.
	 * @return The location of the token in a tokenized input source.
	 */
	public final Location getLocation() {
		return location;
	}

	/**
	 * Gets token-specific information used for formatting.
	 * @return A token-specific information string about some attribute of the token that was read.
	 * For example, an identifier token might represent the identifier that was read.
	 */
	public String getInformation() {
		return null;
	}
	
	@Override
	public String toString() {
		String information = getInformation();
		if(information == null) {
			return String.format("[%s %s]", location.toString(), getClass().getSimpleName());
		} else {
			return String.format("[%s %s=%s]", location.toString(), getClass().getSimpleName(), information);
		}
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}
}
