package pw.usn.mu.tokenizer;

/**
 * Represents the level or 'class' of an {@link OperatorTokenType}.
 */
public enum OperatorTokenType {
	/**
	 * Represents operator tokens dealing with boolean arithmetic.
	 */
	BOOLEAN,
	/**
	 * Represents operator tokens dealing with (in)equality.
	 */
	EQUALITY,
	/**
	 * Represents operator tokens on the level of summation.
	 */
	SUM,
	/**
	 * Represents operator tokens on the level of production.
	 */
	PRODUCT,
	/**
	 * Represents unary operator tokens, which only accept one operand.
	 */
	UNARY
}
