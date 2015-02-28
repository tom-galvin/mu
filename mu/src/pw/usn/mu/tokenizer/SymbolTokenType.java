package pw.usn.mu.tokenizer;

import java.util.regex.Pattern;

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
	 * Represents an opening square bracket ({@code [}), for sequence literals or types.
	 */
	SEQUENCE_OPEN("["),
	/**
	 * Represents a closing square bracket ({@code ]}), for sequence literals or types.
	 */
	SEQUENCE_CLOSE("]"),
	/**
	 * Represents a namespace qualifier (with the symbol {@link
	 * Identifier#QUALIFIER_SYMBOL}), used for specifying the parent module
	 * that contains an identifier.
	 */
	NAMESPACE_QUALIFIER(String.valueOf(Identifier.QUALIFIER_SYMBOL)),
	/**
	 * Represents the starting sigil for a function ({@code \}),
	 * to be followed by an identifier name for an argument.
	 */
	FUNCTION_BEGIN("\\"),
	/**
	 * Represents the starting sigil for a module ({@code @}),
	 * to be followed by a list of definitions.
	 */
	MODULE_BEGIN("@"),
	/**
	 * Represents the comma symbol ({@code ,}).
	 */
	COMMA(","),
	/**
	 * Represents the back-arrow ({@code <-}) used to create a binding.
	 */
	BINDING("<-"),
	/**
	 * Represents the forward-arrow ({@code ->}) used to declare the beginning of
	 * a function body, or represent a function in a type expression.
	 */
	FUNCTION("->"),
	/**
	 * Represents a type specificaton sigil ({@code :}) on a generic type.
	 */
	SPECIFICATION(":"),
	/**
	 * Represents the semicolon ({@code ;}) used to end a declaration, such
	 * as a binding or match sub-expression.
	 */
	END_DECLARATION(";");
	
	private String display;
	
	/**
	 * Create a new SymbolTokenType with the specified display string and token
	 * level.
	 * @param display The display string of the given symbol token type.
	 */
	private SymbolTokenType(String display) {
		this.display = display;
	}
	
	/**
	 * Gets a pattern for this symbol token type during tokenization.
	 * @return A pattern that matches this symbol token type in mu source code.
	 */
	public Pattern getPattern() { 
		return Pattern.compile(Pattern.quote(display));
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
