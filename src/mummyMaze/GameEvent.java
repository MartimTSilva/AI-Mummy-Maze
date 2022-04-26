package mummyMaze;

import java.util.EventObject;

public class GameEvent extends EventObject {

    public GameEvent(GameState source) {
        super(source);
    }
}
