package quinzical;

import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.robot.Robot;
import quinzical.controllers.play.AskQuestionController;


public class CountdownTimer extends Task<Integer>{

    public CountdownTimer(){

    }

    @Override
    public Integer call() throws Exception {
        for (int i = 10; i > 0; i--) {
           updateProgress(i,10);
           Thread.sleep(1000);
        }

        return 0;
    }

    @Override
    protected void updateProgress(double sec, double max){
        updateMessage((int)sec + " Secs");
        super.updateProgress(sec,max);
    }

}