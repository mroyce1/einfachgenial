package agent_mcts;

import application.Auxiliary;
import application.Constants;
import application.GameController;
import application.Main;
import domain.*;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MCTSNode implements Comparable<MCTSNode> {
    private static double explorationWeight = 1.44;
    private GameState state;
    private MCTSNode father;
    private ArrayList<HexMove> moves;
    private ArrayList<MCTSNode> children;
    private double uct;
    private double visits;
    private double wins;
    private HexMove move;
    private double won;
    private int id;
    private boolean first;

    public MCTSNode(MCTSNode f, GameState s, HexMove m, boolean first) {
        this.father = f;
        this.state = s;
        if (this.state.getFreetiles() <= Constants.DIAGONAL_LENGTH) {
            this.moves = this.state.getActivePlayer().generatePossibleMoves(this.state);
        } else {
            this.moves = this.state.getActivePlayer().generateDesirableMoves(this.state);
        }
        this.children = new ArrayList<MCTSNode>();
        this.uct = 0.0;
        this.visits = 0.0;
        this.wins = 0.0;
        this.won = 0.0;
        this.move = m;
        this.id = state.getInactivePlayer().getId();
        this.first = first;
    }

    @Override
    public String toString() {
        return "uct: " + this.uct + "   visits: " + this.visits + "   wins: " + this.wins + "   player: " + this
                .state.getByID(this.id);
    }


    public List<MCTSNode> getChildren() {
        return children;
    }

    public List<HexMove> getMoves() {
        return moves;
    }

    @Override
    public int compareTo(MCTSNode other) {
        if (this.uct > other.uct) {
            return 1;
        } else if (this.uct < other.uct) {
            return -1;
        }
        int diff = (this.state.getInactivePlayer().getSumOfScores() - this.father.getState().getActivePlayer()
                .getSumOfScores()) - (other.state.getInactivePlayer().getSumOfScores() - other.father.getState()
                .getActivePlayer().getSumOfScores());
        if (diff > 0) {
            return 1;
        } else if (diff < 0) {
            return -1;
        }
        return 0;
    }

    public double getWins() {
        return wins;
    }

    public GameState getState() {
        return this.state;
    }

    public void setChildren(ArrayList<MCTSNode> children) {
        this.children = children;
    }

    public double getVisits() {
        return visits;
    }

    public void setVisits(double visits) {
        this.visits = visits;
    }

    public void expand(int depth) {
        this.visits += 1.0;
        if (this.moves.isEmpty()) {
            this.selectChild(depth);
            return;
        }
        int moveIndex = Auxiliary.getRandomInt(0, this.moves.size());
        HexMove m = this.moves.remove(moveIndex);
        this.moves.trimToSize();
        this.initializeChild(m);
    }

    public double getUct() {
        return uct;
    }

    public void setUct(double uct) {
        this.uct = uct;
    }

    private void selectChild(int depth) {
        if (this.children.isEmpty()) {
            this.propagate(-2, this.won);
            return;
        }
        Collections.max(this.children).expand(depth + 1);
    }

    public HexMove getMove() {
        return move;
    }

    public void setMove(HexMove move) {
        this.move = move;
    }

    private void initializeChild(HexMove m) {
        GameState copy = new GameState(this.state);
        m.execute(copy);
        MCTSNode child = new MCTSNode(this, copy, m, this.first);
        child.visits += 1.0;
        double win = child.runRandomSimulation();
        this.children.add(child);
        int k = win > 0 ? this.state.getInactivePlayer().getId() : this.state.getActivePlayer().getId();
        child.propagate(k, win);
    }


    private double calcResult(GameState copy) {
        int res = GameController.determineWinner(copy);
        if (res == -1) {
            return 0.5;
        }
        if(res == state.getInactivePlayer().getId()){
            return 1.0;
        }
        return 0.0;
    }


    private double runRandomSimulation() {
        GameState copy = new GameState(this.state);
        List<HexMove>mvs = copy.getActivePlayer().generateBestMovesHeavyPlayout(copy);
        int rdm;
        int k = 0;
        HexMove m1;
        while (mvs.size() > 0 && k < 10) {
            //min will return the move with the maximum payout sum
            m1 = Collections.min(mvs);
            m1.execute(copy);
            mvs = copy.getActivePlayer().generateBestMovesHeavyPlayout(copy);
            k++;
        }
        return calcResult(copy);
    }


    public void setState(GameState state) {
        this.state = state;
    }

    private void propagate(int id, double val) {
        if (id != -2) {
            this.won = val;

        }
        MCTSNode fath = this;
        double weight = this.first ? Main.getExplorationWeight1() : Main.getExplorationWeight2();
        while (fath != null) {
            fath.wins += this.state.getActivePlayer().getId() == fath.state.getActivePlayer().getId() ? this.won
                    : (1.0 - this.won);
            for (MCTSNode c : fath.children) {
                c.uct = c.wins / c.visits + weight * Math.sqrt(Math.log(fath.visits) / c.visits);
            }
            fath = fath.father;
        }
    }
}

