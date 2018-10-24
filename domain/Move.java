package domain;

public abstract class Move {

    public boolean isValid(GameState state) {
        return true;
    }

    public void execute(GameState state) {
        if (!this.isValid(state)) {
            return;
        }
        return;
    }
}
