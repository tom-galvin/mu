package pw.usn.mu.tokenizer;

import pw.usn.mu.Source;

/**
 * Represents the location of an element of a program, such as a tokenized token, a parsed
 * AST node or an analysis object, within the original source from which it was read.
 */
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
	 * Initializes a Location used to denote that an element of a program was generated,
	 * and is not actually represented in source code.
	 * @param source The source containing this location.
	 */
	public Location(Source source) {
		this(source, 0, 0);
	}

	/**
	 * Initializes a Location used to denote that an element of a program was generated,
	 * and is not actually represented in source code.
	 */
	public Location() {
		this(null);
	}
	
	/**
	 * Gets the location of the token.
	 * @return A string in the format {@code source@row:column} representing the location of the
	 * token in the source it was read from, and its location in that source.
	 */
	@Override
	public String toString() {
		if(source== null) {
			return "generated";
		} else if(row == 0) {
			return String.format("%s@generated", source.getName());
		} else {
			return String.format("%s@%d:%d", source.getName(), row, column);
		}
	}

	/**
	 * Gets the source containing the location.
	 * @return A {@link Source} representing the source containing the location, or {@code null}
	 * if this Location represents automatically-generated code that is not related to any
	 * source in particular.
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
