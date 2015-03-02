package pw.usn.mu.analyser;

import pw.usn.mu.parser.IdentifierNode;

/**
 * Represents a context in which values are bound to identifiers, and in which
 * the value to which an identifer refers to can be resolved.
 */
public abstract class ResolutionContext {
	private ResolutionContext globalContext, parentContext;
	
	/**
	 * Initializes a new Context.
	 * @param globalContext The global context containing the program.
	 * @param parentContext The context containing this context.
	 */
	public ResolutionContext(ResolutionContext globalContext, ResolutionContext parentContext) {
		this.globalContext = globalContext;
		this.parentContext = parentContext;
	}
	
	/**
	 * Initializes a new Context.
	 * @param parentContext The context containing this context.
	 */
	public ResolutionContext(ResolutionContext parentContext) {
		this(parentContext.globalContext, parentContext);
	}
	
	/**
	 * Initialize a new Context.
	 */
	public ResolutionContext() {
		this(null, null);
	}
	
	/**
	 * Resolves an identifier in the current context to a reference to the value
	 * to which {@code identifier} refers.
	 * @param identifier The identifier to resolve.
	 * @return A reference to the value to which {@code identifier} refers to.
	 */
	public Reference resolve(IdentifierNode identifier) {
		if(identifier.isUnqualified()) {
			if(parentContext != null) {
				return parentContext.resolve(identifier);
			} else {
				throw new UnresolvedIdentifierException(identifier);
			}
		} else if(globalContext != this) {
			try {
				return globalContext.resolve(identifier);
			} catch(UnresolvedIdentifierException e) {
				return parentContext.resolve(identifier);
			}
		} else {
			throw new UnresolvedIdentifierException(identifier);
		}
	}
}
