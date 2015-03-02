package pw.usn.mu.analyser;

import java.util.logging.Logger;

import pw.usn.mu.parser.IdentifierNode;

/**
 * Represents a reference to a value in a mu program.
 */
public class Reference implements Expression {
	private Value value;
	
	/**
	 * Initialize a new reference to a {@link Value}.
	 * @param value The value to refer to.
	 */
	public Reference(Value value) {
		this.value = value;
	}
	
	/**
	 * Determines if this reference refers to the given value.
	 * @param value The other value to check.
	 * @return {@code true} if this {@link Reference} refers to {@code value};
	 * {@code false} otherwise.
	 */
	public boolean refersTo(Value value) {
		return value.equals(this.value);
	}
	
	@Override
	public boolean equals(Object obj) {
		return
				obj != null &&
				obj instanceof Reference &&
				((Reference)obj).value.equals(value);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}
	
	/**
	 * Analyses the given {@code node} in the given context and creates an equivalent
	 * {@link Reference}.
	 * @param context The context in which {@code node} resides.
	 * @param node The AST node to analyse.
	 * @return An {@link Reference} representing the same identifier as {@code node},
	 * resolved into a reference to a value.
	 */
	public static Reference analyse(ResolutionContext context, IdentifierNode node) {
		Reference reference = context.resolve(node);
		Logger.getLogger("analysis").info(String.format("Resolved identifier %s to %d.", node.toString(), reference.hashCode()));
		return reference;
	}
}
