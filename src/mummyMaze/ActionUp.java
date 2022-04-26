package mummyMaze;

import agent.Action;

public class ActionUp extends Action<GameState>{

    public ActionUp(){
        super(1);
    }

    @Override
    public void execute(GameState state){
        state.moveUp();
        state.setAction(this);
    }

    @Override
    public boolean isValid(GameState state){
        return state.canMoveUp();
    }
}