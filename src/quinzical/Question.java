package quinzical;

public class Question {
    private final String _question;
    private final String[] _answers;
    private final int _value;
    private boolean _completed;
    private boolean _available;
  
    // For practice mode.
    private int  _answeredTimes;
    private String _hint;

    public Question(String question, String[] answers, int value, boolean completed, boolean available) {
        _question = question;
        _answers = answers;
        _value = value;
        _completed = completed;
        _available = available;
    }

    // Overload a constructor for practice mode.
    public Question(String question, String[] answers, String hint,int answeredTimes) {
        _value = 0;
        _question = question;
        _answers = answers;
        _answeredTimes = answeredTimes;
        _hint = hint;
    }
    //**************************************************************************************************
    public String getHint() {
        return _hint;
    }

    public int getAnsweredTimes() {
        return _answeredTimes;
    }

    public void set_answeredTimes(int _answeredTimes) {
        this._answeredTimes = _answeredTimes;
    }

    public boolean compareAnswers(String usersAnswer){
        for(String answer : _answers){
            if(usersAnswer.toLowerCase().trim().equals(answer.toLowerCase().trim())){
                return true;
            }
        }
        return false;
    }
    //**************************************************************************************************
    public boolean isAvailable() {
    	return _available;
    }
    
    public void setAvailable(boolean completed) {
    	_available = completed;
    }

    public String getQuestion() {
        return _question;
    }

    public String[] getAnswer() {
        return _answers;
    }

    public int getValue() {
        return _value;
    }

    public String getValueString() {
        return Integer.toString(_value);
    }

    public boolean isCompleted() {
        return _completed;
    }

    public void setCompleted(boolean completed) {
        _completed = completed;
    }
}