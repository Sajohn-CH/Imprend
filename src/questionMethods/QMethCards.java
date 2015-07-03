package questionMethods;

import informationManagement.Information;
import informationManagement.InformationGroup;
import informationManagement.Question;
import informationManagement.Stack;
import spacingAlgorithms.VerySimpleCard;
import utilities.Save;

import java.util.*;

/**
 * Created by samuel on 30.06.15.
 * This is a simple QuestionMethod for normal Cards.
 * It uses the SM-2 algorithm (http://www.supermemo.com/english/ol/sm2.htm), which I slightly modified (less responses)
 *  response:
 * 0 = black out; no idea
 * 1 = incorrect answer
 * 2 = correct answer, but with some difficulties to remember
 * 3 = correct answer, good able to remember
 * 4 = correct answer, instant response
 */
public class QMethCards extends QuestionMethod{

    private Stack stack;
    private ArrayList<InformationGroup> infoGroups = new ArrayList<>();
    private ArrayList<String[]> repCards = new ArrayList<>();   //ArrayList of all cards with a response below 3. They will be displayed at the end once again.
    //pos = 0//private int pos;        //current position in the infoGrops ArrayList. Is increase everytime getNextCard is being called.
    private int infoAsked;  //stores the number of the information (in the ArrayList in the InformationGroup), which act as an answer, to get it again to change date and ease.
    private String[] currentCard;   //Stores the currentyl asked card [question, asnwer]
    private boolean inRepetition;   //indicates if the cards, with an response below 3 are being repeated.

    public QMethCards(String stackPath) {
        stack = new Stack(stackPath);
        //get all InfoGroups where at least one object must be learned.
        infoGroups = stack.getAllInfoGroupsToLearn();
        currentCard = new String[2];
        inRepetition = false;

    }

    @Override
    public ArrayList<String> getNextCard() {
        //This algorithm for getting the next card is quiet simple, and does not work well for every case (e.g. 3 Information-Object, 0 Question-Object). It's main purpose is
        //to deliver a first algorithm, so the programm will work.

        //ArrayList with the card. It contains only 2 Elements. One for the question, the first one, and one for the answer, the second element.
        //It is an ArrayList, because, other questionMethods maybe have to transfer more data.
        ArrayList<String> card = new ArrayList<>();

        //first checking if this wasn't the last card before
        if (infoGroups.size() == 0) {
            if(repCards.size() != 0) {
                //Learning finished: Time the repeat those, which had an response below 3
                card.add(repCards.get(0)[0]);
                card.add(repCards.get(0)[1]);
                inRepetition = true;
                return card;
            }
            card.add("ERROR:lastCard");
            return card;
        }

        ArrayList<Information> infos = (ArrayList<Information>) infoGroups.get(0).getInformations().clone();
        ArrayList<Question> questions = (ArrayList<Question>) infoGroups.get(0).getQuestions().clone();


        //If the infoGroup has no question, all of the Information-Objects must be able to act as an question
        if (questions.size() == 0) {
            if(infos.size() <= 1) {
                //With no Question-Object and only 1 or 0 Information-Object, you can't make a card.
                return null;
            }

            //get the Information-Object which need to be learned earlier.
            infoAsked = getEarliestInformationPos(infos);
            Information info = infos.get(infoAsked);
            //First there must be placed the question first in the ArrayList, then the answer
            card.add("");       //this is a palceholder for the question
            card.add(info.getInformation());
            //remove the Information-Object which acts as the Answer, so it won't be choosen as the question
            infos.remove(infos.indexOf(info));
            //replace the placeholder through the real question
            Random random = new Random();
            card.set(0, infos.get(random.nextInt(infos.size())).getInformation());
        } else {
            //There is atleast one question
            Random random = new Random();
            card.add(questions.get(random.nextInt(questions.size())).getQuestion());
            infoAsked = getEarliestInformationPos(infos);
            card.add(infos.get(infoAsked).getInformation());

        }
        //pos++;
        currentCard[0] = card.get(0);
        currentCard[1] = card.get(1);
        return card;

    }

    @Override
    public void setResponse (int response) {
        //set the response for the last asked information.
        //This method is called by the Panel, asking the "questions" after it has received the "response" (how good the card was remembered")

        if(inRepetition) {
            if(response > 2) {
                //response > 2 -> card removed
                repCards.remove(0);
            } else {
                //response <= 2 -> card need to be repeated again
                repCards.add(repCards.get(0));
                repCards.remove(0);
            }
            return;
        }

        if(response < 3) {
            //response not that good -> repetition at the end
            repCards.add(currentCard);
        }
        if (response < 2) {
            //incorrect answer -> will be displayed again
            infoGroups.add(infoGroups.get(0));
            infoGroups.remove(0);
            return;
        }

        Information info = infoGroups.get(0).getInformations().get(infoAsked);
        info.increaseAmountRepetition();
        info.setDate(VerySimpleCard.getNextDate(info.getEase(), info.getAmountRepetition(), info.getOldDate(), info.getDate()));
        info.setEase(VerySimpleCard.getNewEase(info.getEase(), response));
        infoGroups.remove(0);
    }

    @Override
    public void stackFinished() {
        //save the stack, to save the new dates and eases
        Save.saveStack(stack);
    }

    @Override
    public  boolean hasCards() {
        if(infoGroups.size() == 0) {
            return false;
        }
        return true;
    }
    private int getEarliestInformationPos(ArrayList<Information> infos) {
        //get the Information-Object which need to be learned earlier.
        //get all earliest date. (earliest = smalles number when transfered to long with getTime()
        int posEarliest = 0;       //position of the object with the earliest date
        for (int i = 1; i < infos.size(); i++) {
            if (infos.get(i).getDate().getTime() < infos.get(posEarliest).getDate().getTime()) {
                posEarliest = i;
            }
        }
        return posEarliest;
    }

}

