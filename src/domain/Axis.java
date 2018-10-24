package domain;

public class Axis {
    private int x;
    private int y;
    private int z;

    public Axis(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Axis(Axis other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public Axis add(Axis other) {
        return new Axis(this.x + other.x(), this.y + other.y(), this.z + other.z());
    }

    public Axis subtract(Axis other) {
        return new Axis(this.x - other.x(), this.y - other.y(), this.z - other.z());
    }

    @Override
    public boolean equals(Object other) {
        Axis tmp = (Axis) other;
        return this.x == tmp.x() && this.y == tmp.y() && this.z == tmp.z();
    }

    @Override
    public int hashCode() {
        int res = 17;
        res = 31 * res + String.valueOf(this.x).hashCode();
        res = 31 * res + String.valueOf(this.y).hashCode();
        return 31 * res + String.valueOf(this.z).hashCode();
    }


    @Override
    public String toString() {
        return "(" + this.x + " | " + this.y + " | " + this.z + ")";
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    public int z() {
        return this.z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }
}
