package com.cs410.finalproject.ioutils;

import java.io.File;
import java.io.PrintWriter;
import java.io.Writer;

public class GrammarWriter {
	
	/**
	 * Writes a grammar to the file location provided. If the file does not exist, it is created. If it does exist, it is overwritten.
	 * @param filePath the path to the output location
	 * @param grammarString the grammar, in string form, to be written to the file
	 */
	public static void writeToFile(String filePath, String grammarString) {
		
		//Create a file object from the provided path
		File fileToWrite = new File(filePath);
		Writer writer;
		
		try {
			//Try to create the file, if it already exists, notify user that the file is being overwritten
			if (!fileToWrite.createNewFile()) {
				System.out.println("File overwritten.");
			}
			//Initialize writer and write the grammar to the file, and flush the writer
			writer = new PrintWriter(fileToWrite, "UTF-8");
			writer.write(grammarString);
			writer.flush();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
