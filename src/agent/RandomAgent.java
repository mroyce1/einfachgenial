package agent;

import application.Auxiliary;
import application.Constants;
import domain.GameState;
import domain.HexMove;

import java.util.List;

public class RandomAgent extends Player {

    public RandomAgent(String name, boolean first) {
        super(name, PlayerType.RANDOM_AGENT, first);
    }

    @Override
    public HexMove notifyMove(GameState state, GameState previousState, HexMove previousMove) {
        List<HexMove> moves = this.generatePossibleMoves(state);
        return moves.get(Auxiliary.getRandomInt(0, moves.size()));
    }
}
