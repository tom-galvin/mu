package pw.usn.mu.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pw.usn.mu.tokenizer.IdentifierToken;
import pw.usn.mu.tokenizer.SymbolTokenType;
import pw.usn.mu.tokenizer.Token;

/**
 * Represents a collection of functions or values.
 */
public class Module implements Parsable {
	private Map<Identifier, Expression> definitions;
	private Map<Identifier, Module> submodules;
	
	/**
	 * Initializes a new empty Module.
	 */
	public Module() {
		this.definitions = new HashMap<Identifier, Expression>();
		this.submodules = new HashMap<Identifier, Module>();
	}
	
	/**
	 * Gets an array of the identifiers of all definitions in this module.
	 * @return The identifiers of all (non-module) definitions defined within
	 * this module, as an array.
	 */
	public Identifier[] getDefinitions() {
		Set<Identifier> keys = definitions.keySet();
		Identifier[] identifiers = new Identifier[keys.size()];
		keys.toArray(identifiers);
		return identifiers;
	}
	
	/**
	 * Gets an array of the identifiers of all submodules in this module.
	 * @return The identifiers of all submodules defined within this module,
	 * as an array.
	 */
	public Identifier[] getSubmodules() {
		Set<Identifier> keys = submodules.keySet();
		Identifier[] identifiers = new Identifier[keys.size()];
		keys.toArray(identifiers);
		return identifiers;
	}
	
	/**
	 * Gets the value of the definition defined within this module with the
	 * given identifier.
	 * @param identifier The identifier of the definition to get.
	 * @return The value of the definition bound to {@code identifier} within
	 * this module.
	 */
	public Expression getDefinition(Identifier identifier) {
		if(definitions.containsKey(identifier)) {
			return definitions.get(identifier);
		} else {
			throw new IllegalArgumentException(String.format("This module does not define anything named \"%s\".",
					identifier.getName()));
		}
	}
	
	/**
	 * Gets the submodule defined within this module with the
	 * given identifier.
	 * @param identifier The identifier of the submodule to get.
	 * @return The submodule named {@code identifier} within this module.
	 */
	public Module getSubmodule(Identifier identifier) {
		if(submodules.containsKey(identifier)) {
			return submodules.get(identifier);
		} else {
			throw new IllegalArgumentException(String.format("This module does not define a submodule named \"%s\".",
					identifier.getName()));
		}
	}
	
	/**
	 * Adds a definition to this module under the given identifier.
	 * @param identifier The identifier to which to bind the given value.
	 * @param value The value to bind to {@code identifier}.
	 */
	public void addDefinition(Identifier identifier, Expression value) {
		if(identifier.getModules().length == 0) {
			if(!definitions.containsKey(identifier)) {
				definitions.put(identifier, value);
			} else {
				throw new IllegalArgumentException(String.format("This module already defines a value with the name \"%s\".",
						identifier.getName()));
			}
		} else {
			throw new IllegalArgumentException("Module definition name cannot be qualified.");
		}
	}
	
	/**
	 * Adds a submodule to this module under the given identifier.
	 * @param identifier The identifier to which to associate with the given submodule.
	 * @param module The submodule to be named {@code identifier} in this module.
	 */
	public void addSubmodule(Identifier identifier, Module module) {
		if(identifier.getModules().length == 0) {
			if(!submodules.containsKey(identifier)) {
				submodules.put(identifier, module);
			} else {
				throw new IllegalArgumentException(String.format("This module already defines a submodule with the name \"%s\".",
						identifier.getName()));
			}
		} else {
			throw new IllegalArgumentException("Submodule name cannot be qualified.");
		}
	}
	
	/**
	 * Parses the content of a module from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return An {@link Module}, as parsed from the current input.
	 */
	public static Module parse(Parser parser) {
		Module module = new Module();
		while(parser.test(token -> token instanceof IdentifierToken)) {
			Token identifierToken = parser.current(1);
			Identifier identifier = Identifier.parse(parser);
			if(identifier.getModules().length > 0) {
				throw new ParserException("Module definition cannot be qualified.", identifierToken);
			}
			parser.expect(token -> token.isSymbolToken(SymbolTokenType.BINDING), "Expected back-arrow in module definition.");
			if(parser.test(token -> token.isSymbolToken(SymbolTokenType.PAREN_OPEN)) &&
			   parser.test(token -> token.isSymbolToken(SymbolTokenType.MODULE_BEGIN), 1)) {
				parser.next();
				parser.next();
				Module submodule = Module.parse(parser);
				parser.expect(token -> token.isSymbolToken(SymbolTokenType.PAREN_CLOSE), "Expected closing bracket to end module.");
				module.addSubmodule(identifier, submodule);
			} else {
				Expression expression = Expression.parse(parser);
				module.addDefinition(identifier, expression);
			}
			parser.expect(token -> token.isSymbolToken(SymbolTokenType.END_DECLARATION), "Expected semi-colon after module definition.");
		}
		return module;
	}
}
