package domain;

import agent.Player;
import application.Auxiliary;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class HexMove implements Comparable {
    private ColorDuplet duplet;
    private int playerID;
    private byte[] benefit;
    private boolean swap;
    private Axis axis1;
    private Axis axis2;
    int num1;
    int num2;

    public HexMove(Player p, ColorDuplet d, Axis a1, Axis a2, HashMap<Axis, GameHexagon> hexagons) {
        this.playerID = p.getId();
        this.duplet = d;
        this.axis1 = a1;
        this.axis2 = a2;
        this.swap = false;
        this.benefit = new byte[]{0, 0, 0, 0, 0, 0};
        this.benefit[this.duplet.getC1().getVal()] += hexagons.get(this.axis1).getScores()[this.duplet.getC1()
                .getVal()];
        this.benefit[this.duplet.getC2().getVal()] += hexagons.get(this.axis2).getScores()[this.duplet.getC2()
                .getVal()];

    }


    public HexMove(Player p, ColorDuplet d, Axis a1, Axis a2, GameHexagon g1, GameHexagon g2) {
        this.playerID = p.getId();
        this.duplet = d;
        this.axis1 = a1;
        this.axis2 = a2;
        this.swap = false;
        this.benefit = new byte[]{0, 0, 0, 0, 0, 0};
        this.benefit[this.duplet.getC1().getVal()] += g1.getScores()[this.duplet.getC1()
                .getVal()];
        this.benefit[this.duplet.getC2().getVal()] += g2.getScores()[this.duplet.getC2()
                .getVal()];
        this.num1 = g1.getNum();
        this.num2 = g2.getNum();
    }

    public HexMove(HexMove other) {
        this.playerID = other.playerID;
        this.duplet = other.duplet;
        this.swap = other.swap;
        this.axis1 = other.axis1;
        this.axis2 = other.axis2;
        this.benefit = other.benefit.clone();
        this.num1 = other.num1;
        this.num2 = other.num2;
    }

    public ColorDuplet getDuplet() {
        return duplet;
    }

    public void setDuplet(ColorDuplet duplet) {
        this.duplet = duplet;
    }

    public Axis getAxis1() {
        return this.axis1;
    }

    public void setAxis1(Axis axis1) {
        this.axis1 = axis1;
    }

    public Axis getAxis2() {
        return this.axis2;
    }

    public void setAxis2(Axis axis2) {
        this.axis2 = axis2;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public byte[] getBenefit() {
        return benefit;
    }

    public void setBenefit(byte[] benefit) {
        this.benefit = benefit;
    }


    public boolean isValid(GameState state) {
        if (state.getHexagons()[this.num1].getCol() != ColorType.WHITE) {
            return false;
        }
        if (state.getHexagons()[this.num2].getCol() != ColorType.WHITE) {
            return false;
        }
        if (!state.getHexagons()[this.num1].getNeighbours().contains(this.num2)) {
            return false;
        }
        return true;
    }

    public void execute(GameState state) {
        state.addAdditionalRounds(state.getByID(this.playerID).updateScores(this.benefit));
        state.removeHexagonFromScores(this.num1);
        state.removeHexagonFromScores(this.num2);

        state.getHexagons()[this.num1].setCol(this.duplet.getC1());
        state.getHexagons()[this.num2].setCol(this.duplet.getC2());
        state.getHexagons()[this.num1].updateLinearSuccessorScores(state);
        state.getHexagons()[this.num2].updateLinearSuccessorScores(state);
        state.getByID(this.playerID).removeDuplet(this.duplet);
        if (this.swap) {
            ColorDuplet[] tmp = state.getActivePlayer().getDuplets();
            state.getActivePlayer().clearAllDuplets();
            state.distributeDuplets();
            state.getStack().addAll(Arrays.asList(tmp));
        } else {
            state.distributeDuplets();
        }
        state.incrementMovesPlayed();
        state.reduceFreeTiles(2);
        state.switchTurn();
    }

    private boolean checkForSwap(GameState state) {
        int min = 19;
        HashSet<Integer> minColors = new HashSet<Integer>();
        for (int i = 0; i < 6; i++) {
            if (state.getActivePlayer().getScores()[i] == min) {
                minColors.add(i);
            }
            if (state.getActivePlayer().getScores()[i] < min) {
                min = state.getActivePlayer().getScores()[i];
                minColors.clear();
                minColors.add(i);
            }
        }
        for (ColorDuplet d : state.getActivePlayer().getDuplets()) {
            if (d == null) {
                continue;
            }
            if (minColors.contains(d.getC1().getVal()) || minColors.contains(d.getC2().getVal())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        if (!this.duplet.sameColor()) {
            return this.getAxis1() + "   " + this.duplet.getC1().toString() + ": " + this.benefit[this.duplet.getC1()
                    .getVal()] + "   " + this.getAxis2() + "   " + this.duplet.getC2().toString() + ": " + this
                    .benefit[this.duplet.getC2().getVal()] + "   swap: " + this.swap;
        }
        return this.getAxis1() + "   " + this.duplet.getC1().toString() + ": " + this.benefit[this.duplet.getC1()
                .getVal()] + "   " + this.getAxis2() + "   " + this.duplet.getC2().toString() + ": 0" + "   swap: " +
                this.swap;
    }

    @Override
    public int compareTo(Object o) {
        HexMove h = (HexMove) o;
        if (Auxiliary.getByteSum(this.benefit) > Auxiliary.getByteSum(h.benefit)) {
            return -1;
        } else if ((Auxiliary.getByteSum(this.benefit) < Auxiliary.getByteSum(h.benefit))) {
            return 1;
        }
        if (this.swap && !h.swap) {
            return -1;
        }
        if (!this.swap && h.swap) {
            return 1;
        }
        return 0;
    }

    public boolean isSwap() {
        return this.swap;
    }

    public void setSwap(boolean val) {
        this.swap = val;
    }


    @Override
    public boolean equals(Object other) {
        HexMove b = (HexMove) other;
        if (this.playerID != b.playerID) {
            return false;
        }
        if (this.swap != b.swap) {
            return false;
        }
        for (int i = 0; i < 6; i++) {
            if (this.benefit[i] != b.benefit[i]) {
                return false;
            }
        }
        if (this.axis1 == null || this.axis2 == null || b.axis1 == null || b.axis2 == null) {

            return this.duplet.getC1().getVal() == b.duplet.getC1().getVal() && this.duplet.getC2().getVal() == b
                    .duplet.getC2().getVal() ||
                    this.duplet.getC1().getVal() == b.duplet.getC2().getVal() && this.duplet.getC2().getVal() == b
                            .duplet.getC1().getVal();
        }
        if (this.axis1.equals(b.axis1) && this.axis2.equals(b.axis2)) {

            return this.duplet.getC1().getVal() == b.duplet.getC1().getVal() && this.duplet.getC2().getVal() == b
                    .duplet.getC2().getVal();
        } else if (this.axis1.equals(b.axis2) && this.axis2.equals(b.axis1)) {
            return this.duplet.getC1().getVal() == b.duplet.getC2().getVal() && this.duplet.getC2().getVal() == b
                    .duplet.getC1().getVal();
        }
        return false;
    }

    @Override
    public int hashCode() {
        int prime1 = 17 * (this.duplet.getC1().getVal() + this.axis1.hashCode());
        int prime2 = 17 * (this.duplet.getC2().getVal() + this.axis2.hashCode());
        return prime1 + prime2;
    }


}
