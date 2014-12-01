package com.cs410.finalproject.models;

import java.util.List;

/**
 * @author Colin
 * Class which holds objects representing the different states of a grammar, including
 * each possible derivation.
 */
public class State {
	
	/**
	 * The character denoting the "name" of the state.
	 */
	private char nonTerminal;
	
	/**
	 * The list of possible derivations within this state.
	 */
	private List<String> derivations;
	
	public State(char nonTerminal) {
		this.nonTerminal = nonTerminal;
	}

	/**
	 * Gets the nonTerminal for this state.
	 * @return the state's nonTerminal
	 */
	public char getNonTerminal() {
		return nonTerminal;
	}

	/**
	 * Sets a new nonTerminal for this state.
	 * @param nonTerminal the new nonTerminal to be set.
	 */
	public void setNonTerminal(char nonTerminal) {
		this.nonTerminal = nonTerminal;
	}

	/**
	 * Gets the list of derivations for this state.
	 * @return the list of derivations
	 */
	public List<String> getDerivations() {
		return derivations;
	}

	/**
	 * Sets a new list of derivations for this state.
	 * @param derivations the new list of derivations to be set
	 */
	public void setDerivations(List<String> derivations) {
		this.derivations = derivations;
	}
	
	/**
	 * Removes a derivation from this state's list of derivations.
	 * @param derivation the derivation to be removed
	 */
	public void removeDerivation(String derivation){
		derivations.remove(derivation);
	}
	
	/**
	 * Adds a new derivation to this state's list of derivations.
	 * @param derivation the new derivation to be added
	 */
	public void addDerivation(String derivation) {
		derivations.add(derivation);
	}
	
}
