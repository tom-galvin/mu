package pw.usn.mu.tokenizer;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import pw.usn.mu.Source;

/**
 * Represents a rule that determines whether a token can be read (and creates
 * that token), or if a token cannot be read, from the result of a string containing
 * the token.
 */
public interface TokenizerRule {
	/**
	 * Gets a token from the given match result, using this rule.
	 * @param source The location from which the token has been read.
	 * @param result The match result containing the matched data in the input source. This is the
	 * result of matching with the {@link Pattern} returned by {@link TokenizerRule#getPattern()}.
	 * @return The token that was read.
	 */
	public Token read(Location location, MatchResult result);
	
	/**
	 * Gets a pattern used to determine whether this specific rule can be used at the
	 * current position of a {@link pw.usn.mu.Source Source}.
	 * @return The pattern that a {@link Source} will use to determine if this rule can be used.
	 */
	public Pattern getPattern();
}
