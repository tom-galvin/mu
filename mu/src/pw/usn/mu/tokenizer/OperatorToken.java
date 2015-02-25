package pw.usn.mu.tokenizer;

/**
 * Represents a token for an infix operator.
 */
public class OperatorToken extends Token {
	private String operator;

	/**
	 * Initializes a new OperatorToken with the given location and operator string.
	 * @param location The location of the token.
	 * @param operator The string of the operator being performed.
	 */
	public OperatorToken(Location location, String operator) {
		super(location);
		this.operator = operator;
	}

	/**
	 * Gets the operator string for this OperatorToken.
	 * @return A string containing the operator symbol for this OperatorToken. For
	 * example, calling this method on an addition OperatorToken would return the
	 * string {@code "+"}.
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * Gets the operator token type for this OperatorToken, which is used for
	 * determining operator precedence for binary tokens. This is determined by the
	 * first character in the operator string:
	 * <ul>
	 * <li>{@code &|^#} {@link OperatorTokenType#BOOLEAN}</li>
	 * <li>{@code <>=$} {@link OperatorTokenType#EQUALITY}</li>
	 * <li>{@code +-:} {@link OperatorTokenType#SUM}</li>
	 * <li>{@code /*%} {@link OperatorTokenType#PRODUCT}</li>
	 * <li>{@code !`} {@link OperatorTokenType#UNARY}</li>
	 * </ul>
	 * @return The {@link OperatorTokenType} for this OperatorToken.
	 */
	public OperatorTokenType getType() {
		char operatorInitialCharacter = operator.charAt(0);
		switch(operatorInitialCharacter) {
		case '&':
		case '|':
		case '^':
		case '#':
			return OperatorTokenType.BOOLEAN;
		case '<':
		case '>':
		case '=':
		case '$':
			return OperatorTokenType.EQUALITY;
		case '+':
		case '-':
		case ':':
			return OperatorTokenType.SUM;
		case '*':
		case '/':
		case '%':
			return OperatorTokenType.PRODUCT;
		case '!':
		case '`':
			return OperatorTokenType.UNARY;
		default: // bind as loose as possible by default
			return OperatorTokenType.BOOLEAN;
		}
	}
	
	@Override
	public String getInformation() {
		return String.format("%s, level=%s",
				operator,
				getType());
	}
}
