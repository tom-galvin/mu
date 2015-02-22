package pw.usn.mu.tokenizer;

import java.util.ArrayList;
import java.util.List;

import pw.usn.mu.Source;

public class Tokenizer {
	private List<TokenizerRule> rules;
	
	public Tokenizer() {
		rules = new ArrayList<TokenizerRule>();
	}
	
	public void addRule(TokenizerRule rule) {
		rules.add(rule);
	}
	
	public Token[] tokenize(Source source) {
		List<Token> tokens = new ArrayList<Token>();
		while(!source.endOfSource()) {
			boolean tokenEncountered = false;
			for(TokenizerRule rule : rules) {
				Token token = source.readToken(rule);
				
				if(token != null) {
					tokens.add(token);
					tokenEncountered = true;
					break;
				}
			}
			
			if(!tokenEncountered) {
				throw new TokenizerException("No token was matched.", source.getLocation(), source.getRemainderOfLine());
			}
		}
		Token[] tokenArray = new Token[tokens.size()];
		tokens.toArray(tokenArray);
		return tokenArray;
	}
}
