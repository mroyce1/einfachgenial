package application;

import domain.HexMove;

import java.util.Comparator;
import java.util.List;

public class MoveComparator implements Comparator<HexMove> {
    private List<Integer> lowest = null;

    public MoveComparator(List<Integer> lowest) {
        this.lowest = lowest;
    }

    public List<Integer> getLowest() {
        return lowest;
    }

    public void setLowest(List<Integer> lowest) {
        this.lowest = lowest;
    }

    public int compare(HexMove m1, HexMove m2) {
        if (this.lowest == null) {
            if (Auxiliary.getByteSum(m1.getBenefit()) > Auxiliary.getByteSum(m2.getBenefit())) {
                return -1;
            } else if ((Auxiliary.getByteSum(m1.getBenefit()) < Auxiliary.getByteSum(m2.getBenefit()))) {
                return 1;
            }
            if (m1.isSwap() && !m2.isSwap()) {
                return -1;
            }
            if (!m1.isSwap() && m2.isSwap()) {
                return 1;
            }
            return 0;
        }
        for (int i : this.lowest) {
            if (m1.getBenefit()[i] > m2.getBenefit()[i]) {
                return 1;
            } else if (m1.getBenefit()[i] < m2.getBenefit()[i]) {
                return -1;
            }
        }
        return 0;
    }
}
