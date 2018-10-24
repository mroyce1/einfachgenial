package application;

import domain.Axis;
import domain.ColorType;
import domain.ColorDuplet;

import java.util.ArrayList;

public class Constants {


    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = (int) (SCREEN_WIDTH / 16 * 9);

        public static final int DIAGONAL_LENGTH = 11;
    public static final int NUM_OF_TILES = 91;


    public static final double[] ORIGIN = {(double) (SCREEN_WIDTH / 2.0), (double) (SCREEN_HEIGHT / 2.0)};

    public static final int COORDS_X = (int) (SCREEN_WIDTH / 4);




    public static final int RADIUS = (int) (SCREEN_WIDTH / 48);


    public static final int RADIUS_OFFSET = (int) (RADIUS / 10);


    public static final int RADIUS_PIECE = (int) (SCREEN_WIDTH / 48);

    public static final int PIECE_P1_X = (int) (SCREEN_WIDTH / 10);

    public static final int PIECE_P1_Y = (int) (SCREEN_WIDTH / 4);
    public static final int PIECE_P2_Y = PIECE_P1_Y;

    public static final int PIECE_P2_X = SCREEN_WIDTH - (PIECE_P1_X * 2);
    public static final int PIECE_VERTICAL_OFFSET = (int) (PIECE_P1_X / 2);


    public static final int FONT_SIZE = (int) ((SCREEN_WIDTH / 96));

    public static final int FONT_SIZE_INFO = (int) (SCREEN_WIDTH / 96);

    public static final String FONT_TYPE = "Arial";

    public static final int SCORE_P1_X = (int) SCREEN_WIDTH / 32;

    public static final int SCORE_P1_Y = (int) SCREEN_HEIGHT / 18;

    public static final int SCORE_P2_Y = SCORE_P1_Y;


    public static final int SCORE_P2_X = SCREEN_WIDTH - (int) (SCREEN_WIDTH / 4);

    public static final int SCORE_VERTICAL_OFFSET = FONT_SIZE * 3;
    public static final int[] SCORE_BACKGROUND_RGB = {119, 136, 153};


    public static final ArrayList<Axis> PIECE_POSITIONS = new ArrayList<Axis>();
    public static final ArrayList<ColorDuplet> DUPLETS = new ArrayList<ColorDuplet>();

    public static void setupConstantsLists() {
        PIECE_POSITIONS.add(new Axis(0, -1, 1));
        PIECE_POSITIONS.add(new Axis(1, -1, 0));
        PIECE_POSITIONS.add(new Axis(1, 0, -1));
        PIECE_POSITIONS.add(new Axis(0, 1, -1));
        PIECE_POSITIONS.add(new Axis(-1, 1, 0));
        PIECE_POSITIONS.add(new Axis(-1, 0, 1));

        DUPLETS.add(new ColorDuplet(ColorType.ORANGE, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.ORANGE, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.ORANGE));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.RED));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.ORANGE));
        DUPLETS.add(new ColorDuplet(ColorType.VIOLET, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.ORANGE, ColorType.RED));
        DUPLETS.add(new ColorDuplet(ColorType.VIOLET, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.GREEN));
        DUPLETS.add(new ColorDuplet(ColorType.RED, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.RED, ColorType.RED));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.ORANGE));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.RED));
        DUPLETS.add(new ColorDuplet(ColorType.YELLOW, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.RED));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.ORANGE));
        DUPLETS.add(new ColorDuplet(ColorType.VIOLET, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.ORANGE, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.YELLOW, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.VIOLET, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.GREEN));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.ORANGE));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.ORANGE, ColorType.RED));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.GREEN));
        DUPLETS.add(new ColorDuplet(ColorType.ORANGE, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.ORANGE, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.RED));
        DUPLETS.add(new ColorDuplet(ColorType.VIOLET, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.YELLOW, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.YELLOW, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.RED));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.ORANGE, ColorType.ORANGE));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.RED, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.RED, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.ORANGE));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.RED));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.GREEN));
        DUPLETS.add(new ColorDuplet(ColorType.VIOLET, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.RED, ColorType.RED));
        DUPLETS.add(new ColorDuplet(ColorType.RED, ColorType.RED));
        DUPLETS.add(new ColorDuplet(ColorType.VIOLET, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.RED, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.ORANGE));
        DUPLETS.add(new ColorDuplet(ColorType.ORANGE, ColorType.RED));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.RED, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.RED, ColorType.RED));
        DUPLETS.add(new ColorDuplet(ColorType.ORANGE, ColorType.ORANGE));
        DUPLETS.add(new ColorDuplet(ColorType.ORANGE, ColorType.ORANGE));
        DUPLETS.add(new ColorDuplet(ColorType.ORANGE, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.GREEN));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.GREEN));
        DUPLETS.add(new ColorDuplet(ColorType.RED, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.ORANGE));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.RED, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.RED));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.BLUE));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.ORANGE));
        DUPLETS.add(new ColorDuplet(ColorType.ORANGE, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.GREEN));
        DUPLETS.add(new ColorDuplet(ColorType.ORANGE, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.YELLOW, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.ORANGE, ColorType.RED));
        DUPLETS.add(new ColorDuplet(ColorType.ORANGE, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.ORANGE));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.BLUE));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.ORANGE));
        DUPLETS.add(new ColorDuplet(ColorType.ORANGE, ColorType.RED));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.ORANGE));
        DUPLETS.add(new ColorDuplet(ColorType.ORANGE, ColorType.RED));
        DUPLETS.add(new ColorDuplet(ColorType.ORANGE, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.ORANGE, ColorType.ORANGE));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.GREEN));
        DUPLETS.add(new ColorDuplet(ColorType.RED, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.RED, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.RED, ColorType.RED));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.RED));
        DUPLETS.add(new ColorDuplet(ColorType.RED, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.GREEN));
        DUPLETS.add(new ColorDuplet(ColorType.VIOLET, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.RED, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.RED));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.GREEN));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.BLUE));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.GREEN));
        DUPLETS.add(new ColorDuplet(ColorType.VIOLET, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.BLUE));
        DUPLETS.add(new ColorDuplet(ColorType.ORANGE, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.GREEN, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.ORANGE, ColorType.ORANGE));
        DUPLETS.add(new ColorDuplet(ColorType.ORANGE, ColorType.ORANGE));
        DUPLETS.add(new ColorDuplet(ColorType.VIOLET, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.RED));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.RED));
        DUPLETS.add(new ColorDuplet(ColorType.VIOLET, ColorType.VIOLET));
        DUPLETS.add(new ColorDuplet(ColorType.ORANGE, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.YELLOW));
        DUPLETS.add(new ColorDuplet(ColorType.BLUE, ColorType.BLUE));
        DUPLETS.add(new ColorDuplet(ColorType.RED, ColorType.YELLOW));
    }
}
