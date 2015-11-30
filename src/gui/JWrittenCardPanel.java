package gui;

import questionMethods.QuestionMethod;
import utilities.Imprend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Kindklasse von {@link JNavPanel}. Es dient dazu Wörter bzw. InfoObjects schriftlich abzufragen. Dies geschieht dadurch, dass zuerst die Frage samt Synonyme angezeigt wird, dann muss der
 * Benutzer die Antwort in ein Textfeld eintippen. Die Antwort wird dann mit der richtigen Antwort verglichen, daraus wird dann berechnet wie gut der Benutzer die Information erinnern konnte. Dem Benutzer wird
 * dies angezeigt. Er hat die Möglichkeit die Rückmeldung (wie gut er es konnte) zu verbessern, bevor die nächste Frage angezeigt wird. Es funktioniert sehr ähnlich wie das JCardPanel {@link JCardPanel}
 * <br> Es gibt zwei verscheidenen Panels. Das pnlRunning ist für das Abfragen zuständig, das pnlResult ist für das Anzeigen der Antwort zuständig. Zwischen den zwei Panels wird mithilfe des Layoutmanagers
 * CardLayout hin und her gewechselt. Viele Elemente (meistens JLabels) sind doppelt vorhanden, weil sie auf beiden Panels vorkommen.<br>
 * Erstellt am 06.10.15.
 */
public class JWrittenCardPanel extends JNavPanel implements ItemListener {


    private boolean justStarted;                    //Indicates wheter this is the first card or not, so the back-Button would lead then back to the menu
    private boolean redo;                           //Indicates whether the back-Button just has been pressed, so it can't be pressed two times in a row.
    private QuestionMethod questMeth;
    private CardLayout cd;                          //The CardLayout. Responsible for switching between asking and showing the answer.

    private JPanel pnlRunning;                      //Panel used to ask the question
    private JLabel lblGrp1;                         //the "question"
    private JLabel lblGrp2;                         //place to rule out some synonyms. All synonyms staying here can not be the answer
    private JLabel lblSynonyms;                     //label displaying the word synonyms, to lable what the lblGrp2 is showing
    private JTextField tFieldGrp2;                  //place to write the answer
    private JLabel lblNotAnswer;                    //display all "wrong answers", meaning synonyms of the asked answer.
    private JLabel lblDescribeNotAnswer;            //label describing the lblNot answer.
    private JButton btnFinished;                    //pressed the finished writting the answer


    private JPanel pnlResult;                       //panel to show the result, including: Wrong letters, which respond this will result in, and the ability to change this.
    private ArrayList<JLabel> lblCorrectAnswer;     //labels to show the correct answer
    private JLabel lblCorrectAnswDes;               //label that describes the Labels lblCorrectAnswer
    private JPanel pnlYourAnswer;                   //panel to show the users answer, used several labels, to be able to color them each, so that every letter is colored
    private ArrayList<JLabel> lblYourAnswer;        //labels to show the answer of the user
    private JLabel lblYourAnswDes;                  //label that describes the lblYourAnswer labels
    private JPanel pnlCorrectAnswer;                //panel to show the correct answer, used several labels, to be able to color them each, so that every letter is colored
    private JButton btnNextCard;                    //pressed for showing the next Card
    private JLabel lblGrp2Res;                      //same label as lblGrp2Res, for the pnlResult (one label for 2 panels doesn't work)
    private JLabel lblLettersWrong;                 //label to show how many letters where wrong, description
    private JLabel lblLettersWrongNum;              //label to show how many letters where wrong, number
    private JLabel lblResponse;                     //label to show the calculated response. description
    private JLabel lblResponseNum;                  //label to show the calculated response. number
    private JButton btnChangeResponse;              //button to change response;
    private JPanel pnlFinished;                     //panel showing when user finished learning
    private JButton btnMenu;                        //Button to get back to the menu
    private JComboBox<String> combo;                //combo to change the response

