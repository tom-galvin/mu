package pw.usn.mu.analyser.closure;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import pw.usn.mu.analyser.AnalysisErrorException;
import pw.usn.mu.analyser.Function;
import pw.usn.mu.analyser.Reference;
import pw.usn.mu.analyser.Value;

/**
 * Represents a context in which free variables can be bound to function
 * closures during the lambda-lifting process. This class also handles the
 * lifting of values from enclosing contexts into new ones by creating {@link
 * Lift}s which the relevant {@link Function} will turn into closure data.
 */
public class FunctionClosureContext extends ClosureContext {
	private Map<Value, Value> localClosure;
	private Function function;
	
	/**
	 * Initializes a new FunctionClosureContext with the specified enclosing scope.
	 * @param function The function which maintains this {@link FunctionClosureContext}.
	 * @param enclosingScope The scope in which this context exists, or {@code null}
	 * to specify a top-level context.
	 */
	public FunctionClosureContext(Function function, ClosureContext enclosingScope) {
		super(enclosingScope);
		localClosure = new HashMap<Value, Value>();
		this.function = function;
	}
	
	/**
	 * Initializes a new top-level FunctionClosureContext.
	 * @param function The function which maintains this {@link FunctionClosureContext}.
	 */
	public FunctionClosureContext(Function function) {
		this(function, null);
	}
	
	/**
	 * Gets all lifted values in the local closure, where values in the enclosing
	 * scope are mapped to lifted values local to the closure representing this
	 * function.
	 * @return The local closure of this function closure context.
	 */
	public Map<Value, Value> getLocalClosure() {
		return localClosure;
	}
	
	@Override
	public Stream<Value> getLocalValues() {
		return Stream.of(function.getArgument());
	}
	
	/**
	 * Gets the function scope in which this context exists, or {@code null} if there
	 * is no {@link FunctionClosureContext} in the scope hierarchy above this context.
	 * @return The enclosing function scope of this {@link FunctionClosureContext}.
	 */
	private FunctionClosureContext getEnclosingFunctionScope() {
		ClosureContext enclosingContext = getEnclosingScope();
		while(!(enclosingContext instanceof FunctionClosureContext)) {
			if(enclosingContext == null) {
				/* Return null if we reach the top-level context before
				 * finding a function closure context.
				 */
				return null;
			}
			enclosingContext = enclosingContext.getEnclosingScope();
		}
		return (FunctionClosureContext)enclosingContext;
	}
	
	public Value liftValue(Value value) {
		if(localClosure.containsKey(value)) {
			return localClosure.get(value);
		} else {
			ClosureContext enclosingScope = getEnclosingScope();
			if(enclosingScope == null) {
				throw new AnalysisErrorException(String.format("Unscoped value %s.", value.getName()), function.getLocation());
			}
			if(enclosingScope.getLocalValues().anyMatch(v -> v.equals(value))) {
				/* Lift a value from the context directly enclosing this function. */
				Value local = new Value(value.getName());
				localClosure.put(value, local);
				return local;
			} else {
				FunctionClosureContext enclosingFunctionScope = getEnclosingFunctionScope();
				if(enclosingFunctionScope == null) {
					throw new AnalysisErrorException(String.format("Unscoped value %s.", value.getName()), function.getLocation());
				}
				/* Lift a value that the enclosing context has already lifted. */
				Value liftedValue = enclosingFunctionScope.liftValue(value);
				Value local = new Value(liftedValue.getName());
				localClosure.put(liftedValue, local);
				return local;
			}
		}
	}

	@Override
	public void liftReference(Reference reference) {
		if(reference.isNonLocalReference()) {
			/* Do nothing - the reference refers to a value that is not
			 * declared in a local scope, so we cannot close over it.
			 */
		} else if(reference.refersTo(function.getArgument())) {
			/* Do nothing - we know this reference refers to a value
			 * in local scope (ie. the value of this function's argument),
			 * so we don't need to do anything else.
			 */
		} else {
			/* This reference must refer to a value not local to the
			 * scope of this function, so we need to lift it.
			 */
			reference.redirect(liftValue(reference.getValue()));
		}
	}
}
