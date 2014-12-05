package com.cs410.finalproject.operations;

import java.util.ArrayList;
import java.util.Scanner;

import com.cs410.finalproject.models.Grammar;
import com.cs410.finalproject.models.State;

public class CKYParser {
	
	/**
	 * @author Brian Hudi
	 * @param input the string that you are checking to see can be generated by the grammar.
	 * @param chomskyGrammar the grammar in chomsky normal form.
	 */
	private void checkInput(String input, Grammar chomskyGrammar)
	{
		int n = input.length();
		String p[][] = new String[n][n];
		ArrayList<String> rule = new ArrayList<String>();
		rule = (ArrayList<String>) chomskyGrammar.getStartState().getDerivations();
		
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
			{
				p[i][j] = "";
			}
		}
		
		//step 1 of the algorithm
		//i have tested this multiple times and i believe all of this logic is correct.
		for(int i = 0; i < n; i++) 
		{
			for(State state : chomskyGrammar.getStates())
			{
				for(int j = 0; j < state.getDerivations().size(); j++)
				{
					if(state.getDerivations().get(j).contains(input.charAt(i) + ""))
					{
						p[0][i] = p[0][i] + state.getNonTerminal() + " ";
					}
				}
			}
		}
		
		//step 2 of the algorithm
		//all of the logic for this is correct, i believe, except for the last iff statement
		// have not quite figured that one out yet.
		for(int i = 1; i < n; i++)
		{
			for(int j = 0; j < n - i + 1; j++)
			{
				for(int k = 0; k < i - 1; k++)
				{
					for(State state : chomskyGrammar.getStates())
					{
						for(int h = 0; h < state.getDerivations().size(); h++)
						{
							if(state.getDerivations().get(h).length() == 2 && state.getDerivations().get(h).toUpperCase().equals(state.getDerivations().get(h)))
							{
								//this part is not working correctly yet
								if(p[k][j].contains(state.getDerivations().get(h).charAt(0) + "") &&
									p[i-k][j+k].contains(state.getDerivations().get(h).charAt(1) + ""))
								{
									p[i][j] = p[i][j] + state.getNonTerminal() + " ";
								}
							}
						}
					}
				}
			}	
		}
		//this is just for testing
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				System.out.println(p[i][j]);
			}
		}
		
		//this is the final check, not quite sure how this works yet.
		if(p[n-1][1].contains(rule.get(0)))
		{
			System.out.println(input + " is in the grammar.");
		}
	}
	
	/**
	 * @author Brian Hudi
	 * @param chomskyGrammar the grammar in chomsky normal form.
	 * @return the grammar
	 */
	public Grammar parseCKY(Grammar chomskyGrammar)
	{
		//this needs some changing, but i'm going to wait until it is completely working.
		String input;
		Scanner reader = new Scanner(System.in);
		System.out.println("Enter a potential string in the grammar: ");
		//get user input for a potential string created by the grammar.
		input=reader.nextLine();
		//System.out.println(input.length());
		checkInput(input, chomskyGrammar);
		return chomskyGrammar;
	}

}