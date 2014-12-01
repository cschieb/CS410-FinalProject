package com.cs410.finalproject.testing;

import java.io.IOException;

import com.cs410.finalproject.operations.Simplifier;
import com.cs410.finalproject.operations.ChomskyConverter;
import com.cs410.finalproject.input.Parser;
import com.cs410.finalproject.ioutils.GrammarWriter;
import com.cs410.finalproject.models.Grammar;

public class ProgramTester {
	
	public static void main (String[] args) {
		
		Parser fileParser = new Parser();
		Grammar parsedGrammar = null;
		
		try {
			parsedGrammar = fileParser.parseGrammarFromFile("AnotherCFG.txt");
		} catch (IOException e) {
			System.out.println("File was not found, or there was an error reading it.");
			e.printStackTrace();
		}
		
		System.out.println(parsedGrammar.toString());
		
		Simplifier simplifier = new Simplifier();
		ChomskyConverter converter = new ChomskyConverter();
		Grammar simplifiedGrammar = simplifier.simplify(parsedGrammar);
		System.out.println(simplifiedGrammar.toString());
		Grammar chomskyNormalGrammar = converter.convertToChomsky(simplifiedGrammar);
		System.out.println(chomskyNormalGrammar.toString());
		
		GrammarWriter.writeToFile("output.txt", chomskyNormalGrammar.toString());
	}
}
