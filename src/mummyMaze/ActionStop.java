package mummyMaze;

import agent.Action;

public class ActionStop extends Action<MummyMazeState>{

    public ActionStop(){
        super(1);
    }

    @Override
    public void execute(MummyMazeState state){
        state.dontMove();
        state.setAction(this);
    }

    @Override
    public boolean isValid(MummyMazeState state){
        return state.canMoveDown();
    }
}