package pw.usn.mu.analyser.builtin;

import pw.usn.mu.analyser.Value;

/**
 * Represents the type of a built-in function.
 */
public class Builtin extends Value {
	private int arity;
	
	/**
	 * Initializes a new Builtin with the given arity.
	 * @param name The name of the built-in function.
	 * @param arity The arity of the built-in function represented.
	 */
	public Builtin(String name, int arity) {
		super(name);
		this.arity = arity;
	}
	
	/**
	 * Gets the arity of this function, i.e. the number of arguments
	 * it accepts.
	 * @return The arity of this built-in function.
	 */
	public int getArity() {
		return this.arity;
	}
}
