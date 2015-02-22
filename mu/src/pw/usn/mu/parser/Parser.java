package pw.usn.mu.parser;

import java.util.function.Predicate;

import pw.usn.mu.tokenizer.Token;

/**
 * A class used by a parsing process to enumerate over an array of tokens, as would
 * be done by a look-ahead parser.
 */
public class Parser {
	private Token[] tokens;
	private int index;
	
	/**
	 * Initialize a new Parser with the given array of tokens, and index in the token
	 * array to begin.
	 * @param tokens The tokens which this Parser is to parse.
	 * @param index The index to set the current (not next) token to; if this is set
	 * to {@code -1}, then the parser starts at the beginning - ie. with no current
	 * token, and the next token as the first token.
	 */
	private Parser(Token[] tokens, int index) {
		this.tokens = tokens;
		this.index = index;
	}
	
	/**
	 * Initialize a new Parser with the given array of tokens.
	 * @param tokens The tokens which this Parser is to parse.
	 */
	public Parser(Token[] tokens) {
		this(tokens, -1);
	}
	
	/**
	 * Gets the current Token.
	 * @return The token in the token array at the location of the parser head.
	 */
	public Token current() {
		if(index >= 0 && index < tokens.length) {
			return tokens[index];
		} else {
			return null;
		}
	}
	
	/**
	 * Determines if the the current token is also the last token in the tokenized array.
	 * @return Returns {@code true} if the current token is the end of the token array;
	 * otherwise, returns {@code false}.
	 */
	public boolean eof() {
		return index + 1 >= tokens.length;
	}
	
	/**
	 * Advances the parser lookahead one token forward. Returns {@code true} if this
	 * is successful, and {@code false} otherwise (for example, if the current {@link
	 * Token} is the last in the token array).
	 * @return Whether the operation was successful or not.
	 */
	public boolean next() {
		if(index + 1 >= tokens.length) {
			return false;
		} else{
			index += 1;
			return true;
		}
	}
	
	/**
	 * Tests whether the next token in the token array satisfies the given condition.
	 * If there is no next token (ie. the parser is at the end of the token array), then
	 * this function returns {@code false}.
	 * @param condition The condition that the next token must satisfy.
	 * @return Whether the next token exists, and if so, it satisfies {@code condition}.
	 */
	public boolean test(Predicate<Token> condition) {
		return !eof() && condition.test(tokens[index + 1]);
	}
	
	/**
	 * Advances the parser lookahead one token forward, if and only if the next token
	 * exists (ie. the parser is not at the end of the token array) and it satisfies
	 * the given {@code condition}.
	 * @param condition The condition that the next token must satisfy.
	 * @return Whether the next token exists, and if so, it satisfies {@code condition}.
	 * If this function returns true, then the parser was also advanced one token forward.
	 */
	public boolean accept(Predicate<Token> condition) {
		if(test(condition)) {
			next();
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * If the parser has not reached the end of the token array, and the next token in the
	 * token array satisfies {@code condition}, advances the parser one token forward.
	 * Otherwise, throws a {@link ParserException} with the exception message given in
	 * {@code errorMessage}.
	 * @param condition The condition that the next token must satisfy.
	 * @param errorMessage If the conditions are not satisfied, the error message of the
	 * {@link ParserException} that will be thrown.
	 */
	public void expect(Predicate<Token> condition, String errorMessage) {
		if(!accept(condition)) {
			throw new ParserException(errorMessage, current());
		}
	}
	
	/**
	 * Creates a new {@link Parser} instance with the same token array and position. This
	 * might be used in the case of look-ahead parsing.
	 * @return A new {@link Parser} instance with the same state.
	 */
	public Parser copyState() {
		return new Parser(tokens, index);
	}
}