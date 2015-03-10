package pw.usn.mu.analyser;

import pw.usn.mu.tokenizer.Location;

/**
 * Represents a RuntimeException that occurs as a result of an analysis error in a
 * mu program during analysis.
 */
public class AnalysisErrorException extends RuntimeException {
	private static final long serialVersionUID = 5005840847969461626L;
	private Location location;
	
	/**
	 * Initializes a new AnalysisErrorExceptino with the given message and unresolved
	 * identifier.
	 * @param message The detail message of the exception.
	 * @param location The location of the analysis error in a source. Ideally this should
	 * be as intuitively close to the location of the error as possible, but this will
	 * depend on the type of AST node and error type.
	 */
	public AnalysisErrorException(String message, Location location) {
		super(message);
		this.location = location;
	}
	
	/**
	 * Gets the location of the error in a source.
	 * @return The location of this analysis error in a mu source.
	 */
	public Location getLocation() {
		return location;
	}
	
	@Override
	public String getMessage() {
		return String.format("%s (at %s)", super.getMessage(), location.toString());
	}
}
