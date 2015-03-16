package pw.usn.mu.parser.binding;

import java.util.ArrayList;
import java.util.List;

import pw.usn.mu.parser.Node;
import pw.usn.mu.parser.Parser;
import pw.usn.mu.tokenizer.Location;
import pw.usn.mu.tokenizer.SymbolTokenType;

/**
 * Represents a binding which decomposes a tuple.
 */
public class BindTupleNode extends Node {
	private Node[] values;
	
	/**
	 * Initializes a new BindTupleNode, with the given values.
	 * @param location The location of the AST node in a parsed input source.
	 * @param values The values to bind.
	 */
	public BindTupleNode(Location location, Node[] values) {
		super(location);
		this.values = values;
	}

	/**
	 * Gets the size of the suple to decompose.
	 * @return The size of the tuple to decompose.
	 */
	public int getSize() {
		return values.length;
	}
	
	/**
	 * Gets the value at zero-based index {@code index} in this tuple.
	 * @param index The index in the range <b>[0, {@link #getSize()} - 1]</b>, of the
	 * value to get.
	 * @return The value at index {@code index} in this tuple.
	 */
	public Node getValue(int index) {
		return values[index];
	}
	
	/**
	 * Parses a tuple deconstruction binding from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return A tuple deconstruction binding, as parsed from the current input. If
	 * the input tuple only has one value, then this simply returns that value instead.
	 */
	public static Node parse(Parser parser) {
		List<Node> nodes = new ArrayList<Node>();
		nodes.add(BindConsNode.parse(parser));
		while(parser.accept(token -> token.isSymbolToken(SymbolTokenType.COMMA))) {
			nodes.add(BindConsNode.parse(parser));
		}
		if(nodes.size() == 1) {
			return nodes.get(0);
		} else {
			Node[] nodesArray = new Node[nodes.size()];
			nodes.toArray(nodesArray);
			/*
			 * Tuple doesn't really have a well-defined starting location, so just use the
			 * location of the first member instead.
			 */
			return new BindTupleNode(nodesArray[0].getLocation(), nodesArray);
		}
	}
}
