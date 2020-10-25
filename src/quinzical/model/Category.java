package quinzical.model;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private final String _categoryName;
    private final List<Question> _questions = new ArrayList<Question>();

    private boolean _selected = false;

    public void setSelected(boolean selected){
        _selected = selected;
    }

    public boolean getSelectStatus(){
        return _selected;
    }

    /**
     * Represents a category
     * @param categoryName the name of the category
     */
    public Category(String categoryName){
        _categoryName = categoryName;
    }

    /**
     * Add a question to the category
     * @param question the question to be added
     */
    public void addQuestion(Question question) {
        _questions.add(question);
    }

    /**
     * Gets the category name
     * @return the category name
     */
    public String getCategoryName() {
        return _categoryName;
    }

    /**
     * Gets all the question within the category
     * @return list of questions
     */
    public List<Question> getQuestions() {
        return _questions;
    }

    /**
     * Each question will have a different value. Find a question by its value
     * @param value the value to search for
     * @return the question that was found
     */
    public Question findQuestionByValue(int value) {
        for (Question question : _questions) {
            if (question.getValue() == value) {
                return question;
            }

        }
        return null;
    }
}