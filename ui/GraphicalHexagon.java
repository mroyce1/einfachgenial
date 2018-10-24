package ui;

import application.Constants;
import domain.Axis;
import domain.ColorType;
import domain.GameHexagon;
import domain.Hexagon;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GraphicalHexagon extends Hexagon {
    private static int numCounter = 0;
    private Polygon polygon;
    private List<double[]> coords;
    private int radius;
    private double horizontalOffset;
    private double[] center;
    private double[] bottom;
    private double[] top;
    private double[] topLeft;
    private double[] topRight;
    private double[] bottomLeft;
    private double[] bottomRight;
    private boolean visible;
    private Text coordText;
    private int num;

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public double getHorizontalOffset() {
        return horizontalOffset;
    }

    public void setHorizontalOffset(double horizontalOffset) {
        this.horizontalOffset = horizontalOffset;
    }

    public double[] getCenter() {
        return center;
    }

    public void setCenter(double[] center) {
        this.center = center;
    }

    public double[] getBottom() {
        return bottom;
    }

    public void setBottom(double[] bottom) {
        this.bottom = bottom;
    }

    public double[] getTop() {
        return top;
    }

    public void setTop(double[] top) {
        this.top = top;
    }

    public double[] getTopLeft() {
        return topLeft;
    }

    public void setTopLeft(double[] topLeft) {
        this.topLeft = topLeft;
    }

    public double[] getTopRight() {
        return topRight;
    }

    public void setTopRight(double[] topRight) {
        this.topRight = topRight;
    }

    public double[] getBottomLeft() {
        return bottomLeft;
    }

    public void setBottomLeft(double[] bottomLeft) {
        this.bottomLeft = bottomLeft;
    }

    public double[] getBottomRight() {
        return bottomRight;
    }

    public void setBottomRight(double[] bottomRight) {
        this.bottomRight = bottomRight;
    }

    public List<double[]> getCoords() {
        return coords;
    }

    public void setCoords(List<double[]> coords) {
        this.coords = coords;
    }


    private void generateCoords() {
        this.bottom = new double[]{this.center[0], this.center[1] - this.radius, 0};
        this.top = new double[]{this.center[0], this.center[1] + this.radius, 0};
        this.topLeft = new double[]{this.center[0] - this.horizontalOffset, this.center[1] + (this.radius / 2.0), 0};
        this.bottomLeft = new double[]{this.center[0] - this.horizontalOffset, this.center[1] - (this.radius / 2.0), 0};
        this.bottomRight = new double[]{this.center[0] + this.horizontalOffset, this.center[1] - (this.radius / 2.0),
                0};
        this.topRight = new double[]{this.center[0] + this.horizontalOffset, this.center[1] + (this.radius / 2.0), 0};
        this.coords = new ArrayList<double[]>();
        this.coords.addAll(Arrays.asList(this.bottom, this.bottomLeft, this.topLeft, this.top, this.topRight, this
                .bottomRight));
    }

    public Polygon getPolygon() {
        return polygon;
    }

    private void generatePolygon() {
        this.polygon = new Polygon();
        if (this.visible) {
            this.polygon.setStroke(Color.BLACK);
            this.polygon.setFill(this.getCol().getJFXColor());
        } else {
            this.polygon.setStroke(Color.WHITE);
            this.polygon.setFill(Color.WHITE);
        }
        for (double[] q : this.coords) {
            this.polygon.getPoints().add(q[0]);
            this.polygon.getPoints().add(q[1]);
        }
    }

    private void calculateHorizontalOffset() {
        this.horizontalOffset = Math.sqrt(Math.pow(this.radius, 2) - Math.pow((this.radius / 2), 2));
    }


    private void calculateCenter(double[] origin) {
        this.center = new double[]{(origin[0] + this.getAxis().x() * this.horizontalOffset + this.horizontalOffset *
                this.getAxis().z() * 2.0), (origin[1] + this.radius * this.getAxis().x() * 3 / 2), 0.0};
    }

    public boolean isClickInside(double x, double y) {
        if (x < this.topLeft[0] || x > this.topRight[0]) {
            return false;
        }
        if (y > this.topLeft[1] || y < this.bottomRight[1]) {
            return false;
        }
        return true;
    }

    public void updatePosition(double[] origin) {
        this.calculateCenter(origin);
        this.generateCoords();
        this.generatePolygon();
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        this.generatePolygon();
    }

    public void setCol(ColorType c) {
        this.col = c;
        this.generatePolygon();
    }

    public Text getCoordText() {
        return coordText;
    }

    public void setCoordText(Text coordText) {
        this.coordText = coordText;
    }

    public int getNum() {
        return num;
    }

    public GraphicalHexagon(Axis axis, ColorType c, boolean humanPlayer) {
        super(axis, c);
        this.radius = Constants.RADIUS;
        this.calculateHorizontalOffset();
        this.updatePosition(Constants.ORIGIN);
        this.visible = true;
        this.num = GraphicalHexagon.numCounter++;
        if (humanPlayer) {
            this.coordText = new Text(this.center[0] - 5, this.center[1] + 5,
                    String.valueOf(this.num));
            this.coordText.setStyle("-fx-font: 15 arial;");
            this.coordText.setStroke(Color.BLACK);
            this.coordText.setStrokeWidth(0.1);

        }
    }


    public static void resetNumCounter() {
        GraphicalHexagon.numCounter = 0;
    }

}
