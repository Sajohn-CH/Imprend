package gui;

import questionMethods.QuestionMethod;
import utilities.Imprend;
import utilities.UTF8Control;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by samuel on 01.07.15.
 * A Panel for learning simple cards.
 */
public class JCardPanel extends JNavPanel implements MouseListener{

    private QuestionMethod questMeth;

    private CardLayout cd;
    private JLabel lblQuest;
    private JLabel lblAnsw;
    private JLabel lblSynonyms;
    private JLabel lblDescribeSynonyms; //label displaying the word synonyms, to labe/describe  what the lblSynonyms is showing
    private JLabel lblNotAnswer;        //display all "wrong answers", meaning synonyms of the asked answer.
    private JLabel lblDescribeNotAnswer;        //label describing the lblNot answer.
    private JPanel pnlFinish;
    private JPanel pnlCenter;
    private JPanel pnlResponse;

    private JButton btnMenu;
    private JButton btnResp0, btnResp1, btnResp2, btnResp3, btnResp4;

    private boolean redo;       //Indicates whether the back-Button just has been pressed, so it can't be pressed two times in a row.
    private boolean justStarted;    //Indicates wheter this is the first card or not, so the back-Button would lead then back to the menu

    private ResourceBundle resource;
    public JCardPanel(final Imprend imprend) {
        resource = ResourceBundle.getBundle(imprend.settings.getResourceBundles()+".JCardPanelBundle", imprend.settings.getLocale(), new UTF8Control());
        redo = false;

        pnlCenter = new JPanel();
        lblQuest = new JLabel();
        lblAnsw = new JLabel();
        lblSynonyms = new JLabel();
        lblDescribeSynonyms = new JLabel("   " + resource.getString("synonyms") + ": ");
        lblNotAnswer = new JLabel();
        lblDescribeNotAnswer = new JLabel("   " + resource.getString("notTheAnswer") + ": ");

        pnlFinish = new JPanel();
        JLabel lblFinish = new JLabel(resource.getString("StackFinished"));
        btnMenu = new JButton(resource.getString("backToMenu"));


        pnlResponse = new JPanel();
        btnResp0 = new JButton(resource.getString("Resp0"));
        btnResp1 = new JButton(resource.getString("Resp1"));
        btnResp2 = new JButton(resource.getString("Resp2"));
        btnResp3 = new JButton(resource.getString("Resp3"));
        btnResp4 = new JButton(resource.getString("Resp4"));;

        lblAnsw.setVisible(false);

        pnlFinish.add(lblFinish);
        pnlFinish.add(btnMenu);

        pnlCenter.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 0, 30, 5);
        pnlCenter.add(lblQuest, c);

        c.gridx = 1;
        c.gridy = 1;
        pnlCenter.add(lblDescribeSynonyms, c);

        c.gridx = 2;
        c.gridy = 1;
        pnlCenter.add(lblSynonyms, c);

        c.gridx = 3;
        c.gridy = 1;
        pnlCenter.add(lblDescribeNotAnswer, c);

        c.gridx = 4;
        c.gridy = 1;
        pnlCenter.add(lblNotAnswer, c);

        c.gridx = 0;
        c.gridy = 2;
        pnlCenter.add(lblAnsw, c);

        pnlResponse.add(btnResp0);
        pnlResponse.add(btnResp1);
        pnlResponse.add(btnResp2);
        pnlResponse.add(btnResp3);
        pnlResponse.add(btnResp4);

        JPanel pnlRunning = new JPanel();
        pnlRunning.setLayout(new BorderLayout());
        pnlRunning.add(pnlCenter, BorderLayout.CENTER);
        pnlRunning.add(pnlResponse, BorderLayout.PAGE_END);


        cd = new CardLayout();
        setLayout(cd);
        add(pnlRunning, "pnlRunning");
        add(pnlFinish, "pnlFinish");
        cd.show(this, "pnlRunning");

