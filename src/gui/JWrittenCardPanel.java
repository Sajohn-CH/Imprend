package gui;

import informationManagement.Question;
import org.omg.CORBA.IMP_LIMIT;
import questionMethods.QuestionMethod;
import sun.java2d.opengl.GLXSurfaceData;
import utilities.Imprend;
import utilities.UTF8Control;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by samuel on 06.10.15.
 */
public class JWrittenCardPanel extends JNavPanel implements ItemListener {


    private boolean justStarted;  //Indicates wheter this is the first card or not, so the back-Button would lead then back to the menu
    private boolean redo;       //Indicates whether the back-Button just has been pressed, so it can't be pressed two times in a row.
    private QuestionMethod questMeth;

    private CardLayout cd;
    private JLabel lblGrp1;     //the "question"
    private JLabel lblGrp2;     //place to rule out some synonyms. All synonyms staying here can not be the answer
    private JTextField tFieldGrp2;      //place to write the answer
    private JButton btnFinished;        //pressed the finished writting the answer
    private JButton btnNextCard;        //pressed for showing the next Card
    private JPanel pnlCorrectAnswer;                //panel to show the correct answer, used several labels, to be able to color them each
    private ArrayList<JLabel> lblCorrectAnswer;     //labels to show the correct answer
    private JPanel pnlYourAnswer;                   //panel to show the users answer, used several labels, to be able to color them each
    private ArrayList<JLabel> lblYourAnswer;        //labels to show the answer of the user
    private JPanel pnlResult;                       //panel to show the result, including: Wrong letters, which respond this will result in, and the ability to change this.
    private JLabel lblLettersWrong;                  //label to show how many letters where wrong, description
    private JLabel lblLettersWrongNum;              //label to show how many letters where wrong, number
    private JLabel lblResponse;                     //label to show the calculated response. description
    private JLabel lblResponseNum;                     //label to show the calculated response. number
    private JButton btnChangeResponse;              //button to change response;
    private JPanel pnlFinished;                     //panel shwoing when user finished learning
    private JButton btnMenu;                        //Button to get back to the menu
    private JComboBox<String> combo;

    private ResourceBundle resource;
    private String correctAnswer;       //representing what the currently correct answer would be
    private int response;               //representing what the last response was (response = how good the answer was)
    public JWrittenCardPanel(final Imprend imprend) {
        resource = ResourceBundle.getBundle(imprend.settings.getResourceBundles() + ".JCardPanelBundle", imprend.settings.getLocale(), new UTF8Control());
        correctAnswer = "";

        pnlYourAnswer = new JPanel();
        lblYourAnswer = new ArrayList<>();
        pnlCorrectAnswer = new JPanel();
        lblCorrectAnswer = new ArrayList<>();

        JPanel pnlRunning = new JPanel(new GridBagLayout());
        lblGrp1 = new JLabel();
        lblGrp2 = new JLabel();
        tFieldGrp2 = new JTextField(25);
        btnFinished = new JButton(resource.getString("finished"));
        btnNextCard = new JButton(resource.getString("nextCard"));

        pnlResult = new JPanel();
        lblLettersWrong = new JLabel(resource.getString("lettersWrong") + ": ");
        lblLettersWrongNum = new JLabel();
        lblResponse = new JLabel(" --> " + resource.getString("response") + ": ");
        lblResponseNum = new JLabel();
        btnChangeResponse = new JButton(resource.getString("changeResponse"));
        combo = new JComboBox();
        combo.addItem(resource.getString("Resp0"));
        combo.addItem(resource.getString("Resp1"));
        combo.addItem(resource.getString("Resp2"));
        combo.addItem(resource.getString("Resp3"));
        combo.addItem(resource.getString("Resp4"));
        combo.setVisible(false);
        combo.addItemListener(this);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        pnlRunning.add(lblGrp1, c);

        c.gridx = 0;
        c.gridy = 2;
        pnlRunning.add(tFieldGrp2, c);

        c.gridx = 1;
        c.gridy = 2;
        pnlRunning.add(lblGrp2, c);

        c.gridx = 0;
        c.gridy = 4;
        c.gridwidth = 3;
        pnlRunning.add(btnFinished, c);

        pnlResult.setLayout(new GridBagLayout());
        GridBagConstraints c2 = new GridBagConstraints();
        c2.gridx = 0;
        c2.gridy = 1;
        pnlResult.add(pnlYourAnswer, c2);

        c2.gridx = 1;
        c2.gridy = 1;
        pnlResult.add(lblGrp2, c2);

        c2.gridx = 0;
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
        /*btnNextCard.setVisible(false);
        btnFinished.setVisible(true);
        tFieldGrp2.setVisible(true);
        pnlYourAnswer.setVisible(false);
        pnlCorrectAnswer.setVisible(false);
        pnlResult.setVisible(false);*/
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
        if(card.size() > 2) {
            lblGrp2.setText(card.get(2));
        }
        correctAnswer = card.get(1);
        tFieldGrp2.requestFocusInWindow();

    }

    private void showAnswer() {
        response = getResponse();
        //shows the correct answer
        /*btnFinished.setVisible(false);
        btnNextCard.setVisible(true);
        tFieldGrp2.setVisible(false);
        pnlCorrectAnswer.setVisible(true);
        pnlYourAnswer.setVisible(true);
        pnlResult.setVisible(true);*/
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
                System.out.println();
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

    public void initNewLearning(QuestionMethod questMeth) {
        //Performs actions, which are needed to first time, a new Learning session is started
        //This can't be in the constructor, because the constructor is being called only once, when the program starts
        //This is being called everytime the user wants to learn a cardstack.
        this.questMeth = questMeth;
        nextCard();
        justStarted = true;
        tFieldGrp2.requestFocusInWindow();
    }

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

    @Override
    public void itemStateChanged(ItemEvent itemEvent) {
        //find out wich respond it is, according to the content of the selected item (String)
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
