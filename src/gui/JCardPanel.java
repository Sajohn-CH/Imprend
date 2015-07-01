package gui;

import InformationManagement.Question;
import QuestionMethods.QuestionMethod;
import utilities.Imprend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

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

    JButton btnMenu;
    public JCardPanel(final Imprend imprend) {

        pnlCenter = new JPanel();
        lblQuest = new JLabel();
        lblAnsw = new JLabel();

        pnlFinish = new JPanel(new GridLayout(2,1));
        JLabel lblFinish = new JLabel("Finished");  //need to transfer ro ResourceBundle
        btnMenu = new JButton("BackTOMeu");  //need to transfer ro ResourceBundle


        lblAnsw.setVisible(false);

        pnlFinish.add(lblFinish);
        pnlFinish.add(btnMenu);

        pnlCenter.setLayout(new GridLayout(2, 1));
        pnlCenter.add(lblQuest);
        pnlCenter.add(lblAnsw);

        setLayout(new BorderLayout());
        add(pnlCenter);

        addMouseListener(this);

        ActionListener goMenu = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                imprend.switchPanel(imprend.strPnlMenu);
            }
        };

        btnMenu.addActionListener(goMenu);
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

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }

    //Methods from the MouseListener
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        System.out.println("clicked");
        if(!lblAnsw.isVisible()) {
            //it only can show the answer if it is not already visible
            showAnswer();
        } else {
            nextCard();
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
}
