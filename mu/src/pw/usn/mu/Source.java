package pw.usn.mu;

import java.io.Closeable;

import pw.usn.mu.tokenizer.Location;
import pw.usn.mu.tokenizer.Token;
import pw.usn.mu.tokenizer.TokenizerRule;

/**
 * A source from which {@link pw.usn.mu.tokenizer.Token Token}s can be read. For example,
 * this could represent a source code file on a hard drive.
 */
public interface Source extends Closeable {
	/**
	 * Gets the name of this source file. For example, this might return the file
	 * name of a source code file.
	 * @return The name of the source, dependent on its implementation.
	 */
	public String getName();
	
	/**
	 * Determines if the end of the source has been reached.
	 * @return {@code true} if no more data can be read from this source; {@code false} otherwise.
	 */
	public boolean endOfSource();
	
	/**
	 * Attempts to read a token from this source using the given tokenizer rule.
	 * This will also advance the source reader ahead to the end of the token that was just
	 * read, so this method is not idempotent.
	 * @param rule The rule with which to attempt to read a token.
	 * @return Returns {@code null} if no token can be read using {@code rule} from this
	 * input source; otherwise, returns the token that was read.
	 */
	public Token readToken(TokenizerRule rule);
	
	/**
	 * Reads data up to (and excluding) the next new line in the source.
	 * @return A string representing all data in the source up to and excluding the next new-line read.
	 */
	public String getRemainderOfLine();
	
	/**
	 * @return Returns the current location of the reading head in the source.
	 */
	public Location getLocation();
}
