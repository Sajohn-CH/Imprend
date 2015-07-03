package gui;

import questionMethods.QMethCards;
import questionMethods.QuestionMethod;
import utilities.Imprend;
import utilities.Save;
import utilities.UTF8Control;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Created by samuel on 01.07.15.
 * A Panel for learning simple cards.
 */
public class JCardPanel extends JNavPanel implements MouseListener, KeyListener{

    QuestionMethod questMeth;

    JLabel lblQuest;
    JLabel lblAnsw;
    JPanel pnlFinish;
    JPanel pnlCenter;
    JPanel pnlResponse;

    JButton btnMenu;
    JButton btnResp0, btnResp1, btnResp2, btnResp3, btnResp4;
    public JCardPanel(final Imprend imprend) {
        final ResourceBundle card = ResourceBundle.getBundle(imprend.settings.getResourceBundles()+".JCardPanelBundle", imprend.settings.getLocale(), new UTF8Control());

        pnlCenter = new JPanel();
        lblQuest = new JLabel();
        lblAnsw = new JLabel();

        pnlFinish = new JPanel(new GridLayout(2,1));
        JLabel lblFinish = new JLabel("Finished");  //need to transfer ro ResourceBundle; Label not used at the moment
        btnMenu = new JButton("BackTOMeu");  //need to transfer ro ResourceBundle; Button not used at the moment (only as btnMenu.doClick()


        pnlResponse = new JPanel();
        btnResp0 = new JButton(card.getString("Resp0"));
        btnResp1 = new JButton(card.getString("Resp1"));
        btnResp2 = new JButton(card.getString("Resp2"));
        btnResp3 = new JButton(card.getString("Resp3"));
        btnResp4 = new JButton(card.getString("Resp4"));;

        lblAnsw.setVisible(false);

        pnlFinish.add(lblFinish);
        pnlFinish.add(btnMenu);

        pnlCenter.setLayout(new GridLayout(2, 1));
        pnlCenter.add(lblQuest);
        pnlCenter.add(lblAnsw);

        pnlResponse.add(btnResp0);
        pnlResponse.add(btnResp1);
        pnlResponse.add(btnResp2);
        pnlResponse.add(btnResp3);
        pnlResponse.add(btnResp4);


        setLayout(new BorderLayout());
        add(pnlCenter, BorderLayout.CENTER);
        add(pnlResponse, BorderLayout.PAGE_END);

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
                questMeth.setResponse(0);
                nextCard();
            }
        };

        ActionListener goResp1 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                questMeth.setResponse(1);
                nextCard();
            }
        };

        ActionListener goResp2 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                questMeth.setResponse(2);
                nextCard();
            }
        };

        ActionListener goResp3 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                questMeth.setResponse(3);
                nextCard();
            }
        };

        ActionListener goResp4 = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                questMeth.setResponse(4);
                nextCard();
            }
        };

        btnMenu.addActionListener(goMenu);
        btnResp0.addActionListener(goResp0);
        btnResp1.addActionListener(goResp1);
        btnResp2.addActionListener(goResp2);
        btnResp3.addActionListener(goResp3);
        btnResp4.addActionListener(goResp4);

        addMouseListener(this);
        //addKeyListener(this);

        //Add KeyBindings
        //doesn't work at the moment
        Action action0 = new Action0();
        Action action1 = new Action1();
        Action action2 = new Action2();
        Action action3 = new Action3();
        Action action4 = new Action4();
        Action actionEnter = new EnterAction();
        int mapName = JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT;
        InputMap imap = getInputMap(mapName);
        KeyStroke ZeroKey = KeyStroke.getKeyStroke('0');
        KeyStroke OneKey = KeyStroke.getKeyStroke('1');
        KeyStroke TwoKey = KeyStroke.getKeyStroke('2');
        KeyStroke ThreeKey = KeyStroke.getKeyStroke('3');
        KeyStroke FourKey = KeyStroke.getKeyStroke('4');
        KeyStroke EnterKey = KeyStroke.getKeyStroke("ENTER");
        imap.put(ZeroKey, "0");
        imap.put(OneKey, "1");
        imap.put(TwoKey, "2");
        imap.put(ThreeKey, "3");
        imap.put(FourKey, "4");
        imap.put(EnterKey, "enter");
        ActionMap amap = getActionMap();
        amap.put("0", action0);
        amap.put("1", action1);
        amap.put("2", action2);
        amap.put("3", action3);
        amap.put("4", action4);
        amap.put("enter", actionEnter);
    }

    public void initNewLearning(QuestionMethod questMeth) {
        this.questMeth = questMeth;
        nextCard();

    }

    private void showAnswer() {
        lblAnsw.setVisible(true);
    }

    private void nextCard() {
        ArrayList<String> card = questMeth.getNextCard();
        //checking if the stack if finished
        if(card.size() == 1 && card.get(0).equals("ERROR:lastCard")) {
            //stack is over
           /* System.out.println("Stack finished");
            remove(pnlCenter);
            add(pnlFinish);
            */
            questMeth.stackFinished();
            btnMenu.doClick();
            return;

        }
        //first element of the card ArrayList is the question
        lblQuest.setText(card.get(0));
        //secon element is the answer
        lblAnsw.setText(card.get(1));
        lblAnsw.setVisible(false);

    }

    //Method from the parentclass JNavPanel
    @Override
    public void back(Imprend imprend) {

    }

    //Method from the KeyListener
    @Override
    public void keyTyped(KeyEvent keyEvent) {
        System.out.println(keyEvent.getKeyChar());
       /* switch (keyEvent.getKeyChar()) {
            case '0':
                btnResp0.doClick();
                break;
            case '1':
                btnResp1.doClick();
                break;
            case '2':
                btnResp2.doClick();
                break;
            case '3':
                btnResp3.doClick();
                break;
            case '4':
                btnResp4.doClick();
                break;
        }*/
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        System.out.println("pressed");

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        System.out.println("released");
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
