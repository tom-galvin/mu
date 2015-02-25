package pw.usn.mu.tokenizer;

/**
 * A factory for creating mu language tokenizers.
 */
public class DefaultTokenizerFactory implements TokenizerFactory {
	@Override
	public Tokenizer create() {
		Tokenizer tokenizer = new Tokenizer();

		tokenizer.addRule(new IgnoreTokenizerRule(
				"(?s)/\\*.*?\\*/")); // multi line comment
		tokenizer.addRule(new IgnoreTokenizerRule(
				"(?m)//.*?$")); // single line comment
		
		tokenizer.addRule(new SimpleTokenizerRule(
				"[A-Za-z_][A-Za-z0-9_]*",
				(loc, res) -> new IdentifierToken(loc, res.group())));
		
		addLiteralRules(tokenizer);
		addSymbolRules(tokenizer);
		
		tokenizer.addRule(new SimpleTokenizerRule(
				"[\\Q&|^#<>=$+-:/*%!`\\E][\\Q&|^#<>=$+-:/*%!`()[]{}\\E@]*",
				(loc, res) -> new OperatorToken(loc, res.group())));
		
		tokenizer.addRule(new IgnoreTokenizerRule("[ \t\\n]+"));
		tokenizer.addRule(new IgnoreTokenizerRule("(?m)#.*?$"));
		return tokenizer;
	}
	
	/**
	 * Adds rules for tokenizing symbols to the given {@link Tokenizer}.
	 * @param tokenizer The tokenizer to add symbol tokenizing rules to.
	 */
	private void addSymbolRules(Tokenizer tokenizer) {
		tokenizer.addRule(new SymbolTokenizerRule(SymbolTokenType.BINDING));
		tokenizer.addRule(new SymbolTokenizerRule(SymbolTokenType.PAREN_OPEN));
		tokenizer.addRule(new SymbolTokenizerRule(SymbolTokenType.PAREN_CLOSE));
		tokenizer.addRule(new SymbolTokenizerRule(SymbolTokenType.NAMESPACE_QUALIFIER));
		tokenizer.addRule(new SymbolTokenizerRule(SymbolTokenType.COMMA));
		tokenizer.addRule(new SymbolTokenizerRule(SymbolTokenType.FUNCTION_BEGIN));
		tokenizer.addRule(new SymbolTokenizerRule(SymbolTokenType.MODULE_BEGIN));
		tokenizer.addRule(new SymbolTokenizerRule(SymbolTokenType.END_DECLARATION));
	}
	
	/**
	 * Adds rules for tokenizing literal values to the given {@link
	 * Tokenizer}.
	 * @param tokenizer The tokenizer to add literal tokenizing rules to.
	 */
	private void addLiteralRules(Tokenizer tokenizer) {
		tokenizer.addRule(new SimpleTokenizerRule(
				LiteralStringToken.getLiteralStringPattern(),
				(loc, res) -> new LiteralStringToken(loc, res.group(1))));
		
		/* The rule for parsing decimal must be after the rules for parsing other bases.
		 * Otherwise, the '0' at the start of the base prefix will be parsed as a decimal
		 * literal value zero.
		 */
		
		tokenizer.addRule(new SimpleTokenizerRule(
				"0b[01]+",
				(loc, res) -> new LiteralIntToken(
						loc,
						LiteralIntTokenBase.BINARY.fromString(res.group()),
						LiteralIntTokenBase.BINARY)));
		
		tokenizer.addRule(new SimpleTokenizerRule(
				"0x[0-9A-Fa-f]+",
				(loc, res) -> new LiteralIntToken(
						loc,
						LiteralIntTokenBase.HEXADECIMAL.fromString(res.group()),
						LiteralIntTokenBase.HEXADECIMAL)));
		
		tokenizer.addRule(new SimpleTokenizerRule(
				"[0-9]+",
				(loc, res) -> new LiteralIntToken(
						loc,
						LiteralIntTokenBase.DECIMAL.fromString(res.group()),
						LiteralIntTokenBase.DECIMAL)));
	}
}