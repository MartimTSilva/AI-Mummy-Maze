package mummyMaze;

import agent.Action;

public class ActionDown extends Action<GameState>{

    public ActionDown(){
        super(1);
    }

    @Override
    public void execute(GameState state){
        state.moveDown();
        state.setAction(this);
    }

    @Override
    public boolean isValid(GameState state){
        return state.canMoveDown();
    }
}