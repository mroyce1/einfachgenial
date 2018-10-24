package application;

import agent.Player;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class LogWriter {


    public static void writeLog(Player p1, Player p2, int maxRounds, double[] wonRounds, int[] matchRounds, long
            moveTime1, long moveTime2,
                                long totalMoveTime1, long totalMoveTime2) {
        int i = 0;
        String filename = "stats" + i;
        while ((new File("./" + filename + ".txt").exists())) {
            i++;
            filename = "stats" + i;
        }
        try {
            PrintWriter writer = new PrintWriter("./" + filename + ".txt", "UTF-8");
            writer.println("Final result: " + p1 + " (" + wonRounds[0] + " | " + wonRounds[1] +
                    ") " + p2);
            writer.println("Rounds played: " + maxRounds);
            writer.println("Total move time: " + (totalMoveTime1 / 1000d) + "s | " + (
                    totalMoveTime2 /
                            1000d) + "s");
            writer.println("Average move time: " + (totalMoveTime1 / ((double) matchRounds[0] * 1000d) + "s | " +
                    (totalMoveTime2 / ((double) matchRounds[1] * 1000d) + "s")));
            writer.println("\n\n");
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
