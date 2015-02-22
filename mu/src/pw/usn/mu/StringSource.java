package pw.usn.mu;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pw.usn.mu.tokenizer.Location;
import pw.usn.mu.tokenizer.Token;
import pw.usn.mu.tokenizer.TokenizerRule;

/**
 * Represents a source that internally stores read data into a {@link String}.
 */
public class StringSource implements Source {
	private String data;
	private String name;
	private boolean closed;
	private int index, row, column;
	
	/**
	 * Initializes a new StringDataSource with the given source text and source name.
	 * @param data The data that is to be read from the source.
	 * @param name The name of the source. This is used for error reporting, and may contain
	 * a value such as a file name or terminal name.
	 */
	public StringSource(String data, String name) {
		this.data = data;
		this.name = name;
		this.index = 0;
		this.row = 1;
		this.column = 0;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean endOfSource() {
		return closed || index == data.length();
	}

	@Override
	public Token readToken(TokenizerRule rule) {
		if(closed) {
			throw new IllegalStateException("Cannot read a token after the source has been closed.");
		} else {
			Matcher matcher = rule.getPattern().matcher(data);
			if(matcher.find(index) && matcher.start() == index) {
				MatchResult result = matcher.toMatchResult();
				Token token = rule.read(getLocation(), result);
				advanceInput(result.group());
				return token;
			} else {
				return null;
			}
		}
	}
	
	/**
	 * Advances the location information for this stream source according to the content
	 * of the given match text. This updates the index, row and column information according to
	 * the characters in {@code matchText}, assuming that it immediately follows the previous
	 * matched text or the start of the stream.
	 * @param matchText
	 */
	private void advanceInput(String matchText) {
		for(int i = 0; i < matchText.length(); i++) {
			if(matchText.charAt(i) == '\n') {
				this.row += 1;
				this.column = 0;
			} else {
				this.column += 1;
			}
		}
		this.index += matchText.length();
	}

	@Override
	public void close() {
		closed = true;
	}

	@Override
	public String getRemainderOfLine() {
		Matcher matcher = Pattern.compile(".+?$", Pattern.MULTILINE).matcher(data);
		if(matcher.find(index) && matcher.start() == index) {
			return matcher.group();
		} else {
			return null;
		}
	}

	@Override
	public Location getLocation() {
		return new Location(this, row, column);
	}
}
