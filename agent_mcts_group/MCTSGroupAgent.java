package agent_mcts_group;

import agent.Player;
import agent.PlayerType;
import application.Main;
import domain.GameState;
import domain.HexMove;

import java.util.List;

public class MCTSGroupAgent extends Player {
    private int iterations;

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public MCTSGroupAgent(String name, boolean first) {
        super(name, PlayerType.MCTS_GROUP_AGENT3, first);

    }

    public MCTSGroupAgent(String name, int iterations, boolean first) {
        super(name, PlayerType.MCTS_GROUP_AGENT3, first);
        this.iterations = iterations;

    }

    @Override
    public HexMove notifyMove(GameState state, GameState previousState, HexMove previousMove) {
        GameState copy = new GameState(state);
        MCTSMoveNode top = new MCTSMoveNode(null, copy, null, this.getId() == 0);
        List<HexMove> moves = this.generateDesirableMoves(state);
        for (int i = 0; i < this.iterations; i++) {
            top.descend(0);
        }
        MCTSMoveNode n;
        if(this.getId() == 0){
            if(Main.isTrigger1()){
                n = top.getBestNode();
            }
            else{
                n = top.getBestNode2();
            }
        }
        else{
            if(Main.isTrigger2()){
                n = top.getBestNode();
            }
            else{
                n = top.getBestNode2();
            }
        }
        return n.getMove();
    }
}
