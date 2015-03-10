package pw.usn.mu.analyser.module;

import pw.usn.mu.analyser.Value;

/**
 * Represents a defined value in a module in a mu program.
 */
public class ModuleValue extends Value {
	private Module parent;
	
	/**
	 * Initializes a new unique ModuleValue.
	 * @param name The original name of the value, as written in the source code of
	 * the module in which this value is defined.
	 * @param parent The module in which this value is defined.
	 */
	public ModuleValue(String name, Module parent) {
		super(name);
		this.parent = parent;
	}
	
	/**
	 * Gets the module in which this value is defined.
	 * @return The module in which this value is defined.
	 */
	public Module getParent() {
		return parent;
	}
}
