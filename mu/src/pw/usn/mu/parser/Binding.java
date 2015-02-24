package pw.usn.mu.parser;

import pw.usn.mu.tokenizer.SymbolTokenType;

/**
 * Represents a binding, wherein a value is bound to a name.
 */
public class Binding implements Parsable {
	private Identifier name;
	private Parsable value;
	private Parsable content;
	
	/**
	 * Initializes a new Binding with the given name and value, and content which
	 * can refer to the bound value by the given name.
	 * @param name The name of the newly-bound value, represented as an identifier.
	 * @param value The value to bind.
	 * @param content The content of the binding.
	 */
	public Binding(Identifier name, Parsable value, Parsable content) {
		if(name.getModules().length > 0) {
			throw new IllegalArgumentException("Binding name cannot be qualified.");
		}
		this.name = name;
		this.value = value;
		this.content = content;
	}

	/**
	 * Gets the name of the binding.
	 * @return The name to which the value is bound in this binding. For example, if
	 * the value {@code "hello"} is bound to the name {@code myString}, then this
	 * function returns {@code myString}.
	 */
	public Identifier getName() {
		return name;
	}
	
	/**
	 * Gets the value bound in this binding.
	 * @return The value bound to the name returned by {@link Binding#getName()
	 * getName()} in this binding.
	 */
	public Parsable getValue() {
		return value;
	}
	
	/**
	 * Gets the content of this binding.
	 * @return The content of this binding. Any expressions within {@code content} can
	 * refer to the identifier returned by {@link Binding#getName() getName()} to access
	 * the value bound to the name with this binding.
	 */
	public Parsable getContent() {
		return content;
	}
	
	@Override
	public String toSource(int indentationLevel) {
		return String.format("%s %s %s\n%s%s",
				value.toSource(indentationLevel),
				SymbolTokenType.BINDING.getDisplayString(),
				name.toSource(indentationLevel),
				Parsable.indent(indentationLevel),
				content.toSource(indentationLevel));
	}
}