    private ResourceBundle resource;                //ResourceBundle with all Strings for the GUI
    private String correctAnswer;                   //representing what the currently correct answer would be
    private int response;                           //representing what the last response was (response = how good the answer was)

    /**
     * Konstruktor. Initialisiert alle Elemente und ordnet diese an.
     * @param imprend  Imprend als Schnittstelle um z.B. Einstellungen zu erhalten
     */
    public JWrittenCardPanel(final Imprend imprend) {
        resource = imprend.settings.getResourceBundle();
        correctAnswer = "";

        pnlYourAnswer = new JPanel();
        lblYourAnswer = new ArrayList<>();
        pnlCorrectAnswer = new JPanel();
        lblCorrectAnswer = new ArrayList<>();

        pnlRunning = new JPanel(new GridBagLayout());
        lblGrp1 = new JLabel();
        lblGrp2 = new JLabel();
        lblSynonyms = new JLabel(resource.getString("synonyms")+": ");
        tFieldGrp2 = new JTextField(25);
        btnFinished = new JButton(resource.getString("finished"));
        lblNotAnswer = new JLabel();
        lblDescribeNotAnswer = new JLabel(resource.getString("notTheAnswer") + ": ");

        pnlResult = new JPanel();
        lblGrp2Res = new JLabel();
        btnNextCard = new JButton(resource.getString("nextCard"));
        lblLettersWrong = new JLabel(resource.getString("lettersWrong") + ": ");
        lblLettersWrongNum = new JLabel();
        lblResponse = new JLabel(" --> " + resource.getString("response") + ": ");
        lblResponseNum = new JLabel();
        btnChangeResponse = new JButton(resource.getString("changeResponse"));
        lblCorrectAnswDes = new JLabel("richtige Antwort");
        lblYourAnswDes = new JLabel("Deine Antwort");
        combo = new JComboBox();
        combo.addItem(resource.getString("Resp0"));
        combo.addItem(resource.getString("Resp1"));
        combo.addItem(resource.getString("Resp2"));
        combo.addItem(resource.getString("Resp3"));
        combo.addItem(resource.getString("Resp4"));
        combo.setVisible(false);
        combo.addItemListener(this);
        lblYourAnswDes.setLabelFor(pnlYourAnswer);

        //pnlRunning
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        pnlRunning.add(lblGrp1, c);

        c.gridx = 1;
        c.gridy = 1;
        pnlRunning.add(lblSynonyms, c);

        c.gridx = 2;
        c.gridy = 1;
        pnlRunning.add(lblGrp2, c);

        c.gridx = 3;
        c.gridy = 1;
        pnlRunning.add(lblDescribeNotAnswer, c);

        c.gridx = 4;
        c.gridy = 1;
        pnlRunning.add(lblNotAnswer, c);

        c.gridx = 0;
        c.gridy = 2;
        pnlRunning.add(tFieldGrp2, c);

        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 3;
        pnlRunning.add(btnFinished, c);

        //pnlResult
        pnlResult.setLayout(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        c2.gridx = 0;
        c2.gridy = 1;
        pnlResult.add(lblYourAnswDes, c2);

        c2.gridx = 1;
        c2.gridy = 1;
        pnlResult.add(pnlYourAnswer, c2);

        c2.gridx = 2;
        c2.gridy = 1;
        pnlResult.add(lblGrp2Res, c2);

        c2.gridx = 0;
        c2.gridy = 2;
        pnlResult.add(lblCorrectAnswDes, c2);

        c2.gridx = 1;
        c2.gridy = 2;
        pnlResult.add(pnlCorrectAnswer, c2);

        c2.gridx = 0;
        c2.gridy = 3;
        pnlResult.add(lblLettersWrong, c2);

        c2.gridx = 1;
        c2.gridy = 3;
        pnlResult.add(lblLettersWrongNum, c2);

        c2.gridx = 2;
        c2.gridy = 3;
        pnlResult.add(lblResponse, c2);

        c2.gridx = 3;
        c2.gridy = 3;
        pnlResult.add(lblResponseNum, c2);

        c2.gridx = 3;
        c2.gridy = 3;
        pnlResult.add(combo, c2);

        c2.gridx = 4;
        c2.gridy = 3;
        pnlResult.add(btnChangeResponse, c2);

        c2.gridx = 0;
        c2.gridy = 4;
        c2.gridwidth = 5;
        pnlResult.add(btnNextCard, c2);

        pnlFinished = new JPanel();
        JLabel lblFinish = new JLabel(resource.getString("StackFinished"));
        btnMenu = new JButton(resource.getString("backToMenu"));

        pnlFinished.add(lblFinish);
        pnlFinished.add(btnMenu);

        cd = new CardLayout();
        setLayout(cd);
        add(pnlRunning, "pnlRunning");
        add(pnlFinished, "pnlFinished");
        add(pnlResult, "pnlResult");
        cd.show(this, "pnlRunning");

        //ActionListeners
        ActionListener goFinished = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                showAnswer();
            }
        };

