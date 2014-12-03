package com.cs410.finalproject.ioutils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * @author Colin
 *Class which is responsible for reading a grammar in from a file
 */
public class GrammarReader {
	
	/**
	 * Method which reads in a grammar from a file, and returns a Reader, which has been set up to read from that file.
	 * @param path the path to the file which will be read
	 * @return the Reader that is configured to read the given file
	 * @throws IOException thrown when the file path supplied leads to an incorrect location (nonexistent file or directory)
	 */
	public static Reader readFromFile(String path) throws IOException {
		
		File toRead = new File(path);
		BufferedReader configuredReader;
		
		if (!toRead.exists() || toRead.isDirectory()) {
			throw new IOException("The indicated input file path either doesn't exist, or points to a directory. Check the "
					+ "path and try again.");
		}
		else {
			configuredReader = new BufferedReader(new FileReader(toRead));
		}
		
		return configuredReader;
	}

}
