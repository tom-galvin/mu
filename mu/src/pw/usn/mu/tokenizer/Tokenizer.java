package pw.usn.mu.tokenizer;

import java.util.ArrayList;
import java.util.List;

import pw.usn.mu.Source;

/**
 * Represents a tokenizer for mu source code.
 */
public class Tokenizer {
	private List<TokenizerRule> rules;
	
	/**
	 * Initialize a new Tokenizer with an empty set of rules.
	 */
	public Tokenizer() {
		rules = new ArrayList<TokenizerRule>();
	}
	
	/**
	 * Adds a new tokenization rule.
	 * @param rule The {@link TokenizerRule} to add to this tokenizer.
	 */
	public void addRule(TokenizerRule rule) {
		rules.add(rule);
	}
	
	/**
	 * Tokenizes source code from a {@link Source}.
	 * @param source The source code to tokenize.
	 * @return An array of {@link Token}s, representing all non-ignored tokens in {@code source} after
	 * tokenizing.
	 * @throws TokenizerException when the expression cannot be tokenized.
	 */
	public Token[] tokenize(Source source) {
		List<Token> tokens = new ArrayList<Token>();
		while(!source.endOfSource()) {
			boolean tokenEncountered = false;
			for(TokenizerRule rule : rules) {
				Token token = source.readToken(rule);
				
				if(token != null) {
					if(!token.ignore()) {
						tokens.add(token);
					}
					tokenEncountered = true;
					break;
				}
			}
			
			if(!tokenEncountered) {
				throw new TokenizerException("Unrecognized token.", source.getLocation(), source.getRemainderOfLine());
			}
		}
		Token[] tokenArray = new Token[tokens.size()];
		tokens.toArray(tokenArray);
		return tokenArray;
	}
}
