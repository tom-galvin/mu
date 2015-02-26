package pw.usn.mu.parser.type;

import java.util.ArrayList;
import java.util.List;

import pw.usn.mu.parser.Parser;

/**
 * Represents a product type (ie. that of a tuple).
 */
public class ProductType implements Type {
	private Type[] types;
	
	/**
	 * Initializes a new ProductType with the given {@code types}.
	 * @param values The values contained within this tuple.
	 */
	public ProductType(Type... types) {
		if(types.length > 1) {
			this.types = types;
		} else {
			throw new IllegalArgumentException("Product type must have at least two types.");
		}
	}
	
	/**
	 * Gets the length of the tuple.
	 * @return The length (number of type) in this tuple.
	 */
	public int getLength() {
		return types.length;
	}
	
	/**
	 * Gets the {@code index}-th type in the tuple type, where {@code index}={@code 0} is
	 * the first type in the tuple.
	 * @param index The position in the tuple of the type to get.
	 * @return The specified type in the suple.
	 */
	public Type getValues(int index) {
		return types[index];
	}
	
	/**
	 * Parses a product type from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return A type, as parsed from the current input. This cannot return a concrete
	 * {@link ProductType} as the product type parsed may be a product of one type, in which
	 * case the type itself is returned.
	 */
	public static Type parse(Parser parser) {
		List<Type> types = new ArrayList<Type>();
		do {
			types.add(FunctionType.parse(parser));
		} while(parser.accept(token -> token.isOperatorToken("*")));
		if(types.size() == 1) {
			return types.get(0);
		} else {
			Type[] typesArray = new Type[types.size()];
			types.toArray(typesArray);
			return new ProductType(typesArray);
		}
	}
}