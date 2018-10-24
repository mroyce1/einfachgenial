package application;

public class ColorSort implements Comparable<ColorSort> {
    private int val;
    private int index;

    public ColorSort(int index, int val) {
        this.index = new Integer(index);
        this.val = new Integer(val);
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int compareTo(ColorSort other) {
        return this.val - other.val;
    }
}
