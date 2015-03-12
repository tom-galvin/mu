package pw.usn.mu.tokenizer;

import java.util.regex.Pattern;

import pw.usn.mu.parser.LiteralStringNode;
import pw.usn.mu.parser.LiteralSymbolNode;

/**
 * Represents a token containing a symbol literal.
 */
public class LiteralSymbolToken extends Token {
	private String value;
	
	/**
	 * Initializes a new LiteralSymbolToken with the given value and location in a source.
	 * @param location The location of the token.
	 * @param value The value of this symbol literal.
	 */
	public LiteralSymbolToken(Location location, String value) {
		super(location);
		this.value = value;
	}
	
	/**
	 * Gets the value of this symbol literal.
	 * @return The value as a {@link String} that this LiteralSymbolToken represents.
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Returns a pattern used for matching symbol literals. The first group (group 1) in this
	 * pattern will contain the content of the symbol literal.
	 * @return A pattern that can be used by a tokenizer rule for matching literal symbol tokens.
	 */
	public static Pattern[] getLiteralSymbolPatterns() {
		String stringDelimiterPattern = Pattern.quote(LiteralStringNode.LITERAL_STRING_DELIMITER);
		String symbolDelimiterPattern = Pattern.quote(LiteralSymbolNode.LITERAL_SYMBOL_DELIMITER);
		return new Pattern[] {
				Pattern.compile(symbolDelimiterPattern + stringDelimiterPattern + "(.*?)" + stringDelimiterPattern,
						Pattern.DOTALL),
				Pattern.compile(symbolDelimiterPattern + "([a-zA-Z0-9_]+)",
						Pattern.DOTALL)
		};
	}
	
	@Override
	public String getInformation() {
		return String.format("%s%s%s%s",
				LiteralSymbolNode.LITERAL_SYMBOL_DELIMITER,
				LiteralStringNode.LITERAL_STRING_DELIMITER,
				value,
				LiteralStringNode.LITERAL_STRING_DELIMITER);
	}
}
