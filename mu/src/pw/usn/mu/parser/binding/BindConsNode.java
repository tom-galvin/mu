package pw.usn.mu.parser.binding;

import java.util.Stack;

import pw.usn.mu.parser.Node;
import pw.usn.mu.parser.Parser;
import pw.usn.mu.tokenizer.Location;
import pw.usn.mu.tokenizer.SymbolTokenType;

/**
 * Represents a binding which gets the head and tail of a sequence.
 */
public class BindConsNode extends Node {
	private Node head, tail;
	
	/**
	 * Initializes a new BindConsNode with the given head and tail.
	 * @param location The location of the AST node in a parsed input source.
	 * @param head The head of the sequence.
	 * @param tail The tail of the sequence.
	 */
	public BindConsNode(Location location, Node head, Node tail) {
		super(location);
		this.head = head;
		this.tail = tail;
	}

	/**
	 * Gets the binding of the head of the sequence to deconstruct.
	 * @return The head of the sequence to deconstruct.
	 */
	public Node getHead() {
		return head;
	}
	
	/**
	 * Gets the binding of the tail of the sequence to deconstruct.
	 * @return The tail of the sequence to deconstruct.
	 */
	public Node getTail() {
		return tail;
	}
	
	/**
	 * Parses a cons deconstruction binding from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return A cons deconstruction binding, as parsed from the current input. If
	 * no values are consed, then this simply returns the lone value instead.
	 */
	public static Node parse(Parser parser) {
		Stack<Node> nodes = new Stack<Node>();
		Stack<Location> locations = new Stack<Location>();
		
		nodes.push(BindValueNode.parse(parser));
		while(parser.accept(token -> token.isSymbolToken(SymbolTokenType.CONS))) {
			locations.push(parser.current().getLocation());
			nodes.push(BindValueNode.parse(parser));
		}
		Node node = nodes.pop();
		while(!nodes.isEmpty()) {
			node = new BindConsNode(locations.pop(), nodes.pop(), node);
		}
		return node;
	}
}
