package agent_mcts_group;

import application.Auxiliary;
import application.Constants;
import application.GameController;
import application.Main;
import domain.GameState;
import domain.HexMove;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MCTSMoveNode implements Comparable<MCTSMoveNode> {
    private static double explorationWeight = 2.0;

    private GameState state;
    private MCTSGroupNode father;
    private ArrayList<MCTSGroupNode> children;
    private double uct;
    private double visits;
    private double wins;
    private HexMove move;
    private double won;
    private int id;
    private boolean updated;
    private boolean first;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isUpdated() {
        return updated;
    }

    public void setUpdated(boolean updated) {
        this.updated = updated;
    }

    public MCTSMoveNode(MCTSGroupNode f, GameState s, HexMove m, boolean first) {
        this.father = f;
        this.state = s;
        this.move = m;
        this.visits = 0.0;
        this.uct = 0.0;
        this.wins = 0.0;
        this.won = 0.0;
        this.children = null;
        this.updated = false;
        this.first = first;
        if (m != null) {
            this.id = m.getPlayerID();
        } else {
            this.id = s.getActivePlayer().getId();
        }
    }


    public void descend(int depth) {
        if (this.children == null) {
            this.children = new ArrayList<MCTSGroupNode>();
            List<HexMove> moves;
            if (this.state.getFreetiles() > Constants.DIAGONAL_LENGTH) {
                moves = this.state.getActivePlayer().generateDesirableMoves(this.state);
            } else {
                moves = this.state.getActivePlayer().generatePossibleMoves(this.state);
            }
            for (ArrayList<HexMove> p : this.partition2(moves)) {
                this.children.add(new MCTSGroupNode(this, this.state, p, this.first));
            }
            if (this.children.size() == 0) {
                return;
            }
            int rdm = ((int) (this.visits) % this.children.size());
            this.children.get(rdm).descend(depth + 1);
            return;
        }
        if (this.children.isEmpty()) {
            this.propagate(this.id, this.won);
            return;
        }
        if (this.visits < 12.0 / (depth + 1)) {
            int rdm = ((int) (this.visits) % this.children.size());
            this.children.get(rdm).descend(depth + 1);
            return;
        }

        for (MCTSGroupNode c : this.children) {
            c.updateUCT();
            c.setUpdated(false);
        }
        Collections.max(this.children).descend(depth + 1);
    }


    public void propagate(int id, double val) {
        this.visits += 1.0;
        this.updated = true;
        if (id == this.id) {
            this.wins += val;
        }
        if (this.father != null) {
            this.father.propagate(id, val);
        }
    }

    public void updateUCT() {
        double weight = this.first ? Main.getExplorationWeight1() : Main.getExplorationWeight2();
        this.uct = this.wins / this.visits + weight * Math.sqrt((Math.log(this.father.getVisits
                ()) / this.visits));
        this.updated = false;
    }


    public int runRandomSimulation() {
        GameState copy = new GameState(this.state);
        List<HexMove> mvs = copy.getActivePlayer().generateBestMovesHeavyPlayout(copy);
        int k = 0;
        HexMove m1;
        while (mvs.size() > 0 && k < 10) {
            //min will return the move with the maximum payout sum
            m1 = Collections.min(mvs);
            m1.execute(copy);
            mvs = copy.getActivePlayer().generateBestMovesHeavyPlayout(copy);
            k++;
        }
        return GameController.determineWinner(copy);
    }

    private List<ArrayList<HexMove>> partition3(List<HexMove> moves) {
        List<ArrayList<HexMove>> partitions = new ArrayList<ArrayList<HexMove>>();
        ArrayList<HexMove> part1 = new ArrayList<HexMove>();
        ArrayList<HexMove> part2 = new ArrayList<HexMove>();
        List<Integer> lowest = Auxiliary.getLowestScores(this.state.getByID(this.id).getScores());
        HexMove m = null;
        int i = 0;
        while (i < moves.size()) {
            for (int k : lowest) {
                if ((m = moves.get(i)).getBenefit()[k] > 0) {
                    part1.add(m);
                    break;
                }
            }
            i++;
        }

        if (!part1.isEmpty()) {
            partitions.add(part1);
            moves.removeAll(part1);
        }
        if (moves.isEmpty()) {
            return partitions;
        }
        if(this.state.getMovesPlayed() > 21){
            partitions.add(new ArrayList(moves));
            return partitions;
        }
        int max = Auxiliary.getByteSum(moves.get(0).getBenefit()) % 2 == 0 ? Auxiliary.getByteSum(moves.get(0)
                .getBenefit()) / 2 : (Auxiliary.getByteSum(moves.get(0).getBenefit()) + 1) / 2;
        if (max == 1) {
            max = 2;
        }
        i = 0;
        while (i < moves.size() && Auxiliary.getByteSum((m = moves.get(i)).getBenefit()) >= max) {
            part2.add(m);
            i++;
        }
        if (!part2.isEmpty()) {
            partitions.add(part2);
            moves.removeAll(part2);
        }
        if (moves.isEmpty()) {
            return partitions;
        }

        if (!moves.isEmpty()) {
            partitions.add(new ArrayList<>(moves));
        }
        return partitions;
    }

    private List<ArrayList<HexMove>> partition2(List<HexMove> moves) {
        List<ArrayList<HexMove>> partitions = new ArrayList<ArrayList<HexMove>>();
        ArrayList<HexMove> part1 = new ArrayList<HexMove>();
        int k = 0;
        HexMove m;
        while (k < moves.size()) {
            m = moves.get(k);
            if (m.isSwap()) {
                part1.add(m);
            }
            k++;
        }
        if (!part1.isEmpty()) {
            partitions.add(part1);
            moves.removeAll(part1);
        }
        if (moves.isEmpty()) {
            return partitions;
        }
        partitions.addAll(partition3(moves));
        return partitions;
    }

    private List<ArrayList<HexMove>> partition(List<HexMove> moves) {
        List<ArrayList<HexMove>> partitions = new ArrayList<ArrayList<HexMove>>();
        while (!moves.isEmpty()) {
            HexMove m1 = moves.get(0);
            ArrayList<HexMove> part = new ArrayList<HexMove>();
            part.add(m1);
            if (moves.size() == 1) {
                partitions.add(part);
                return partitions;
            }
            for (HexMove m2 : moves) {
                if (samePartitionColor(m1, m2)) {
                    part.add(m2);
                }
            }
            partitions.add(part);
            moves.removeAll(part);
        }
        return partitions;
    }

    private boolean samePartitionColor(HexMove m1, HexMove m2) {
        int sum = 0;
        for (int i = 0; i < 6; i++) {
            if (m1.getBenefit()[i] == m2.getBenefit()[i]) {
                sum++;
                continue;
            }
            if (m1.getBenefit()[i] > 0 && m2.getBenefit()[i] > 0) {
                sum++;
            }
        }
        return sum == 6;
    }


    @Override

    public int compareTo(MCTSMoveNode other) {
        if (this.uct > other.uct) {
            return 1;
        } else if (this.uct < other.uct) {
            return -1;
        }
        return 0;
    }

    @Override
    public String toString() {
        return "uct: " + this.uct + "   visits: " + this.visits + "   wins: " + this.wins + "   player: " + this
                .state.getActivePlayer();
    }

    public double getUCT() {
        return uct;
    }

    public void setUCT(double uct) {
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

    public HexMove getMove() {
        return move;
    }

    public void setMove(HexMove move) {
        this.move = move;
    }


    public GameState getState() {
        return state;
    }

    public void setState(GameState state) {
        this.state = state;
    }

    public MCTSGroupNode getFather() {
        return father;
    }

    public void setFather(MCTSGroupNode father) {
        this.father = father;
    }

    public ArrayList<MCTSGroupNode> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<MCTSGroupNode> children) {
        this.children = children;
    }

    public double getUct() {
        return uct;
    }

    public void setUct(double uct) {
        this.uct = uct;
    }

    public double getWon() {
        return won;
    }

    public void setWon(double won) {
        this.won = won;
    }

    public MCTSMoveNode getBestNode2() {
        List<MCTSMoveNode> nodes = new ArrayList<MCTSMoveNode>();
        for (MCTSGroupNode c : this.children) {
            nodes.addAll(c.getChildren());
        }
        double maxVisits = -1.0;
        List<MCTSMoveNode> bestNodes = new ArrayList<MCTSMoveNode>();
        for (MCTSMoveNode n : nodes) {
            if (n.visits == maxVisits) {
                bestNodes.add(n);
            }
            if (n.visits > maxVisits) {
                bestNodes.clear();
                bestNodes.add(n);
                maxVisits = n.getVisits();
            }
        }
        return Collections.min(bestNodes);
    }


    public MCTSMoveNode getBestNode() {
        double maxVisits = -1.0;
        List<MCTSMoveNode> bestNodes = new ArrayList<MCTSMoveNode>();
        for (MCTSGroupNode c : this.children) {
            if (c.getVisits() == maxVisits) {
                bestNodes.addAll(c.getChildren());
            }
            if (c.getVisits() > maxVisits) {
                bestNodes.clear();
                bestNodes.addAll(c.getChildren());
                maxVisits = c.getVisits();
            }
        }
        maxVisits = -1.0;
        List<MCTSMoveNode> bestNodes2 = new ArrayList<MCTSMoveNode>();
        for (MCTSMoveNode c : bestNodes) {
            if (c.getVisits() == maxVisits) {
                bestNodes2.add(c);
            }
            if (c.getVisits() > maxVisits) {
                bestNodes2.clear();
                bestNodes2.add(c);
                maxVisits = c.getVisits();
            }
        }
        return bestNodes2.get(Auxiliary.getRandomInt(0, bestNodes2.size()));
    }

}
