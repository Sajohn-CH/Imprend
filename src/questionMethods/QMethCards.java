package questionMethods;

import informationManagement.*;
import informationManagement.Stack;
import spacingAlgorithms.VerySimpleCard;
import utilities.Imprend;
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
    private ArrayList<Integer> infoGroups = new ArrayList<>();    //ArrayList of all id of all InformationGroups which will be learned
    private ArrayList<Integer[][]> repCards = new ArrayList<>();   //ArrayList of all cards with a response below 3. The ids are being stored. They will be displayed at the end once again.
    private int infoAsked;  //stores the number of the information (in the ArrayList in the InformationGroup), which act as an answer, to get it again to change date and ease.
    private boolean inRepetition;   //indicates if the cards, with an response below 3 are being repeated.
    private ArrayList<Integer[]> idLog;   //ArrayList with the ids of every question and answer, asked in the session (first question then answer, then next question, next answer etc.). The ids are:[id ofInformationGroup, id of InfoObject]
                                         //It is being used for the back()-method, to be able to redo the last card(s).
    //private InfoObject lastQuestion;         //Last question being asked
    private Information lastInformation;    //Last information, being asked
    private boolean redo;                   //Indicates whether the current card is one being redone
    private boolean recentlyAddRep;         //Indicates whether the last card has been added to the repCards, because the response wasn't that good.
    private boolean recentlyAddInfoGrp;     //Indicates whether the last card has been added to the infoGroups, because the response was so bad.
    public QMethCards(String stackPath) {
        stack = new Stack(stackPath);
        //get all InfoGroups where at least one object must be learned.
        infoGroups = stack.getAllInfoGroupsToLearn();
        //currentCard = new String[2];
        inRepetition = false;
        idLog = new ArrayList<>();
        redo = false;
        recentlyAddRep = false;
        recentlyAddInfoGrp = false;
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
                card.add(stack.getInfoObjectById(repCards.get(0)[0]).getInformation());
                card.add(stack.getInfoObjectById(repCards.get(0)[1]).getInformation());
                inRepetition = true;
                return card;
            }
            card.add("ERROR:lastCard");
            return card;
        }

        //checking if the card is being redone
        if(redo) {
            card.add(stack.getInfoObjectById(idLog.get(idLog.size() - 4)[0], idLog.get(idLog.size() - 4)[1]).getInformation());
            card.add(stack.getInfoObjectById(idLog.get(idLog.size() - 3)[0], idLog.get(idLog.size() - 3)[1]).getInformation());
            return card;
        }

        ArrayList<InfoObject> infos = (ArrayList<InfoObject>) stack.getInfoGroupById(infoGroups.get(0)).copyAllInformations();
        ArrayList<Information> informations = new ArrayList<>();
        ArrayList<Question> questions = new ArrayList<>();

        //Split infos with its InfoObjects Informations and Questions
        for(int i = 0; i < infos.size(); i++) {
            switch (infos.get(i).getType()) {
                case Imprend.strInfoObjectInfo:
                    informations.add((Information) infos.get(i));
                    break;
                case Imprend.strInfoObjectQuest:
                    questions.add((Question) infos.get(i));
                    break;
            }
        }
        infos.clear();

        //If the infoGroup has no question, all of the Information-Objects must be able to act as an question
        if (questions.size() == 0) {
            if(informations.size() <= 1) {
                //With no Question-Object and only 1 or 0 Information-Object, you can't make a card.
                card.add("ERROR:Skip");
                return card;
            }

            //get the Information-Object which need to be learned earlier.
            infoAsked = getEarliestInformationPos(informations);
            Information info = informations.get(infoAsked);
            //First there must be placed the question first in the ArrayList, then the answer
            card.add("");       //this is a palceholder for the question
            card.add(info.getInformation());
            //remove the Information-Object which acts as the Answer, so it won't be choosen as the question
            informations.remove(informations.indexOf(info));
            //replace the placeholder through the real question
            Random random = new Random();
            int posQuest =random.nextInt(informations.size());
            card.set(0,  informations.get(posQuest).getInformation());
            idLog.add(new Integer[]{informations.get(posQuest).getInfoGroupId(), informations.get(posQuest).getId()});
            idLog.add(new Integer[]{info.getInfoGroupId(), info.getId()});
        } else {
            //There is at least one question
            Random random = new Random();
            int posQuest = random.nextInt(questions.size());
            card.add(questions.get(posQuest).getInformation());
            infoAsked = getEarliestInformationPos(informations);
            card.add(informations.get(infoAsked).getInformation());
            idLog.add(new Integer[]{questions.get(posQuest).getInfoGroupId(), questions.get(posQuest).getId()});
            idLog.add(new Integer[]{informations.get(infoAsked).getInfoGroupId(), informations.get(infoAsked).getId()});

        }
        //Store the dates, ease and amountRepetition of the Information being asked, for the case the user wants to redo it (redoLastCard())
        Information info = (Information) stack.getInfoObjectById(idLog.get(idLog.size()-1)[0], idLog.get(idLog.size()-1)[1]);
        lastInformation = new Information();
        lastInformation.setDate(info.getDate());
        lastInformation.setOldDate(info.getOldDate());
        lastInformation.setEase(info.getEase());
        lastInformation.setAmountRepetition(info.getAmountRepetition());

        return card;

    }

    @Override
    public void setResponse (int response) {
        //set the response for the last asked information.
        //This method is called by the Panel, asking the "questions" after it has received the "response" (how good the card was remembered")

        recentlyAddRep = false;
        recentlyAddInfoGrp = false;

        if(response == -1) {
            //-1 means the last Card had benn skipped (due to an error, e.g. uncomplete card)
            infoGroups.remove(0);
            return;
        }
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
            Integer[][] repCard = new Integer[2][2];
            repCard[0] = idLog.get(idLog.size()-2);
            repCard[1] = idLog.get(idLog.size()-1);
            recentlyAddRep = true;
            repCards.add(repCard);
        }
        if (response < 2) {
            //incorrect answer -> will be displayed again
            infoGroups.add(infoGroups.get(0));
            infoGroups.remove(0);
            recentlyAddInfoGrp = true;
            return;
        }

        if(redo) {
            Information info = (Information) stack.getInfoObjectById(idLog.get(idLog.size()-3));
            Date date = info.getDate();
            info.setDate(VerySimpleCard.getNextDate(lastInformation.getEase(), lastInformation.getAmountRepetition(), lastInformation.getOldDate(), lastInformation.getDate()));
            info.setOldDate(date);
            info.setEase(VerySimpleCard.getNewEase(lastInformation.getEase(), response));
            redo = false;
            return;
        }

        Information info = (Information) stack.getInfoObjectById(idLog.get(idLog.size()-1));
        info.increaseAmountRepetition();
        Date date = info.getDate();
        info.setDate(VerySimpleCard.getNextDate(info.getEase(), info.getAmountRepetition(), info.getOldDate(), info.getDate()));
        info.setOldDate(date);
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

    @Override
    public void redoLastCard() {
        if(inRepetition) {
            Integer[][] repCard = new Integer[2][2];
            repCard[0][0] = stack.getInfoObjectById(idLog.get(idLog.size()-2)).getInfoGroupId();
            repCard[0][1] = stack.getInfoObjectById(idLog.get(idLog.size()-2)).getId();
            repCard[1][0] = stack.getInfoObjectById(idLog.get(idLog.size()-2)).getInfoGroupId();
            repCard[1][1] = stack.getInfoObjectById(idLog.get(idLog.size()-2)).getId();
            repCards.add(0, repCard);
        } else {
            if(recentlyAddRep) {
                repCards.remove(repCards.size()-1);
            }
            if(recentlyAddInfoGrp) {
                infoGroups.remove(infoGroups.size()-1);
            }
            redo = true;
        }
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

