package pw.usn.mu.parser.type;

import java.util.List;
import java.util.ArrayList;

import pw.usn.mu.parser.Identifier;
import pw.usn.mu.parser.Parser;
import pw.usn.mu.parser.ParserException;
import pw.usn.mu.tokenizer.SymbolTokenType;

/**
 * Represents a reference (with an identifier) to an already defined type.
 */
public class DefinedType implements Type {
	private Identifier identifier;
	private Type[] parameters;
	
	/**
	 * Initializes a new DefinedType, with the given identifier to which this defined
	 * type refers to, and any type parameters.
	 * @param identifier The identifier of the defined type which this DefinedType
	 * refers to.
	 * @param parameters Type parameters to specialize this this type reference, if
	 * it refers to a generic type.
	 */
	public DefinedType(Identifier identifier, Type[] parameters) {
		this.identifier = identifier;
		this.parameters = parameters != null ? parameters : new Type[0];
	}
	
	/**
	 * Initializes a new DefinedType, with the given identifier to which this defined
	 * type refers to.
	 * @param identifier The identifier of the defined type which this DefinedType
	 * refers to.
	 */
	public DefinedType(Identifier identifier) {
		this(identifier, null);
	}
	
	/**
	 * Gets the identifier which this defined type refers to.
	 * @return The identifier of this defined type.
	 */
	public Identifier getIdentifier() {
		return identifier;
	}
	
	/**
	 * Gets the number of type parameters in this defined type reference.
	 * @return The number of type parameters specified in this type.
	 */
	public int getParametersLength() {
		return parameters.length;
	}
	
	/**
	 * Gets the {@code index}-th type parameter in this defined type reference,
	 * where {@code index}={@code 0} is the first type parameter specified.
	 * @param index The index of the type parameter to get.
	 * @return The specified type parameter.
	 */
	public Type getParameter(int index) {
		return parameters[index];
	}
	
	/**
	 * Parses a defined type reference from the given parser state.
	 * @param parser The parser enumerator to use.
	 * @return A {@link DefinedType}, as parsed from the current input.
	 */
	public static DefinedType parse(Parser parser) {
		Identifier identifier = Identifier.parse(parser);
		if(parser.accept(token -> token.isOperatorToken("<"))) {
			if(parser.test(token -> token.isOperatorToken(">"))) {
				throw new ParserException("Type parameters expected.", parser.current(1));
			}
			List<Type> types = new ArrayList<Type>();
			do {
				types.add(Type.parse(parser));
			} while(parser.accept(token -> token.isSymbolToken(SymbolTokenType.COMMA)));
			parser.expect(token -> token.isOperatorToken(">"), "End of type parameter list expected.");
			Type[] typesArray = new Type[types.size()];
			types.toArray(typesArray);
			return new DefinedType(identifier, typesArray);
		} else {
			return new DefinedType(identifier);
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj != null && obj instanceof DefinedType) {
			DefinedType that = (DefinedType)obj;
			if(!this.identifier.equals(that.identifier)) {
				return false;
			}
			if(this.parameters.length != that.parameters.length) {
				return false;
			}
			for(int i = 0; i < this.parameters.length; i++) {
				if(this.parameters[i].equals(that.parameters[i])) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int code = 0x89a00326;
		code ^= identifier.hashCode();
		for(int i = 0; i < parameters.length; i++) {
			code ^= this.parameters[i].hashCode() << (i % 3);
		}
		return code;
	}
}
