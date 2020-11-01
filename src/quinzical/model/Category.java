package quinzical.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a category
 *
 * @author Beverley Sun, Jinkai Zhang
 */
public class Category {
    private final String categoryName;
    private final List<Question> questions = new ArrayList<>();
    private boolean selected = false;

    /**
     * Represents a category
     * @param categoryName the name of the category
     */
    public Category(String categoryName){
        this.categoryName = categoryName;
    }

    /**
     * Add a question to the category
     * @param question the question to be added
     */
    public void addQuestion(Question question) {
        questions.add(question);
    }

    /**
     * Gets the category name
     * @return the category name
     */
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * Gets all the question within the category
     * @return list of questions
     */
    public List<Question> getQuestions() {
        return questions;
    }

    /**
     * Each question will have a different value. Find a question by its value
     * @param value the value to search for
     * @return the question that was found
     */
    public Question findQuestionByValue(int value) {
        for (Question question : questions) {
            if (question.getValue() == value) {
                return question;
            }

        }
        return null;
    }

    /**
     * @param selected the select status to set to
     */
    public void setSelected(boolean selected){
        this.selected = selected;
    }

    /**
     * True if the category has been selected in the category selection scene
     * @return the selection status
     */
    public boolean getSelectStatus(){
        return selected;
    }
}