package pw.usn.mu.analyser;

import pw.usn.mu.analyser.builtin.Builtin;
import pw.usn.mu.analyser.closure.ClosureContext;
import pw.usn.mu.analyser.module.ModuleValue;
import pw.usn.mu.parser.IdentifierNode;
import pw.usn.mu.tokenizer.Location;

/**
 * Represents a reference to a value in a mu program.
 */
public class Reference extends Expression {
	private Value value;
	
	/**
	 * Initialize a new reference to a {@link Value}.
	 * @param location The original location, in a source, of the code that represents
	 * this expression.
	 * @param value The value to refer to.
	 */
	public Reference(Location location, Value value) {
		super(location);
		redirect(value);
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
	
	/**
	 * Gets the value referred to by this {@link Reference}.
	 * @return The value referred to by this reference.
	 */
	public Value getValue() {
		return value;
	}
	
	@Override
	public void liftClosures(ClosureContext context) {
		context.liftReference(this);
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
	 * Redirects this {@link Reference} to refer to the given value. Calling
	 * this method will remove the link between this reference and the value
	 * referred to previously (if it exists) and create a link between this
	 * reference and the newly referred-to value.
	 * @param value The value to refer to.
	 */
	public void redirect(Value value) {
		if(this.value != null) {
			this.value.removeReference(this);
		}
		this.value = value;
		this.value.addReference(this);
	}
	
	/**
	 * Determines whether this reference refers to a value that cannot be a
	 * local reference, such as a value defined in a module or a built-in
	 * function.
	 * @return Whether this reference is non-local or not.
	 */
	public boolean isNonLocalReference() {
		return
				value instanceof ModuleValue ||
				value instanceof Builtin;
	}
	
	/**
	 * Analyses the given {@code node} in the given context and creates an equivalent reference.
	 * @param context The context in which {@code node} resides.
	 * @param node The AST node to analyse.
	 * @return A reference representing the same identifier as {@code node}, resolved into a
	 * reference to a value.
	 */
	public static Reference analyse(ResolutionContext context, IdentifierNode node) {
		Reference reference = context.resolve(node);
		return reference;
	}
}
