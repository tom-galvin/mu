package pw.usn.mu.parser;

/**
 * Represents a binding, wherein a value is bound to a name.
 */
public class Binding implements Parsable {
	private Identifier boundName;
	private Parsable boundValue;
	private Parsable content;
	
	public Binding(Identifier name, Parsable value, Parsable content) {
		if(name.getModules().length > 0) {
			throw new IllegalArgumentException("Binding name cannot be qualified.");
		}
		this.boundName = name;
		this.boundValue = value;
		this.content = content;
	}

	@Override
	public String toSource(int indentationLevel) {
		// TODO Auto-generated method stub
		return null;
	}
}
