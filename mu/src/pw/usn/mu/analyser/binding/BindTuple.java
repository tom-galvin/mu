package pw.usn.mu.analyser.binding;

/**
 * Represents binding a set of values to the members of a tuple.
 */
public class BindTuple implements BindStructure {
	private BindStructure[] members;
	
	/**
	 * Initializes a new BindTuple with the given bind structure.
	 */
	public BindTuple(BindStructure... members) {
		this.members = members;
	}
	
	/**
	 * Gets the size of the tuple to decompose.
	 * @return The size of the tuple to decompose.
	 */
	public int getSize() {
		return members.length;
	}
	
	/**
	 * Gets the element at zero-based index {@code index} in this tuple
	 * decomposition structure.
	 * @param index The index in the range <b>[0, {@link #getSize()} - 1]</b>, of
	 * the decomposition structure to get.
	 * @return The element at index {@code index} in this tuple to decompose.
	 */
	public BindStructure getMember(int index) {
		return members[index];
	}
}
