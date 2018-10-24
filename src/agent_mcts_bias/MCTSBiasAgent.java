package agent_mcts_bias;

import agent.Player;
import agent.PlayerType;
import domain.GameState;
import domain.HexMove;

import java.util.ArrayList;
import java.util.List;

public class MCTSBiasAgent extends Player {
    private int iterations;


    public MCTSBiasAgent(String name, boolean first) {
        super(name, PlayerType.MCTS_BIAS, first);
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public MCTSBiasAgent(String name, int iterations, boolean first) {
        super(name, PlayerType.MCTS_BIAS, first);
        this.iterations = iterations;

    }

    @Override
    public HexMove notifyMove(GameState state, GameState previousState, HexMove previousMove) {
        GameState copy = new GameState(state);
        MCTSBiasNode top = new MCTSBiasNode(null, copy, null, this.getId() == 0);
        List<HexMove> moves = this.generateDesirableMoves(copy);
        for (int i = 0; i < this.iterations; i++) {
            this.changeOpponentHand(copy);
            top.setState(copy);
            top.expand(0);
        }
        int idx = 0;
        double maxVisits = 0.0;
        List<MCTSBiasNode> mostVisits = new ArrayList<MCTSBiasNode>();
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
        MCTSBiasNode maxChild = top.getChildren().get(idx);
        return maxChild.getMove();
    }

}