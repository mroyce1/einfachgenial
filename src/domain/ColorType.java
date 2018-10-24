package domain;

import javafx.scene.paint.Color;

public enum ColorType {
    BLUE(0), GREEN(1), ORANGE(2), RED(3), VIOLET(4), YELLOW(5), WHITE(6), BLACK(7);

    private int val;

    ColorType(int val) {
        this.val = val;
    }

    public int getVal() {
        switch (this) {
            case BLUE:
                return 0;
            case GREEN:
                return 1;
            case ORANGE:
                return 2;
            case RED:
                return 3;
            case VIOLET:
                return 4;
            case YELLOW:
                return 5;
            case WHITE:
                return 6;
        }
        return 7;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public static ColorType instantiate(int v) {
        ColorType col = ColorType.BLACK;
        switch (v) {
            case 0:
                return ColorType.BLUE;
            case 1:
                return ColorType.GREEN;
            case 2:
                return ColorType.ORANGE;
            case 3:
                return ColorType.RED;
            case 4:
                return ColorType.VIOLET;
            case 5:
                return ColorType.YELLOW;
            case 6:
                return ColorType.WHITE;
        }
        return col;
    }

    @Override
    public String toString() {
        switch (this) {
            case BLUE:
                return "BLUE";
            case GREEN:
                return "GREEN";
            case ORANGE:
                return "ORANGE";
            case RED:
                return "RED";
            case VIOLET:
                return "VIOLET";
            case YELLOW:
                return "YELLOW";
            case WHITE:
                return "WHITE";
            case BLACK:
                return "BLACK";
        }
        return "no valid color";
    }

    public int[] getRGB() {
        int[] rgb = {255, 255, 255};
        switch (this) {
            case BLUE:
                rgb = new int[]{0, 0, 204};
                break;
            case GREEN:
                rgb = new int[]{0, 102, 0};
                break;
            case ORANGE:
                rgb = new int[]{255, 128, 0};
                break;
            case RED:
                rgb = new int[]{255, 0, 0};
                break;
            case VIOLET:
                rgb = new int[]{102, 0, 102};
                break;
            case YELLOW:
                rgb = new int[]{255, 255, 0};
                break;
        }
        return rgb;
    }

    public Color getJFXColor() {
        Color col = Color.WHITE;
        switch (this) {
            case BLUE:
                return Color.BLUE;
            case GREEN:
                return Color.GREEN;
            case ORANGE:
                return Color.ORANGE;
            case RED:
                return Color.RED;
            case VIOLET:
                return Color.PURPLE;
            case YELLOW:
                return Color.YELLOW;
        }
        return col;
    }
}
