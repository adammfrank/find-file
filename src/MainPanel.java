import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by adam on 1/13/16.
 * http://stackoverflow.com/questions/24800417/why-cant-i-get-keyevent-vk-tab-when-i-use-key-binding-for-a-jpanel
 */
public class MainPanel extends JPanel {
    private PathTextField pathTextField = new PathTextField(20, this);
    private JBList fileList = new JBList(new DefaultListModel());
    private Project project;
    public MainPanel(Project project) {
        super();

        this.project = project;

        Set<KeyStroke> forwardKeys = new HashSet<KeyStroke>(1);
        forwardKeys.add(KeyStroke.getKeyStroke(
                KeyEvent.VK_TAB, InputEvent.CTRL_MASK));
        Set<KeyStroke> backwardKeys = new HashSet<KeyStroke>(1);
        backwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, InputEvent.CTRL_MASK));
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys);
        setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardKeys);

        this.setLayout(new GridLayout(2,1));
        //this.setPreferredSize(new Dimension(200,200));

        JFrame frame = new JFrame();
        frame.pack();

        this.add(pathTextField);
        this.add(fileList);
    }

    public PathTextField getPathTextField() {
        return pathTextField;
    }


    public JBList getFileList() {
        return fileList;
    }

    public Project getProject() {
        return project;
    }
}
