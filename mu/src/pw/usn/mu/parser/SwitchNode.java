package pw.usn.mu.parser;

import java.util.ArrayList;
import java.util.List;

import pw.usn.mu.tokenizer.SymbolTokenType;

/**
 * Represents a switch ({@code ?}) expression.
 */
public class SwitchNode extends Node {
	private Node expression;
	private SwitchBranchNode[] branches;
	
	/**
	 * Initializes a new Switch expression with the given branches.
	 * @param expression The expression to switch on.
	 * @param branches The branches in this switch statement.
	 */
	public SwitchNode(Node expression, SwitchBranchNode... branches) {
		this.expression = expression;
		if(branches == null || branches.length < 1) {
			throw new NullPointerException("Switch statement must have at least one branch.");
		} else {
			this.branches = branches;
		}
	}
	
	/**
	 * Gets the number of branch statements in this switch expression.
	 * @return The number of branches.
	 */
	public int getBranchCount() {
		return branches.length;
	}
	
	/**
	 * Gets the {@code index}-th branch in this Switch statement, where {@code
	 * index}={@code 0} is the first branch in the expression.
	 * @param index The branch to get.
	 * @return The specified branch in this switch statement.
	 */
	public SwitchBranchNode getBranch(int index) {
		return branches[index];
	}
	
	/**
	 * Gets the expression that this expression is switching on.
	 * @return The input expression for this switch block.
	 */
	public Node getExpression() {
		return expression;
	}
	
	/**
	 * Parses a switch statement from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return A {@link SwitchNode} expression, as parsed from the current input.
	 */
	public static SwitchNode parse(Parser parser) {
		parser.expect(token -> token.isSymbolToken(SymbolTokenType.SWITCH_DECLARE), "Expected question mark to begin switch statement.");
		Node input = Node.parseAtomic(parser);
		List<SwitchBranchNode> branches = new ArrayList<SwitchBranchNode>();
		do {
			branches.add(SwitchBranchNode.parse(parser));
		} while(parser.accept(token -> token.isSymbolToken(SymbolTokenType.SEPARATOR)));
		SwitchBranchNode[] branchesArray = new SwitchBranchNode[branches.size()];
		branches.toArray(branchesArray);
		return new SwitchNode(input, branchesArray);
	}
}
