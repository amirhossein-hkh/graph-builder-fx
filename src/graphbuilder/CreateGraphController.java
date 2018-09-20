package graphbuilder;

import com.jfoenix.controls.*;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CreateGraphController implements Initializable {

    @FXML
    private Label typeLabel;

    @FXML
    private Label sizeLabel;

    @FXML
    private JFXSlider sizeSlideBar;

    @FXML
    private Label colorLabel;

    @FXML
    private JFXColorPicker ColorPicker;

    @FXML
    private Label colorLabelText;

    @FXML
    private JFXColorPicker ColorPickerText;

    @FXML
    private Label weightLabel;

    @FXML
    private JFXToggleButton distance;

    @FXML
    private JFXTextField weightTextField;

    @FXML
    private Label labelLabel;

    @FXML
    private JFXTextField labelTextField;

    @FXML
    private AnchorPane container;

    @FXML
    private JFXButton remove;

    @FXML
    private VBox right;

    @FXML
    private JFXToggleButton directed;

    static boolean addNode = false;

    static boolean addEdge = false;

    static ArrayList<Node> nodes;
    static ArrayList<Edge> edges;

    static ArrayList<Edge> distanceSelected;

    static Circle tempCircle;

    static Line tempLine;

    static Node firstClickedNode;
    static Edge edgeSelected;

    static boolean isFirst;

    @FXML
    void handleAddNode(ActionEvent event){
        addNode = true;
        addEdge = false;
        container.setOnMouseMoved(e ->{
            if (container.getWidth() > e.getX() +15 && e.getX() > 15 && e.getY() > 15 && container.getHeight() > e.getY() + 15) {
                tempCircle.setCenterX(e.getX());
                tempCircle.setCenterY(e.getY());
                tempCircle.setVisible(true);
            }
        });
    }

    @FXML
    void handleAddEdge(ActionEvent event){
        addEdge = true;
        addNode = false;
        container.setOnMouseMoved(e ->{
            if (container.getWidth() > e.getX() +15 && e.getX() > 15 && e.getY() > 15 && container.getHeight() > e.getY() + 15) {
                tempLine.setEndX(e.getX());
                tempLine.setEndY(e.getY());
            }
        });
    }

    @FXML
    void handleRemove(ActionEvent event){
        if(firstClickedNode != null) {
            container.getChildren().remove(firstClickedNode);
            nodes.remove(firstClickedNode);
            for (int i = 0; i < edges.size(); i++) {
                if(edges.get(i).getFrom().equals(firstClickedNode) || edges.get(i).getTo().equals(firstClickedNode)) {
                    container.getChildren().remove(edges.get(i));
                    edges.remove(i);
                    i--;
                }
            }


            changeFistSelected(null);
            changeOptionVisibility(false);

        }else if(edgeSelected != null){
            container.getChildren().remove(edgeSelected);
            edges.remove(edgeSelected);
            changeEdgeSelected(null);
            changeOptionVisibility(false);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        changeOptionVisibility(false);

        sizeSlideBar.setValue(15);
        sizeSlideBar.valueProperty().addListener(((observable, oldValue, newValue) -> {
            if(firstClickedNode != null) {
                firstClickedNode.getCircle().setRadius(15+(double) newValue/5);
            }else if(edgeSelected != null){
                edgeSelected.setStrokeWidth(5+(double) newValue/20);
            }

        }));

        ColorPicker.setValue(Color.rgb(112, 195, 151));
        ColorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(firstClickedNode !=null) {
                firstClickedNode.getCircle().setFill(newValue);
            }else if(edgeSelected != null){
                edgeSelected.setStroke(newValue);
            }
        });

        ColorPickerText.setValue(Color.BLACK);
        ColorPickerText.valueProperty().addListener((observable, oldValue, newValue) -> {
            firstClickedNode.setColorText(newValue);
        });

        weightTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
            if(!newValue){
                try {
                    Double value = Double.valueOf(weightTextField.getText());
                    if(firstClickedNode !=null) {
                        firstClickedNode.getTooltip().setText("weight : "+value);
                        firstClickedNode.setWeight(value);
                    }else if(edgeSelected != null){
                        edgeSelected.getTooltip().setText("weight : "+value);
                        edgeSelected.setWeight(value);
                    }
                }catch (Exception e){
                    if(firstClickedNode !=null) {
                        weightTextField.setText(firstClickedNode.getWeight()+"");
                    }else if(edgeSelected != null){
                        weightTextField.setText(edgeSelected.getWeight()+"");
                    }

                }
            }
        });

        labelTextField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
            if(!newValue && firstClickedNode != null){
                String value = labelTextField.getText();
                if(labelTextField.getText().trim().length() == 0){
                    value = firstClickedNode.getIndex() + "";
                }
                firstClickedNode.setLabel(value);
            }
        });

        directed.selectedProperty().addListener(((observable, oldValue, newValue) -> {
            System.out.println("the graph should be ");
        }));

        distance.selectedProperty().addListener(((observable1, oldValue1, newValue1) -> {
            if(edgeSelected != null) {
                System.out.println(newValue1);
                if (newValue1) {
                    if(!distanceSelected.contains(edgeSelected)) {
                        distanceSelected.add(edgeSelected);
                    }
                    edgeSelected.startXProperty().addListener(((observable, oldValue, newValue) -> {
                        DoubleProperty translateProperty = (DoubleProperty) observable ;
                        Edge edge = (Edge)translateProperty.getBean();
                        if(distanceSelected.contains(edge)) {
                            edge.setWeight(Math.sqrt(Math.pow(edge.getStartX() - edge.getEndX(), 2) + Math.pow(edge.getStartY() - edge.getEndY(), 2)));
                        }
                    }));
                    edgeSelected.startYProperty().addListener(((observable, oldValue, newValue) -> {
                        DoubleProperty translateProperty = (DoubleProperty) observable ;
                        Edge edge = (Edge)translateProperty.getBean();
                        if(distanceSelected.contains(edge)) {
                            edge.setWeight(Math.sqrt(Math.pow(edge.getStartX() - edge.getEndX(), 2) + Math.pow(edge.getStartY() - edge.getEndY(), 2)));
                        }
                    }));
                    edgeSelected.endXProperty().addListener(((observable, oldValue, newValue) -> {
                        DoubleProperty translateProperty = (DoubleProperty) observable ;
                        Edge edge = (Edge)translateProperty.getBean();
                        if(distanceSelected.contains(edge)) {
                            edge.setWeight(Math.sqrt(Math.pow(edge.getStartX() - edge.getEndX(), 2) + Math.pow(edge.getStartY() - edge.getEndY(), 2)));
                        }
                    }));
                    edgeSelected.endYProperty().addListener(((observable, oldValue, newValue) -> {
                        DoubleProperty translateProperty = (DoubleProperty) observable ;
                        Edge edge = (Edge)translateProperty.getBean();
                        if(distanceSelected.contains(edge)) {
                            edge.setWeight(Math.sqrt(Math.pow(edge.getStartX() - edge.getEndX(), 2) + Math.pow(edge.getStartY() - edge.getEndY(), 2)));
                        }
                    }));
                    edgeSelected.setWeight(Math.sqrt(Math.pow(edgeSelected.getStartX() - edgeSelected.getEndX(), 2) + Math.pow(edgeSelected.getStartY() - edgeSelected.getEndY(), 2)));
                    weightTextField.setDisable(true);
                    weightTextField.setEditable(false);
                    weightTextField.setText(String.format("%.2f",edgeSelected.getWeight()));
                } else {
                    distanceSelected.remove(edgeSelected);
                    weightTextField.setDisable(false);
                    weightTextField.setEditable(true);
                    weightTextField.setText(String.format("%.2f",edgeSelected.getWeight()));
                }
            }
        }));

        tempCircle = new Circle(15,Color.rgb(112, 195, 151));
        tempCircle.setVisible(false);
        container.getChildren().add(tempCircle);

        tempLine = new Line();
        tempLine.setStroke(Color.WHITE);
        tempLine.setStrokeWidth(5);
        tempLine.setVisible(false);
        container.getChildren().add(tempLine);

        isFirst = true;

        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        distanceSelected = new ArrayList<>();

        container.setOnMouseClicked((event -> {

            if(event.getButton() == MouseButton.PRIMARY) {
                System.out.println("left click");
                if (addNode) {

                    nodes.add(new Node(event.getX(), event.getY()));

//                    changeFistSelected(nodes.get(nodes.size() - 1));
//                    changeOptionVisibility(true);

                    nodes.get(nodes.size() - 1).setOnMouseClicked(e -> {
                        Node node = (Node) e.getSource();
                        if(addEdge){
                            if(isFirst) {
                                changeEdgeSelected(null);
                                changeFistSelected(node);

                                container.getChildren().remove(node);

                                tempLine.setStartX(node.getLayoutX() + node.getCircle().getRadius() + node.getTranslateX());
                                tempLine.setStartY(node.getLayoutY() + node.getCircle().getRadius() + node.getTranslateY());
                                tempLine.setVisible(true);
                                container.getChildren().add(node);


                            }else {
                                System.out.println(node);
                                edges.add(new Edge(firstClickedNode,node));
                                edges.get(edges.size()-1).setOnMouseClicked(event1 -> {
                                    System.out.println("fdgfdg");
                                    Edge edge = (Edge)event1.getSource();
                                    changeFistSelected(null);
                                    changeEdgeSelected(edge);
                                    changeOptionVisibility(true);

                                    event1.consume();
                                });
                                container.getChildren().removeAll(firstClickedNode,node);
                                container.getChildren().add(edges.get(edges.size()-1));
                                container.getChildren().addAll(firstClickedNode,node);
                                tempLine.setVisible(false);
                                addEdge = false;
                            }
                            isFirst = !isFirst;
                        }else {
                            changeEdgeSelected(null);
                            changeOptionVisibility(true);

                            changeFistSelected(node);

                            typeLabel.setText("Node");
                        }

                        e.consume();
                    });
                    nodes.get(nodes.size() - 1).setOnMouseDragged(e ->{
                        changeEdgeSelected(null);
                        changeFistSelected((Node)e.getSource());

                        firstClickedNode.setTranslateX(e.getX() + firstClickedNode.getTranslateX() - firstClickedNode.getCircle().getRadius());
                        firstClickedNode.setTranslateY(e.getY() + firstClickedNode.getTranslateY() - firstClickedNode.getCircle().getRadius());

                        e.consume();
                    });


                    container.getChildren().add(nodes.get(nodes.size() - 1));
                }if(addEdge){
                    System.out.println("no edge added");
                } else{
                    changeOptionVisibility(false);

                    changeFistSelected(null);
                    changeEdgeSelected(null);
                }
                isFirst = true;
            }if(event.getButton() == MouseButton.SECONDARY){
                System.out.println("right click");
                changeOptionVisibility(false);

                changeFistSelected(null);
                changeEdgeSelected(null);
            }
            container.setOnMouseMoved(null);
            tempCircle.setVisible(false);
            tempLine.setVisible(false);
            addNode = false;
            addEdge = false;

        }));
    }

    int indexOfEdge(Edge edge){
        for (int i = 0; i < edges.size() ; i++) {
            if(edges.get(i).equals(edge)){
                return i;
            }
        }
        return -1;
    }

    void changeFistSelected(Node n){

        if(firstClickedNode != null){
            firstClickedNode.setEffect(null);
        }
        firstClickedNode = n;
        if(n != null) {
            typeLabel.setText("Node");
            right.getChildren().clear();
            right.getChildren().addAll(typeLabel,sizeLabel,sizeSlideBar,colorLabel,ColorPicker,colorLabelText,ColorPickerText,weightLabel,weightTextField,labelLabel,labelTextField,remove);
            sizeSlideBar.setValue((n.getCircle().getRadius()-15)*5);
            ColorPicker.setValue((Color) firstClickedNode.getCircle().getFill());
            ColorPickerText.setValue((Color)firstClickedNode.getColorText());
            weightTextField.setText(String.format("%.2f",firstClickedNode.getWeight()));
            weightTextField.setDisable(false);
            weightTextField.setEditable(true);
            labelTextField.setText(firstClickedNode.getLabel().getText());
            firstClickedNode.setEffect(new Glow(0.3));
        }
    }

    void changeEdgeSelected(Edge e){
        if(edgeSelected != null){
            edgeSelected.setEffect(null);
        }
        edgeSelected = e;
        if(e != null) {
            typeLabel.setText("Edge");
            right.getChildren().clear();
            right.getChildren().addAll(typeLabel,sizeLabel,sizeSlideBar,colorLabel,ColorPicker,weightLabel,distance,weightTextField,remove);

            sizeSlideBar.setValue((e.getStrokeWidth()-5)*20);
            ColorPicker.setValue((Color) edgeSelected.getStroke());
            weightTextField.setText(String.format("%.2f",edgeSelected.getWeight()));
            if(distanceSelected.contains(edgeSelected)){
                distance.setSelected(true);
                weightTextField.setEditable(false);
                weightTextField.setDisable(true);
            } else {
                distance.setSelected(false);
                weightTextField.setEditable(true);
                weightTextField.setDisable(false);

            }
            edgeSelected.setEffect(new Glow(0.5));
        }
    }

    void changeOptionVisibility(boolean change){
        typeLabel.setVisible(change);
        sizeLabel.setVisible(change);
        sizeSlideBar.setVisible(change);
        colorLabel.setVisible(change);
        ColorPicker.setVisible(change);
        weightLabel.setVisible(change);
        weightTextField.setVisible(change);
        labelLabel.setVisible(change);
        labelTextField.setVisible(change);
        colorLabelText.setVisible(change);
        ColorPickerText.setVisible(change);
        distance.setVisible(change);
        remove.setVisible(change);
    }
}
