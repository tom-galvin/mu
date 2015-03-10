package pw.usn.mu.analyser;

import java.util.Random;
import java.util.logging.Logger;

import pw.usn.mu.tokenizer.Location;

/**
 * Represents a value in a mu program. Note that this only represents the value
 * rather than containing the value itself.
 */
public class Value {
	private long id;
	private static Random random = new Random();
	private String name;
	
	/**
	 * Initializes a new unique Value.
	 * @param name The original name of the value, as written in the source code.
	 * If this parameter has the value {@code null}, then the value is assumed to be
	 * a generated part of the program structure, rather than parsed from source code.
	 */
	public Value(String name) {
		this.name = name;
		this.id = random.nextLong();
	}

	/**
	 * Initializes a new unique Value.
	 */
	public Value() {
		this(null);
	}
	
	/**
	 * Gets the original name of the value, as written in the source code.
	 * @return The original name of this value.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Creates a reference to this value.
	 * @param location The original location, in a source, of the code that represents
	 * this reference.
	 * @return A {@link Reference} which refers to this value.
	 */
	public Reference newReference(Location location) {
		return new Reference(location, this);
	}
	
	@Override
	public boolean equals(Object obj) {
		return
				obj != null &&
				obj instanceof Value &&
				((Value)obj).id == id;
	}
	
	@Override
	public int hashCode() {
		return (int)(id & -1);
	}
	
	@Override
	public String toString() {
		if(name == null) {
			return String.format("val_%d", id);
		} else {
			return String.format("%s_%d", name, id);
		}
	}
}
