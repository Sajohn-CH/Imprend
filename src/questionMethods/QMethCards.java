package questionMethods;

import informationManagement.*;
import informationManagement.Stack;
import spacingAlgorithms.SuperMemo2;
import utilities.Imprend;
import utilities.Save;

import java.util.*;

/**
 * Organisiert das Kartenabfragen für Karten. Es gibt 5 Rückmeldungsmöglichkeiten (Wie gut der Benutzer ein Wort konnte): <br>
 * 0 = Gar keine Ahnung. <br>
 * 1 = Falsche Antwort <br>
 * 2 = Richtige Antwort, aber schwierig zu erinnern <br>
 * 3 = Richtige Antwort, gut zu erinnern <br>
 * 4 = Richtige, sofortige Antwort <br>
 * Zum Berechnen des nächsten Abfragedatums wird ein leicht modifizierte SuperMemo2-Algorithmus gebraucht. Dieser ist in der Klasse {@link SuperMemo2} implementiert. <br>
 * Erstellt am 30.06.15. Teilweise überarbeitet am 10.10.15. um es mit Synonymen und Gruppen kompatibel zu machen
 * @author Samuel Martin
 * {@inheritDoc}
 *
 */
public class QMethCards extends QuestionMethod{

    private Stack stack;
    private ArrayList<Integer[]> infosAsked = new ArrayList<>();    //ArrayList of all ids of all informations which will be learned (and asked)
    private ArrayList<String[]> repCards = new ArrayList<>();       //ArrayList of all cards with a response below 3. The question, synonyms, the synonyms of the answer and the answer are stored. They will be displayed at the end once again.
    private ArrayList<Integer[]> repCardsId;                        //ArrayList of all ids of the information, which need to be repeated again at the end.
    private boolean inRepetition;                                   //indicates if the cards, with an response below 3 are being repeated.
    private ArrayList<Integer[][]> idLog;       //ArrayList with the ids of every question, answer and Synonyms, asked in the session. The ids are:[id ofInformationGroup, id of InfoObject]
                                                //One Element in the ArrayList looks like this: [id ofInformationGroup, id of InfoObject]       the question
                                                //                                              [id ofInformationGroup, id of InfoObject]       the answer
                                                //                                              [id ofInformationGroup, id of InfoObject]   the synonyms (if existing, elsewise it's empty)
                                                //                                              [id ofInformationGroup, id of InfoObject]   the synonyms (if existing, elsewise it's empty)
                                                //                                                  ...
                                                //It is being used for the back()-method, to be able to redo the last card(s).
    private Information lastInformation;        //Last information, being asked
    private boolean redo;                       //Indicates whether the current card is one being redone
    private boolean recentlyAddRep;             //Indicates whether the last card has been added to the repCards, because the response wasn't that good.
    private boolean recentlyAddInfosAsked;      //Indicates whether the last card has been added to the infosAsked, because the response was so bad.

    public QMethCards(String stackPath) {
        stack = new Stack(stackPath);
        //get all InfoGroups where at least one object must be learned. Date was before now or today.
        ArrayList<Integer> infoGrps = stack.getAllInfoGroupsToLearn();
        //initialise Variables
        inRepetition = false;
        idLog = new ArrayList<>();
        redo = false;
        recentlyAddRep = false;
        recentlyAddInfosAsked = false;
        repCardsId = new ArrayList<>();

        //fill the infosAsked arrayList
        infosAsked = getInfosToLearn(infoGrps);

    }

