package application;

import agent.*;
import agent_mcts.MCTSAgent;
import agent_mcts_bias.MCTSBiasAgent;
import agent_mcts_group.MCTSGroupAgent;
import domain.*;
import ui.GraphicalHexagon;
import ui.InitUi;
import ui.MainController;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main extends Thread {
    private static Main instance = null;
    private boolean running;
    private GameState state = null;
    private static Player p1 = null;
    private static Player p2 = null;
    public static List<List<int[]>> stats = new ArrayList<List<int[]>>();
    private static int round = 0;
    private static int maxRounds = 10;
    private static double[] wonRounds = new double[]{0.0, 0.0};
    private static double explorationWeight1 = 1.5;
    private static double explorationWeight2 = 1.5;
    private static long moveTime1 = 0;
    private static long moveTime2 = 0;
    private static long totalMoveTime1 = 0;
    private static long totalMoveTime2 = 0;
    private static int[] matchRounds;
    private static boolean trigger1 = true;
    private static boolean trigger2 = true;


    public static double getExplorationWeight1() {
        return explorationWeight1;
    }

    public static void setExplorationWeight1(double explorationWeight1) {
        Main.explorationWeight1 = explorationWeight1;
    }

    public static double getExplorationWeight2() {
        return explorationWeight2;
    }

    public static void setExplorationWeight2(double explorationWeight2) {
        Main.explorationWeight2 = explorationWeight2;
    }


    public static double[] getWonRounds() {
        return wonRounds;
    }

    private Main() {
        List<int[]> test = new ArrayList<int[]>();
        int[] k = {1, 2};
        test.add(k);
        this.running = true;
        Constants.setupConstantsLists();
        this.init();
        this.start();
    }

    public static boolean isTrigger1() {
        return trigger1;
    }

    public static boolean isTrigger2() {
        return trigger2;
    }

    private void parsePlayers(String s1, String s2) {
        String[] info1 = s1.split(";");
        String[] info2 = s2.split(";");
        if (info1[0].toLowerCase().equals("human")) {
            p1 = new Human(info1[1], true);
        }
        if (info1[0].toLowerCase().equals("plain")) {
            explorationWeight1 = Double.parseDouble(info1[3]);
            p1 = new MCTSAgent(info1[1], Integer.parseInt(info1[2]), true);
        }
        if (info1[0].toLowerCase().equals("group")) {
            p1 = new MCTSGroupAgent(info1[1], Integer.parseInt(info1[2]), true);
            explorationWeight1 = Double.parseDouble(info1[3]);
            trigger1 = Boolean.parseBoolean(info1[4]);
        }
        if (info1[0].toLowerCase().equals("bias")) {
            p1 = new MCTSBiasAgent(info1[1], Integer.parseInt(info1[2]), true);
            explorationWeight1 = Double.parseDouble(info1[3]);
        }
        if (info1[0].toLowerCase().equals("random")) {
            p1 = new RandomAgent(info1[1], true);
        }
        if (info1[0].toLowerCase().equals("min")) {
            p1 = new MinimumAgent(info1[1], true);
            trigger1 = false;
        }
        if (info1[0].toLowerCase().equals("mins")) {
            p1 = new MinimumAgent(info1[1], true);
            trigger1 = true;
        }
        if (info1[0].toLowerCase().equals("max")) {
            p1 = new MaxMinAgent(info1[1], true);
            trigger1 = false;
        }
        if (info1[0].toLowerCase().equals("maxs")) {
            p1 = new MaxMinAgent(info1[1], true);
            trigger1 = true;
        }


        if (info2[0].toLowerCase().equals("human")) {
            p2 = new Human(info2[1], false);
        }
        if (info2[0].toLowerCase().equals("plain")) {
            p2 = new MCTSAgent(info2[1], Integer.parseInt(info2[2]), false);
            explorationWeight2 = Double.parseDouble(info2[3]);
        }
        if (info2[0].toLowerCase().equals("group")) {
            p2 = new MCTSGroupAgent(info2[1], Integer.parseInt(info2[2]), false);
            explorationWeight2 = Double.parseDouble(info2[3]);
            trigger2 = Boolean.parseBoolean(info2[4]);
        }
        if (info2[0].toLowerCase().equals("bias")) {
            p2 = new MCTSBiasAgent(info2[1], Integer.parseInt(info2[2]), false);
            explorationWeight2 = Double.parseDouble(info2[3]);
        }
        if (info2[0].toLowerCase().equals("random")) {
            p2 = new RandomAgent(info2[1], false);
        }
        if (info2[0].toLowerCase().equals("min")) {
            p2 = new MinimumAgent(info2[1], false);
            trigger2 = false;
        }
        if (info2[0].toLowerCase().equals("mins")) {
            p2 = new MinimumAgent(info2[1], false);
            trigger2 = true;
        }
        if (info2[0].toLowerCase().equals("max")) {
            p2 = new MaxMinAgent(info2[1], false);
            trigger2 = false;
        }
        if (info2[0].toLowerCase().equals("maxs")) {
            p2 = new MaxMinAgent(info2[1], false);
            trigger2 = true;
        }
    }

    private void reInit() {
        boolean found = true;
        File file = null;
        FileInputStream is = null;
        InputStreamReader isr = null;
        try {
            is = new FileInputStream("config.cfg");
            isr = new InputStreamReader(is);
        } catch (FileNotFoundException e) {
            System.out.println("config.cfg not found.");
            found = false;
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(isr);
            String st;
            while ((st = br.readLine()) != null) {
                if (st.equals("#Players")) {
                    parsePlayers(br.readLine(), br.readLine());
                    continue;
                }
                if (st.equals("#Rounds")) {
                    continue;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            found = false;
        } catch (IOException e) {
            e.printStackTrace();
            found = false;
        }
        if (found) {
            GameHexagon.resetNumCounter();
            GraphicalHexagon.resetNumCounter();
            this.state = new GameState(p1, p2);
            return;
        }
        System.out.println("Could not read configuration.");
    }

    private void init() {
        boolean found = true;
        File file = null;
        FileInputStream is = null;
        InputStreamReader isr = null;
        try {
            is = new FileInputStream("config.cfg");
            isr = new InputStreamReader(is);
        } catch (FileNotFoundException e) {
            System.out.println("config.cfg not found.");
            found = false;
        }
        BufferedReader br = null;
        try {
            br = new BufferedReader(isr);
            String st;
            while ((st = br.readLine()) != null) {
                if (st.equals("#Players")) {
                    parsePlayers(br.readLine(), br.readLine());
                    continue;
                }
                if (st.equals("#Rounds")) {
                    maxRounds = Integer.parseInt(br.readLine());
                    continue;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            found = false;
        } catch (IOException e) {
            e.printStackTrace();
            found = false;
        }
        if (found) {
            GameHexagon.resetNumCounter();
            GraphicalHexagon.resetNumCounter();
            this.state = new GameState(p1, p2);
            return;
        }
        System.out.println("Could not read configuration");
    }


    public static Main getInstance() {
        if (instance == null) {
            System.out.println("creating new instance");
            instance = new Main();
        }
        return instance;
    }

    public GameState getGameState() {
        return this.state;
    }


    public boolean isRunning() {
        return this.running;
    }

    public void setRunning(boolean val) {
        this.running = val;
    }


    public void run() {
        MainController mc = null;
        long startTime;
        matchRounds = new int[]{0, 0};
        int k = 0;
        try {
            Thread.sleep(1700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        while (this.running) {
            if (this.state.isFinished()) {
                int win = GameController.determineWinner(this.state);
                if (win == -1) {
                    System.out.println("Game over. Draw");
                    wonRounds[0] += 0.5;
                    wonRounds[1] += 0.5;
                } else {
                    Player winner = this.state.getByID(win);
                    System.out.println("Game over. Winner: " + winner);
                    wonRounds[winner.getId()] += 1;
                }
                MainController.getInstance().repaint();
                this.reInit();
                k = 0;
                totalMoveTime1 += moveTime1;
                totalMoveTime2 += moveTime2;
                moveTime1 = 0;
                moveTime2 = 0;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                round++;
            }
            if (round == maxRounds) {
                try {
                    System.out.println("Final result: " + p1 + " (" + wonRounds[0] + " | " + wonRounds[1] +
                            ") " + p2);
                    System.out.println("Rounds played: " + maxRounds);
//                    System.out.println("Total move time: " + (totalMoveTime1 / 1000d) + "s | " + (
//                            totalMoveTime2 /
//                                    1000d) + "s");
//                    System.out.println("Average move time: " + (totalMoveTime1 / ((double) matchRounds[0] * 1000d) +
//                            "s | " +
//                            (totalMoveTime2 / ((double) matchRounds[1] * 1000d) + "s")));
//                    LogWriter.writeLog(p1, p2, maxRounds, wonRounds, matchRounds, moveTime1, moveTime2, totalMoveTime1,
//                            totalMoveTime2);
                    System.in.read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
            matchRounds[k % 2]++;
            k++;
            startTime = System.currentTimeMillis();
            this.state = GameController.signalMove(this.state);
            if (this.state.getInactivePlayer().getId() == 0) {
                moveTime1 += (System.currentTimeMillis() - startTime);
            } else {
                moveTime2 += (System.currentTimeMillis() - startTime);
            }
            if ((mc = MainController.getInstance()) != null) {
                mc.repaint();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static int getRound() {
        return round;
    }

    public static void main(String[] args) {
        Main.getInstance();
        javafx.application.Application.launch(InitUi.class);
    }
}