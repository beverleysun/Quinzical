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

    /**
     * Create a timer object in the AskQuestionController class.
     */
    public CountdownTimer(){
    }

    /**
     * Countdown from 10 to 1 , stop 1 second after each count.
     * @return the count value
     */
    @Override
    public Integer call() throws Exception {
        for (int i = 10; i > 0; i--) {
           updateProgress(i,10);
           Thread.sleep(1000);
        }
        return 0;
    }

    /**
     * This method will update the value of second in the countdown label.
     * @param sec the countdown second
     * @param sec the al time when the countdown starts
     */
    @Override
    protected void updateProgress(double sec, double max){
        updateMessage((int)sec + " Secs");
        super.updateProgress(sec,max);
    }

}