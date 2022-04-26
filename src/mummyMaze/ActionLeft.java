package mummyMaze;

import agent.Action;

public class ActionLeft extends Action<GameState>{

    public ActionLeft(){
        super(1);
    }

    @Override
    public void execute(GameState state){
        state.moveLeft();
        state.setAction(this);
    }

    @Override
    public boolean isValid(GameState state){
        return state.canMoveLeft();
    }
}
