package pw.usn.mu.tokenizer;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * Represents a rule for ignoring matched text.
 */
public class IgnoreTokenizerRule implements TokenizerRule {
	private Pattern pattern;

	/**
	 * Initializes a new IgnoreTokenizerRule with the given pattern.
	 * @param pattern The {@link java.util.regex.Pattern Pattern} which, when matched, will be ignored by the tokenizer.
	 */
	public IgnoreTokenizerRule(Pattern pattern) {
		this.pattern = pattern;
	}

	/**
	 * Initializes a new IgnoreTokenizerRule with the given pattern.
	 * This is equivalent to using the constructor {@link IgnoreTokenizerRule#IgnoreTokenizerRule(Pattern)
	 * IgnoreTokenizerRule(Pattern.compile(regex))}.
	 * @param pattern The regular expression which, when matched, will be ignored by the tokenizer. This will be compiled
	 * to a {@link java.util.regex.Pattern Pattern}.
	 */
	public IgnoreTokenizerRule(String pattern) {
		this(Pattern.compile(pattern));
	}

	@Override
	public Token read(Location location, MatchResult result) {
		return new IgnoreToken(location);
	}

	@Override
	public Pattern getPattern() {
		return pattern;
	}
}
