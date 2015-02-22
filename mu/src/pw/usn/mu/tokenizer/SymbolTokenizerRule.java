package pw.usn.mu.tokenizer;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * Represents a tokenizer rule for matching a parameterless symbol.
 */
public class SymbolTokenizerRule implements TokenizerRule {
	private Pattern pattern;
	private SymbolTokenType type;
	
	/**
	 * Initializes a new SymbolTokenizerRule with the given matching pattern and the type of the symbol token to return.
	 * @param pattern The {@link java.util.regex.Pattern Pattern} to match in order to read a token of type {@code T}.
	 * @param type The type {@link SymbolToken} to return.
	 */
	public SymbolTokenizerRule(Pattern pattern, SymbolTokenType type) {
		this.pattern = pattern;
		this.type = type;
	}
	
	/**
	 * Initializes a new SymbolTokenizerRule with the given matching regular expression and the type of the symbol token to return.
	 * This is equivalent to using the constructor {@link SymbolTokenizerRule#SymbolTokenizerRule(Pattern, SymbolTokenType)
	 * SymbolTokenizerRule(Pattern.compile(regex), type)}.
	 * @param regex The regular expression to match in order to read a symbol token of the given type.
	 * This is compiled to a {@link java.util.regex.Pattern Pattern}.
	 * @param type The type {@link SymbolToken} to return.
	 */
	public SymbolTokenizerRule(String regex, SymbolTokenType type) {
		this(Pattern.compile(regex), type);
	}

	@Override
	public final SymbolToken read(Location location, MatchResult result) {
		return new SymbolToken(location, type);
	}

	@Override
	public final Pattern getPattern() {
		return pattern;
	}
}
