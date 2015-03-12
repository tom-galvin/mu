package pw.usn.mu.parser;

import java.util.ArrayList;
import java.util.List;

import pw.usn.mu.tokenizer.Location;
import pw.usn.mu.tokenizer.SymbolTokenType;

/**
 * Represents a sequence literal in mu source code.
 */
public class SequenceNode extends Node {
	private Node[] values;
	
	/**
	 * Initializes a new Sequence with the given {@code values}.
	 * @param location The location of the AST node in a parsed input source.
	 * @param values The values contained within this sequence.
	 */
	public SequenceNode(Location location, Node... values) {
		super(location);
		this.values = values;
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
	public Node getValue(int index) {
		return values[index];
	}
	
	/**
	 * Parses a sequence from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return A {@link SequenceNode}, as parsed from the current input.
	 */
	public static SequenceNode parse(Parser parser) {
		Location sequenceLocation = parser.expect(token -> token.isSymbolToken(SymbolTokenType.SEQUENCE_OPEN), "Expected start of sequence.");
		if(parser.accept(token -> token.isSymbolToken(SymbolTokenType.SEQUENCE_CLOSE))) {
			return new SequenceNode(sequenceLocation);
		} else {
			List<Node> expressions = new ArrayList<Node>();
			do {
				expressions.add(Node.parseBound(parser));
			} while(parser.accept(token -> token.isSymbolToken(SymbolTokenType.COMMA)));
			parser.expect(token -> token.isSymbolToken(SymbolTokenType.SEQUENCE_CLOSE), "Expected end of sequence.");
			Node[] expressionsArray = new Node[expressions.size()];
			expressions.toArray(expressionsArray);
			return new SequenceNode(sequenceLocation, expressionsArray);
		}
	}
}
