package pw.usn.mu.parser.type;

import pw.usn.mu.parser.Parser;
import pw.usn.mu.parser.ParserException;
import pw.usn.mu.tokenizer.IdentifierToken;
import pw.usn.mu.tokenizer.SymbolTokenType;

/**
 * Defines methods for parsing type-related parts of mu's grammar.
 */
public final class TypeGrammar {
	/**
	 * Parses an atomic type - that is, one such as an identifier or a
	 * parenthesized type.
	 * @param parser The parser enumerator to use.
	 * @return A type, as parsed from the current input.
	 */
	public static Type parseAtomic(Parser parser) {
		if(parser.test(token -> token instanceof IdentifierToken)) {
			return DefinedType.parse(parser);
		} else if(parser.test(token -> token.isSymbolToken(SymbolTokenType.SEQUENCE_OPEN))) {
			return SequenceType.parse(parser);
		} else if(parser.accept(token -> token.isSymbolToken(SymbolTokenType.PAREN_OPEN))) {
			Type type = Type.parse(parser);
			parser.expect(token -> token.isSymbolToken(SymbolTokenType.PAREN_CLOSE), "Expected closing parenthesis in type definition.");
			return type;
		} else {
			throw new ParserException("Unexpected token in type.", parser.current(1));
		}
	}
}
