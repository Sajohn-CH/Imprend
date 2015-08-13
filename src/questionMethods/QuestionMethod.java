package questionMethods;

import java.util.ArrayList;

/**
 * Created by samuel on 30.06.15.
 * This class never will have an existing object. It only has childrens.
 */
public class QuestionMethod {

    public QuestionMethod() {

    }

    public QuestionMethod(String stackPath) {

    }

    public ArrayList<String> getNextCard() {
        //Returns the information of the informations/questions of the next Cards
        return null;
    }

    public void setResponse(int response) {
        //Is being called by the Panel, "asking the questions" to say how good the user remembered the informations
    }

    public long getPredictedInterval(int response) {
        //returns the predicted interval (when the card will be asked the next time) for the given answer
        return 0;
    }

    public void stackFinished() {
        //Method called by the Panel to say that the stack is finished. So everything can be saved
    }

    public boolean hasCards() {
        return true;
    }

    public void redoLastCard() {

    }
}
