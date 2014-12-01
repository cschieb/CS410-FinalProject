package com.cs410.finalproject.models;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Colin
 * Class which represents a Context-Free Grammar.
 */
public class Grammar {
	/**
	 * List of all the states within this Grammar.
	 */
	private List<State> states;
	
	/**
	 * List of all possible terminals(lowercase letters) within this Grammar.
	 */
	private List<Character> terminals;
	
	/**
	 * Pointer to the start state for this Grammar.
	 */
	private State startState;
	
	/**
	 * Gets the current list of states for this Grammar.
	 * @return the list of states
	 */
	public List<State> getStates() {
		return states;
	}

	/**
	 * Sets a new list of states for this Grammar.
	 * @param states the new state list to be set
	 */
	public void setStates(List<State> states) {
		this.states = states;
	}
	
	public List<State> removeState(State toRemove){
		states.remove(toRemove);
		return states;
	}

	/**
	 * Gets the current list of terminals for this Grammar.
	 * @return the list of terminals
	 */
	public List<Character> getTerminals() {
		return terminals;
	}

	/**
	 * Sets a new list of terminals for this Grammar.
	 * @param terminals the new terminals to be set
	 */
	public void setTerminals(List<Character> terminals) {
		this.terminals = terminals;
	}

	/**
	 * Gets the current start state of this Grammar.
	 * @return the current start state
	 */
	public State getStartState() {
		return startState;
	}

	/**
	 * Sets a new start state for this Grammar.
	 * @param startState the new start state to be set
	 */
	public void setStartState(State startState) {
		this.startState = startState;
	}
	
	/**
	 * Searches through the current list of states for the state with the name specified 
	 * by the input char.
	 * @param stateName the name of the state to search for
	 * @return the requested state, or null if the state is not in the current list of states
	 */
	public State getStateWithName(char stateName) {
		for (State state: states) {
			if (state.getNonTerminal() == stateName) {
				return state;
			}
		}
		return null;
	}
	
	/**
	 * Removes any empty states within this grammar instance.
	 */
	public void removeEmptyStates() {
		if (!states.isEmpty()) {
			for (int indexOfState = 0; indexOfState < states.size(); indexOfState++) {
				if (states.get(indexOfState).getDerivations().isEmpty()) {
					states.remove(indexOfState);
				}
			}
		}
		else {
			System.out.println("There are no states in this grammar, so no empty ones were removed.");
		}
	}
	
	/* 
	 * Builds and returns a string representing this Grammar in a form similar to the input file format.
	 * @return the string representation of the Grammar
	 */
	public String toString() {
		String objectString = "";
		objectString += "States: ";
		//Step through the list of states, adding each one to the output string
		for (State state: states) {
			objectString += state.getNonTerminal();
			
			if (!(states.indexOf(state) == (states.size()-1))) {
				objectString += ", ";
			}
		}
		//Step through the list of terminals, adding each one to the output string
		objectString += "\n" + "Terminals: ";
		for (char terminal: terminals) {
			objectString += terminal;
			
			if (!(terminals.indexOf(terminal) == (terminals.size()-1))) {
				objectString += ", ";
			}
		}
		objectString += "\n" + "Start State: " + startState.getNonTerminal() + "\n" + "Rules: " + "\n";
		//Step through the list of rules, adding each one to the output string
		for (State state: states) {
			objectString += state.getNonTerminal() + ": ";
			ArrayList<String> derivations = (ArrayList<String>) state.getDerivations();
			for (String derivation: derivations) {
				objectString += derivation;
				
				if (!(derivations.indexOf(derivation) == (derivations.size()-1))) {
					objectString += "|";
				}
			}
			
			objectString += "\n";
		}
		return objectString;
	}
}