    @Override
    public ArrayList<String> getNextCard() {
        //card contains 4 elements: 1. the question, 2. the answer, 3. the synonyms (if there are no, it will be an empty String ("")), 4. synonyms to the answer = no the answer (if there are no, it will be an empty String (""))
        //It is an ArrayList, because, other questionMethods maybe have to transfer more  or less data.
        ArrayList<String> card = new ArrayList<>();

        //first checking if this wasn't the last card before repetition starts
        if (infosAsked.size() == 0) {
            if(repCards.size() != 0) {
                //Learning finished: Time to repeat those, which had an response below 3
                card.add(repCards.get(0)[0]);
                card.add(repCards.get(0)[1]);
                card.add(repCards.get(0)[2]);
                card.add(repCards.get(0)[3]);
                inRepetition = true;
                return card;
            }
            //If there are no cards to repeat anymore, Stack is finished .To communicate this to the panel, a special message/card will be deliverd. The Panel catchs it.
            card.add("ERROR:lastCard");
            return card;
        }

        //checking if the card is being redone
        if(redo) {
            //add question
            card.add(stack.getInfoObjectById(idLog.get(idLog.size() - 2)[0]).getInformation());
            //add answer
            card.add(stack.getInfoObjectById(idLog.get(idLog.size() - 2)[1]).getInformation());
            //add synonyms
            String syn = "";
            for(int i = 2; i < idLog.get(idLog.size() - 2).length; i++) {
                syn += stack.getInfoObjectById(idLog.get(idLog.size()- 2)[i]).getInformation() + ", ";
            }
            card.add(syn);
            //find the synonyms to the answer and add them
            ArrayList<InfoObject> infos = stack.getInfoGroupById(idLog.get(idLog.size() - 2)[0][0]).copyAllInformations();
            InfoObject info = stack.getInfoObjectById(idLog.get(idLog.size()-1)[1]);
            syn = "";
            if(!info.getGroup().equals("")) {
                //remove the InfoObject which is acting as the answer
                infos.remove(infos.indexOf(info));
                for(int i = infos.size()-1; i >= 0; i--) {
                    if(infos.get(i).getGroup().equals(info.getGroup())) {
                        //the asked Information is in the same group as this InfoObject -> it's an synonym to the answe
                        syn += infos.get(i).getInformation() + ", ";
                    }
                }
            }
            card.add(syn);
            return card;
        }

        //get the InfoObjects from the InformationGroup where the info asked is.
        int infoGroupId = stack.getInfoObjectById(infosAsked.get(0)).getInfoGroupId();
        Information info = (Information) stack.getInfoObjectById(infosAsked.get(0));    //the information asked
        ArrayList<InfoObject> infos = stack.getInfoGroupById(infoGroupId).copyAllInformations();
        ArrayList<Information> informations = new ArrayList<>();    //all Informations in the InformationGroup
        ArrayList<Question> questions = new ArrayList<>();      //all Questions in the InformationGroup

        //remove the InfoObject which is acting as the answer, it won't be needed in the process of chosing the question and adding the synonyms
        infos.remove(infos.indexOf(info));
        //removing all infoObjects being in the same group as the information asked and add them to the synToAnswer ArrayList. Those InfoObjects will be display as, "not the answer"
        //which means, that they are synonym to the answer.
        ArrayList<InfoObject> synToAnswer = new ArrayList<>();
        if(!info.getGroup().equals("")) {
            for(int i = infos.size()-1; i >= 0; i--) {
                if(infos.get(i).getGroup().equals(info.getGroup())) {
                    synToAnswer.add(infos.get(i));
                    infos.remove(i);
                }
            }
        }

        //idLogElement, is the element of the idLog created, with the creation of this card.
        Integer[][] idLogElement = new Integer[infos.size()+1][2];

        //split the infoObjects in infos into Informations and Questions
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

        if(questions.size() >= 1) {
            //There is atleast one question, so the question(s) will always be taken to ask
            //One question will be chosen by random
            Random random = new Random();
            int posQuest = random.nextInt(questions.size());
            //add question
            card.add(questions.get(posQuest).getInformation());
            //add question to idLog
            idLogElement[0][0] = questions.get(posQuest).getInfoGroupId();
            idLogElement[0][1] = questions.get(posQuest).getId();
        } else {
            if(informations.size() < 1) {
                //With no Question-Object and only 1 or 0 Information-Object, you can't make a card.
                card.add("ERROR:Skip");
                return card;
            }
            //chose an Information to act as a question by random
            Random random = new Random();
            int posQuest = random.nextInt(informations.size());
            //add question
            card.add(informations.get(posQuest).getInformation());
            //add question to idLog
            idLogElement[0][0] = informations.get(posQuest).getInfoGroupId();
            idLogElement[0][1] = informations.get(posQuest).getId();
            //remove the Information acting as a question from the informations-List, so the rest can be added, as synonyms
            informations.remove(posQuest);
        }
        //add the answer
        card.add(info.getInformation());
        //add the answer to the idLog
        idLogElement[1][0] = info.getInfoGroupId();
        idLogElement[1][1] = info.getId();

        //add the rest of the Informations as synonyms
        String synonyms = "";
        for(int i = 0; i < informations.size(); i++) {
            synonyms += informations.get(i).getInformation() + ", ";
            //add them each to the idLog
            idLogElement[i+2][0] = informations.get(i).getInfoGroupId();
            idLogElement[i+2][1] = informations.get(i).getId();
        }
        card.add(synonyms);

        //add those synonym to the answer
        String strSynToAnswer = "";
        for(int i = 0; i < synToAnswer.size(); i++) {
            strSynToAnswer += synToAnswer.get(i).getInformation() + ", ";

        }
        card.add(strSynToAnswer);
        //Add the idLogElement to the idLog
        idLog.add(idLogElement);

        //Store the dates, ease and amountRepetition of the Information being asked, for the case the user wants to redo it (redoLastCard())
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
        recentlyAddInfosAsked = false;

        if(response == -1) {
            //-1 means the last Card had benn skipped (due to an error, e.g. uncompleted card)
            infosAsked.remove(0);
            return;
        }
        if(inRepetition) {
            if(response > 2) {
                //response > 2 -> card removed
                repCards.remove(0);
                repCardsId.remove(0);
            } else {
                //response <= 2 -> card need to be repeated again
                repCards.add(repCards.get(0));
                repCardsId.add(repCardsId.get(0));
                repCards.remove(0);
                repCardsId.remove(0);
            }
            return;
        }

        if(response < 3) {
            //response not that good -> repetition at the end
            String[] repCard = new String[4];
            repCard[0] = stack.getInfoObjectById(idLog.get(idLog.size()-1)[0]).getInformation();
            repCard[1] = stack.getInfoObjectById(idLog.get(idLog.size()-1)[1]).getInformation();
            repCard[2] = "";
            for(int i = 2; i < idLog.get(idLog.size()-1).length; i++) {
                repCard[2] += stack.getInfoObjectById(idLog.get(idLog.size()-1)[i]).getInformation();
            }
            //find the synonyms to the answer and add them
            ArrayList<InfoObject> infos = stack.getInfoGroupById(idLog.get(idLog.size()-1)[0][0]).copyAllInformations();
            InfoObject info = stack.getInfoObjectById(idLog.get(idLog.size()-1)[1]);
            repCard[3] = "";
            if(!info.getGroup().equals("")) {
                //remove the InfoObject which is acting as the answer
                infos.remove(infos.indexOf(info));
                for(int i = infos.size()-1; i >= 0; i--) {
                    if(infos.get(i).getGroup().equals(info.getGroup())) {
                        //the asked Information is in the same group as this InfoObject -> it's an synonym to the answer
                        repCard[3] += infos.get(i).getInformation() + ", ";
                    }
                }
            }
            recentlyAddRep = true;
            repCards.add(repCard);
            repCardsId.add(idLog.get(idLog.size()-1)[0]);
        }
        if (response < 2) {
            //incorrect answer -> will be displayed again directly
            recentlyAddInfosAsked = true;
            return;
        }

        if(redo) {
            //If the user pressed the redo/undo/back button (the one with an arrow to the right), the informations in lastInformation will be used to calculate the values.
            //lastInformation has been initilised before in getNextCard();
            Information info = (Information) stack.getInfoObjectById(idLog.get(idLog.size()-2)[1]);
            Date date = info.getDate();
            lastInformation.increaseAmountRepetition();
            info.setDate(SuperMemo2.getNextDate(lastInformation.getEase(), lastInformation.getAmountRepetition(), lastInformation.getOldDate(), lastInformation.getDate()));
            info.setOldDate(date);
            info.setEase(SuperMemo2.getNewEase(lastInformation.getEase(), response));
            redo = false;
            return;
        }

        Information info = (Information) stack.getInfoObjectById(idLog.get(idLog.size()-1)[1]);
        info.increaseAmountRepetition();
        Date date = info.getDate();
        info.setDate(SuperMemo2.getNextDate(info.getEase(), info.getAmountRepetition(), info.getOldDate(), info.getDate()));
        info.setOldDate(date);
        info.setEase(SuperMemo2.getNewEase(info.getEase(), response));
        infosAsked.remove(0);
    }

