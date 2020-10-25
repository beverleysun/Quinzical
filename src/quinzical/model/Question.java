package quinzical.model;

/**
 * Represents a singular question
 *
 * @author Beverley Sun, Jinkai Zhang
 */
public class Question {
    private final String question;
    private final String[] answers;
    private final int value;
    private boolean completed;
    private boolean available;

    /**
     * Constructor for game mode
     * @param question the question string
     * @param answers all the accepted answers to the question
     * @param value how much the question is worth
     * @param completed if the question has been answered
     * @param available if the question is available to ask
     */
    public Question(String question, String[] answers, int value, boolean completed, boolean available) {
        this.question = question;
        this.answers = answers;
        this.value = value;
        this.completed = completed;
        this.available = available;
    }

    /**
     * Constructor for practice mode
     * @param question the question string
     * @param answers all the accepted answers to the question
     */
    public Question(String question, String[] answers) {
        value = 0;
        this.question = question;
        this.answers = answers;
    }

    /**
     * Validate the user's answer with the actual answer(s)
     * @param usersAnswer the string to compare the answer with
     * @return true if answer matches up, false otherwise
     */
    public boolean compareAnswers(String usersAnswer){
        for(String answer : answers){
            if(usersAnswer.toLowerCase().trim().equals(answer.toLowerCase().trim())){
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the question is available to answer
     * @return true if question is available to answer, false otherwise
     */
    public boolean isAvailable() {
    	return available;
    }

    /**
     * Set the availability of the question
     * @param completed value to set availability to
     */
    public void setAvailable(boolean completed) {
    	available = completed;
    }

    /**
     * Get the question in string form
     * @return the question string
     */
    public String getQuestionStr() {
        return question;
    }

    /**
     * Get all the answers to the questions
     * @return an array of type String storing all the answers
     */
    public String[] getAnswers() {
        return answers;
    }

    /**
     * Get the question value
     * @return the value of the question of type int
     */
    public int getValue() {
        return value;
    }

    /**
     * Get the question value in string form
     * @return the value of the question of type String
     */
    public String getValueString() {
        return Integer.toString(value);
    }

    /**
     * Check if the question has been completed
     * @return true if completed, false otherwise
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Set the question to be completed or not
     * @param completed true if question has been completed, false otherwise
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}