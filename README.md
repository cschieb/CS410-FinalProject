Currently the project should work as-is when the source code is compiled and run,
AS LONG AS AnotherCFG.txt is located in the root directory of your eclipse project folder.
This change has been made in order to remove the need to change the file path within the
ProgramTester class each time someone else wishes to run the project.

At this point in development, the project's fully functional components are as follows:

1. Parses a Context Free Grammar from a file (in the default case, "AnotherCFG.txt"), 
and creates a Grammar object representation of the parsed Grammar.

2. Simplifies the Context Free Grammar. Simplifying consists of the following steps:

	2a. Removing epsilon derivations (Denoted by $ in the input file)

	2b. Removing unit productions (i.e S -> A, a derivation which is a single non 
	terminal)

	2c. Removing useless states (Unreachable and unproductive states)

3. Converts the simplified Context Free Grammar to Chomsky Normal Form, which consists
of the following steps:

	3a. Removing mixed derivations (Derivations which contain one or more terminals
	AND non terminals)

	3b. Removing long derivations (Derivations which have a length longer than 2
	characters)

4. Outputs the simplifed, Chomsky normal form grammar to "output.txt", which is located in
the project's root directory (The same place where "AnotherCFG.txt" is located by default)


Some additional notes & tips:

-The ProgramTester class does not contain any standard or javadoc comments, because its sole
purpose is to facilitate testing, and is constantly being changed and edited as new components
are added to the project.

-When creating a new input file, there are a few guidelines which must be followed:

	-V, the list of states, must ALWAYS be before S, the start state.

	-T, the list of terminals, can be anywhere above P, the list of rules.
	
	-P, the list of rules, must ALWAYS be at the bottom of the file, following V, T, and S.

	-Make sure that there are no spaces in between lines of the file, or the parser
	will not work properly.

	-Inline spaces are allowed, so if you prefere to have spaces in between derivations,
	the parser will still work properly.

	-In general, if you follow the same layout as "AnotherCFG.txt" when creating a new
	input file, there should be no issue parsing the file.

This is a work in progress, and new features will be added in time. A few of
these features are as follows:

1. Application of the CYK algorithm on a selected input string.

2. Adding a class/method to handle file input, rather than hard-coding it into the test method.


*Contributors please read*
I will be creating working branches for each contributor to this project. If for some reason
I forget to do this for you, please contact me and I will create one as soon as possible.
Note that nothing should be merged into master until the change has been verified in your
working branch by the other group members. My hope is that the master branch will be kept
in a working state, and changes will only be made to it once the next feature of the project
is fully functional.

Thanks,
Colin
