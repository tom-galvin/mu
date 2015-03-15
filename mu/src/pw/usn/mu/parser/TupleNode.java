package pw.usn.mu.parser;

import java.util.ArrayList;
import java.util.List;

import pw.usn.mu.tokenizer.Location;
import pw.usn.mu.tokenizer.SymbolTokenType;

/**
 * Represents an unnamed tuple of values.
 */
public class TupleNode extends Node {
	private Node[] values;
	
	/**
	 * Initializes a new untagged TupleNode with the given {@code values}.
	 * @param location The location of the AST node in a parsed input source.
	 * @param values The values contained within this tuple.
	 */
	public TupleNode(Location location, Node... values) {
		super(location);
		if(values.length != 1) {
			this.values = values;
		} else {
			throw new IllegalArgumentException(
					  "Tuple may not have only one value. "
					+ "Tuples with zero values are allowed as unit values, and tuples "
					+ "with two values or more are allowed as tuples.");
		}
	}
	
	/**
	 * Gets the size of the tuple.
	 * @return The size (number of values) in this tuple.
	 */
	public int getSize() {
		return values.length;
	}
	
	/**
	 * Gets the {@code index}-th value in the tuple, where {@code index}={@code 0} is
	 * the first item in the tuple.
	 * @param index The position in the tuple of the value to get.
	 * @return The specified value in the suple.
	 */
	public Node getValue(int index) {
		return values[index];
	}
	
	/**
	 * Parses a tuple from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return An expression, as parsed from the current input. This method must
	 * return an {@link Node} rather than a concrete {@link TupleNode} as
	 * the tuple may have a length of one, in which case the value returned is
	 * the first (and only) value in the tuple.
	 */
	public static Node parse(Parser parser) {
		List<Node> expressions = new ArrayList<Node>(1);
		do {
			expressions.add(Node.parseTight(parser));
		} while(parser.accept(token -> token.isSymbolToken(SymbolTokenType.COMMA)));
		if(expressions.size() == 1) {
			return expressions.get(0);
		} else {
			Node[] expressionsArray = new Node[expressions.size()];
			expressions.toArray(expressionsArray);
			/*
			 * Tuple doesn't really have a well-defined starting location, so just use the
			 * location of the first member instead.
			 */
			return new TupleNode(expressionsArray[0].getLocation(), expressionsArray);
		}
	}
}
