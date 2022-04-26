package mummyMaze;

import agent.Action;

public class ActionRight extends Action<GameState>{

    public ActionRight(){
        super(1);
    }

    @Override
    public void execute(GameState state){
        state.moveRight();
        state.setAction(this);
    }

    @Override
    public boolean isValid(GameState state){
        return state.canMoveRight();
    }
}