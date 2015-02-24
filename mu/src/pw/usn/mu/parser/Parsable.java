package pw.usn.mu.parser;

/**
 * Represents something that has been parsed from mu source code, such
 * that it can be reconstructed back into equivalent code.
 */
public interface Parsable {
	/**
	 * Gets the equivalent mu source code for this element.
	 * @param indentationLevel The current indentation level at which to
	 * output the code.
	 * @return Source code which, when parsed, would be identical to
	 * this object.
	 */
	public String toSource(int indentationLevel);
	
	/**
	 * Gets a string used for indenting generated code.
	 * @param indentationLevel The number of space to return.
	 * @return A string of {@code indentationLevel} spaces.
	 */
	public static String indent(int indentationLevel) {
		char[] data = new char[indentationLevel];
		for(int i = 0; i < data.length; i++) {
			data[i] = ' ';
		}
		return String.valueOf(data);
	}
}