    @Override
    public long getPredictedInterval(int response) {
        //returns the predicted intervall for the current information being asked, for the given response
        if(inRepetition) {
            if(response > 2) {
                Information info = (Information) stack.getInfoObjectById(repCardsId.get(0));
                Date date = info.getDate();
                Date newDate = SuperMemo2.getNextDate(info.getEase(), info.getAmountRepetition() + 1, info.getOldDate(), info.getDate());
                Long interval = newDate.getTime() - date.getTime();
                return interval;
            } else {
                return 0;
            }
        } else {
            if(response < 2) {
                return 0;
            }
            Information info = (Information) stack.getInfoObjectById(idLog.get(idLog.size()-1)[1]);
            Date date = info.getDate();
            Date newDate = SuperMemo2.getNextDate(info.getEase(), info.getAmountRepetition() + 1, info.getOldDate(), info.getDate());
            Long interval = newDate.getTime() - date.getTime();
            return interval;

        }
    }

    @Override
    public void stackFinished() {
        //save the stack, to save the new dates and eases
        Save.saveStack(stack);
    }

    @Override
    public  boolean hasCards() {
        //returns if the stack has cards to learn left
        if(infosAsked.size() == 0) {
            return false;
        }
        return true;
    }

    @Override
    public void redoLastCard() {
        //is putting the last made card at the place where it will be asked next, so it will be redone.
        if(inRepetition) {
            String[] repCard = new String[4];
            repCard[0] = stack.getInfoObjectById(idLog.get(idLog.size()-1)[0]).getInformation();
            repCard[1] = stack.getInfoObjectById(idLog.get(idLog.size()-1)[1]).getInformation();
            for(int i = 2; i < idLog.get(idLog.size()-1).length; i++) {
                repCard[2] += stack.getInfoObjectById(idLog.get(idLog.size()-1)[i]).getInformation();
            }
            //find the synonyms to the answer and add them
            ArrayList<InfoObject> infos = stack.getInfoGroupById(idLog.get(idLog.size()-1)[0][0]).copyAllInformations();
            InfoObject info = stack.getInfoObjectById(idLog.get(idLog.size()-1)[1]);
            repCard[3] = "";
            if(!info.getGroup().equals("")) {
                //remove the InfoObject which is acting as the answer
                infos.remove(infos.indexOf(info));
                for(int i = infos.size()-1; i >= 0; i--) {
                    if(infos.get(i).getGroup().equals(info.getGroup())) {
                        //the asked Information is in the same group as this InfoObject -> it's an synonym to the answer
                        repCard[3] += infos.get(i).getInformation() + ", ";
                    }
                }
            }
            repCards.add(repCard);
        } else {
            if(recentlyAddRep) {
                repCards.remove(repCards.size()-1);
                repCardsId.remove(repCardsId.size()-1);
            }
            if(recentlyAddInfosAsked) {
                infosAsked.remove(0);
            }
            redo = true;
        }
    }

