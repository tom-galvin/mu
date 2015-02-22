package pw.usn.mu.parser;

import pw.usn.mu.tokenizer.Token;

/**
 * Represents a RuntimeException that occurs as a result of attempting to parse invalid program input.
 */
public class ParserException extends RuntimeException {
	private static final long serialVersionUID = -823442794160195509L;
	private Token errorToken;

	/**
	 * Initializes a new ParserException with the given message and erroneous token location.
	 * @param message The detail message of the exception.
	 * @param errorToken The token which caused the parser exception to occur.
	 */
	public ParserException(String message, Token errorToken) {
		super(message);
		this.errorToken = errorToken;
	}
	
	/**
	 * Gets the error token for this ParserException.
	 * @return The token which caused the parser exception to occur.
	 */
	public Token getErrorToken() {
		return errorToken;
	}
	
	@Override
	public String getMessage() {
		return String.format("%s (at %s)", super.getMessage(), errorToken.getLocation().toString());
	}
}
