package agent;

import agent_mcts.MCTSAgent;
import agent_mcts_bias.MCTSBiasAgent;
import agent_mcts_group.MCTSGroupAgent;
import application.Auxiliary;
import application.ColorSort;
import domain.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public abstract class Player {
    private String name;
    private int id;
    private byte[] scores;
    private ColorDuplet[] duplets;
    private PlayerType type;

    public PlayerType getType() {
        return type;
    }

    public void setType(PlayerType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getScores() {
        return scores;
    }

    public void setScores(byte[] scores) {
        this.scores = scores;
    }

    public ColorDuplet[] getDuplets() {
        return duplets;
    }

    public void setDuplets(ColorDuplet[] duplets) {
        this.duplets = duplets;
    }

    public void removeDuplet(ColorDuplet d) {
        for (int i = 0; i < 6; i++) {
            if (this.duplets[i] == null) {
                continue;
            }
            if (this.duplets[i].equals(d)) {
                this.duplets[i] = null;
                return;
            }
        }
    }

    public void changeOpponentHand(GameState state) {
        ColorDuplet[] tmp = state.getByID((this.id + 1) % 2).getDuplets().clone();
        for (ColorDuplet d : state.getByID((this.id + 1) % 2).getDuplets()) {
            state.getByID((this.id + 1) % 2).removeDuplet(d);
        }
        state.distributeDuplets();
        for (ColorDuplet d : tmp) {
            state.getStack().add(d);
        }
    }

    public void clearAllDuplets() {
        for (int i = 0; i < 6; i++) {
            this.duplets[i] = null;
        }
    }

    public int getSumOfScores() {
        int sum = 0;
        for (int i : this.scores) {
            sum += i;
        }
        return sum;
    }

    public ArrayList<HexMove> generatePossibleMoves(GameState state) {
        ArrayList<HexMove> moves = new ArrayList<HexMove>();
        GameHexagon hex, neighbour;
        for (int i = 0; i < state.getHexagons().length; i++) {
            if ((hex = state.getHexagons()[i]).getCol() != ColorType.WHITE) {
                continue;
            }
            for (int j : hex.getNeighbours()) {
                if ((neighbour = state.getHexagons()[j]).getCol() != ColorType.WHITE) {
                    continue;
                }
                for (ColorDuplet c : state.getActivePlayer().getDuplets()) {
                    if (c == null) {
                        continue;
                    }
                    HexMove m = new HexMove(this, c, hex.getAxis(), neighbour.getAxis(), hex, neighbour);
                    moves.add(m);
                }
            }
        }
        List<HexMove> swapMoves = new ArrayList<HexMove>();
        for (int i = 0; i < moves.size(); i++) {
            if (swapPossible(state, moves.get(i))) {
                HexMove m2 = new HexMove(moves.get(i));
                m2.setSwap(true);
                swapMoves.add(m2);
            }
        }
        moves.addAll(swapMoves);
        return moves;
    }

    public ArrayList<HexMove> generateDesirableMoves(GameState state) {
        HashSet<HexMove> moves = new HashSet<HexMove>(330, 0.75F);
        GameHexagon g, neighbour;
        HexMove m;
        ColorDuplet d;
        for (int i = 0; i < state.getActivePlayer().getDuplets().length; i++) {
            if ((d = state.getActivePlayer().getDuplets()[i]) == null) {
                continue;
            }
            for (int a : state.getPositiveScores().get(d.getC1().getVal())) {
                g = state.getHexagons()[a];
                for (int n : g.getNeighbours()) {
                    if ((neighbour = state.getHexagons()[n]).getCol() != ColorType.WHITE) {
                        continue;
                    }
                    m = new HexMove(this, d, g.getAxis(), neighbour.getAxis(), state.getHexagons()[g.getNum()],
                            neighbour);
                    moves.add(m);
                }
            }
            if (d.sameColor()) {
                continue;
            }
            for (int a : state.getPositiveScores().get(d.getC2().getVal())) {
                g = state.getHexagons()[a];
                for (int n : g.getNeighbours()) {
                    if ((neighbour = state.getHexagons()[n]).getCol() != ColorType.WHITE) {
                        continue;
                    }
                    m = new HexMove(this, d, neighbour.getAxis(), g.getAxis(), neighbour, state.getHexagons()[g
                            .getNum()]);
                    moves.add(m);
                }
            }
        }
        if (moves.isEmpty()) {
            return generatePossibleMoves(state);
        }
        List<HexMove> swapMoves = new ArrayList<HexMove>();
        for (HexMove m1 : moves) {
            if (swapPossible(state, m1)) {
                HexMove m2 = new HexMove(m1);
                m2.setSwap(true);
                swapMoves.add(m2);
            }
        }
        moves.addAll(swapMoves);
        return new ArrayList<>(moves);
    }

    public boolean swapPossible(GameState state, HexMove m) {
        byte[] scoreCopy = state.getActivePlayer().scores.clone();
        int min = 19;
        ArrayList<Integer> lowest = new ArrayList<Integer>();
        for (int i = 0; i < 6; i++) {
            scoreCopy[i] += m.getBenefit()[i];
            if (scoreCopy[i] == min) {
                lowest.add(i);
            }
            if (scoreCopy[i] < min) {
                lowest.clear();
                lowest.add(i);
                min = scoreCopy[i];
            }
        }
        for (ColorDuplet d : state.getActivePlayer().getDuplets()) {
            if (d.equals(m.getDuplet())) {
                continue;
            }
            if (lowest.contains(d.getC1().getVal()) || lowest.contains(d.getC2().getVal())) {
                return false;
            }
        }
        return true;
    }


    public int determineBestMoveForWorstColor(List<HexMove> moves) {
        List<ColorSort> sorted = new ArrayList<ColorSort>();
        for (int i = 0; i < 6; i++) {
            sorted.add(new ColorSort(i, this.scores[i]));
        }
        Collections.sort(sorted);
        int benefit = 0;
        int q = 0;
        int lowest = sorted.get(0).getVal();
        for (ColorSort c : sorted) {
            if (c.getVal() > lowest && benefit > 0) {
                break;
            }
            for (int i = 0; i < moves.size(); i++) {
                if (moves.get(i).getBenefit()[c.getIndex()] > benefit) {
                    q = i;
                    benefit = moves.get(i).getBenefit()[c.getIndex()];
                }
            }
        }
        return q;
    }

    public int determineMaximumMove(List<HexMove> moves) {
        int max = 0;
        int tmp;
        ArrayList<Integer> maxList = new ArrayList<Integer>();
        for (int i = 0; i < moves.size(); i++) {
            if (!checkMoveScoringMax(moves.get(i).getBenefit())) {
                continue;
            }
            tmp = Auxiliary.getByteSum(moves.get(i).getBenefit());
            if (tmp == max) {
                maxList.add(i);
            }
            if (tmp > max) {
                maxList.clear();
                maxList.add(i);
                max = tmp;
            }
        }
        return maxList.get(Auxiliary.getRandomInt(0, maxList.size()));
    }

    public boolean checkMoveScoringMax(byte[] benefit) {
        for (int k = 0; k < 6; k++) {
            if (this.scores[k] + benefit[k] > 20) {
                return false;
            }
        }
        return true;
    }


    public int updateScores(byte[] s) {
        int addRounds = 0;
        for (int i = 0; i < 6; i++) {
            if (this.scores[i] >= 18) {
                continue;
            }
            this.scores[i] += s[i];
            if (this.scores[i] >= 18) {
                addRounds++;
                this.scores[i] = 18;
            }
        }
        return addRounds;
    }

    public Player(String name, PlayerType type, boolean first) {
        this.name = name;
        this.id = first ? 0 : 1;
        this.type = type;
        this.scores = new byte[6];
        for (int i = 0; i < 6; i++) {
            this.scores[i] = 0;
        }
        this.duplets = new ColorDuplet[6];
        for (int i = 0; i < 6; i++) {
            this.duplets[i] = null;
        }
    }

    public Player clonePlayer() {
        Player p = null;
        switch (this.getType()) {
            case RANDOM_AGENT:
                p = new RandomAgent(this.getName(), true);
                break;
            case MINIMUM_AGENT:
                p = new MinimumAgent(this.getName(), true);
                break;
            case MCTS_AGENT:
                p = new MCTSAgent(this.getName(), ((MCTSAgent) this).getIterations(), true);
                break;
            case MAXMIN_AGENT:
                p = new MaxMinAgent(this.getName(), true);
                break;
            case HUMAN:
                p = new Human(this.getName(), true);
                break;
            case MCTS_GROUP_AGENT3:
                p = new MCTSGroupAgent(this.getName(), ((MCTSGroupAgent) this).getIterations(), true);
                break;
            case MCTS_BIAS:
                p = new MCTSBiasAgent(this.getName(), ((MCTSBiasAgent) this).getIterations(), true);
                break;
        }
        p.setId(this.id);
        p.setScores(this.scores.clone());
        p.duplets = this.duplets.clone();
        p.setType(this.type);
        return p;
    }

    public List<HexMove> generateBestMovesHeavyPlayout(GameState state) {
        HashSet<HexMove> moves = new HashSet<HexMove>();
        GameHexagon g, neighbour;
        HexMove m;
        ColorDuplet d;
        List<Integer> lowest = Auxiliary.getLowestScores(state.getActivePlayer().getScores());
        for (int i = 0; i < state.getActivePlayer().getDuplets().length; i++) {
            if ((d = state.getActivePlayer().getDuplets()[i]) == null) {
                continue;
            }
            if (lowest.contains(d.getC1().getVal())) {
                for (int a : state.getPositiveScores().get(d.getC1().getVal())) {
                    g = state.getHexagons()[a];
                    for (int n : g.getNeighbours()) {
                        if ((neighbour = state.getHexagons()[n]).getCol() != ColorType.WHITE) {
                            continue;
                        }
                        m = new HexMove(this, d, g.getAxis(), neighbour.getAxis(), state.getHexagons()[g.getNum()],
                                neighbour);
                        moves.add(m);
                    }
                }
            }
            if (d.sameColor()) {
                continue;
            }
            if (lowest.contains(d.getC2().getVal())) {
                for (int a : state.getPositiveScores().get(d.getC2().getVal())) {
                    g = state.getHexagons()[a];
                    for (int n : g.getNeighbours()) {
                        if ((neighbour = state.getHexagons()[n]).getCol() != ColorType.WHITE) {
                            continue;
                        }
                        m = new HexMove(this, d, neighbour.getAxis(), g.getAxis(), neighbour, state.getHexagons()[g
                                .getNum()]);
                        moves.add(m);
                    }
                }
            }
        }
        if (moves.isEmpty()) {
            return this.generateDesirableMoves(state);
        }
        HexMove m2;
        List<HexMove> swapMoves = new ArrayList<HexMove>();
        for (HexMove m1 : moves) {
            if (swapPossible(state, m1)) {
                m2 = new HexMove(m1);
                m2.setSwap(true);
                swapMoves.add(m2);
                break;
            }
        }
        moves.addAll(swapMoves);
        return new ArrayList<HexMove>(moves);
    }


    public HexMove notifyMove(GameState state, GameState previousState, HexMove previousMove) {
        List<HexMove> moves = this.generatePossibleMoves(state);
        return moves.remove(0);
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object other) {
        return this.id == ((Player) other).id;
    }
}
