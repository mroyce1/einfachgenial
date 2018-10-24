package ui;

import agent.PlayerType;
import application.Constants;
import application.Main;
import domain.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;


public class MainController implements Initializable {
    private static MainController instance;
    @FXML
    private Pane pane;
    private HashMap<Axis, GraphicalHexagon> grid;
    private List<Duplet> player1Duplets;
    private List<Duplet> player2Duplets;
    private List<Text> player1Scores;
    private List<Text> player2Scores;
    private List<Text> human1Text;
    private List<Text> human2Text;
    private Text activePlayer;
    private Text scores;

    public static MainController getInstance() {
        return instance;
    }

    public void repaint() {
        GameState state;
        if ((state = Main.getInstance().getGameState()) == null) {
            return;
        }
        for (Map.Entry<Axis, GraphicalHexagon> entry : this.grid.entrySet()) {
            GraphicalHexagon tmp = entry.getValue();
            tmp.setCol(state.getHexagons()[entry.getValue().getNum()].getCol());
            this.grid.put(entry.getKey(), tmp);
        }
        for (int i = 0; i < state.getPlayer1().getDuplets().length; i++) {
            this.player1Duplets.get(i).updateCol(state.getPlayer1().getDuplets()[i]);
            this.player2Duplets.get(i).updateCol(state.getPlayer2().getDuplets()[i]);
        }
        this.player1Scores.clear();
        for (int i = 0; i < state.getPlayer1().getScores().length; i++) {
            int k = state.getPlayer1().getScores()[i];
            Text tmp = new Text(Constants.SCORE_P1_X, Constants.SCORE_P1_Y + Constants.SCORE_VERTICAL_OFFSET * i,
                    ColorType.instantiate(i) + ": " + k);
            tmp.setFill(ColorType.instantiate(i).getJFXColor());
            tmp.setStyle("-fx-font: 20 arial;");
            tmp.setStroke(Color.BLACK);
            tmp.setStrokeWidth(0.25);
            this.player1Scores.add(tmp);
        }
        this.player2Scores.clear();
        for (int i = 0; i < state.getPlayer2().getScores().length; i++) {
            int k = state.getPlayer2().getScores()[i];
            Text tmp = new Text(Constants.SCORE_P2_X, Constants.SCORE_P2_Y + Constants.SCORE_VERTICAL_OFFSET * i,
                    ColorType.instantiate(i) + ": " + k);
            tmp.setFill(ColorType.instantiate(i).getJFXColor());
            tmp.setStyle("-fx-font: 20 arial;");
            tmp.setStroke(Color.BLACK);
            tmp.setStrokeWidth(0.25);
            this.player2Scores.add(tmp);
        }

        if (this.human1Text != null) {
            this.human1Text.clear();
            int k = 0;
            for (int i = 0; i < 6; i++) {
                Text tmp1 = new Text(Constants.PIECE_P1_X - 5, Constants.PIECE_P1_Y + 5 + Constants
                        .PIECE_VERTICAL_OFFSET
                        * i,
                        String.valueOf((char) (i + 65 + k)));
                Text tmp2 = new Text(Constants.PIECE_P1_X + 38, Constants.PIECE_P1_Y + 5 + Constants
                        .PIECE_VERTICAL_OFFSET
                        * i,
                        String.valueOf((char) (i + 66 + k)));
                tmp1.setStyle("-fx-font: 20 arial;");
                tmp1.setStroke(Color.BLACK);
                tmp1.setStrokeWidth(0.25);
                tmp2.setStyle("-fx-font: 20 arial;");
                tmp2.setStroke(Color.BLACK);
                tmp2.setStrokeWidth(0.25);
                this.human1Text.add(tmp1);
                this.human1Text.add(tmp2);
                k++;
            }
        }

        if (this.human2Text != null) {
            this.human2Text.clear();
            int k = 0;
            for (int i = 0; i < 6; i++) {
                Text tmp1 = new Text(Constants.PIECE_P2_X - 7, Constants.PIECE_P2_Y + 5 + Constants
                        .PIECE_VERTICAL_OFFSET
                        * i,
                        String.valueOf((char) (i + 65 + k)));
                Text tmp2 = new Text(Constants.PIECE_P2_X + 38, Constants.PIECE_P2_Y + 5 + Constants
                        .PIECE_VERTICAL_OFFSET
                        * i,
                        String.valueOf((char) (i + 66 + k)));
                tmp1.setStyle("-fx-font: 20 arial;");
                tmp1.setStroke(Color.BLACK);
                tmp1.setStrokeWidth(0.25);
                tmp2.setStyle("-fx-font: 20 arial;");
                tmp2.setStroke(Color.BLACK);
                tmp2.setStrokeWidth(0.25);
                this.human2Text.add(tmp1);
                this.human2Text.add(tmp2);
                k++;
            }
        }

        String side = state.getActivePlayer().getId() == 0 ? " (left)" : " (right)";
        this.activePlayer = new Text(Constants.SCREEN_WIDTH / 2 - Constants.SCREEN_WIDTH / 8, 100, state
                .getActivePlayer().getName() + "'s turn" + side);
        this.activePlayer.setFill(Color.BLACK);
        this.activePlayer.setStyle("-fx-font: 30 arial;");
        this.activePlayer.setStroke(Color.BLACK);
        this.activePlayer.setStrokeWidth(0.25);

        this.scores = new Text(Constants.SCREEN_WIDTH / 2 - 45, Constants.SCREEN_HEIGHT - 100, Main.getWonRounds()[0]
                + " " +
                ": " + Main.getWonRounds()[1]);
        this.scores.setFill(Color.BLACK);
        this.scores.setStyle("-fx-font: 30 arial;");
        this.scores.setStroke(Color.BLACK);
        this.scores.setStrokeWidth(0.25);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                List<Node> tmp = new ArrayList<Node>(pane.getChildren());
                tmp.clear();
                tmp.add(activePlayer);
                tmp.addAll(player1Scores);
                tmp.addAll(player2Scores);
                tmp.add(scores);
                Text tmpText;
                for (GraphicalHexagon h : grid.values()) {
                    tmp.add(h.getPolygon());
                    if ((tmpText = h.getCoordText()) != null && h.getCol() == ColorType.WHITE) {
                        tmp.add(tmpText);
                    }
                }
                for (Duplet d : player1Duplets) {
                    if (!d.isVisible()) {
                        continue;
                    }
                    tmp.add(d.getHex1().getPolygon());
                    tmp.add(d.getHex2().getPolygon());
                }
                for (Duplet d : player2Duplets) {
                    if (!d.isVisible()) {
                        continue;
                    }
                    tmp.add(d.getHex1().getPolygon());
                    tmp.add(d.getHex2().getPolygon());
                }
                if (human1Text != null) {
                    tmp.addAll(human1Text);
                }
                if (human2Text != null) {
                    tmp.addAll(human2Text);
                }
                pane.getChildren().clear();
                pane.getChildren().addAll(tmp);
            }
        });
    }

    public void handleScrollEvent(ScrollEvent e) {
        System.out.println("scrolled");
        Duplet current = null;
        for (Duplet d : this.player1Duplets) {
            if (d.isClickInside(e.getX(), e.getY())) {
                d.rotate(true);
                current = d;
                break;
            }
        }
        for (Duplet d : this.player2Duplets) {
            if (d.isClickInside(e.getX(), e.getY())) {
                d.rotate(true);
                current = d;
                break;
            }
        }
        return;
    }

    public void handleEvent(MouseEvent e) {
        if (e.getEventType() == MouseEvent.MOUSE_CLICKED) {
            for (Map.Entry<Axis, GraphicalHexagon> entry : this.grid.entrySet()) {
                if (entry.getValue().isClickInside(e.getX(), e.getY())) {
                    GameHexagon gm = Main.getInstance().getGameState().getHexagons()[entry.getValue().getNum()];
                    System.out.println("clicked  " + entry.getKey() + "   " + entry.getValue().getCol());
                    System.out.println("Scoring opportunities:");
                    for (int i = 0; i < 6; i++) {
                        System.out.println(ColorType.instantiate(i) + ":  " + gm.getScores()[i]);
                    }
                    return;
                }
            }

        }
    }

    public void initialize(URL location, ResourceBundle resources) {
        this.grid = Hexagon.setupGraphicalGrid(Main.getInstance().getGameState().getPlayer1().getType() ==
                PlayerType
                        .HUMAN || Main.getInstance().getGameState().getPlayer2().getType() == PlayerType.HUMAN);
        this.player1Duplets = Duplet.setupDuplets(true);
        this.player2Duplets = Duplet.setupDuplets(false);
        this.player1Scores = new ArrayList<Text>();
        this.player2Scores = new ArrayList<Text>();
        this.activePlayer = new Text();
        this.scores = new Text();
        if (Main.getInstance().getGameState().getPlayer1().getType() == PlayerType.HUMAN) {
            this.human1Text = new ArrayList<Text>();
        }
        if (Main.getInstance().getGameState().getPlayer2().getType() == PlayerType.HUMAN) {
            this.human2Text = new ArrayList<Text>();
        }
        instance = this;
        InitUi.injectMaincontroller(this);
        this.repaint();
    }
}