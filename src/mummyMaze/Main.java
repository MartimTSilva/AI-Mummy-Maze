package mummyMaze;

import showSolution.SolutionPanel;

import java.util.LinkedList;
import java.util.List;

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

    }
}
