package com.cs410.finalproject.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cs410.finalproject.models.Grammar;
import com.cs410.finalproject.models.State;



/**
 * @author Colin
 * Class containing methods to parse a Grammar and return a Grammar object representing it.
 */
public class Parser {
	
	/**
	 * The Grammar that has been/is currently being parsed.
	 */
	private Grammar parsedGrammar;
	
	/**
	 * Parse a Grammar from a file.
	 * @param filePath the name of the file containing the Grammar to parse
	 * @return the Grammar object representing the parsed Grammar
	 * @throws IOException 
	 */
	public Grammar parseGrammarFromFile(String filePath) throws IOException {
		//Create new Grammar and set up file to be read
		parsedGrammar = new Grammar();
		File toParse = new File(filePath);
		FileReader fReader = new FileReader (toParse);
		BufferedReader reader = new BufferedReader(fReader);
		
		//Create boolean flag to indicate if the file is currently in the first few lines of parsing (The lines before the rules)
		boolean initialSetup = true;
		
		//Read through file and parse each line accordingly
		String currentLine = reader.readLine();
		
		if (!(currentLine == null)) {
			while (!currentLine.isEmpty() && !(currentLine.trim().equals(""))) {
				if (initialSetup) {
					if (currentLine.startsWith("V:")) {
						parseStates((currentLine.split(":")[1]).trim());
					}
					else if (currentLine.startsWith("T:")) {
						parseTerminals(currentLine.split(":")[1].trim());
					}
					else if (currentLine.startsWith("S:")) {
						parseStartState(currentLine.split(":")[1].trim());
					}
					//With the current file format, all lines after 'P:' should be rules, so there is
					//no need to keep checking for these first four cases.
					else if (currentLine.startsWith("P:")) {
						initialSetup = false;
					}
				}
				else {
					parseRules(currentLine.trim());
				}
				currentLine = reader.readLine();
				
				if (currentLine == null) {
					currentLine = "";
				}
			}
		}
		//Close bufferedreader and return finished grammar
		reader.close();
		return parsedGrammar;
	}
	
	/**
	 * Parse the list of states (should be first line of file) and create list of State objects,
	 * and set the list to parsedGrammar's 'states' member variable.
	 * @param newStates the input string from the file
	 */
	public void parseStates(String newStates) {
		String[] splitStates = newStates.split(",");
		List<State> statesToAdd = new ArrayList<State>();
		for (String state: splitStates) {
			state = state.trim();
			State newState = new State(state.charAt(0));
			statesToAdd.add(newState);
		}
		parsedGrammar.setStates(statesToAdd);
	}
	
	/**
	 * Parse the list of terminals(should be second line of file) and create a list of Characters,
	 * which represent the terminals, and set the list to parsedGrammar's 'terminals' member variable.
	 * @param terminals the input string from the file
	 */
	public void parseTerminals(String terminals) {
		String[] splitTerminals = terminals.split(",");
		List<Character> terminalsToAdd = new ArrayList<Character>();
		for (String terminal: splitTerminals) {
			terminal = terminal.trim();
			terminalsToAdd.add(terminal.charAt(0));
		}
		parsedGrammar.setTerminals(terminalsToAdd);
	}
	
	/**
	 * Parse the line containing the start state(should be the third line of file) and search for that 
	 * state within parsedGrammar's current list of states, and set that state as parsedGrammar's
	 * start state.
	 * @param startState the input string from the file
	 */
	public void parseStartState(String startState) {
		parsedGrammar.setStartState(parsedGrammar.getStateWithName(startState.trim().charAt(0)));
	}
	
	/**
	 * Parse the lines containing the rules(all lines of the file after 'P:') and add the rules as
	 * new rule objects to parsedGrammar's list of rules, and set the derivations to the correct
	 * states within parsedGrammar's list of states.
	 * @param rulesToParse the input string from the file
	 */
	public void parseRules(String rulesToParse) {
		State stateToEdit = parsedGrammar.getStateWithName(rulesToParse.charAt(0));
		List<String> derivationsToAdd = new ArrayList<String>();
		String[] unparsedDerivations = rulesToParse.split("->")[1].trim().split("\\|");
		for (String derivation: unparsedDerivations) {
			derivation = derivation.trim();
			derivationsToAdd.add(derivation);
			
		}
		
		stateToEdit.setDerivations(derivationsToAdd);
	}

}
