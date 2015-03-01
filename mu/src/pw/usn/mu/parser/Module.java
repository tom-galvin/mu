package pw.usn.mu.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pw.usn.mu.tokenizer.IdentifierToken;
import pw.usn.mu.tokenizer.OperatorToken;
import pw.usn.mu.tokenizer.SymbolTokenType;
import pw.usn.mu.tokenizer.Token;

/**
 * Represents a collection of functions or values.
 */
public class Module extends Expression {
	private Map<String, Expression> definitions;
	private Map<String, Module> submodules;
	
	/**
	 * Initializes a new empty Module.
	 */
	public Module() {
		this.definitions = new HashMap<String, Expression>();
		this.submodules = new HashMap<String, Module>();
	}
	
	/**
	 * Gets an array of the names of all definitions in this module.
	 * @return The names of all (non-module) definitions defined within
	 * this module, as an array.
	 */
	public String[] getDefinitions() {
		Set<String> keys = definitions.keySet();
		String[] identifiers = new String[keys.size()];
		keys.toArray(identifiers);
		return identifiers;
	}
	
	/**
	 * Gets an array of the names of all submodules in this module.
	 * @return The names of all submodules defined within this module,
	 * as an array.
	 */
	public String[] getSubmodules() {
		Set<String> keys = submodules.keySet();
		String[] identifiers = new String[keys.size()];
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
	public Expression getDefinition(String identifier) {
		if(definitions.containsKey(identifier)) {
			return definitions.get(identifier);
		} else {
			throw new IllegalArgumentException(String.format("This module does not define anything named \"%s\".",
					identifier));
		}
	}
	
	/**
	 * Gets the submodule defined within this module with the
	 * given identifier.
	 * @param identifier The identifier of the submodule to get.
	 * @return The submodule named {@code identifier} within this module.
	 */
	public Module getSubmodule(String identifier) {
		if(submodules.containsKey(identifier)) {
			return submodules.get(identifier);
		} else {
			throw new IllegalArgumentException(String.format("This module does not define a submodule named \"%s\".",
					identifier));
		}
	}
	
	/**
	 * Adds a definition to this module under the given identifier.
	 * @param identifier The name to which to bind the given value.
	 * @param value The value to bind to {@code identifier}.
	 */
	public void addDefinition(String identifier, Expression value) {
		if(!definitions.containsKey(identifier)) {
			definitions.put(identifier, value);
		} else {
			throw new IllegalArgumentException(String.format("This module already defines a value with the name \"%s\".",
					identifier));
		}
	}
	
	/**
	 * Adds a submodule to this module under the given identifier.
	 * @param identifier The name to which to associate with the given submodule.
	 * @param module The submodule to be named {@code identifier} in this module.
	 */
	public void addSubmodule(String identifier, Module module) {
		if(!submodules.containsKey(identifier)) {
			submodules.put(identifier, module);
		} else {
			throw new IllegalArgumentException(String.format("This module already defines a submodule with the name \"%s\".",
					identifier));
		}
	}
	
	/**
	 * Parses the content of a module from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return A {@link Module}, as parsed from the current input.
	 */
	public static Module parse(Parser parser) {
		Module module = new Module();
		while(parser.test(token -> token instanceof IdentifierToken) ||
		      parser.test(token -> token.isSymbolToken(SymbolTokenType.PAREN_OPEN))) {
			Token identifierToken;
			Identifier identifier;
			boolean isSymbolIdentifer;
			
			if(parser.accept(token -> token.isSymbolToken(SymbolTokenType.PAREN_OPEN))) {
				parser.expect(token -> token instanceof OperatorToken, "Expected operator symbol in operator definition.");
				identifierToken = parser.current();
				identifier = new Identifier(((OperatorToken)identifierToken).getOperator());
				parser.expect(token -> token.isSymbolToken(SymbolTokenType.PAREN_CLOSE), "Expected closing bracket after operator symbol.");
				isSymbolIdentifer = true;
			} else {
				identifierToken = parser.current(1);
				identifier = Identifier.parse(parser);
				if(!identifier.isUnqualified()) {
					throw new ParserException("Module definition cannot be qualified.", identifierToken);
				}
				isSymbolIdentifer = false;
			}
			
			parser.expect(token -> token.isSymbolToken(SymbolTokenType.BIND), "Expected back-arrow in module definition.");
			if(parser.test(token -> token.isSymbolToken(SymbolTokenType.PAREN_OPEN)) &&
			   parser.test(token -> token.isSymbolToken(SymbolTokenType.MODULE_DECLARE), 1)) {
				if(isSymbolIdentifer) {
					throw new ParserException("Module definition cannot be bound to an operator symbol.", identifierToken);
				}
				parser.next();
				parser.next();
				Module submodule = Module.parse(parser);
				parser.expect(token -> token.isSymbolToken(SymbolTokenType.PAREN_CLOSE), "Expected closing bracket to end module.");
				module.addSubmodule(identifier.getName(), submodule);
			} else {
				Expression expression = Expression.parse(parser);
				module.addDefinition(identifier.getName(), expression);
			}
			parser.expect(
					token -> token.isSymbolToken(SymbolTokenType.SEPARATOR),
					String.format("Expected semi-colon after definition of %s.",
							identifier.toString()));
		}
		return module;
	}
}
