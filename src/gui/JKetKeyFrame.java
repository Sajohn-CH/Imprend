package gui;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by samuel on 13.08.15.
 */
public class JKetKeyFrame extends JFrame implements KeyListener{

    String key;

    public JKetKeyFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(200, 100);

        key = "";

        JPanel pnl = new JPanel();
        JLabel lbl = new JLabel("pressKey");
        pnl.add(lbl);
        add(pnl);

    }

    public String getKey() {
        setVisible(true);
        while(key.equals(""));
        dispose();
        return key;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {
        key = String.valueOf(keyEvent.getKeyChar());
        System.out.println(key);
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {

    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}
