package pw.usn.mu.parser;

import pw.usn.mu.tokenizer.IdentifierToken;
import pw.usn.mu.tokenizer.LiteralIntToken;
import pw.usn.mu.tokenizer.LiteralStringToken;
import pw.usn.mu.tokenizer.OperatorToken;
import pw.usn.mu.tokenizer.OperatorTokenType;
import pw.usn.mu.tokenizer.SymbolTokenType;

/**
 * Defines methods for parsing expression-related parts of mu's grammar.
 */
public final class ExpressionGrammar {
	/**
	 * Creates an expression representing the application of a binary operator with a left
	 * and right operand.
	 * @param operator The operator to apply.
	 * @param left The left-hand side of the operation.
	 * @param right The right-hand side of the operation.
	 * @return An {@link Application} syntax tree node representing the application of the
	 * given {@code operator} with the given {@code left} and {@code right} operands.
	 */
	private static Application createOperationApplication(Expression operator, Expression left, Expression right) {
		return new Application(new Application(operator, left), right); 
	}
	/**
	 * Creates an expression representing the application of an unary operator with an
	 * operand.
	 * @param operator The operator to apply.
	 * @param operand The operand of the operation.
	 * @return An {@link Application} syntax tree node representing the application of the
	 * given {@code operator} with the given {@code operand}.
	 */
	private static Application createOperationApplication(Expression operator, Expression operand) {
		return new Application(operator, operand); 
	}
	
	/**
	 * Parses an atomic expression - that is, one such as a literal expression,
	 * an identifier, a bracketed expression or a function.
	 * @param parser The parser enumerator to use.
	 * @return An expression, as parsed from the current input.
	 */
	public static Expression parseAtomic(Parser parser) {
		if(parser.test(token -> token instanceof LiteralStringToken)) {
			return LiteralString.parse(parser);
		} else if(parser.test(token -> token instanceof LiteralIntToken)) {
			return LiteralInt.parse(parser);
		} else if(parser.test(token -> token instanceof IdentifierToken)) {
			return Identifier.parse(parser);
		} else if(parser.test(token -> token.isSymbolToken(SymbolTokenType.SEQUENCE_OPEN))) {
			return Sequence.parse(parser);
		} else if(parser.accept(token -> token.isSymbolToken(SymbolTokenType.PAREN_OPEN))) {
			Expression expression;
			if(parser.test(token -> token.isSymbolToken(SymbolTokenType.FUNCTION_BEGIN))) {
				expression = Function.parse(parser);
			} else {
				expression = Expression.parse(parser);
			}
			parser.expect(token -> token.isSymbolToken(SymbolTokenType.PAREN_CLOSE), "Closing parenthesis expected.");
			return expression;
		} else {
			parser.next();
			throw new ParserException("Unexpected token in expression.", parser.current(1));
		}
	}
	
	/**
	 * Parses an expression at the precedence of the boolean operators.
	 * @param parser The parser enumerator to use.
	 * @return An expression, as parsed from the current input.
	 */
	public static Expression parseBooleanPrecedence(Parser parser) {
		Expression left = parseEqualityPrecedence(parser);
		while(parser.accept(token -> token.isOperatorToken(OperatorTokenType.BOOLEAN))) {
			String operator = ((OperatorToken)parser.current()).getOperator();
			left = createOperationApplication(new Identifier(operator), left, parseEqualityPrecedence(parser));
		}
		return left;
	}
	
	/**
	 * Parses an expression at the precedence of the (in)equality operators.
	 * @param parser The parser enumerator to use.
	 * @return An expression, as parsed from the current input.
	 */
	public static Expression parseEqualityPrecedence(Parser parser) {
		Expression left = parseSummationPrecedence(parser);
		while(parser.accept(token -> token.isOperatorToken(OperatorTokenType.EQUALITY))) {
			String operator = ((OperatorToken)parser.current()).getOperator();
			left = createOperationApplication(new Identifier(operator), left, parseSummationPrecedence(parser));
		}
		return left;
	}
	
	/**
	 * Parses an expression at the precedence of the summation operators.
	 * @param parser The parser enumerator to use.
	 * @return An expression, as parsed from the current input.
	 */
	public static Expression parseSummationPrecedence(Parser parser) {
		Expression left = parseProductionPrecedence(parser);
		while(parser.accept(token -> token.isOperatorToken(OperatorTokenType.SUM))) {
			String operator = ((OperatorToken)parser.current()).getOperator();
			left = createOperationApplication(new Identifier(operator), left, parseProductionPrecedence(parser));
		}
		return left;
	}
	
	/**
	 * Parses an expression at the precedence of the production operators.
	 * @param parser The parser enumerator to use.
	 * @return An expression, as parsed from the current input.
	 */
	public static Expression parseProductionPrecedence(Parser parser) {
		Expression left = parseUnaryPrecedence(parser);
		while(parser.accept(token -> token.isOperatorToken(OperatorTokenType.PRODUCT))) {
			String operator = ((OperatorToken)parser.current()).getOperator();
			left = createOperationApplication(new Identifier(operator), left, parseUnaryPrecedence(parser));
		}
		return left;
	}
	
	/**
	 * Parses an expression at the precedence of the production operators.
	 * @param parser The parser enumerator to use.
	 * @return An expression, as parsed from the current input.
	 */
	public static Expression parseUnaryPrecedence(Parser parser) {
		if(parser.accept(token -> token.isOperatorToken(OperatorTokenType.UNARY))) {
			String operator = ((OperatorToken)parser.current()).getOperator();
			return createOperationApplication(new Identifier(operator), parseUnaryPrecedence(parser));
		} else {
			return Application.parse(parser);
		}
	}
}
