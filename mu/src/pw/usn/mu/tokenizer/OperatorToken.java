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
	 * Gets the operator string for this OperatorToken. For example, calling this
	 * method on an addition OperatorToken would return the string {@code "+"},
	 * and calling this method on an OperatorToken for the infix operator {@code `myOp`}
	 * will return the strign {@code "myOp"} (the backticks are stripped).
	 * @return A string containing the operator symbol for this OperatorToken.
	 */
	public String getOperator() {
		if(operator.startsWith("`") && operator.endsWith("`")) {
			return operator.substring(1, operator.length() - 2);
		} else {
			return operator;
		}
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
	 * <li>{@code !~} {@link OperatorTokenType#UNARY}</li>
	 * </ul>
	 * Any other operators, such as text operators enclosed in backticks like an
	 * operator named {@code `myAdd`}, is bound as tight as possible with the {@link
	 * OperatorTokenType#PRODUCT} precedence.
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
		case '~':
			return OperatorTokenType.UNARY;
		default:
			/* Bind as tight as possible by default. This includes
			 * operators in backticks.
			 */
			return OperatorTokenType.PRODUCT;
		}
	}
	
	@Override
	public String getInformation() {
		return String.format("%s, level=%s",
				operator,
				getType());
	}
}
