package agent_mcts;

import agent.Player;
import agent.PlayerType;
import application.Auxiliary;
import application.Constants;
import application.Main;
import domain.ColorDuplet;
import domain.GameState;
import domain.HexMove;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MCTSAgent extends Player {
    private int iterations;


    public MCTSAgent(String name, boolean first) {
        super(name, PlayerType.MCTS_AGENT, first);
    }

    public MCTSAgent(String name, int iterations, boolean first) {
        super(name, PlayerType.MCTS_AGENT, first);
        this.iterations = iterations;
    }


    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    @Override
    public HexMove notifyMove(GameState state, GameState previousState, HexMove previousMove) {
        GameState copy = new GameState(state);
        MCTSNode top = new MCTSNode(null, copy, null, this.getId() == 0);
        List<HexMove> moves = this.generateDesirableMoves(copy);
        for (int i = 0; i < this.iterations; i++) {
            this.changeOpponentHand(copy);
            top.setState(copy);
            top.expand(0);
        }
        int idx = 0;
        double maxVisits = 0.0;
        List<MCTSNode> mostVisits = new ArrayList<MCTSNode>();
        for (int i = 0; i < top.getChildren().size(); i++) {
            if (top.getChildren().get(i).getVisits() == maxVisits) {
                mostVisits.add(top.getChildren().get(i));
            }
            if (top.getChildren().get(i).getVisits() > maxVisits) {
                maxVisits = top.getChildren().get(i).getVisits();
                mostVisits.clear();
                mostVisits.add(top.getChildren().get(i));
                idx = i;
            }
        }
        MCTSNode maxChild = top.getChildren().get(idx);
        return maxChild.getMove();
    }

    @Override
    public String toString() {
        return "MCTSAgent: " + this.getName() + "  iterations: " + this.iterations + "  C-param: " + (this.getId() ==
                0 ? Main.getExplorationWeight1() : Main.getExplorationWeight2());
    }
}