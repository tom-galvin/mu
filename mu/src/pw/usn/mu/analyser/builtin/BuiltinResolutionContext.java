package pw.usn.mu.analyser.builtin;

import java.util.HashMap;
import java.util.Map;

import pw.usn.mu.analyser.Reference;
import pw.usn.mu.analyser.ResolutionContext;
import pw.usn.mu.parser.IdentifierNode;

/**
 * Represents the context from which built-in function identifiers can be resolved.
 */
public class BuiltinResolutionContext extends ResolutionContext {
	private Map<String, Builtin> builtins = new HashMap<String, Builtin>();
	
	/**
	 * Initializes a new BuiltinResolutionContext.
	 * @param globalContext The global context containing the program.
	 * @param parentContext The context containing this context.
	 */
	public BuiltinResolutionContext(ResolutionContext globalContext, ResolutionContext parentContext) {
		super(globalContext, parentContext);
		registerBuiltins();
	}
	
	/**
	 * Initializes a new BuiltinResolutionContext.
	 * @param parentContext The context containing this context.
	 */
	public BuiltinResolutionContext(ResolutionContext parentContext) {
		super(parentContext);
		registerBuiltins();
	}
	
	/**
	 * Initialize a new BuiltinResolutionContext.
	 */
	public BuiltinResolutionContext() {
		super();
		registerBuiltins();
	}
	
	/**
	 * Registers a built-in function which can be resolved.
	 * @param builtin The built-in function to register.
	 */
	protected final void registerBuiltin(Builtin builtin) {
		builtins.put(builtin.getName(), builtin);
	}
	
	/**
	 * Registers the default built-in functions.
	 */
	protected void registerBuiltins() {
		registerBuiltin(new Builtin("__add", 2));
		registerBuiltin(new Builtin("__cons", 2));
	}
	
	@Override
	public Reference resolve(IdentifierNode identifier) {
		if(identifier.isUnqualified()) {
			String name = identifier.getName();
			if(builtins.containsKey(name)) {
				return builtins.get(name).newReference(identifier.getLocation());
			}
		}
		return super.resolve(identifier);
	}
}
