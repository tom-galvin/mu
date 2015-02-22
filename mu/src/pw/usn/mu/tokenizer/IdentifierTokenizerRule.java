package pw.usn.mu.tokenizer;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * Represents a tokenizer rule that reads an identifier token.
 */
public class IdentifierTokenizerRule implements TokenizerRule {
	private Pattern pattern;
	
	public IdentifierTokenizerRule() {
		pattern = Pattern.compile("[A-Za-z_][A-Za-z0-9_]*");
	}

	@Override
	public Token read(Location location, MatchResult result) {
		return new IdentifierToken(location, result.group());
	}

	@Override
	public Pattern getPattern() {
		return pattern;
	}
}
