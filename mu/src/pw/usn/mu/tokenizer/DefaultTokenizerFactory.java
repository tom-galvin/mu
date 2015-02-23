package pw.usn.mu.tokenizer;

import pw.usn.mu.parser.Identifier;

/**
 * A factory for creating mu language tokenizers.
 */
public class DefaultTokenizerFactory implements TokenizerFactory {
	@Override
	public Tokenizer create() {
		Tokenizer tokenizer = new Tokenizer();
		tokenizer.addRule(new SimpleTokenizerRule(
				"[A-Za-z_][A-Za-z0-9_]*",
				(l, r) -> new IdentifierToken(l, r.group())));
		tokenizer.addRule(new SimpleTokenizerRule(
				LiteralStringToken.getLiteralStringPattern(),
				(l, r) -> new LiteralStringToken(l, r.group(1))));
		tokenizer.addRule(new SymbolTokenizerRule("\\(", SymbolTokenType.PAREN_OPEN));
		tokenizer.addRule(new SymbolTokenizerRule("\\)", SymbolTokenType.PAREN_CLOSE));
		tokenizer.addRule(new SymbolTokenizerRule(Identifier.QUALIFIER_SYMBOL, SymbolTokenType.NAMESPACE_QUALIFIER));
		tokenizer.addRule(new SymbolTokenizerRule(",", SymbolTokenType.COMMA));
		tokenizer.addRule(new SymbolTokenizerRule("\\\\", SymbolTokenType.FUNCTION_BEGIN));
		tokenizer.addRule(new IgnoreTokenizerRule("[ \t\\n]+"));
		tokenizer.addRule(new IgnoreTokenizerRule("(?m)#.*?$"));
		return tokenizer;
	}
}