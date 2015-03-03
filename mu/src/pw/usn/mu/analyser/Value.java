package pw.usn.mu.analyser;

import java.util.Random;

import pw.usn.mu.tokenizer.Location;

/**
 * Represents a value in a mu program. Note that this only represents the value
 * rather than containing the value itself.
 */
public class Value {
	private long id;
	private static Random random = new Random();
	
	/**
	 * Initializes a new unique Value.
	 */
	public Value() {
		this.id = random.nextLong();
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
}
