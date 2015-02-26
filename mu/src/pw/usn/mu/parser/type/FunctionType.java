package pw.usn.mu.parser.type;

import java.util.Stack;

import pw.usn.mu.parser.Parser;
import pw.usn.mu.tokenizer.SymbolTokenType;

/**
 * Represents a function type (ie. that of a tagged union).
 */
public class FunctionType implements Type {
	private Type from, to;
	
	/**
	 * Initializes a new FunctionType with the given {@code from} and {@code to} type.
	 * @param from The type of the function argument.
	 * @param to The type of the function return value.
	 */
	public FunctionType(Type from, Type to) {
		this.from = from;
		this.to = to;
	}
	
	/**
	 * Gets the type of the function argument.
	 * @return The type of the function argument of functions of this type.
	 */
	public Type getFrom() {
		return from;
	}
	
	/**
	 * Gets the type of the function return value.
	 * @return The type of the function return value of functions of this type.
	 */
	public Type getTo() {
		return to;
	}
	
	/**
	 * Parses a function type from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return A type, as parsed from the current input. This cannot return a concrete
	 * {@link FunctionType} as no function symbol ({@code ->}) may be parsed, meaning
	 * that no function is present.
	 */
	public static Type parse(Parser parser) {
		Stack<Type> types = new Stack<Type>();
		do {
			types.push(TypeGrammar.parseAtomic(parser));
		} while(parser.accept(token -> token.isSymbolToken(SymbolTokenType.FUNCTION)));
		Type currentType = types.pop();
		while(!types.empty()) {
			currentType = new FunctionType(types.pop(), currentType);
		}
		return currentType;
	}
}
