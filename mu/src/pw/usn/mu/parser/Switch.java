package pw.usn.mu.parser;

import java.util.ArrayList;
import java.util.List;

import pw.usn.mu.tokenizer.SymbolTokenType;

/**
 * Represents a switch ({@code ?}) expression.
 */
public class Switch extends Expression {
	private Expression expression;
	private SwitchBranch[] branches;
	
	/**
	 * Initializes a new Switch expression with the given branches.
	 * @param expression The expression to switch on.
	 * @param branches The branches in this switch statement.
	 */
	public Switch(Expression expression, SwitchBranch... branches) {
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
	public SwitchBranch getBranch(int index) {
		return branches[index];
	}
	
	/**
	 * Gets the expression that this expression is switching on.
	 * @return The input expression for this switch block.
	 */
	public Expression getExpression() {
		return expression;
	}
	
	/**
	 * Parses a switch statement from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return A {@link Switch} expression, as parsed from the current input.
	 */
	public static Switch parse(Parser parser) {
		parser.expect(token -> token.isSymbolToken(SymbolTokenType.SWITCH_DECLARE), "Expected question mark to begin switch statement.");
		Expression input = Expression.parseAtomic(parser);
		List<SwitchBranch> branches = new ArrayList<SwitchBranch>();
		do {
			branches.add(SwitchBranch.parse(parser));
		} while(parser.accept(token -> token.isSymbolToken(SymbolTokenType.SEPARATOR)));
		SwitchBranch[] branchesArray = new SwitchBranch[branches.size()];
		branches.toArray(branchesArray);
		return new Switch(input, branchesArray);
	}
}
