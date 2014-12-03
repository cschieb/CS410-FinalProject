package com.cs410.finalproject.operations;

import java.util.ArrayList;
import java.util.List;

import com.cs410.finalproject.models.Grammar;
import com.cs410.finalproject.models.State;

public class Simplifier {

	/**
	 * @author Colin/Matt
	 * Removes all epsilon derivations from a grammar, using the removeEps algorithm.
	 * @param cfg the grammar whose epsilon derivations will be removed
	 */
	@SuppressWarnings("unchecked")
	private void removeEpsilon(Grammar cfg) {
		//Initialize a list for the nullable variables, and get the list of states from the Grammar
		List<State> nullables = new ArrayList<State>();
		ArrayList<State> statesInGrammar = (ArrayList<State>) cfg.getStates();
		
		//Initialize boolean flag to determine whether or not a variable is nullable
		boolean isNullable;
		
		//Iterate through each state, and seqsequently through each derivation in said state,
		//and if one derivation contains '$'(epsilon), add that state to the list of nullables
		for (State state: statesInGrammar) {
			//reset flag for each new state
			isNullable = false;
			for (String derivation: state.getDerivations()) {
				//If an epsilon is found, set the boolean flag to true
				if (derivation.equals("$")) {
					isNullable = true;
				}
			}
			//If this state has been marked as nullable, add it to the nullable list
			if (isNullable) {
				nullables.add(state);
			}
		}
		
		//Initialize boolean flag to indicate if a change has been made to the nullable list during
		//an iteration of the following loop.
		boolean newNullableAdded;
		
		//Create a copy of the list of states within the grammar, and remove all current nullables
		//from said list.
		ArrayList<State> nonNullableStates = (ArrayList<State>) ((ArrayList<State>)cfg.getStates()).clone();
		nonNullableStates.removeAll(nullables);
		
		//Step through the list of non nullable states, and check for any states that qualify for 
		//the second property of nullable variables, in which one or more derivations for a state
		//contain all nullable variables.
		do {
			newNullableAdded = false;
			int nullableVariableCount;
			
			//Step into a state, and then into each of its' derivations, until all states have been
			//iterated through.
			for (State state: nonNullableStates) {
				for (String derivation: state.getDerivations()) {
					nullableVariableCount = 0;
					
					//Split the derivation into a character array and compare each character in the array
					//to see if it matches up with one of the nullable variables
					char[] splitDerivation = derivation.toCharArray();
					for (char variable: splitDerivation) {
						
						for (State nullableState: nullables) {
							//If a nullable variable is found in the character array, increase the count
							//of nullable variables in the derivation by 1.
							if (nullableState.getNonTerminal() == variable) {
								nullableVariableCount++;
							}
						}
					}
					//If every character in the derivation is nullable, add that state to the list of
					//nullable states, then remove it from the list of non nullable states
					if (nullableVariableCount == derivation.length()) {
						nullables.add(state);
						nonNullableStates.remove(state);
						//Indicate that a new state has been added to the list of nullables, so another
						//iteration should be done to check for new nullables.
						newNullableAdded = true;
					}
				}
			}
		} while(newNullableAdded);
		
		
		ArrayList<String> derivations;
		//Get the list of derivations for each state, and create lists of derivations to add and remove
		//from the list of derivations for that state.
		for (State state: statesInGrammar) {
			derivations = (ArrayList<String>) state.getDerivations();
			ArrayList<String> derivationsToAdd = new ArrayList<String>();
			ArrayList<String> derivationsToRemove = new ArrayList<String>();
			
			//This step checks for any modifiable rules, that is, if a derivation of a rule contains a
			//nullable variable and is not a unit production, a new rule is added that is a copy of
			//the same derivation, but with the nullable variable removed. This accounts for the chance
			//that said nullable variable is never generated.
			for (String derivation: derivations) {
				for (State nullableState: nullables) {
					if (derivation.contains(((Character)nullableState.getNonTerminal()).toString())
							&& derivation.length() != 1) {
						
						//makes a char array of a derivation so that it can be manipulated to remove any nullable variables
						char[] derivationToChar = derivation.toCharArray();
						char[] newDerivationToAdd = new char[derivation.length()-1];
						int index=0;
						for(char c : derivationToChar){
							if(c!=nullableState.getNonTerminal()){
								newDerivationToAdd[index]=c;
								index++;
								
							}
						}
						String newDerivation = new String(newDerivationToAdd);
						derivationsToAdd.add(newDerivation);
					}
					//This step performs the final step of removeEps, which is the act of actually keeping
					//track of the epsilon rules to be removed from the grammar.
					else if (derivation.equals("$")) {
						derivationsToRemove.add(derivation);
					}
				}
			}
			//Perform additions and removals of the necessary derivations in 
			derivations.addAll(derivationsToAdd);
			derivations.removeAll(derivationsToRemove);
		}
	}

