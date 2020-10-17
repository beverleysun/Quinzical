package quinzical;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("scenes/StartPage.fxml"));
            Font.loadFont(getClass().getResourceAsStream("../assets/PTSans-Regular"), 14);

            primaryStage.setTitle("Quinzical");
            primaryStage.setMinHeight(300);
            primaryStage.setMinWidth(400);

            primaryStage.setScene(new Scene(root, 800, 500));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
