package graphbuilder;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Edge extends Line {
    private Node from;
    private Node to;
    private Label label;
    private double weight;
    private Tooltip tooltip;

    public Edge(Node from,Node to){
        this.from = from;
        this.to = to;
        from.translateXProperty().addListener(((observable, oldValue, newValue) -> {
            this.setStartX(from.getLayoutX()+from.getCircle().getRadius() + from.getTranslateX());
        }));
        from.translateYProperty().addListener(((observable, oldValue, newValue) -> {
            this.setStartY(from.getLayoutY()+from.getCircle().getRadius() + from.getTranslateY());
        }));
        to.translateXProperty().addListener(((observable, oldValue, newValue) -> {
            this.setEndX(to.getLayoutX()+to.getCircle().getRadius() + to.getTranslateX());
        }));
        to.translateYProperty().addListener(((observable, oldValue, newValue) -> {
            this.setEndY(to.getLayoutY()+to.getCircle().getRadius() + to.getTranslateY());
        }));

        from.getCircle().radiusProperty().addListener(((observable, oldValue, newValue) -> {
            this.setStartX(from.getLayoutX()+from.getCircle().getRadius() + from.getTranslateX());
            this.setStartY(from.getLayoutY()+from.getCircle().getRadius() + from.getTranslateY());
        }));
        to.getCircle().radiusProperty().addListener(((observable, oldValue, newValue) -> {
            this.setEndX(to.getLayoutX()+to.getCircle().getRadius() + to.getTranslateX());
            this.setEndY(to.getLayoutY()+to.getCircle().getRadius() + to.getTranslateY());
        }));
        this.setStartX(from.getLayoutX()+from.getCircle().getRadius() + from.getTranslateX());
        this.setStartY(from.getLayoutY()+from.getCircle().getRadius() + from.getTranslateY());
        this.setEndX(to.getLayoutX()+to.getCircle().getRadius() + to.getTranslateX());
        this.setEndY(to.getLayoutY()+to.getCircle().getRadius() + to.getTranslateY());
        this.setStroke(Color.WHITE);
        this.setStrokeWidth(5);

        tooltip = new Tooltip("weight : "+weight);

        Tooltip.install(this,tooltip);

        this.label = new Label("");
        this.label.setTextFill(Color.WHITE);

    }

    public void setTo(Node to) {
        this.to = to;
    }

    public Tooltip getTooltip() {
        return tooltip;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Node getTo() {
        return to;
    }

    public void setFrom(Node from) {
        this.from = from;
    }

    public Node getFrom() {
        return from;
    }

    public void setLineColor(Color color){
        this.setStroke(color);
    }

    public Color getLineColor(Color color){
        return (Color)this.getStroke();
    }

    public void setTextColor(Color color){
        this.label.setTextFill(color);
    }

    public Color getTextColor(Color color){
        return (Color)this.label.getTextFill();
    }

    public void setText(String text){
        this.label.setText(text);
    }

}