	/**
	 * @author Colin
	 * Removes all unit productions from a grammar (Unit productions are derivations whose right-hand sides contain 1 nonterminal)
	 * @param cfg the grammar which will have its' unit productions removed
	 */
	private void removeUnitProduction(Grammar cfg) {
		//Initialize lists to keep track of the total unit productions removed, as well as the unit productions removed
		//for each state.
		ArrayList<String> totalRemovedUnitProductions = new ArrayList<String>();
		ArrayList<String> removedUnitProductionsPerState;
		ArrayList<State> states = (ArrayList<State>) cfg.getStates();
		
		for (State state: states) {
			//Re-initialize the list of removed unit productions per state for each state in the list of states.
			removedUnitProductionsPerState = new ArrayList<String>();
			for (String derivation: state.getDerivations()) {
				//This step checks if the derivation is a unit production, in which case it is added to both corresponding lists
				if (derivation.length() == 1 && Character.isUpperCase(derivation.toCharArray()[0])) {
					totalRemovedUnitProductions.add(derivation);
					removedUnitProductionsPerState.add(derivation);
				}
			}
			//If there were any unit productions found in the current state, remove the unit production, and add in any necessary
			//derivations to the state from which the unit production was removed.
			if (removedUnitProductionsPerState.size() > 0) {
				for (String toRemove: removedUnitProductionsPerState) {
					state.removeDerivation(toRemove);
					
					for (String derivation: cfg.getStateWithName(toRemove.toCharArray()[0])
							.getDerivations()) {
						
						if (!derivation.equals(toRemove)){
							state.addDerivation(derivation);
						}
					}
				}
			}
		}
		
		//Remove any empty states from the list of states in the grammar.
		cfg.removeEmptyStates();
	}

