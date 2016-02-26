import com.intellij.ui.components.JBList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by adam on 2/24/16.
 */
public class FileList extends JBList implements ListSelectionListener, ActionListener, KeyListener {

    public FileList(ListModel model) {
        super(model);
        this.addListSelectionListener(this);
        valueChanged(new ListSelectionEvent(this, 0, 1, true));
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("ActionListener!");
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        System.out.println("SelectionListener!");
    }

    @Override
    public void keyTyped(KeyEvent e) {
        System.out.println("key typed " + e.getKeyChar());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("key pressed " + e.getKeyChar());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        System.out.println("key released " + e.getKeyChar());
    }
}
