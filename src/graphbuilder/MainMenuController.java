package graphbuilder;

import javafx.animation.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private AnchorPane container;

    @FXML
    void handleStart(ActionEvent event){
        Stage stage = (Stage) container.getScene().getWindow();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("fxmls/CreateGraph.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(MainMenuController.class.getResource("css/component.css").toExternalForm());
            stage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        final double length = 80;

        //initializing
        Line[] centerLines = new Line[5];
        for (int i = 0; i < centerLines.length; i++) {
            centerLines[i] = new Line();
            centerLines[i].setStroke(Color.WHITE);
            centerLines[i].setStrokeWidth(3);
            centerLines[i].setSmooth(true);
            container.getChildren().add(centerLines[i]);
        }


        Line[][] surroundingLines = new Line[5][5];
        for (int i = 0; i < surroundingLines.length; i++) {
            for (int j = 0; j < surroundingLines[0].length; j++) {
                surroundingLines[i][j] = new Line();
                surroundingLines[i][j].setStroke(Color.WHITE);
                surroundingLines[i][j].setStrokeWidth(3);
                surroundingLines[i][j].setSmooth(true);
                container.getChildren().add(surroundingLines[i][j]);
            }
        }

        Circle center = new Circle(8,Color.rgb(112, 195, 151));
        container.getChildren().add(center);

        Circle[] centerCircles = new Circle[5];
        for (int i = 0; i < centerCircles.length; i++) {
            centerCircles[i] = new Circle(8,Color.rgb(112, 195, 151));
            container.getChildren().add(centerCircles[i]);
        }

        Circle[][] surroundingCircles = new Circle[5][5];
        for (int i = 0; i < surroundingCircles.length; i++) {
            for (int j = 0; j < surroundingCircles[i].length; j++) {
                surroundingCircles[i][j] = new Circle(8,Color.rgb(112, 195, 151));
                container.getChildren().add(surroundingCircles[i][j]);
            }
        }

        //changing the position of elements with regard to the width and height of page
        container.widthProperty().addListener((observable, oldValue, newValue) ->{
            center.setCenterX((double)newValue/2.0);
            double startX = (double)newValue/2.0;
            for (int i = 0; i < centerLines.length; i++) {
                double endX = length * Math.cos((72 * i - 18)* Math.PI /180) + startX;
                centerLines[i].setStartX(startX);
                centerLines[i].setEndX(endX);
                centerCircles[i].setCenterX(endX);
            }

            for (int i = 0; i < surroundingLines.length; i++) {
                for (int j = 0; j < surroundingLines[i].length; j++) {
                    double startXSurrounding = length * Math.cos((72 * i - 18) * Math.PI /180) + startX;
                    double endX = length * Math.cos((72 * j -18)* Math.PI /180)+ startXSurrounding;
                    surroundingLines[i][j].setStartX(startXSurrounding);
                    surroundingLines[i][j].setEndX(endX);
                    surroundingCircles[i][j].setCenterX(endX);
                }

            }

        });

        container.heightProperty().addListener((observable, oldValue, newValue) -> {
            center.setCenterY((double) newValue / 2.0);
            double startY = (double)newValue/2.0;
            for (int i = 0; i < centerLines.length; i++) {
                double endY = length * Math.sin((72 * i - 18) * Math.PI /180) + startY;
                centerLines[i].setStartY(startY);
                centerLines[i].setEndY(endY);
                centerCircles[i].setCenterY(endY);
            }

            for (int i = 0; i < surroundingLines.length; i++) {
                for (int j = 0; j < surroundingLines[i].length; j++) {
                    double startYSurrounding = length * Math.sin((72 * i - 18) * Math.PI /180) + startY;
                    double endY = length * Math.sin((72 * j -18 )* Math.PI /180) + startYSurrounding;
                    surroundingLines[i][j].setStartY(startYSurrounding);
                    surroundingLines[i][j].setEndY(endY);
                    surroundingCircles[i][j].setCenterY(endY);
                }

            }

            //animations

            SequentialTransition level1 = new SequentialTransition(makeChangeColorAnimation(center,Color.rgb(38, 90, 62)));

            ParallelTransition level2 = new ParallelTransition();
            for (int i = 0; i < surroundingCircles.length; i++) {
                level2.getChildren().add(makeChangeColorAnimation(surroundingCircles[i][(i+3)%5],Color.rgb(38, 90, 62)));
                level2.getChildren().add(makeChangeColorAnimation(surroundingCircles[i][(i+2)%5],Color.rgb(38, 90, 62)));
            }

            ParallelTransition level3 = new ParallelTransition();
            for (int i = 0; i < centerCircles.length; i++) {
                level3.getChildren().add(makeChangeColorAnimation(centerCircles[i],Color.rgb(38, 90, 62)));
            }

            ParallelTransition level4 = new ParallelTransition();
            for (int i = 0; i < surroundingCircles.length; i++) {
                level4.getChildren().add(makeChangeColorAnimation(surroundingCircles[i][(i+1)%5],Color.rgb(38, 90, 62)));
                level4.getChildren().add(makeChangeColorAnimation(surroundingCircles[i][(i+4)%5],Color.rgb(38, 90, 62)));
            }

            ParallelTransition level5 = new ParallelTransition();
            for (int i = 0; i < surroundingCircles.length; i++) {
                level5.getChildren().add(makeChangeColorAnimation(surroundingCircles[i][(i)%5],Color.rgb(38, 90, 62)));
            }

            SequentialTransition forward = new SequentialTransition(level1,level2,level3,level4,level5);

            Timeline level1_back = makeChangeColorAnimation(center,Color.rgb(112, 195, 151));

            ParallelTransition level2_back = new ParallelTransition();
            for (int i = 0; i < surroundingCircles.length; i++) {
                level2_back.getChildren().add(makeChangeColorAnimation(surroundingCircles[i][(i+3)%5],Color.rgb(112, 195, 151)));
                level2_back.getChildren().add(makeChangeColorAnimation(surroundingCircles[i][(i+2)%5],Color.rgb(112, 195, 151)));
            }

            ParallelTransition level3_back = new ParallelTransition();
            for (int i = 0; i < centerCircles.length; i++) {
                level3_back.getChildren().add(makeChangeColorAnimation(centerCircles[i],Color.rgb(112, 195, 151)));
            }

            ParallelTransition level4_back = new ParallelTransition();
            for (int i = 0; i < surroundingCircles.length; i++) {
                level4_back.getChildren().add(makeChangeColorAnimation(surroundingCircles[i][(i+1)%5],Color.rgb(112, 195, 151)));
                level4_back.getChildren().add(makeChangeColorAnimation(surroundingCircles[i][(i+4)%5],Color.rgb(112, 195, 151)));
            }

            ParallelTransition level5_back = new ParallelTransition();
            for (int i = 0; i < surroundingCircles.length; i++) {
                level5_back.getChildren().add(makeChangeColorAnimation(surroundingCircles[i][(i)%5],Color.rgb(112, 195, 151)));
            }

            SequentialTransition back = new SequentialTransition(level5_back,level4_back,level3_back,level2_back,level1_back);

            SequentialTransition finalAnimation = new SequentialTransition(forward, back);
            finalAnimation.setCycleCount(SequentialTransition.INDEFINITE);
            finalAnimation.play();
        });

    }

    Timeline makeChangeColorAnimation(Circle c,Color color){
        KeyValue colorChange = new KeyValue(c.fillProperty(),color);
        KeyFrame animationColor = new KeyFrame(Duration.seconds(0.5),colorChange);
        Timeline tm1 = new Timeline(animationColor);

        return tm1;
    }

}
