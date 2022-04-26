package mummyMaze;

import agent.Action;

public class ActionStop extends Action<GameState>{

    public ActionStop(){
        super(1);
    }

    @Override
    public void execute(GameState state){
        state.dontMove();
        state.setAction(this);
    }

    @Override
    public boolean isValid(GameState state){
        return state.canMoveDown();
    }
}