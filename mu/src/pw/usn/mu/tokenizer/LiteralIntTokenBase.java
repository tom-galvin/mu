package pw.usn.mu.tokenizer;

/**
 * Represents the base, as specified in the original source, of an int literal.
 */
public enum LiteralIntTokenBase {
	/**
	 * A decimal (base-10) literal.
	 * This is the default.
	 */
	DECIMAL(10),
	/**
	 * A hexadecimal (base-16) literal.
	 * This is specified with the {@code 0x} prefix in a literal.
	 */
	HEXADECIMAL(16, "0x"),
	/**
	 * A binary (base-2) literal.
	 * This is specified with the {@code 0b} prefix in a literal.
	 */
	BINARY(2, "0b");
	
	private int radix;
	private String prefix;
	
	/**
	 * Initialize a new LiteralIntTokenBase enum member with the given radix and
	 * formatting prefix.
	 * @param radix The radix of this base.
	 * @param prefix The prefix to insert into converted values.
	 */
	private LiteralIntTokenBase(int radix, String prefix) {
		if(radix < 2) {
			throw new IllegalArgumentException("The radix of a LiteralIntTokenBase cannot be less than 2.");
		} else {
			this.radix = radix;
			this.prefix = prefix;
		}
	}
	
	/**
	 * Initialize a new LiteralIntTokenBase enum member with the given radix.
	 * @param radix The radix of this base.
	 */
	private LiteralIntTokenBase(int radix) {
		this(radix, "");
	}
	
	/**
	 * Gets the radix of this base.
	 * @return The radix of this base. For example, binary has a radix of 2, and
	 * decimal has a radix of 10.
	 */
	public int getRadix() {
		return radix;
	}
	
	/**
	 * Gets the prefix used for formatting numbers as this base.
	 * @return The formatting prefix for this base.
	 */
	public String getPrefix() {
		return prefix;
	}
	
	/**
	 * Converts a given integer value to a string representing it in this base.
	 * @param value The value to convert.
	 * @return Returns {@code value} formatted in the correct way for the current {@link
	 * LiteralIntTokenBase}.
	 */
	public String toString(int value) {
		StringBuilder builder = new StringBuilder();
		if(value < 0) {
			builder.append('-');
		}
		if(prefix != null) {
			builder.append(prefix);
		}
		builder.append(Integer.toString(value < 0 ? -value : value, radix));
		return builder.toString();
	}
	
	/**
	 * Converts a given string value, representing an integer in the current base, to an
	 * integer representing the same value.
	 * @param value The string value to convert to an integer.
	 * @return An integer representing the same value as {@code value}.
	 */
	public int fromString(String value) {
		int multiplier = 1;
		if(value.startsWith("-")) {
			multiplier *= -1;
			value = value.substring(1);
		}
		if(!value.startsWith(prefix)) {
			throw new IllegalArgumentException(String.format("The value %s is not in base-%d.", value, radix));
		} else {
			value = value.substring(prefix.length());
		}
		return Integer.valueOf(value, radix) * multiplier;
	}
}
