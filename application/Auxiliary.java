package application;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;

public class Auxiliary {
    private static SplittableRandom rand = new SplittableRandom();

    public static byte getByteSum(byte[] b) {
        byte sum = 0;
        for (byte a : b) {
            sum += a;
        }
        return sum;
    }

    public static List<Byte> getByteList(byte[] b) {
        List<Byte> l = new ArrayList<>();
        for (byte a : b) {
            l.add(a);
        }
        return l;
    }

    /*
    Returns random integers in the specified range. Lower bound is inclusive, upper bound exclusive.
     */
    public static int getRandomInt(int lBound, int uBound) {
        return rand.nextInt(lBound, uBound);
    }

    public static List<Integer> getLowestScores(byte[] scores) {
        byte lowest = 19;
        List<Integer> indices = new ArrayList<Integer>();
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] == lowest) {
                indices.add(i);
            } else if (scores[i] < lowest) {
                indices.clear();
                indices.add(i);
                lowest = scores[i];
            }
        }
        return indices;
    }

    public static List<Integer> getHighestScores(byte[] scores) {
        byte highest = -1;
        List<Integer> indices = new ArrayList<Integer>();
        for (int i = 0; i < scores.length; i++) {
            if (scores[i] == highest) {
                indices.add(i);
            } else if (scores[i] > highest) {
                indices.clear();
                indices.add(i);
                highest = scores[i];
            }
        }
        return indices;
    }
}
