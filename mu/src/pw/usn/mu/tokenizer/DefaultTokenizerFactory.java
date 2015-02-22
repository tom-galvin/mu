package pw.usn.mu.tokenizer;

/**
 * A factory for creating mu language tokenizers.
 */
public class DefaultTokenizerFactory implements TokenizerFactory {
	@Override
	public Tokenizer create() {
		Tokenizer tokenizer = new Tokenizer();
		tokenizer.addRule(new IdentifierTokenizerRule());
		tokenizer.addRule(new SymbolTokenizerRule("\\(", SymbolTokenType.PAREN_OPEN));
		tokenizer.addRule(new SymbolTokenizerRule("\\)", SymbolTokenType.PAREN_CLOSE));
		tokenizer.addRule(new SymbolTokenizerRule("/", SymbolTokenType.NAMESPACE_QUALIFIER));
		tokenizer.addRule(new SymbolTokenizerRule(",", SymbolTokenType.COMMA));
		tokenizer.addRule(new SymbolTokenizerRule("\\\\", SymbolTokenType.FUNCTION_BEGIN));
		tokenizer.addRule(new SymbolTokenizerRule("/", SymbolTokenType.NAMESPACE_QUALIFIER));
		tokenizer.addRule(new IgnoreTokenizerRule("[ \t\\n]+"));
		tokenizer.addRule(new IgnoreTokenizerRule("(?m)#.*?$"));
		return tokenizer;
	}
}