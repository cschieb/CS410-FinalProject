package com.cs410.finalproject.testing;

import java.io.IOException;

import com.cs410.finalproject.operations.CKYParser;
import com.cs410.finalproject.operations.Simplifier;
import com.cs410.finalproject.operations.ChomskyConverter;
import com.cs410.finalproject.parsing.Parser;
import com.cs410.finalproject.ioutils.GrammarWriter;
import com.cs410.finalproject.models.Grammar;

public class ProgramTester {
	
	public static void main (String[] args) {
		Parser fileParser = new Parser();
		Grammar parsedGrammar = null;
		
		try {
			parsedGrammar = fileParser.parseGrammarFromFile("AnotherCFG.txt");
		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		
		if (parsedGrammar != null) {
			System.out.println(parsedGrammar.toString());

			Simplifier simplifier = new Simplifier();
			ChomskyConverter converter = new ChomskyConverter();
			Grammar simplifiedGrammar = simplifier.simplify(parsedGrammar);
			System.out.println(simplifiedGrammar.toString());
			Grammar chomskyNormalGrammar = converter.convertToChomsky(simplifiedGrammar);
			System.out.println(chomskyNormalGrammar.toString());
			
			GrammarWriter.writeToFile("output.txt", chomskyNormalGrammar.toString());
			
			CKYParser stringInGrammar = new CKYParser();
			stringInGrammar.parseCKY(chomskyNormalGrammar);
		}
		else {
			System.out.println("The parsed Grammar was null, so execution of the program has ceased.");
		}
	}
}
