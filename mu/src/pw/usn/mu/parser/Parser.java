package pw.usn.mu.parser;

import java.util.function.Predicate;

import pw.usn.mu.tokenizer.Location;
import pw.usn.mu.tokenizer.Token;

/**
 * A class used by a parsing process to enumerate over an array of tokens, as would
 * be done by a look-ahead parser.
 */
public class Parser {
	private Token[] tokens;
	private int index;
	private Parser parent;
	
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
		this.parent = null;
	}
	
	/**
	 * Initialize a new Parser with the given array of tokens.
	 * @param tokens The tokens which this Parser is to parse.
	 */
	public Parser(Token[] tokens) {
		this(tokens, -1);
	}
	
	/**
	 * Gets the Token {@code lookahead} places ahead of the current token in the Token
	 * array.
	 * @return The token in the token array at the location of the parser head, plus
	 * {@code lookahead}.
	 */
	public Token current(int lookahead) {
		if(index + lookahead >= 0 && index + lookahead < tokens.length) {
			return tokens[index + lookahead];
		} else {
			return null;
		}
	}
	
	/**
	 * Gets the current Token.
	 * @return The token in the token array at the location of the parser head.
	 */
	public Token current() {
		return current(0);
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
	 * Tests whether the token {@code lookahead}+1 ahead of the current token in the
	 * token array satisfies the given condition. If there is no such token (ie. the
	 * token would be after the end of the token array), then this function returns
	 * {@code false}.
	 * @param condition The condition that the token must satisfy.
	 * @param lookahead The number of tokens to look ahead after the next token. A
	 * value of {@code 0} indicates the next token. A value of {@code 1} indicates the
	 * token after that, and so on.
	 * @return Whether the given lookahead token exists, and if so, it satisfies {@code
	 * condition}.
	 */
	public boolean test(Predicate<Token> condition, int lookahead) {
		return !eof() && condition.test(tokens[index + 1 + lookahead]);
	}
	
	/**
	 * Tests whether the next token in the token array satisfies the given condition.
	 * If there is no next token (ie. the parser is at the end of the token array), then
	 * this function returns {@code false}.
	 * @param condition The condition that the next token must satisfy.
	 * @return Whether the next token exists, and if so, it satisfies {@code condition}.
	 */
	public boolean test(Predicate<Token> condition) {
		return test(condition, 0);
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
	 * @returns The location of the token that was successfully accepted.
	 */
	public Location expect(Predicate<Token> condition, String errorMessage) {
		if(!accept(condition)) {
			throw new ParserException(errorMessage, current(1));
		} else {
			return current().getLocation();
		}
	}
	
	/**
	 * Creates a new {@link Parser} instance with the same token array and position. This
	 * might be used in the case of look-ahead parsing.
	 * @return A new {@link Parser} instance with the same state.
	 */
	public Parser copyState() {
		Parser child = new Parser(tokens, index);
		child.parent = this;
		return child;
	}
	
	/**
	 * Determines if this parser was created by calling {@link Parser#copyState()} on either
	 * {@code ancestor}, or a (recursive) child of {@code ancestor}.
	 * @param ancestor The ancestor to check if this Parser descends from.
	 * @return Whether this parser is a descendant of {@code ancestor}.
	 */
	public boolean descendsFrom(Parser ancestor) {
		if(this.parent == null) {
			return false;
		} else if(this.parent == ancestor) {
			return true;
		} else {
			return this.parent.descendsFrom(ancestor);
		}
	}
	
	/**
	 * Fast-forwards this parser to the state of the given child parser.
	 * @param parser The child parser. The current parser will be fast-forwarded to whichever
	 * token {@code parser} is at currently.
	 */
	public void fastForward(Parser parser) {
		if(parser.descendsFrom(this)) {
			this.index = parser.index;
		} else {
			throw new IllegalArgumentException("This parser must be an ancestor of the given child parser.");
		}
	}
}