package quinzical.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import quinzical.model.Database;
import quinzical.model.SceneChanger;
import quinzical.model.User;

import java.io.IOException;
import java.util.List;

public class ScoresController {

    @FXML VBox scoresVBox;

    /**
     * Adds the top 10 scores into the list
     */
    public void initialize(){
        List<User> sortedScores = Database.getInstance().getSortedScores();

        // Add top 10 scores
        int count = 1;
        for (User user : sortedScores) {
            GridPane entry = new GridPane();

            // Set grid column cell sizes
            ColumnConstraints c1 = new ColumnConstraints();
            c1.setPercentWidth(50);
            ColumnConstraints c2 = new ColumnConstraints();
            c2.setPercentWidth(50);
            entry.getColumnConstraints().addAll(c1, c2);

            // Set grid row cell sizes
            RowConstraints r1 = new RowConstraints();
            r1.setPrefHeight(27);
            r1.setMinHeight(27);
            entry.getRowConstraints().add(r1);

            // Labels for name and score
            Label score = new Label("$" + user.getScore());
            Label name = new Label(user.getName());

            // Align the name label to the right
            GridPane.setHalignment(name, HPos.RIGHT);

            // Styling of labels
            score.getStylesheets().add("/assets/style.css");
            score.getStyleClass().addAll("purple-text-fill", "font-16");
            name.getStylesheets().add("/assets/style.css");
            name.getStyleClass().addAll("purple-text-fill", "font-16");

            // Add to the list
            entry.add(score, 0, 0);
            entry.add(name, 1, 0);
            scoresVBox.getChildren().add(entry);

            count++;
            if (count > 10) {
                break;
            }
        }
    }

    /** This method is invoked when the user click the back button
     * It will switch to the StartPage interface.
     *  @param e the source of the click
     */
    public void back(MouseEvent e) {
        try {
            // Load start page scene
            Parent startPage = FXMLLoader.load(getClass().getResource("/quinzical/scenes/StartPage.fxml"));
            SceneChanger.changeScene(e, startPage);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
