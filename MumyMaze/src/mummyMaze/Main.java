package mummyMaze;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import showSolution.SolutionPanel;

public class Main {

	public static void main(String[] args) {

		List<String> states = new LinkedList<>();

		String state = 	"             \n" +
				" . . . . .|. \n" +
				"     -       \n" +
				" . . . H . . \n" +
				"     -       \n" +
				" . . . .|. . \n" +
				"       -   - \n" +
				" . . . . .|. \n" +
				"   - -       \n" +
				" . . . M . . \n" +
				"         -   \n" +
				" . . . . . . \n" +
				" S           \n";
		states.add(state);
		state = 	"             \n" +
				" . . . . .|. \n" +
				"     -       \n" +
				" . . H . .   \n" +
				"     -       \n" +
				" . . . .|. . \n" +
				"       -   - \n" +
				" . . . . .|. \n" +
				"   - -       \n" +
				" . . . M . . \n" +
				"         -   \n" +
				" . . . . . . \n" +
				" S           \n";
		states.add(state);
		state = 	"             \n" +
				" . . . . .|. \n" +
				"     -       \n" +
				" . . H . .   \n" +
				"     -       \n" +
				" . . . .|. . \n" +
				"       -   - \n" +
				" . . . . .|. \n" +
				"   - -       \n" +
				" . . M . . . \n" +
				"         -   \n" +
				" . . . . . . \n" +
				" S           \n";
		states.add(state);
		state = 	"             \n" +
				" . . . . .|. \n" +
				"     -       \n" +
				" . H . . . . \n" +
				"     -       \n" +
				" . . . .|. . \n" +
				"       -   - \n" +
				" . . . . .|. \n" +
				"   - -       \n" +
				" . . M . . . \n" +
				"         -   \n" +
				" . . . . . . \n" +
				" S           \n";
		states.add(state);
		state = 	"             \n" +
				" . . . . .|. \n" +
				"     -       \n" +
				" . H . . . . \n" +
				"     -       \n" +
				" . . . .|. . \n" +
				"       -   - \n" +
				" . . . . .|. \n" +
				"   - -       \n" +
				" . M . . . . \n" +
				"         -   \n" +
				" . . . . . . \n" +
				" S           \n";
		states.add(state);
		state = 	"             \n" +
				" . . . . .|. \n" +
				"     -       \n" +
				" H . . . . . \n" +
				"     -       \n" +
				" . . . .|. . \n" +
				"       -   - \n" +
				" . . . . .|. \n" +
				"   - -       \n" +
				" . M . . . . \n" +
				"         -   \n" +
				" . . . . . . \n" +
				" S           \n";
		states.add(state);
		state = 	"             \n" +
				" . . . . .|. \n" +
				"     -       \n" +
				" H . . . . . \n" +
				"     -       \n" +
				" . . . .|. . \n" +
				"       -   - \n" +
				" . . . . .|. \n" +
				"   - -       \n" +
				" M . . . . . \n" +
				"         -   \n" +
				" . . . . . . \n" +
				" S           \n";
		states.add(state);
		state = 	"             \n" +
				" . . . . .|. \n" +
				"     -       \n" +
				" . . . . . . \n" +
				"     -       \n" +
				" H . . .|. . \n" +
				"       -   - \n" +
				" . . . . .|. \n" +
				"   - -       \n" +
				" M . . . . . \n" +
				"         -   \n" +
				" . . . . . . \n" +
				" S           \n";
		states.add(state);
		SolutionPanel.showSolution(states, states.size());
/*int i=0, j=0;
		char matrix[][] = new char[13][13];
		System.out.println(state.toCharArray().length);
		for (char t:  state.toCharArray()){
			if(t!='\n') {
				matrix[i][j] = t;
				j++;
			}else{
				j=0;
				i++;
			}
		}
		String s="";
		for (int k = 0; k < 13; k++) {
			s+=String.valueOf(matrix[k])+"\n";
		}


		System.out.println(s);
		SolutionPanel.showState(s);
*/

		//SolutionPanel.showSolution(states,7);

		/*
		String state = 	"             \n" +
				" . . . . .|. \n" +
				"     -       \n" +
				" . . . . . . \n" +
				"     -       \n" +
				" . . . .|. . \n" +
				"       -   - \n" +
				" . . . . .|M \n" +
				"   - -       \n" +
				" . . . . H . \n" +
				"         -   \n" +
				" . . . . . . \n" +
				" S           \n";
		SolutionPanel.showState(state);

		 */
	}

}
