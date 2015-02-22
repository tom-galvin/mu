package pw.usn.mu.tokenizer;

/**
 * Represents an object for creating {@link Tokenizer}s.
 */
public interface TokenizerFactory {
	/**
	 * Create a {@link Tokenizer}.
	 * @return Returns a tokenizer with factory-specific properties.
	 */
	public Tokenizer create();
}
