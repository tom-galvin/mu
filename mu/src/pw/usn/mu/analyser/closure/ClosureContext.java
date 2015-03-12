package pw.usn.mu.analyser.closure;

import java.util.stream.Stream;

import pw.usn.mu.analyser.Reference;
import pw.usn.mu.analyser.Value;

/**
 * Represents a context in which free variables can be bound to
 * function closures during the lambda-lifting process.
 */
public abstract class ClosureContext {
	private ClosureContext enclosingScope;
	
	/**
	 * Initializes a new ClosureContext with the specified enclosing scope.
	 * @param enclosingScope The scope in which this context exists, or {@code null}
	 * to specify a top-level context.
	 */
	public ClosureContext(ClosureContext enclosingScope) {
		this.enclosingScope = enclosingScope;
	}
	
	/**
	 * Initializes a new top-level ClosureContext.
	 */
	public ClosureContext() {
		this(null);
	}
	
	/**
	 * Gets the scope in which this context exists, or {@code null} if this context
	 * is a top-level context.
	 * @return The enclosing scope of this {@link ClosureContext}.
	 */
	public ClosureContext getEnclosingScope() {
		return enclosingScope;
	}
	
	/**
	 * Gets all values defined locally in this context.
	 * @return A sequence of values defined locally in this context, down to the scope
	 * of the function directly enclosing this context.
	 */
	public abstract Stream<Value> getLocalValues();
	
	/**
	 * Lifts a reference into a local scope where necessary.
	 * @param reference The reference to lift.
	 */
	public abstract void liftReference(Reference reference);
}
