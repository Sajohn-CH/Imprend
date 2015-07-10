package informationManagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by samuel on 30.06.15.
 *
 * A class representing a group of informations, which are all connected to each other.
 * A can contain only Information-Objects, but then, every Information-Object must be a question, where the other are the answer, and the other way around.
 * E.g. when you have 2 words in 2 different languages und you have to learn them in both direction. Then those 2 words are both Information-Objects.
 */
public class InformationGroup {

    private ArrayList<Information> informations;
    private ArrayList<Question> questions;
    private Date youngestDate;
    private String comment;
    private int id;

    public InformationGroup() {
        informations = new ArrayList<>();
        questions = new ArrayList<>();
        youngestDate = null;
    }

    public Date getYoungestDate() {
        ArrayList<Date> dates = new ArrayList<>();
        for(int i = 0; i < informations.size(); i++) {
            dates.add(informations.get(i).getDate());
        }
        Collections.sort(dates);
        youngestDate = dates.get(0);
        return youngestDate;
    }

    public ArrayList<Information> getInformations() {
        return informations;
    }

    public void setInformations(ArrayList<Information> informations) {
        this.informations = informations;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public void addInformation(Information info) {
        informations.add(info);
    }

    public void addQuestion(Question quest) {
        questions.add(quest);
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public InfoObject getInfoObjectById(int id) {
        //returns the Information or Questions with the id given
        for(int i = 0; i < informations.size(); i++) {
            if(informations.get(i).getId() == id) {
                return informations.get(i);
            }
        }

        for(int i = 0; i < questions.size(); i++) {
            if(questions.get(i).getId() == id) {
                return questions.get(i);
            }
        }
        return null;
    }
}

