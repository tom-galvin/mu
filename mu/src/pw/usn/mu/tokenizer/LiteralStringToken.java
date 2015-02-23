package pw.usn.mu.tokenizer;

import java.util.regex.Pattern;

import pw.usn.mu.parser.LiteralString;

/**
 * Represents a token containing a string literal.
 */
public class LiteralStringToken extends Token {
	private String value;
	
	/**
	 * Initializes a new LiteralStringToken with the given value and location in a source.
	 * @param location The location of the token.
	 * @param value The value of this string literal.
	 */
	public LiteralStringToken(Location location, String value) {
		super(location);
		this.value = value;
	}
	
	/**
	 * Gets the value of this string literal.
	 * @return The value as a {@link String} that this LiteralStringToken represents.
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Returns a pattern used for matching string literals. The first group (group 1) in this
	 * pattern will contain the content of the string literal.
	 * @return A pattern that can be used by a tokenizer rule for matching literal string tokens.
	 */
	public static Pattern getLiteralStringPattern() {
		String stringDelimiterPattern = Pattern.quote(LiteralString.LITERAL_STRING_DELIMITER);
		return Pattern.compile(
				stringDelimiterPattern + "(.*?)" + stringDelimiterPattern,
				Pattern.DOTALL);
	}
	
	@Override
	public String getInformation() {
		return String.format("%s%s%s",
				LiteralString.LITERAL_STRING_DELIMITER,
				value,
				LiteralString.LITERAL_STRING_DELIMITER);
	}
}
