package pw.usn.mu.analyser;

import pw.usn.mu.parser.IdentifierNode;

/**
 * Represents a RuntimeException that occurs as a result of being unable to resolve
 * an identifier in the AST.
 */
public class UnresolvedIdentifierException extends RuntimeException {
	private static final long serialVersionUID = 5005840847969461626L;
	private IdentifierNode identifier;
	
	/**
	 * Initializes a new UnresolvedIdentifierException with the given message and unresolved
	 * identifier.
	 * @param message The detail message of the exception.
	 * @param identifier The identifier that could not be resolved.
	 */
	public UnresolvedIdentifierException(String message, IdentifierNode identifier) {
		super(message);
		this.identifier = identifier;
	}
	
	/**
	 * Initializes a new UnresolvedIdentifierException with the given unresolved identifier.
	 * @param identifier The identifier that could not be resolved.
	 */
	public UnresolvedIdentifierException(IdentifierNode identifier) {
		this(
				String.format(
						"The identifier %s could not be resolved.",
						identifier.toString()),
				identifier);
	}
	
	/**
	 * Gets the identifier that could not be resolved.
	 * @return The identifier that could not be resolved.
	 */
	public IdentifierNode getIdentifier() {
		return identifier;
	}
	
	@Override
	public String getMessage() {
		return String.format("%s (at %s)", super.getMessage(), identifier.getLocation().toString());
	}
}
