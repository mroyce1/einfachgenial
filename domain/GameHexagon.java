package domain;

import application.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameHexagon extends Hexagon {
    private static int numCounter = 0;


    private HashMap<Axis, ArrayList<Integer>> linearSuccessors;
    private List<Integer> neighbours;
    private byte[] scores;
    private int num;

    public int getNum() {
        return num;
    }

    public GameHexagon(Axis axis, ColorType c) {
        super(axis, c);
        this.linearSuccessors = new HashMap<Axis, ArrayList<Integer>>();
        this.neighbours = new ArrayList<Integer>();
        this.scores = new byte[]{0, 0, 0, 0, 0, 0};
        this.num = GameHexagon.numCounter++;

    }

    public GameHexagon(GameHexagon hex) {
        super(hex.axis, hex.col);
        this.linearSuccessors = hex.linearSuccessors;
        this.scores = hex.scores.clone();
        this.neighbours = hex.neighbours;
        this.num = hex.num;
    }

    @Override
    public boolean equals(Object other) {
        GameHexagon b = (GameHexagon) other;
        if (!this.axis.equals(b.axis)) {
            return false;
        }
        if (this.col.getVal() != b.col.getVal()) {
            return false;
        }
        return true;
    }

    public byte[] getScores() {
        return scores;
    }

    public HashMap<Axis, ArrayList<Integer>> getLinearSuccessors() {
        return linearSuccessors;
    }

    public void setLinearSuccessors(HashMap<Axis, ArrayList<Integer>> linearSuccessors) {
        this.linearSuccessors = linearSuccessors;
    }

    public void updateScores(GameState state) {
        byte[] newScores = new byte[]{0, 0, 0, 0, 0, 0};
        if (this.col != ColorType.WHITE) {
            this.scores = newScores;
            return;
        }
        int sum;
        GameHexagon nextNeighbour;
        for (ArrayList<Integer> ls : this.linearSuccessors.values()) {
            sum = 0;
            ColorType closestColor = state.getHexagons()[ls.get(0)].getCol();
            for (int a : ls) {
                nextNeighbour = state.getHexagons()[a];
                if (nextNeighbour.getCol() == ColorType.WHITE || nextNeighbour.getCol() != closestColor) {
                    break;
                }
                sum++;
            }
            if (closestColor != ColorType.WHITE) {
                newScores[closestColor.getVal()] += sum;
            }
        }
        this.scores = newScores;
        if (state.getPositiveScores() == null) {
            return;
        }
        for (int i = 0; i < 6; i++) {
            if (this.scores[i] > 0) {
                state.getPositiveScores().get(i).add(this.num);
            }
        }
    }

    public void updateLinearSuccessorScores(GameState state) {
        for (ArrayList<Integer> ls : this.linearSuccessors.values()) {
            for (int a : ls) {
                state.getHexagons()[a].updateScores(state);
            }
        }
    }

    public List<Integer> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(List<Integer> neighbours) {
        this.neighbours = neighbours;
    }

    public void setScores(byte[] scores) {
        this.scores = scores;
    }

    public void generateNeighbours(GameHexagon[] hexagons) {
        for (GameHexagon a : hexagons) {
            if (a.axis.equals(this.axis)) {
                continue;
            }
            for (int b : this.neighbours) {
                if (!hexagons[b].axis.equals(a.axis)) {
                    continue;
                }
                break;
            }
            for (Axis b : Constants.PIECE_POSITIONS) {
                if (a.axis.subtract(b).equals(this.axis)) {
                    this.neighbours.add(a.getNum());
                }
            }
        }
    }

    @Override
    public String toString() {
        return this.axis + "   " + this.col;
    }

    public static int getNumCounter() {
        return numCounter;
    }


    public static void resetNumCounter() {
        GameHexagon.numCounter = 0;
    }
}