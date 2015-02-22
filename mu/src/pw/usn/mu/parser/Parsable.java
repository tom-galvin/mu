package pw.usn.mu.parser;

/**
 * Represents something that has been parsed from mu source code, such
 * that it can be reconstructed back into equivalent code.
 */
public interface Parsable {
	/**
	 * Gets the equivalent mu source code for this element.
	 * @return Source code which, when parsed, would be identical to
	 * this object.
	 */
	public String toSource();
}
