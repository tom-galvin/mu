package pw.usn.mu.parser;

import pw.usn.mu.tokenizer.SymbolTokenType;

/**
 * Represents a branch of a switch statement.
 */
public class SwitchBranch extends Expression {
	private Expression pattern, condition, result;
	
	/**
	 * Initialize a new SwitchBranch with the given {@code pattern} to match, {@code condition} to
	 * satisfy and {@code result} of the expression should the condition be satisfied for the given
	 * pattern.
	 * @param pattern The pattern to be matched for this case.
	 * @param condition The condition to satisfy.
	 * @param result The result of this branch.
	 */
	public SwitchBranch(Expression pattern, Expression condition, Expression result) {
		this.pattern = pattern;
		this.condition = condition;
		this.result = result;
	}
	
	/**
	 * Initialize a new SwitchBranch with the given {@code pattern} to match and {@code result} of
	 * the expression should the input match the given pattern.
	 * @param pattern The pattern to be matched for this case.
	 * @param result The result of this branch.
	 */
	public SwitchBranch(Expression pattern, Expression result) {
		this.pattern = pattern;
		this.condition = new Identifier("true");
		this.result = result;
	}
	
	/**
	 * Gets the pattern to match for this switch branch.
	 * @return The pattern to match.
	 */
	public Expression getPattern() {
		return pattern;
	}
	
	/**
	 * Gets the condition to satisfy for this switch branch.
	 * @return The condition to satisfy.
	 */
	public Expression getCondition() {
		return condition;
	}
	
	/**
	 * Gets the result of evaluating this switch branch.
	 * @return The result of this branch.
	 */
	public Expression getResult() {
		return result;
	}
	
	/**
	 * Parses a switch branch from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return A {@link SwitchBranch}, as parsed from the current input.
	 */
	public static SwitchBranch parse(Parser parser) {
		Expression pattern = Expression.parse(parser);
		if(parser.accept(token -> token.isSymbolToken(SymbolTokenType.FUNCTION_DECLARE))) {
			Expression condition = Expression.parse(parser);
			parser.expect(
					token -> token.isSymbolToken(SymbolTokenType.FUNCTION_BEGIN),
					"Expected forward-arrow after branch condition.");
			Expression result = Expression.parse(parser);
			return new SwitchBranch(pattern, condition, result);
		} else {
			parser.expect(
					token -> token.isSymbolToken(SymbolTokenType.FUNCTION_BEGIN),
					"Expected forward-arrow after branch pattern.");
			Expression result = Expression.parse(parser);
			return new SwitchBranch(pattern, result);
		}
	}
}
