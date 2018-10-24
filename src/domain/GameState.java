package domain;

import agent.Player;
import application.Auxiliary;
import application.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class GameState {
    private Player player1;
    private Player player2;
    private int activePlayer;
    private int inactivePlayer;
    private ArrayList<ColorDuplet> stack;
    private int additionalRounds;
    private boolean finished;
    private int freetiles;
    private ArrayList<HashSet<Integer>> positiveScores;
    private GameHexagon[] hexagons;
    private int movesPlayed;

    public GameHexagon[] getHexagons() {
        return hexagons;
    }

    public void setHexagons(GameHexagon[] hexagons) {
        this.hexagons = hexagons;
    }

    public GameState(Player p1, Player p2) {
        System.out.println("gs created");

        this.player1 = p1;
        this.player2 = p2;
        this.activePlayer = p1.getId();
        this.inactivePlayer = p2.getId();
        this.additionalRounds = 0;
        this.movesPlayed = 0;
        this.finished = false;
        this.stack = new ArrayList<ColorDuplet>();

        for (int i = 0; i < Constants.DUPLETS.size(); i++) {
            this.stack.add(new ColorDuplet(Constants.DUPLETS.get(i)));
        }
        Collections.shuffle(this.stack);
        this.hexagons = Hexagon.setupGameGrid(this);

        this.distributeDuplets();
        this.freetiles = this.hexagons.length - 6;
        this.positiveScores = Hexagon.setInitialPositiveScores(this.hexagons);
    }

    public int getMovesPlayed() {
        return movesPlayed;
    }

    public void incrementMovesPlayed() {
        this.movesPlayed++;
    }

    public GameState(GameState state) {
        this.player1 = state.getPlayer1().clonePlayer();
        this.player2 = state.getPlayer2().clonePlayer();
        this.activePlayer = state.getPlayer1().getId() == state.activePlayer ? this.player1.getId() :
                this.player2.getId();
        this.inactivePlayer = state.getPlayer1().getId() == state.inactivePlayer ? this.player1.getId() :
                this.player2.getId();
        this.stack = new ArrayList<ColorDuplet>(state.stack);
        this.hexagons = new GameHexagon[state.getHexagons().length];
        for (int i = 0; i < state.getHexagons().length; i++) {
            this.hexagons[i] = new GameHexagon(state.getHexagons()[i]);
        }
        this.additionalRounds = state.additionalRounds;
        this.freetiles = state.freetiles;
        this.finished = state.finished;
        this.movesPlayed = state.movesPlayed;

        this.positiveScores = new ArrayList<HashSet<Integer>>();
        for (int i = 0; i < state.positiveScores.size(); i++) {
            HashSet<Integer> tmp = new HashSet<>(state.positiveScores.get(i));
            this.positiveScores.add(tmp);
        }
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public Player getActivePlayer() {
        return this.getByID(this.activePlayer);
    }

    public void setActivePlayer(Player activePlayer) {
        this.activePlayer = activePlayer.getId();
    }

    public Player getInactivePlayer() {
        return this.getByID(this.inactivePlayer);
    }

    public void setInactivePlayer(Player inactivePlayer) {
        this.inactivePlayer = inactivePlayer.getId();
    }

    public ArrayList<ColorDuplet> getStack() {
        return stack;
    }

    public void setStack(ArrayList<ColorDuplet> stack) {
        this.stack = stack;
    }


    public Player getByID(int id) {
        if (id == this.player1.getId()) {
            return this.player1;
        }
        return this.player2;
    }

    public void removeHexagonFromScores(int num) {
        Integer q = new Integer(num);
        for (int i = 0; i < 6; i++) {
            if (this.positiveScores.get(i).contains(q)) {
                this.positiveScores.get(i).remove(q);
            }
        }
    }

    public boolean isFinished() {
        return this.finished;
    }

    public void setFinished(boolean v) {
        this.finished = v;
    }

    public int getAdditionalRounds() {
        return additionalRounds;
    }

    public void distributeDuplets() {
        int rdm;
        for (int i = 0; i < 6; i++) {
            if (this.stack.isEmpty()) {
                System.out.println("stack empty. no distribution possible");
                return;
            }
            if (this.player1.getDuplets()[i] == null) {
                rdm = Auxiliary.getRandomInt(0, this.stack.size());
                this.player1.getDuplets()[i] = this.stack.remove(rdm);
            }
            if (this.player2.getDuplets()[i] == null) {
                rdm = Auxiliary.getRandomInt(0, this.stack.size());
                this.player2.getDuplets()[i] = this.stack.remove(rdm);
            }
        }
        this.stack.trimToSize();
    }

    public void reduceFreeTiles(int tiles) {
        this.freetiles -= tiles;
    }

    public int getFreetiles() {
        return this.freetiles;
    }

    public void switchTurn() {
//        if (this.additionalRounds > 0) {
//            this.additionalRounds--;
//            return;
//        }
        this.activePlayer = (this.activePlayer + 1) % 2;
        this.inactivePlayer = (this.inactivePlayer + 1) % 2;
    }

    public void addAdditionalRounds(int k) {
        this.additionalRounds += k;
    }

    public ArrayList<HashSet<Integer>> getPositiveScores() {
        return positiveScores;
    }

    public void setPositiveScores(ArrayList<HashSet<Integer>> positiveScores) {
        this.positiveScores = positiveScores;
    }
}