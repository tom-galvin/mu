package pw.usn.mu.parser.type;

import pw.usn.mu.parser.Parser;
import pw.usn.mu.tokenizer.SymbolTokenType;

/**
 * Represents type of a sequence of another type.
 */
public class SequenceType implements Type {
	Type of;
	
	/**
	 * Initialize a new sequence type.
	 * @param of The type that this is a sequence of.
	 */
	public SequenceType(Type of) {
		this.of = of;
	}
	
	/**
	 * Returns the type of which this is a sequence.
	 * @return The sequence type of the SequenceType.
	 */
	public Type getSequenceType() {
		return of;
	}
	
	/**
	 * Parses a sequence type from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return A {@link SequenceType}, as parsed from the current input.
	 */
	public static SequenceType parse(Parser parser) {
		parser.expect(token -> token.isSymbolToken(SymbolTokenType.SEQUENCE_OPEN), "Expected start of sequence.");
		Type of = Type.parse(parser);
		parser.expect(token -> token.isSymbolToken(SymbolTokenType.SEQUENCE_CLOSE), "Expected end of sequence.");
		return new SequenceType(of);
	}
}
