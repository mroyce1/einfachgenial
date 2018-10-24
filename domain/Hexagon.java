package domain;

import application.Constants;
import ui.GraphicalHexagon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public abstract class Hexagon {
    protected ColorType col;
    protected Axis axis;


    public Axis getAxis() {
        return axis;
    }

    public void setAxis(Axis axis) {
        this.axis = axis;
    }

    public ColorType getCol() {
        return this.col;
    }

    public void setCol(ColorType c) {
        this.col = c;
    }

    /*
    Sets up the graphical grid of hexagons that will be displayed on screen.
     */

    public static HashMap<Axis, GraphicalHexagon> setupGraphicalGrid(boolean humanPlayer) {
        HashMap<Axis, GraphicalHexagon> grid = new HashMap<Axis, GraphicalHexagon>();
        if (Constants.DIAGONAL_LENGTH % 2 == 0) {
            System.out.println("Diagonal must have an odd number of hexagons!");
            System.exit(0);
        }
        int lower = (int) -((Constants.DIAGONAL_LENGTH - 1) / 2);
        int upper = (int) ((Constants.DIAGONAL_LENGTH + 1) / 2);
        for (int i = lower; i < upper; i++) {
            for (int j = lower; j < upper; j++) {
                for (int k = lower; k < upper; k++) {
                    if (i + j + k != 0) {
                        continue;
                    }
                    Axis tmp = new Axis(i, j, k);
                    grid.put(tmp, new GraphicalHexagon(tmp, determineColor(i, j, k, lower), humanPlayer));
                }
            }
        }
        return grid;
    }





    public static GameHexagon[] setupGameGrid(GameState state) {
        GameHexagon[] grid = new GameHexagon[Constants.NUM_OF_TILES];
        HashMap<Axis, GameHexagon> grid2 = new HashMap<Axis, GameHexagon>();
        if (Constants.DIAGONAL_LENGTH % 2 == 0) {
            System.out.println("Diagonal must have an odd number of hexagons!");
            System.exit(0);
        }
        int lower = (int) -((Constants.DIAGONAL_LENGTH - 1) / 2);
        int upper = (int) ((Constants.DIAGONAL_LENGTH + 1) / 2);
        for (int i = lower; i < upper; i++) {
            for (int j = lower; j < upper; j++) {
                for (int k = lower; k < upper; k++) {
                    //the sum of the three coordinates of one hexagon in the grid must always be equal to zero
                    if (i + j + k != 0) {
                        continue;
                    }
                    Axis tmp = new Axis(i, j, k);
                    GameHexagon gm = new GameHexagon(tmp, determineColor(i, j, k, lower));
                    grid[gm.getNum()] = gm;
                    grid2.put(tmp, gm);
                }
            }
        }
        setLinearSuccessors(grid2);
        state.setHexagons(grid);
        setInitialMapScores(state);
        setNeighbours(grid);
        return grid;
    }


    /*
    Sets the linear successors for each hexagon of the game grid. I.e. all hexagons that are reachable via a straight
     line from a given hexagon.
     */

    private static void setLinearSuccessors(HashMap<Axis, GameHexagon> hexagons) {
        for (Axis a : hexagons.keySet()) {
            for (Axis b : Constants.PIECE_POSITIONS) {
                Axis ax = a;
                while (true) {
                    ax = ax.add(b);
                    //check whether the respective coordinates are inside the grid
                    if (hexagons.get(ax) == null) {
                        break;
                    }
                    ArrayList<Integer> tmp = null;
                    if ((tmp = hexagons.get(a).getLinearSuccessors().get(b)) == null) {
                        tmp = new ArrayList<Integer>();
                    }
                    tmp.add(hexagons.get(ax).getNum());
                    hexagons.get(a).getLinearSuccessors().put(b, tmp);
                }
            }
        }
    }

    /*
    Sets the initial map scores for the hexagon grid.
     */

    private static void setInitialMapScores(GameState state) {
        for (GameHexagon g : state.getHexagons()) {
            g.updateScores(state);
        }
    }


    private static void setNeighbours(GameHexagon[] hexagons) {
        for (GameHexagon g : hexagons) {
            g.generateNeighbours(hexagons);
        }
    }

    public static ArrayList<HashSet<Integer>> setInitialPositiveScores(GameHexagon[] hexagons) {
        ArrayList<HashSet<Integer>> positiveScores = new ArrayList<HashSet<Integer>>();
        for (int i = 0; i < 6; i++) {
            positiveScores.add(new HashSet<Integer>());
            for (GameHexagon g : hexagons) {
                if (g.getScores()[i] > 0) {
                    positiveScores.get(i).add(g.getNum());
                }
            }
        }
        return positiveScores;
    }


    /*
    Determines the initial color of a hexagon according to its coordinates.
     */
    private static ColorType determineColor(int i, int j, int k, int lower) {
        ColorType col = ColorType.WHITE;
        if (i == 0 && j == lower) {
            col = ColorType.ORANGE;
        } else if (i == 0 && j == -lower) {
            col = ColorType.RED;
        } else if (j == 0 && i == -lower) {
            col = ColorType.YELLOW;
        } else if (k == 0 && j == lower) {
            col = ColorType.BLUE;
        } else if (k == 0 && j == -lower) {
            col = ColorType.VIOLET;
        } else if (j == 0 && i == lower) {
            col = ColorType.GREEN;
        }
        return col;
    }


    public Hexagon(Axis axis, ColorType c) {
        this.axis = axis;
        this.col = c;
    }
}