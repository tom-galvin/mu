package pw.usn.mu.tokenizer;

/**
 * Represents a RuntimeException that occurs as a result of attempting to tokenize malformed source code.
 */
public class TokenizerException extends RuntimeException {
	private static final long serialVersionUID = -8471963086788073873L;
	private String near;
	private Location location;

	/**
	 * Initializes a new TokenizerException with the given message and location.
	 * @param message The detail message of the exception.
	 * @param location The location where the exception occurred.
	 * @param near Some text (typically the nearest line) where the error occurred. The value
	 * of this may depend on the type of source.
	 */
	public TokenizerException(String message, Location location, String near) {
		super(message);
		this.near = near;
		this.location = location;
	}

	/**
	 * Initializes a new TokenizerException with the given message and location.
	 * @param message The detail message of the exception.
	 * @param location The location where the exception occurred.
	 */
	public TokenizerException(String message, Location location) {
		this(message, location, null);
	}
	
	/**
	 * Gets the location at which the exception occurred.
	 * @return The location where the exception occurred.
	 */
	public Location getLocation() {
		return location;
	}
	
	@Override
	public String getMessage() {
		if(near == null) {
			return String.format("%s (at %s)", super.getMessage(), location.toString());
		} else {
			return String.format("%s (at %s, near %s)", super.getMessage(), location.toString(), near);
		}
	}
}
