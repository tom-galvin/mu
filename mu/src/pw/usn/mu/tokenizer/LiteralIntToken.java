package pw.usn.mu.tokenizer;

/**
 * Represents a token containing an integer literal.
 */
public class LiteralIntToken extends Token {
	private int value;
	private LiteralIntTokenBase base;
	
	/**
	 * Initializes a new LiteralIntToken with the given value, numerical base and 
	 * location in a source.
	 * @param location The location of the token.
	 * @param value The value of this integer literal.
	 * @param base The radix of this integer literal, as specified in the source. This has no
	 * effect on the value of the literal, but may be used for reconstruction later on.
	 */
	public LiteralIntToken(Location location, int value, LiteralIntTokenBase base) {
		super(location);
		this.value = value;
		this.base = base;
	}
	
	/**
	 * Initializes a new LiteralIntToken with the given value and location in a source.
	 * @param location The location of the token.
	 * @param value The value of this integer literal.
	 */
	public LiteralIntToken(Location location, int value) {
		this(location, value, LiteralIntTokenBase.DECIMAL);
	}
	
	/**
	 * Gets the value of this int literal.
	 * @return The value as an integer that this LiteralIntToken represents.
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * Gets the base of this int literal.
	 * @return The base of the LiteralIntToken, as specified in the original source from
	 * which this token was tokenized.
	 */
	public LiteralIntTokenBase getBase() {
		return base;
	}
	
	@Override
	public String getInformation() {
		return base.toString(value);
	}
}
