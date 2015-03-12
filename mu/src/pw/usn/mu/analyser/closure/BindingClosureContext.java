package pw.usn.mu.analyser.closure;

import java.util.stream.Stream;

import pw.usn.mu.analyser.Binding;
import pw.usn.mu.analyser.Reference;
import pw.usn.mu.analyser.Value;

/**
 * Represents a context maintained by a binding expression, in which free
 * variables can be bound to function closures during the lambda-lifting
 * process.
 */
public class BindingClosureContext extends ClosureContext {
	private Binding binding;
	
	/**
	 * Initializes a new BindingClosureContext with the specified enclosing scope.
	 * @param binding The binding which maintains this {@link BindingClosureContext}.
	 * @param enclosingScope The scope in which this context exists, or {@code null}
	 * to specify a top-level context.
	 */
	public BindingClosureContext(Binding binding, ClosureContext enclosingScope) {
		super(enclosingScope);
		this.binding = binding;
	}
	
	/**
	 * Initializes a new top-level BindingClosureContext.
	 * @param binding The binding which maintains this {@link BindingClosureContext}.
	 */
	public BindingClosureContext(Binding binding) {
		this(binding, null);
	}
	
	@Override
	public void liftReference(Reference reference) {
		if(reference.isNonLocalReference()) {
			/* Do nothing - the reference refers to a value that is not
			 * declared in a local scope, so we cannot close over it.
			 */
		} if(reference.refersTo(binding.getValue())) {
			/* Do nothing - we know this reference refers to a value
			 * in local scope (ie. a value bound in this binding),
			 * so we don't need to do anything else.
			 */
		} else {
			getEnclosingScope().liftReference(reference);
		}
	}
	
	@Override
	public Stream<Value> getLocalValues() {
		return Stream.concat(
				Stream.of(binding.getValue()),
				getEnclosingScope().getLocalValues());
	}
}
