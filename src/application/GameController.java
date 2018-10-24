package application;

import domain.GameState;
import domain.HexMove;


import java.util.Collections;
import java.util.List;

public class GameController {
    private static HexMove previousMove = null;
    private static GameState previousState = null;


    public static GameState signalMove(GameState state) {
        GameState copy = new GameState(state);
        List<HexMove> moves = copy.getActivePlayer().generatePossibleMoves(copy);
        if (moves.size() == 0) {
            copy.setFinished(true);
            return copy;
        }
        previousState = new GameState(copy);
        HexMove move = copy.getActivePlayer().notifyMove(copy, previousState, previousMove);
        previousMove = move;
        move.execute(copy);
        System.out.println(state.getActivePlayer().getName() + " performed move " + move);
        return copy;
    }

    public static int determineWinner(GameState state) {
        List<Byte> p1scores = Auxiliary.getByteList(state.getActivePlayer().getScores());
        List<Byte> p2scores = Auxiliary.getByteList(state.getInactivePlayer().getScores());
        Collections.sort(p1scores);
        Collections.sort(p2scores);
        int i = 0;
        while (i <= 5 && (p1scores.get(i) == p2scores.get(i))) {
            i++;
        }
        i = i == 6 ? 5 : i;
        if (p1scores.get(i) < p2scores.get(i)) {
            return state.getInactivePlayer().getId();
        }
        if (p1scores.get(i) > p2scores.get(i)) {
            return state.getActivePlayer().getId();
        }
        return -1;
    }
}
