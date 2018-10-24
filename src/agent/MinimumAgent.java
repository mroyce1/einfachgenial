package agent;

import application.Main;
import domain.GameState;
import domain.HexMove;

import java.util.List;

public class MinimumAgent extends Player {

    public MinimumAgent(String name, boolean first) {
        super(name, PlayerType.MINIMUM_AGENT, first);
    }


    @Override
    public HexMove notifyMove(GameState state, GameState previousState, HexMove previousMove) {
        List<HexMove> moves = this.generatePossibleMoves(state);
        int k = this.determineBestMoveForWorstColor(moves);
        HexMove m = moves.get(k);
        if (this.getId() == 0) {
            if (Main.isTrigger1()) {
                if (this.swapPossible(state, m)) {
                    m.setSwap(true);
                }
            }
        } else {
            if (Main.isTrigger2()) {
                if (this.swapPossible(state, m)) {
                    m.setSwap(true);
                }
            }
        }
        return m;
    }
}
