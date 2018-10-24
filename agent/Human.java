package agent;

import domain.*;

import java.util.Scanner;
import java.util.regex.Pattern;

public class Human extends Player {
    public Human(String name, boolean first) {
        super(name, PlayerType.HUMAN, first);
    }

    @Override
    public HexMove notifyMove(GameState state, GameState previousState, HexMove previousMove) {
        Scanner scanner = new Scanner(System.in);
        String move = "";
        boolean valid = false;
        char a = '_';
        char b = '_';
        int h1 = 0;
        int h2 = 0;
        GameHexagon g1 = null;
        GameHexagon g2 = null;
        ColorDuplet d = null;
        while (!valid) {
            System.out.println("Please enter your move: ");
            String regex = "^[a-lA-L]\\d{1,2}[a-lA-L]\\d{1,2}";
            while (!Pattern.matches(regex, move = scanner.nextLine())) {
                System.out.println("Enter your move like this: c15d8");
            }
            a = move.charAt(0);
            b = '_';
            h1 = 0;
            h2 = 0;
            g1 = null;
            g2 = null;
            d = null;
            if (Pattern.matches("[a-lA-L]{1}\\d[a-lA-L]{1}\\d{1,2}", move)) {
                h1 = Integer.parseInt(move.substring(1, 2));
                h2 = Integer.parseInt(move.substring(3, move.length()));
            } else if (Pattern.matches("[a-lA-L]{1}\\d{2}[a-lA-L]{1}\\d{1,2}", move)) {
                h1 = Integer.parseInt(move.substring(1, 3));
                h2 = Integer.parseInt(move.substring(4, move.length()));
            }
            if (a >= 97) {
                a -= 32;
            }
            switch (a) {
                case 65:
                    d = this.getDuplets()[0];
                    break;
                case 66:
                    d = this.getDuplets()[0];
                    break;
                case 67:
                    d = this.getDuplets()[1];
                    break;
                case 68:
                    d = this.getDuplets()[1];
                    break;
                case 69:
                    d = this.getDuplets()[2];
                    break;
                case 70:
                    d = this.getDuplets()[2];
                    break;
                case 71:
                    d = this.getDuplets()[3];
                    break;
                case 72:
                    d = this.getDuplets()[3];
                    break;
                case 73:
                    d = this.getDuplets()[4];
                    break;
                case 74:
                    d = this.getDuplets()[4];
                    break;
                case 75:
                    d = this.getDuplets()[5];
                    break;
                case 76:
                    d = this.getDuplets()[5];
            }
            for (GameHexagon g : state.getHexagons()) {
                if (g.getNum() == h1) {
                    g1 = g;
                }
                if (g.getNum() == h2) {
                    g2 = g;
                }
            }
            if (g1 == null || g2 == null) {
                continue;
            }
            valid = g2.getNeighbours().contains(g1.getNum()) && g1.getCol() == ColorType.WHITE && g2.getCol() ==
                    ColorType.WHITE;
        }
        HexMove m;
        if (a % 2 == 1) {
            m = new HexMove(this, d, g1.getAxis(), g2.getAxis(), g1, g2);
        } else {
            m = new HexMove(this, d, g2.getAxis(), g1.getAxis(), g2, g1);
        }
        if (this.swapPossible(state, m)) {
            System.out.println("Swap? y/n");
            while (!Pattern.matches("^[yYnN]", move = scanner.nextLine())) {
                System.out.println("Swap? y/n");
            }
        }
        if (move.equals("y") || move.equals("Y")) {
            m.setSwap(true);
        }
        return m;
    }
}
