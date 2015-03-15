package pw.usn.mu.parser;

import pw.usn.mu.tokenizer.Location;
import pw.usn.mu.tokenizer.SymbolTokenType;

/**
 * Represents a branch of a switch statement.
 */
public class SwitchBranchNode extends Node {
	private Node pattern, condition, result;
	
	/**
	 * Initialize a new SwitchBranchNode with the given {@code pattern} to match, {@code
	 * condition} to satisfy and {@code result} of the expression should the condition be
	 * satisfied for the given pattern.
	 * @param location The location of the AST node in a parsed input source.
	 * @param pattern The pattern to be matched for this case.
	 * @param condition The condition to satisfy.
	 * @param result The result of this branch.
	 */
	public SwitchBranchNode(Location location, Node pattern, Node condition, Node result) {
		super(location);
		this.pattern = pattern;
		this.condition = condition;
		this.result = result;
	}
	
	/**
	 * Initialize a new SwitchBranch with the given {@code pattern} to match and {@code
	 * result} of the expression should the input match the given pattern.
	 * @param location The location of the AST node in a parsed input source.
	 * @param pattern The pattern to be matched for this case.
	 * @param result The result of this branch.
	 */
	public SwitchBranchNode(Location location, Node pattern, Node result) {
		this(location, pattern, new LiteralSymbolNode(location, "true"), result);
	}
	
	/**
	 * Gets the pattern to match for this switch branch.
	 * @return The pattern to match.
	 */
	public Node getPattern() {
		return pattern;
	}
	
	/**
	 * Gets the condition to satisfy for this switch branch.
	 * @return The condition to satisfy.
	 */
	public Node getCondition() {
		return condition;
	}
	
	/**
	 * Gets the result of evaluating this switch branch.
	 * @return The result of this branch.
	 */
	public Node getResult() {
		return result;
	}
	
	/**
	 * Parses a switch branch from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return A {@link SwitchBranchNode}, as parsed from the current input.
	 */
	public static SwitchBranchNode parse(Parser parser) {
		Node pattern = Node.parse(parser);
		if(parser.accept(token -> token.isSymbolToken(SymbolTokenType.FUNCTION_DECLARE))) {
			Node condition = Node.parse(parser);
			Location switchBranchLocation = parser.expect(
					token -> token.isSymbolToken(SymbolTokenType.FUNCTION_BEGIN),
					"Expected forward-arrow after branch condition.");
			Node result = Node.parse(parser);
			return new SwitchBranchNode(switchBranchLocation, pattern, condition, result);
		} else {
			Location switchBranchLocation = parser.expect(
					token -> token.isSymbolToken(SymbolTokenType.FUNCTION_BEGIN),
					"Expected forward-arrow after branch pattern.");
			Node result = Node.parse(parser);
			return new SwitchBranchNode(switchBranchLocation, pattern, result);
		}
	}
}
