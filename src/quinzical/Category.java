package quinzical;

import java.util.ArrayList;
import java.util.List;

public class Category {
    private final String _categoryName;
    private final List<Question> _questions = new ArrayList<Question>();

    public Category(String categoryName){
        _categoryName = categoryName;
    }

    public void addQuestion(Question question) {
        _questions.add(question);
    }

    public String getCategoryName() {
        return _categoryName;
    }

    public List<Question> getQuestions() {
        return _questions;
    }
}