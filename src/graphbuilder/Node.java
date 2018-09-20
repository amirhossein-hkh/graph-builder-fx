package graphbuilder;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Node extends StackPane {
    private Circle shape;
    private Label label;
    private double weight;
    private Color color;
    private int index;
    private Tooltip tooltip;

    private static int size;

    public Node(double centerX,double centerY){
        this.index = size++;
        this.shape = new Circle(15,Color.web("#70C397"));
        this.label = new Label(index+"");
        this.weight = 0;
        this.getChildren().add(shape);
        this.getChildren().add(label);
        this.setAlignment(Pos.CENTER);
        this.setLayoutX(centerX-15);
        this.setLayoutY(centerY-15);
        tooltip = new Tooltip("weight : "+weight);
        Tooltip.install(this,tooltip);

    }

    public Label getLabel() {
        return label;
    }

    public Tooltip getTooltip() {
        return tooltip;
    }

    public double getWeight() {
        return weight;
    }

    public int getIndex() {
        return index;
    }


    public Circle getCircle() {
        return shape;
    }

    public void setLabel(String labelText) {
        this.label.setText(labelText);
    }

    public void setColorText(Color color){
        label.setTextFill(color);
    }

    public Color getColorText(){
        return (Color)label.getTextFill();
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setColor(Color color) {
        this.color = color;
        this.shape.setFill(color);
    }

    public void setRadius(int r){
        shape.setRadius(r);
    }
}
