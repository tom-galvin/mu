package pw.usn.mu.tokenizer;

import java.util.regex.MatchResult;

import pw.usn.mu.Source;

/**
 * Represents a factory for creating {@link Token}s from a tokenizer pattern
 * match result.
 */
@FunctionalInterface
public interface TokenFactory {
	/**
	 * Creates a factory-specific {@link Token} from the given pattern
	 * match result and location in the source.
	 * @param location The location in a {@link Source} from which {@code result}
	 * was matched.
	 * @param result The pattern match result containing the string in the source
	 * that matched the pattern of a tokenizer rule.
	 * @return Returns a token based on {@code result}.
	 */
	public Token create(Location location, MatchResult result);
}
