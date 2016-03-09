package questionMethods;

import informationManagement.InfoObject;
import utilities.Imprend;

import java.util.ArrayList;

/**
 * Kindklasse von {@link QMethCards}, welche dies für WordFormationCards rudimentär erweitert.
 * Erstellt am 04.02.16.
 * @author Samuel Martin
 */
public class WordFormationQMeth extends QMethCards{

    public WordFormationQMeth(String stackPath) {
        super(stackPath);
    }

    @Override
    public ArrayList<String> getNextCard() {
        //Replaces the question through group (which schould be verb, nomen, etc) of the answer + "von" + the "old" question
        //get the card with the Method from QMethCards
        ArrayList<String> card = super.getNextCard();
        //doesn't replace question, when it is the last card.
        if(card.get(0).equals("ERROR:lastCard")) {
            return card;
        }
        //extracts the question
        String question = card.get(0);
        //infoObject of the answer
        InfoObject answer = super.getStack().getInfoObjectById(super.getCardId().get(1));
        //replace the question
        question = answer.getGroup() + " von " + question;
        card.set(0, question);
        return card;
    }

    @Override
    public void setResponse (int response) {
        super.setResponse(response);
        //Add Card Learned to Statistic (includes repetitions)
        if(response >= 3) {
            Imprend.statistic.addCardLearnedToday();
        }
    }

}