        //ActionListeners
        ActionListener goMenu = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                imprend.switchPanel(imprend.strPnlMenu);
            }
        };

        ActionListener goResp0 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (lblAnsw.isVisible()) {
                    questMeth.setResponse(0);
                    redo = false;
                    nextCard();
                }

            }
        };

        ActionListener goResp1 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (lblAnsw.isVisible()) {
                    questMeth.setResponse(1);
                    redo = false;
                    nextCard();
                }
            }
        };

        ActionListener goResp2 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (lblAnsw.isVisible()) {
                    questMeth.setResponse(2);
                    redo = false;
                    nextCard();
                }
            }
        };

        ActionListener goResp3 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (lblAnsw.isVisible()) {
                    questMeth.setResponse(3);
                    redo = false;
                    nextCard();
                }
            }
        };

        ActionListener goResp4 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (lblAnsw.isVisible()) {
                    questMeth.setResponse(4);
                    redo = false;
                    nextCard();
                }
            }
        };

        btnMenu.addActionListener(goMenu);
        btnResp0.addActionListener(goResp0);
        btnResp1.addActionListener(goResp1);
        btnResp2.addActionListener(goResp2);
        btnResp3.addActionListener(goResp3);
        btnResp4.addActionListener(goResp4);

        btnResp0.setVisible(false);
        btnResp1.setVisible(false);
        btnResp2.setVisible(false);
        btnResp3.setVisible(false);
        btnResp4.setVisible(false);

        addMouseListener(this);
        cd.show(this, "pnlRunning");

        loadKeyBindings(imprend);
    }

    public void initNewLearning(QuestionMethod questMeth) {
        //Performs actions, which are needed to first time, a new Learning session is started
        //This can't be in the constructor, because the constructor is being called only once, when the program starts
        //This is being called everytime the user wants to learn a cardstack.
        this.questMeth = questMeth;
        nextCard();
        justStarted = true;

    }

    private void showAnswer() {
        lblAnsw.setVisible(true);
        btnResp0.setVisible(true);
        btnResp1.setVisible(true);
        btnResp2.setVisible(true);
        btnResp3.setVisible(true);
        btnResp4.setVisible(true);
    }

    private void nextCard() {
        justStarted = false;
        cd.show(this, "pnlRunning");
        ArrayList<String> card = questMeth.getNextCard();
        //checking if the stack is finished
        while(card.get(0).equals("ERROR:Skip")){
            questMeth.setResponse(-1);
            card = questMeth.getNextCard();
        }
        if(card.size() == 1 && card.get(0).equals("ERROR:lastCard")) {
            //stack is over
            questMeth.stackFinished();
            cd.show(this, "pnlFinish");
            return;
        }
        //first element of the card ArrayList is the question
        lblQuest.setText(card.get(0));
        //Add Synonyms to lblSynonyms
        lblDescribeSynonyms.setVisible(false);
        lblSynonyms.setVisible(false);
        if(!card.get(2).equals("")) {
            lblSynonyms.setText(card.get(2));
            //only displaying those, if there are synonyms
            lblDescribeSynonyms.setVisible(true);
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
        //second element is the answer
        lblAnsw.setText(card.get(1));

        lblAnsw.setVisible(false);
        btnResp0.setVisible(false);
        btnResp1.setVisible(false);
        btnResp2.setVisible(false);
        btnResp3.setVisible(false);
        btnResp4.setVisible(false);

    }

    public void loadKeyBindings(Imprend imprend) {
        //Add KeyBindings
        Action action0 = new Action0();
        Action action1 = new Action1();
        Action action2 = new Action2();
        Action action3 = new Action3();
        Action action4 = new Action4();
        Action actionEnter = new EnterAction();
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
        imap.put(strokes[0], "0");
        imap.put(strokes[1], "1");
        imap.put(strokes[2], "2");
        imap.put(strokes[3], "3");
        imap.put(strokes[4], "4");
        imap.put(strokes[5], "enter");
        ActionMap amap = getActionMap();
        amap.put("0", action0);
        amap.put("1", action1);
        amap.put("2", action2);
        amap.put("3", action3);
        amap.put("4", action4);
        amap.put("enter", actionEnter);
    }

    //Method from the parentclass JNavPanel
    @Override
    public boolean back(Imprend imprend) {
        //undo the last response

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

    //Methods from the MouseListener
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        if(!lblAnsw.isVisible()) {
            //it only can show the answer if it is not already visible
            showAnswer();
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    //Methods for the Keybindings. (The Action of each key)
    private class Action0 extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            btnResp0.doClick();
        }
    }

    private class Action1  extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            btnResp1.doClick();
        }
    }

    private class Action2 extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            btnResp2.doClick();
        }
    }

    private class Action3 extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            btnResp3.doClick();
        }
    }

    private class Action4 extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            btnResp4.doClick();
        }
    }

    private class EnterAction extends AbstractAction {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(!lblAnsw.isVisible()) {
                //it only can show the answer if it is not already visible
                showAnswer();
            }
        }
    }





}
