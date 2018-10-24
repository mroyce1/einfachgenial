package ui;

import application.Constants;
import domain.Axis;
import domain.ColorDuplet;
import domain.ColorType;

import java.util.ArrayList;
import java.util.List;

public class Duplet {
    private GraphicalHexagon hex1;
    private GraphicalHexagon hex2;
    private int orientation;
    private Axis offset;
    private boolean visible;

    public Duplet(GraphicalHexagon hex1, GraphicalHexagon hex2) {
        this.hex1 = hex1;
        this.hex2 = hex2;
        this.orientation = 0;
        this.offset = new Axis(0, -1, 1);
        this.visible = true;
    }


    public void setVisible(boolean v) {
        this.visible = v;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void updateCol(ColorDuplet c) {
        if (c == null) {
            this.hex1.setVisible(false);
            this.hex2.setVisible(false);
            return;
        }
        this.hex1.setCol(c.getC1());
        this.hex2.setCol(c.getC2());
    }

    public void rotate(boolean clockwise) {
        if (this.hex1.getCol() == ColorType.WHITE.WHITE || this.hex2.getCol() == ColorType.WHITE) {
            return;
        }
        this.orientation = clockwise ? this.orientation + 1 : this.orientation - 1;
        this.hex2.setAxis(Constants.PIECE_POSITIONS.get(this.orientation % Constants.PIECE_POSITIONS.size()));
        this.offset = this.hex1.getAxis().subtract(this.hex2.getAxis());
        this.updatePosition(this.hex2.getCenter());
    }

    public void updatePosition(double[] pos) {
        if (!this.visible) {
            return;
        }
        this.hex1.setCenter(pos);
        this.hex2.setCenter(pos);
        Axis tmp = this.hex1.getAxis().add(this.offset);
        this.hex1.updatePosition(pos);
        this.hex2.setAxis(tmp);
        this.hex2.updatePosition(pos);
    }

    public GraphicalHexagon getHex1() {
        return hex1;
    }

    public void setHex1(GraphicalHexagon hex1) {
        this.hex1 = hex1;
    }

    public GraphicalHexagon getHex2() {
        return hex2;
    }

    public void setHex2(GraphicalHexagon hex2) {
        this.hex2 = hex2;
    }

    public int getOrientation() {
        return orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public Axis getOffset() {
        return offset;
    }

    public void setOffset(Axis offset) {
        this.offset = offset;
    }

    public boolean isClickInside(double x, double y) {
        if (this.hex1.isClickInside(x, y)) {
            return true;
        }
        if (this.hex2.isClickInside(x, y)) {
            return true;
        }
        return false;
    }

    public static List<Duplet> setupDuplets(boolean p1) {
        List<Duplet> tmp = new ArrayList<Duplet>();
        if (p1) {
            for (int i = 0; i < 6; i++) {
                double[] c1 = {Constants.PIECE_P1_X, Constants.PIECE_P1_Y + Constants.PIECE_VERTICAL_OFFSET * i};
                GraphicalHexagon h1 = new GraphicalHexagon(new Axis(0, 0, 0), ColorType.WHITE, false);
                GraphicalHexagon h2 = new GraphicalHexagon(new Axis(0, 0, 0), ColorType.WHITE, false);
                Duplet d = new Duplet(h1, h2);
                d.updatePosition(c1);
                tmp.add(d);
            }
        } else {
            for (int i = 0; i < 6; i++) {
                double[] c1 = {Constants.PIECE_P2_X, Constants.PIECE_P2_Y + Constants.PIECE_VERTICAL_OFFSET * i};
                GraphicalHexagon h1 = new GraphicalHexagon(new Axis(0, 0, 0), ColorType.WHITE, false);
                GraphicalHexagon h2 = new GraphicalHexagon(new Axis(0, 0, 0), ColorType.WHITE, false);
                Duplet d = new Duplet(h1, h2);
                d.updatePosition(c1);
                tmp.add(d);
            }
        }
        return tmp;
    }


}