	/**
	 * @author Matt/Colin
	 * Removes all of the unproductive and unreachable states from a grammar.
	 * @param cfg the grammar which will have its' useless states removed
	 */
	private void removeUselessStates(Grammar cfg){
		//Initialize lists for productive/unproductive symbols
		ArrayList<Character> unproductive = new ArrayList<Character>();
		ArrayList<Character> productive = new ArrayList<Character>();
		
		//add each non-terminal to the unproductive list
		for(State state : cfg.getStates()){
			unproductive.add(state.getNonTerminal());
		}
		
		//add each terminal to the productive list
		for(Character term : cfg.getTerminals()){
			productive.add(term);
		}
		
		
		int q=0;
		boolean placeHolder = true;
		
		//continue to check for symbols that can be changed to productive until
		//there is an iteration in which no symbols are changed
		
		while(placeHolder&&q<10){ 
			placeHolder = false;
			for(State state1 : cfg.getStates()){
				
				//iterate through derivations
					for(String deriv : state1.getDerivations()){
						
						char[] derived = deriv.toCharArray();
						int safeVerify = 0;
						
						//iterate through characters in each derivation
						//'i' represents the character in each iteration
						for(char i : derived){
							
							
							//check whether the character is productive
							if(productive.contains(i)){
								safeVerify++;
							}
					
						}
						
						//if all of the states on the right hand side of a rule are productive, we must change the
						//non-terminal on the left hand side of the rule to productive.
						if(safeVerify==deriv.length())
						{
							//check to see that the non-terminal has not already been marked as productive
							//if it hasn't, mark it as productive.
							if(!productive.contains(state1.getNonTerminal())){
								productive.add(state1.getNonTerminal());
								int index=unproductive.indexOf(state1.getNonTerminal());
								unproductive.remove(index);
								placeHolder=true;

							}
						}
						}
			}
			q++;
		}
		
		//Iterate through each state, and check if the state it-self is still marked as unproductive.
		//If it is marked as unproductive, we must remove the whole state from the current list of states.
		boolean toDelete = false;
		for(State ridDeriv : cfg.getStates()){//
			ArrayList<String> toRemove = new ArrayList<String>();
			
			//check whether one of the states derivations contain an unproductive state. If it does, remove this
			//derivation from this state. 
			for(String d : ridDeriv.getDerivations()){//
				char[] derivation = d.toCharArray();
				toDelete=false;
				
				//for each state, go through each character of each derivation.
				for(char c : derivation){
					if(unproductive.contains(c))
						toDelete=true;
				}
				//add the derivation to the list of to be removed from this state's derivations. 
				if(toDelete)
				toRemove.add(d);
			}
			//remove all derivations from this state that include an unproductive character
			ridDeriv.getDerivations().removeAll(toRemove);
			
		}
		
		//list of states that each new state will be added to, which will then be set as the new
		//list of states for the context-free grammar (cfg) that was passed as a parameter.
		ArrayList<State> newStates = new ArrayList<State>();
		
		//iterate through the list of productive characters
		//for each productive character, check whether it is a Non-terminal or not
		//(by convention, non-terminals are upper-case characters (in this context 'A'-'Z'))
		for(char productiveState : productive){
			if(Character.isUpperCase(productiveState))

				//iterate through current states of cfg, and if the state's non-terminal == productive character on this iteration,
				//add this state to the new list of states
				for(State check : cfg.getStates()){
					if(check.getNonTerminal()==productiveState){
						newStates.add(check);
						}
				
				}
				
		}
		
		cfg.setStates(newStates);
		
		//This section performs the removeUnreachable algorithm, searching through the derivations of each state, starting with
		//the start state, and marking each state that can be reached as reachable, and, once complete, removes all states that are
		//not reachable from the grammar.
		
		//Initialize a list to contain the reachable states, and insert the start state into that list
		ArrayList<State> reachableStates = new ArrayList<State>();
		reachableStates.add(cfg.getStartState());
		
		//Check through each state in the grammar, and, if it has been denoted as reachable, search through its' derivations
		//to look for all states that are reachable via the current state, and add them to the list of reachable states, 
		//if they have not been added already.
		for (State currentState: cfg.getStates()) {
			if (reachableStates.contains(currentState)) {
				for (String currentDerivation: currentState.getDerivations()) {
					char[] splitDerivation = currentDerivation.toCharArray();
					for (char currentChar: splitDerivation) {
						if (Character.isUpperCase(currentChar)) {
							State reachableState = cfg.getStateWithName(currentChar);
							if (!(reachableStates.contains(reachableState))) {
								reachableStates.add(reachableState);
							}
						}
					}
				}
			}
		}
		
		//Set the list of states to the list of reachable states
		cfg.setStates(reachableStates);
		
		//set the new list of terminals. This is used because if we delete all derivations that contain a specific terminal
		//call it 'x' (due to the possibility that derivation containing 'x' may always contain an unreachable state) ,
		//then 'x' is considered unreachable. 		
		//If the above case is true, we must delete this terminal 'x' from cfg's current list of terminal characters.	
		ArrayList<Character> newTerminals = new ArrayList<Character>();
		for(State s : cfg.getStates()){
			for(String deriv : s.getDerivations()){
				
				char[] derived = deriv.toCharArray();
				for(char i : derived){
					if(Character.isLowerCase(i) && !newTerminals.contains(i)){
						newTerminals.add(i);
		
					}
				}
			}
		}
		cfg.setTerminals(newTerminals);
	}
	
				

	/**
	 * Simplifies a grammar. (Removes epsilon derivations, unit productions, and useless states)
	 * @param cfg the grammar to be simplified
	 * @return the simplified grammar
	 */
	public Grammar simplify(Grammar cfg) {
		removeEpsilon(cfg);
		removeUnitProduction(cfg);
		removeUselessStates(cfg);
		return cfg;
	}
}
