package pw.usn.mu.tokenizer;

import pw.usn.mu.parser.Identifier;

/**
 * Represents the type of symbol represented by a {@link SymbolToken}.
 */
public enum SymbolTokenType {
	/**
	 * Represents an opening parenthesis ({@code (}).
	 */
	PAREN_OPEN("("),
	/**
	 * Represents a closing parenthesis ({@code )}).
	 */
	PAREN_CLOSE(")"),
	/**
	 * Represents a namespace qualifier (with the symbol {@link
	 * Identifier#QUALIFIER_SYMBOL}), used for specifying the parent module
	 * that contains an identifier.
	 */
	NAMESPACE_QUALIFIER(String.valueOf(Identifier.QUALIFIER_SYMBOL)),
	/**
	 * Represents the starting sigil for a function ({@code \}), to be
	 * followed by an identifier name for a new variable.
	 */
	FUNCTION_BEGIN("\\"),
	/**
	 * Represents the comma symbol ({@code ,}).
	 */
	COMMA(",");
	
	private String display;
	
	/**
	 * Create a new SymbolTokenType with the specified display string.
	 * @param display The display string of the given symbol token type.
	 */
	private SymbolTokenType(String display) {
		this.display = display;
	}
	
	/**
	 * Gets the display string for this token type.
	 * @return A string containing this symbol token type, as it would appear in
	 * a mu source code file.
	 */
	public String getDisplayString() {
		return display;
	}
}
