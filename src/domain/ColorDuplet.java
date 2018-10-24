package domain;

import java.io.IOException;

public class ColorDuplet {
    private ColorType c1;
    private ColorType c2;

    public ColorType getC1() {
        return c1;
    }

    public void setC1(ColorType c1) {
        this.c1 = c1;
    }

    public ColorType getC2() {
        return c2;
    }

    public void setC2(ColorType c2) {
        this.c2 = c2;
    }

    public ColorDuplet(ColorDuplet duplet) {
        this.c1 = ColorType.instantiate(duplet.c1.getVal());
        this.c2 = ColorType.instantiate(duplet.c2.getVal());
    }


    public void setCol(ColorDuplet c) {
        this.c1 = c.getC1();
        this.c2 = c.getC2();
    }


    public ColorDuplet(ColorType c1, ColorType c2, Axis ax1, Axis ax2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    public boolean sameColor() {
        return this.c1.equals(this.c2);
    }


    public ColorDuplet(ColorType c1, ColorType c2) {
        this.c1 = c1;
        this.c2 = c2;
    }

    @Override
    public String toString() {
        return this.c1 + " | " + this.c2;
    }
}
