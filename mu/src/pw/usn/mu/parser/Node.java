package pw.usn.mu.parser;

import pw.usn.mu.tokenizer.IdentifierToken;
import pw.usn.mu.tokenizer.LiteralIntToken;
import pw.usn.mu.tokenizer.LiteralStringToken;
import pw.usn.mu.tokenizer.Location;
import pw.usn.mu.tokenizer.OperatorToken;
import pw.usn.mu.tokenizer.OperatorTokenType;
import pw.usn.mu.tokenizer.SymbolTokenType;

/**
 * Represents a node of the parsed AST produced from parsing a source file.
 */
public abstract class Node implements Parsable {
	private Location location;
	
	/**
	 * Initializes a new Node with the given location in the original parsed source file.
	 */
	public Node(Location location) {
		this.location = location;
	}
	
	/**
	 * Gets the location of the AST node.
	 * @return The location of the AST node in a parsed input source.
	 */
	public final Location getLocation() {
		return location;
	}
	
	/**
	 * Parses an full expression from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return An expression, as parsed from the current input.
	 */
	public static Node parse(Parser parser) {
		return TupleNode.parse(parser);
	}
	
	/**
	 * Parses a bound expression from the given parser state. This allows infix operators in
	 * the parsed expression and should be use when the expression to be parsed is delimited.
	 * This does not, however, permit unparenthesized tuples.
	 * @param parser The parser enumerator to use.
	 * @return An expression, as parsed from the current input.
	 */
	public static Node parseBound(Parser parser) {
		return parseBooleanPrecedence(parser);
	}
	
	/**
	 * Creates an expression representing the application of a binary operator with a left
	 * and right operand.
	 * @param operator The operator to apply.
	 * @param left The left-hand side of the operation.
	 * @param right The right-hand side of the operation.
	 * @return An {@link ApplicationNode} syntax tree node representing the application of the
	 * given {@code operator} with the given {@code left} and {@code right} operands.
	 */
	private static ApplicationNode createOperationApplication(Node operator, Node left, Node right) {
		return new ApplicationNode(
				operator.getLocation(),
				new ApplicationNode(
						operator.getLocation(),
						operator,
						left),
				right); 
	}
	/**
	 * Creates an expression representing the application of an unary operator with an
	 * operand.
	 * @param operator The operator to apply.
	 * @param operand The operand of the operation.
	 * @return An {@link ApplicationNode} syntax tree node representing the application of the
	 * given {@code operator} with the given {@code operand}.
	 */
	private static ApplicationNode createOperationApplication(Node operator, Node operand) {
		return new ApplicationNode(
				operator.getLocation(),
				operator,
				operand); 
	}
	
	/**
	 * Parses an atomic expression - that is, one such as a literal expression,
	 * an identifier, a bracketed expression or a function.
	 * @param parser The parser enumerator to use.
	 * @return An expression, as parsed from the current input.
	 */
	public static Node parseAtomic(Parser parser) {
		if(parser.test(token -> token instanceof LiteralStringToken)) {
			return LiteralStringNode.parse(parser);
		} else if(parser.test(token -> token instanceof LiteralIntToken)) {
			return LiteralIntNode.parse(parser);
		} else if(parser.test(token -> token instanceof IdentifierToken)) {
			return IdentifierNode.parse(parser);
		} else if(parser.test(token -> token.isSymbolToken(SymbolTokenType.SEQUENCE_OPEN))) {
			return SequenceNode.parse(parser);
		} else if(parser.accept(token -> token.isSymbolToken(SymbolTokenType.PAREN_OPEN))) {
			Node expression;
			if(parser.test(token -> token.isSymbolToken(SymbolTokenType.FUNCTION_DECLARE))) {
				expression = FunctionNode.parse(parser);
			} else if(parser.test(token -> token.isSymbolToken(SymbolTokenType.SWITCH_DECLARE))) {
				expression = SwitchNode.parse(parser);
			} else {
				expression = Node.parse(parser);
			}
			parser.expect(token -> token.isSymbolToken(SymbolTokenType.PAREN_CLOSE), "Closing parenthesis expected.");
			return expression;
		} else {
			throw new ParserException("Unexpected token in expression.", parser.current(1));
		}
	}
	
	/**
	 * Parses an expression at the precedence of the boolean operators.
	 * @param parser The parser enumerator to use.
	 * @return An expression, as parsed from the current input.
	 */
	private static Node parseBooleanPrecedence(Parser parser) {
		Node left = parseEqualityPrecedence(parser);
		while(parser.accept(token -> token.isOperatorToken(OperatorTokenType.BOOLEAN))) {
			OperatorToken operatorToken = (OperatorToken)parser.current();
			String operator = operatorToken.getOperator();
			left = createOperationApplication(new IdentifierNode(operatorToken.getLocation(), operator), left, parseEqualityPrecedence(parser));
		}
		return left;
	}
	
	/**
	 * Parses an expression at the precedence of the (in)equality operators.
	 * @param parser The parser enumerator to use.
	 * @return An expression, as parsed from the current input.
	 */
	private static Node parseEqualityPrecedence(Parser parser) {
		Node left = parseSummationPrecedence(parser);
		while(parser.accept(token -> token.isOperatorToken(OperatorTokenType.EQUALITY))) {
			OperatorToken operatorToken = (OperatorToken)parser.current();
			String operator = operatorToken.getOperator();
			left = createOperationApplication(new IdentifierNode(operatorToken.getLocation(), operator), left, parseSummationPrecedence(parser));
		}
		return left;
	}
	
	/**
	 * Parses an expression at the precedence of the summation operators.
	 * @param parser The parser enumerator to use.
	 * @return An expression, as parsed from the current input.
	 */
	private static Node parseSummationPrecedence(Parser parser) {
		Node left = parseProductionPrecedence(parser);
		while(parser.accept(token -> token.isOperatorToken(OperatorTokenType.SUM))) {
			OperatorToken operatorToken = (OperatorToken)parser.current();
			String operator = operatorToken.getOperator();
			left = createOperationApplication(new IdentifierNode(operatorToken.getLocation(), operator), left, parseProductionPrecedence(parser));
		}
		return left;
	}
	
	/**
	 * Parses an expression at the precedence of the production operators.
	 * @param parser The parser enumerator to use.
	 * @return An expression, as parsed from the current input.
	 */
	private static Node parseProductionPrecedence(Parser parser) {
		Node left = parseUnaryPrecedence(parser);
		while(parser.accept(token -> token.isOperatorToken(OperatorTokenType.PRODUCT))) {
			OperatorToken operatorToken = (OperatorToken)parser.current();
			String operator = operatorToken.getOperator();
			left = createOperationApplication(new IdentifierNode(operatorToken.getLocation(), operator), left, parseUnaryPrecedence(parser));
		}
		return left;
	}
	
	/**
	 * Parses an expression at the precedence of the production operators.
	 * @param parser The parser enumerator to use.
	 * @return An expression, as parsed from the current input.
	 */
	private static Node parseUnaryPrecedence(Parser parser) {
		if(parser.accept(token -> token.isOperatorToken(OperatorTokenType.UNARY))) {
			OperatorToken operatorToken = (OperatorToken)parser.current();
			String operator = operatorToken.getOperator();
			return createOperationApplication(new IdentifierNode(operatorToken.getLocation(), operator), parseUnaryPrecedence(parser));
		} else {
			return ApplicationNode.parse(parser);
		}
	}
}