    private int getEarliestInformationPos(ArrayList<Information> infos) {
        //get the Information-Object which need to be learned earlier.
        //get all earliest date. (earliest = smallest number when transfered to long with getTime()
        int posEarliest = 0;       //position of the object with the earliest date
        for (int i = 1; i < infos.size(); i++) {
            if (infos.get(i).getDate().getTime() < infos.get(posEarliest).getDate().getTime()) {
                posEarliest = i;
            }
        }
        return posEarliest;
    }

    private ArrayList<Integer[]> getInfosToLearn(ArrayList<Integer> informationGroups) {
        //Method returns all ids of the information, which have to be asked, in the given InformationGroups
        ArrayList<Integer[]> ids = new ArrayList<>();
        for(int i = 0; i < informationGroups.size(); i++) {
            InformationGroup infoGroup = stack.getInfoGroupById(informationGroups.get(i));
            for(int j = 0; j < infoGroup.getAmountInformations(); j++) {
                if(infoGroup.getInfoObjectById(j).getType().equals(Imprend.strInfoObjectInfo)) {
                    //it's an InformationObject, and not a Question
                    Information info = (Information) infoGroup.getInfoObjectById(j);
                    if(info.getDate().getTime() < System.currentTimeMillis()) {
                        //Date before now
                        Integer[] id = new Integer[2];
                        id[0] = info.getInfoGroupId();
                        id[1] = info.getId();
                        ids.add(id);
                    }
                }
            }
        }
        //Shuffle list, wo it won't be always asked in the same order, the order it was saved and generated the ids.
        Collections.shuffle(ids);
        return ids;
    }

}

