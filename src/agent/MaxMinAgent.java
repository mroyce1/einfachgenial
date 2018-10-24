package agent;

import application.Main;
import domain.GameState;
import domain.HexMove;

import java.io.IOException;
import java.util.ArrayList;

public class MaxMinAgent extends Player {

    public MaxMinAgent(String name, boolean first) {
        super(name, PlayerType.MAXMIN_AGENT, first);
    }

    @Override
    public HexMove notifyMove(GameState state, GameState previousState, HexMove previousMove) {
        ArrayList<HexMove> moves = this.generatePossibleMoves(state);
        HexMove m;
        int q;
        if (state.getMovesPlayed() <= 21) {
            q = this.determineMaximumMove(moves);
        } else {
            q = this.determineBestMoveForWorstColor(moves);
        }
        m = moves.get(q);
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
