package QuestionMethods;

import InformationManagement.Information;
import InformationManagement.InformationGroup;
import InformationManagement.Question;
import InformationManagement.Stack;

import java.text.CollationElementIterator;
import java.util.*;

/**
 * Created by samuel on 30.06.15.
 * This is a simple QuestionMethod for normal Cards.
 */
public class QMethCards extends QuestionMethod{

    private ArrayList<InformationGroup> infoGroups = new ArrayList<>();
    private int pos;        //current position in the infoGrops ArrayList. Is increase everytime getNextCard is being called.

    public QMethCards(String stackPath) {
        Stack stack = new Stack(stackPath);
        //get all InfoGroups where at least one object must be learned.
        infoGroups = stack.getAllInfoGroupsToLearn();

    }

    @Override
    public ArrayList<String> getNextCard() {
        //This algorithm for getting the next card is quiet simple, and does not work well for every case (e.g. 3 Information-Object, 0 Question-Object). It's main purpose is
        //to deliver a first algorithm, so the programm will work.

        //ArrayList with the card. It contains only 2 Elements. One for the question, the first one, and one for the answer, the second element.
        //It is an ArrayList, because, other QuestionMethods maybe have to transfer more data.
        ArrayList<String> card = new ArrayList<>();

        //first checking if this wasn't the last card before
        if(pos == infoGroups.size()) {
            card.add("ERROR:lastCard");
            return card;
        }

        ArrayList<Information> infos = infoGroups.get(pos).getInformations();
        ArrayList<Question> questions = infoGroups.get(pos).getQuestions();


        //If the infoGroup has no question, all of the Information-Objects must be able to act as an question
        if (questions.size() == 0) {
            if(infos.size() <= 1) {
                return null;
            }

            //get the Information-Object which need to be learned earlier.
            Information info = getEarliestInformation(infos);
            //First there must be placed the question first in the ArrayList, then the answer
            card.add("");       //this is a palceholder for the question
            card.add(info.getInformation());
            infos.remove(infos.indexOf(info));
            Random random = new Random();
            //replace the placeholder through the real question
            card.set(0, infos.get(random.nextInt(infos.size())).getInformation());
        } else {
            //There is atleast one question
            Random random = new Random();
            card.add(questions.get(random.nextInt(questions.size())).getQuestion());
            card.add(getEarliestInformation(infos).getInformation());

        }
        pos++;
        return card;

    }

    private Information getEarliestInformation(ArrayList<Information> infos) {
        //get the Information-Object which need to be learned earlier.
        //get all earliest date. (earliest = smalles number when transfered to long with getTime()
        int posEarliest = 0;       //position of the object with the earliest date
        for (int i = 1; i < infos.size(); i++) {
            if (infos.get(i).getDate().getTime() < infos.get(posEarliest).getDate().getTime()) {
                posEarliest = i;
            }
        }
        return infos.get(posEarliest);
    }

}
