package quinzical;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PracticeAnsweringController extends PlayController  {


    public PracticeAnsweringController(){

    }

    @FXML
    private Label questionClue;
    @FXML
    private void initialize(){
        questionClue.setText(PracticeController.getClue());
    }





}