        ActionListener goMenu = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                imprend.switchPanel(imprend.strPnlMenu);
            }
        };

        ActionListener goNextCard = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //send respond for the last card
                questMeth.setResponse(response);
                //show next card
                nextCard();
            }
        };

        ActionListener goChangeResponse = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                combo.setSelectedIndex(response);
                combo.setVisible(true);
                lblResponseNum.setVisible(false);
            }
        };

        btnFinished.addActionListener(goFinished);
        btnMenu.addActionListener(goMenu);
        btnNextCard.addActionListener(goNextCard);
        btnChangeResponse.addActionListener(goChangeResponse);

        loadKeyBindings(imprend);
    }

    private void nextCard() {
        //load next card
        justStarted = false;
        cd.show(this, "pnlRunning");
        tFieldGrp2.setText("");

        ArrayList<String> card = questMeth.getNextCard();
        //checking if the stack is finished
        while(card.get(0).equals("ERROR:Skip")){
            questMeth.setResponse(-1);
            card = questMeth.getNextCard();
        }
        if(card.size() == 1 && card.get(0).equals("ERROR:lastCard")) {
            //stack is over
            questMeth.stackFinished();
            cd.show(this, "pnlFinished");
            return;
        }
        lblGrp1.setText(card.get(0));
        //add Synonyms
        lblGrp2.setVisible(false);
        lblGrp2Res.setVisible(false);
        lblSynonyms.setVisible(false);
        if(!card.get(2).equals("")) {
            lblGrp2.setText(card.get(2));
            lblGrp2Res.setText(card.get(2));
            //only displaying those, if there are synonyms
            lblGrp2.setVisible(true);
            lblGrp2Res.setVisible(true);
            lblSynonyms.setVisible(true);
        }
        //add those synonym to the answer
        lblDescribeNotAnswer.setVisible(false);
        lblNotAnswer.setVisible(false);
        if(!card.get(3).equals("")) {
            lblNotAnswer.setText(card.get(3));
            //only displaying those, if there are synonyms to the answer
            lblDescribeNotAnswer.setVisible(true);
            lblNotAnswer.setVisible(true);
        }

        correctAnswer = card.get(1);
        tFieldGrp2.requestFocusInWindow();

    }

    private void showAnswer() {
        response = getResponse();
        //shows the correct answer
        cd.show(this, "pnlResult");
    }

    private int getResponse() {
        //takes the tField with the answer and the correctAnswer and compares them to find out how correct the answer was. If tField is:
        //empty = Resp0 (BlackOut)
        //word wrong = Resp1(Wrong answer)
        //only one letter wrong (spelling mistake) = Resp2 (correct answer, but with some difficulties to remember)
        //everything correct = Resp3(correct answer, good able to remember)
        //Resp4(correct answer, instant response) is not possible to achive.

        int lettersWrong = 0;
        String answer = tFieldGrp2.getText();
        //remove all spaces at the end
        for(int i = answer.length()-1; i >= 0; i--) {
            if(answer.charAt(i) == ' ') {
                answer = answer.substring(0, i);
            } else {
                //Exit loop, only searching for spaces at the end
                i = 0;
            }
        }
        boolean answerShorter = false;
        ArrayList<Integer> posWrong = new ArrayList<>();
        if(answer.equals("")) {
            //Resp0
             lettersWrong = -1;
            for(int i = 0; i < correctAnswer.length(); i++) {
                posWrong.add(i);
            }
            answerShorter = true;
        } else if(answer.equals(correctAnswer)) {
            //Resp3
            lettersWrong = 0;
        } else {
            //find out how many letters are wrong
            if(answer.length() == correctAnswer.length()) {
                for(int i = 0; i < answer.length(); i++) {
                    if(answer.charAt(i) != correctAnswer.charAt(i)) {
                        posWrong.add(i);
                        lettersWrong++;
                    }
                }
            } else {
                String strLonger = "";
                String strShorter = "";
                if(answer.length() > correctAnswer.length()) {
                    //answer is longer
                    strLonger = answer;
                    strShorter = correctAnswer;
                } else {
                    //correctAnswer is longer
                    strLonger = correctAnswer;
                    strShorter = answer;
                    answerShorter = true;
                }
                int difference = 0;
                for(int i = 0; i < strShorter.length() && i+difference < strLonger.length(); i++) {
                    while(difference+i < strLonger.length() && strLonger.charAt(i+difference) != strShorter.charAt(i)) {
                        posWrong.add(i+difference);
                        difference++;
                        lettersWrong++;
                    }
                }
                if(strShorter.length()+difference < strLonger.length()) {
                    for(int i = strShorter.length()+difference; i < strLonger.length(); i++) {
                        lettersWrong++;
                        posWrong.add(i);
                    }
                }
            }
        }


        //Highlight the errors with red, make the whole answer given green, when it was correct
        //First delet what was before there
        pnlCorrectAnswer.removeAll();
        pnlYourAnswer.removeAll();
        for(int i = lblCorrectAnswer.size(); i > 0; i--) {
            lblCorrectAnswer.remove(0);
        }
        for(int i = lblYourAnswer.size(); i > 0; i--) {
            lblYourAnswer.remove(0);
        }

        for(int i = 0; i < correctAnswer.length(); i++) {
            lblCorrectAnswer.add(new JLabel(String.valueOf(correctAnswer.charAt(i))));
            pnlCorrectAnswer.add(lblCorrectAnswer.get(i));
        }
        for(int i = 0; i < answer.length(); i++) {
            lblYourAnswer.add(new JLabel(String.valueOf(answer.charAt(i))));
            pnlYourAnswer.add(lblYourAnswer.get(i));
        }

        if(posWrong.size() == 0) {
        //answer was correct
            for(int i = 0; i < lblYourAnswer.size(); i++) {
                lblYourAnswer.get(i).setForeground(Color.green);
            }
        } else {
            if(answerShorter) {
                for(int i = 0; i < lblCorrectAnswer.size(); i++) {
                    if(posWrong.contains(i)) {
                        lblCorrectAnswer.get(i).setForeground(Color.red);
                    }
                }
            } else {
                for(int i = 0; i < answer.length(); i++) {
                    if (posWrong.contains(i)) {
                        lblYourAnswer.get(i).setForeground(Color.red);
                    }
                }
            }
        }

        int response = 0;
        if(lettersWrong == -1) {
            response =  0;
        } else if(lettersWrong == 0) {
            response = 3;
        } else if(lettersWrong == 1) {
            response = 2;
        } else {
            response = 1;
        }

        lblResponseNum.setText(resource.getString("Resp"+response));
        lblLettersWrongNum.setText(String.valueOf(lettersWrong));

        this.response = response;
        return response;
    }

    /**
     * Startet das Lernen mit der gegebenen Abfragemethode. Die Abfragemethode beinhaltet auch die Karten (bzw. InformationGroup), die abgefragt werden sollen
     * @param questMeth  Abfragemethode, mit der abgefragt werden soll.
     */
    public void initNewLearning(QuestionMethod questMeth) {
        //Performs actions, which are needed to first time, a new Learning session is started
        //This can't be in the constructor, because the constructor is being called only once, when the program starts
        //This is being called everytime the user wants to learn a cardstack.
        this.questMeth = questMeth;
        nextCard();
        justStarted = true;
        tFieldGrp2.requestFocusInWindow();
    }


    /**
     * Lädt die Tastaturbelegung für das Panel.
     * @param imprend  Imprend als Schnittstelle um z.B. Einstellungen zu erhalten
     */
    public void loadKeyBindings(Imprend imprend) {
        //Add KeyBindings
        Action actionNext = new ActionNext();
        int mapName = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap imap = getInputMap(mapName);

        String[] keyStrokes = imprend.settings.getKeysForJCardBtns();
        KeyStroke[] strokes = new KeyStroke[6];
        for(int i = 0; i < strokes.length; i++) {
            if(keyStrokes[i].length() == 1) {
                //the keyStroke should be converted into a char. It is a letter and not a key like "ENTER"
                strokes[i] = KeyStroke.getKeyStroke(keyStrokes[i].charAt(0));
            } else {
                //the String stays a string
                strokes[i] = KeyStroke.getKeyStroke(keyStrokes[i]);
            }
        }
        imap.put(strokes[5], "enter");
        ActionMap amap = getActionMap();
        amap.put("enter", actionNext);
    }

    /**
     * Überschreibt die Methode des {@link JNavPanel}s. Sorgt dafür, dass die letzt Karte nochmals angezeigt und gelernt werden kann (Macht das letzt Lernen rückgängig).
     * @param imprend  Imprend als Schnittstelle um z.B. Einstellungen zu erhalten
     * @return immer false, da es selber die Rückgängig-Aktion ausführt.
     */
    @Override
    public boolean back(Imprend imprend) {
        if(justStarted) {
            return true;
        }
        if(!redo) {
            questMeth.redoLastCard();
            nextCard();
            redo = true;
            return false;
        }
        return false;
    }

    /**
     * Überschreibt Methode der Vaterklasse {@link JNavPanel}. Sorgt dafür das Lernfortschritt gespeichert wird, wenn das Panel geschlossen wird.
     * @param imprend   Imprend als Schnittstelle um z.B. Einstellungen zu erhalten
     */
    @Override
    public void cleanUp(Imprend imprend) {
        if(pnlResult.isVisible()) { //save the response, when the user exit at the panel, when the result is showing and he hasn't order the next card
            questMeth.setResponse(response);
        }
        questMeth.stackFinished();
    }

    /**
     * Implementiert den ItemListener, der die JComboBox überwacht, bei der man die Rückmeldung auswählen kann.
     * Er setzt die Rückmeldung (Variable response und entsprechendes Label lblResponseNum) neu, und macht die JComboBox wieder unsichtbar.
     * Die JComboBox wird nur durch Klicken eines Knopfs sichtbar.
     * @param itemEvent
     */
    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        //find out which respond it is, according to the content of the selected item (String)
        response = combo.getSelectedIndex();
        lblResponseNum.setText(String.valueOf(combo.getSelectedItem()));
        combo.setVisible(false);
        lblResponseNum.setVisible(true);
    }

    private class ActionNext extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(pnlResult.isVisible()) {
                //next card
                btnNextCard.doClick();
            } else if (pnlFinished.isVisible()) {
                btnMenu.doClick();
            } else {
                //show answer
                btnFinished.doClick();
            }
        }
    }
}
