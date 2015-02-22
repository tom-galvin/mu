package pw.usn.mu.tokenizer;

import pw.usn.mu.Source;

public class Location {
	private int row, column;
	private Source source;
	
	/**
	 * Initializes a new Location class.
	 * @param source The source containing this location.
	 * @param row The row in the source, starting from row 1.
	 * @param column The column in the source, starting from column 0 (the left of the row).
	 */
	public Location(Source source, int row, int column) {
		this.row = row;
		this.column = column;
		this.source = source;
	}
	
	/**
	 * Gets the location of the token.
	 * @return A string in the format {@code source@row:column} representing the location of the
	 * token in the source it was read from, and its location in that source.
	 */
	@Override
	public String toString() {
		return String.format("%s@%d:%d", source.getName(), row, column);
	}

	/**
	 * Gets the source containing the location.
	 * @return A {@link Source} representing the source containing the location.
	 */
	public final Source getSource() {
		return source;
	}
	
	/**
	 * Gets the row in the source code containing the location.
	 * @return The row (starting from row 1, i.e. line 1) where the location is.
	 */
	public final int getRow() {
		return row;
	}
	
	/**
	 * Gets the column in the source code containing the location.
	 * @return The column (starting from column 0, i.e. the left of the row) where the location is.
	 */
	public final int getColumn() {
		return column;
	}
}
