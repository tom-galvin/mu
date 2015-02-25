package pw.usn.mu.parser;

import pw.usn.mu.tokenizer.LiteralIntToken;
import pw.usn.mu.tokenizer.LiteralIntTokenBase;

/**
 * Represents an int literal in mu source code.
 */
public class LiteralInt implements Parsable, Expression {
	private int value;
	private LiteralIntTokenBase originalBase;
	
	/**
	 * Initializes a new LiteralInt with the given value and original base,
	 * as specified in the original source.
	 * @param value The value of the int literal.
	 * @param originalBase The original numeric base, as specified in the
	 * source from which the original {@link pw.usn.mu.tokenizer.LiteralIntToken
	 * LiteralIntToken} was tokenized.
	 */
	public LiteralInt(int value, LiteralIntTokenBase originalBase) {
		this.value = value;
		this.originalBase = originalBase;
	}
	
	/**
	 * Initializes a new LiteralInt with the given value.
	 * @param value The value of the int literal.
	 */
	public LiteralInt(int value) {
		this(value, LiteralIntTokenBase.DECIMAL);
	}
	
	/**
	 * Gets the value of this int literal.
	 * @return The integer value represented by this LiteralInt.
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Gets the original base of this int literal.
	 * @return The original numeric base, as specified in the source from which
	 * the original {@link pw.usn.mu.tokenizer.LiteralIntToken LiteralIntToken}
	 * was tokenized.
	 */
	public LiteralIntTokenBase getOriginalBase() {
		return originalBase;
	}
	
	/**
	 * Parses an int literal from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return An int literal, as parsed from the current input.
	 */
	public static LiteralInt parse(Parser parser) {
		parser.expect(token -> token instanceof LiteralIntToken, "Int literal expected.");
		LiteralIntToken current = (LiteralIntToken)parser.current();
		return new LiteralInt(current.getValue(), current.getBase());
	}
}
