package pw.usn.mu;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Represents a source that reads data from a stream, such as a file.
 */
public class StreamSource extends StringSource {
	/**
	 * Initializes a new StreamSource with the given stream and source name.
	 * @param stream The stream from which to read the source data.
	 * @param name The name of the source. This is used for error reporting, and may contain
	 * a value such as a file name or terminal name.
	 */
	public StreamSource(InputStream stream, String name) {
		super(readToEnd(stream), name);
	}
	
	/**
	 * Creates a new StreamSource from a {@link java.io.File File} object. The source name will
	 * be set according to the file name.
	 * @param file The file from which to read the source data.
	 * @return A {@link StreamSource}, using a {@link java.io.InputStream FileInputStream} based
	 * on {@code file}.
	 * @throws IOException
	 */
	public static StreamSource fromFile(File file) throws IOException {
		if(file.exists() && file.isFile()) {
			return new StreamSource(new FileInputStream(file), file.getName());
		} else {
			throw new IOException("The source file must be a file that already exists.");
		}
	}
	
	/**
	 * Reads {@code stream} to the end.
	 * @param stream The input stream to read.
	 * @return All text data contained in {@code stream} as a string, starting from the current position.
	 */
	@SuppressWarnings("resource")
	private static String readToEnd(InputStream stream) {
		try(Scanner scanner = new Scanner(stream).useDelimiter("\\A")) {
			return scanner.next();
		}
	}
}
