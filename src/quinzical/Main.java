package quinzical;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import quinzical.model.Database;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Database.getInstance(); // initiates the database by allowing the singleton Database class to be instantiated

        Parent root = FXMLLoader.load(getClass().getResource("/quinzical/scenes/StartPage.fxml"));
        Font.loadFont(getClass().getResourceAsStream("../assets/PTSans-Regular"), 14);

        primaryStage.setTitle("Quinzical");
        primaryStage.setMinHeight(300);
        primaryStage.setMinWidth(400);

        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
