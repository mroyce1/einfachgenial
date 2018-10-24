package agent_mcts_group;

import application.Auxiliary;
import domain.GameState;
import domain.HexMove;

import java.util.ArrayList;
import java.util.Collections;

public class MCTSGroupNode implements Comparable<MCTSGroupNode> {
    private GameState state;
    private MCTSMoveNode father;
    private ArrayList<HexMove> moves;
    private ArrayList<MCTSMoveNode> children;
    private double uct;
    private double visits;
    private double wins;
    private double won;
    private int id;
    private boolean updated;
    boolean first;


    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public MCTSGroupNode(MCTSMoveNode f, GameState s, ArrayList<HexMove> moves, boolean first) {
        this.father = f;
        this.state = s;
        this.moves = moves;
        this.children = new ArrayList<MCTSMoveNode>();
        this.uct = 0.0;
        this.visits = 1.0;
        this.won = 0.0;
        this.id = this.father.getId();
        this.updated = false;
        this.first = first;
        this.wins = 0.0;

    }

    public void descend(int depth) {
        this.visits+=1.0;
        if (this.moves.isEmpty()) {
            this.selectChild(depth);
            return;
        }
        int moveIndex = Auxiliary.getRandomInt(0, this.moves.size());
        HexMove m = this.moves.remove(moveIndex);
        this.moves.trimToSize();
        this.initializeChild(m);
    }

    private void selectChild(int depth) {
        if (this.children.isEmpty()) {
            return;
        }
        for (MCTSMoveNode c : this.children) {
            c.updateUCT();
        }
        Collections.max(this.children).descend(depth + 1);
    }


    private void initializeChild(HexMove m) {
        GameState copy = new GameState(this.father.getState());
        if (this.father.getFather() == null) {
            //must be called on the active player because the opponent's hand needs to change
            copy.getActivePlayer().changeOpponentHand(copy);
        }
        m.execute(copy);
        MCTSMoveNode child = new MCTSMoveNode(this, copy, m, this.first);

        double val = 0.0;
        int win = child.runRandomSimulation();
        if (win == child.getId()) {
            val = 1.0;
        }
        if (win == -1) {
            val = 0.5;
        }
        this.children.add(child);
        child.setWon(val);
        child.propagate(child.getId(), val);

    }

    public void propagate(int id, double val) {
        if(id == this.id){
            this.wins += val;
        }
        this.updated = true;
        this.father.propagate(id, val);
    }


    public void updateUCT() {
        if (this.children.size() == 0) {
            return;
        }

        double u = 0.0;
        double w = 0.0;
        MCTSMoveNode node;
        for (int i = 0; i < this.children.size(); i++) {
            node = this.children.get(i);
            node.updateUCT();
            u += node.getUCT();
            w += node.getVisits();
        }
        this.uct = u/this.children.size();
    }


    public MCTSMoveNode getFather() {
        return father;
    }

    public void setFather(MCTSMoveNode father) {
        this.father = father;
    }

    public double getUct() {
        return uct;
    }

    public void setUct(double uct) {
        this.uct = uct;
    }

    public double getVisits() {
        return visits;
    }

    public void setVisits(double visits) {
        this.visits = visits;
    }

    public double getWins() {
        return wins;
    }

    public void setWins(double wins) {
        this.wins = wins;
    }

    public double getWon() {
        return won;
    }

    public void setWon(double won) {
        this.won = won;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public ArrayList<MCTSMoveNode> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<MCTSMoveNode> children) {
        this.children = children;
    }

    @Override
    public int compareTo(MCTSGroupNode other) {
        if (this.uct > other.uct) {
            return 1;
        } else if (this.uct < other.uct) {
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "uct: " + this.uct + "   visits: " + this.visits + "   wins: " + this.wins + "   children: " + this
                .children.size() + "   player: " + this
                .state.getActivePlayer();
    }
}