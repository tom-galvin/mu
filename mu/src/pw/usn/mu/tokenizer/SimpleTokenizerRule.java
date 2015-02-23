package pw.usn.mu.tokenizer;

import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * Represents a tokenizer rule that produces a token from a regular expression
 * pattern match result. As a rule of thumb, use this class only when the logic for
 * producing a {@link Token} object from a match result is simple and limited. If
 * extended processing is required to create a token from the match result, consider
 * creating a dedicated tokenizer rule class that implements {@link TokenizerRule}.
 */
public class SimpleTokenizerRule implements TokenizerRule {
	private Pattern pattern;
	private TokenFactory factory;
	
	/**
	 * Initializes a new SimpleTokenizerRule with the given pattern and
	 * token factory.
	 * @param pattern The pattern which, when read in the tokenizer, will invoke
	 * this tokenizer rule in order to create a {@link Token} object.
	 * @param factory The factory which produces a {@link Token} from the match
	 * result of {@code pattern}.
	 */
	public SimpleTokenizerRule(Pattern pattern, TokenFactory factory) {
		this.pattern = pattern;
		this.factory = factory;
	}
	
	/**
	 * Initializes a new SimpleTokenizerRule with the given pattern and
	 * token factory. This is equivalent to using the other constructor
	 * {@link SimpleTokenizerRule#SimpleTokenizerRule(Pattern, TokenFactory)
	 * SimpleTokenizerRule(Pattern.compile(regex), factory)}.
	 * @param regex The regular expression which, when read in the tokenizer, will
	 * invoke this tokenizer rule in order to create a {@link Token} object. This
	 * will be compiled to a {@link Pattern} object.
	 * @param factory The factory which produces a {@link Token} from the match
	 * result of {@code pattern}.
	 */
	public SimpleTokenizerRule(String regex, TokenFactory factory) {
		this(Pattern.compile(regex), factory);
	}

	@Override
	public Token read(Location location, MatchResult result) {
		return factory.create(location, result);
	}

	@Override
	public Pattern getPattern() {
		return pattern;
	}
}
