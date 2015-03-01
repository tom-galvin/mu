package pw.usn.mu.parser;

import java.util.List;
import java.util.ArrayList;

import pw.usn.mu.tokenizer.SymbolTokenType;

/**
 * Represents a sequence literal in mu source code.
 */
public class Sequence extends Expression {
	private Expression[] values;
	
	/**
	 * Initializes a new Sequence with the given {@code values}.
	 * @param values The values contained within this sequence.
	 */
	public Sequence(Expression... values) {
		if(values.length > 1) {
			this.values = values;
		} else {
			throw new IllegalArgumentException("Sequence must have at least two values.");
		}
	}
	
	/**
	 * Gets the length of the sequence.
	 * @return The length (number of values) of the sequence.
	 */
	public int getLength() {
		return values.length;
	}
	
	/**
	 * Gets the {@code index}-th value in the sequence, where {@code index}={@code 0} is
	 * the first item in the sequence.
	 * @param index The position in the sequence of the value to get.
	 * @return The specified value in the suple.
	 */
	public Expression getValues(int index) {
		return values[index];
	}
	
	/**
	 * Parses a sequence from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return A {@link Sequence}, as parsed from the current input.
	 */
	public static Sequence parse(Parser parser) {
		parser.expect(token -> token.isSymbolToken(SymbolTokenType.SEQUENCE_OPEN), "Expected start of sequence.");
		if(parser.accept(token -> token.isSymbolToken(SymbolTokenType.SEQUENCE_CLOSE))) {
			return new Sequence();
		} else {
			List<Expression> expressions = new ArrayList<Expression>();
			do {
				expressions.add(Expression.parseBound(parser));
			} while(parser.accept(token -> token.isSymbolToken(SymbolTokenType.COMMA)));
			parser.expect(token -> token.isSymbolToken(SymbolTokenType.SEQUENCE_CLOSE), "Expected end of sequence.");
			Expression[] expressionsArray = new Expression[expressions.size()];
			expressions.toArray(expressionsArray);
			return new Sequence(expressionsArray);
		}
	}
}
