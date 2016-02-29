import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBScrollPane;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by adam on 1/13/16.
 * http://stackoverflow.com/questions/24800417/why-cant-i-get-keyevent-vk-tab-when-i-use-key-binding-for-a-jpanel
 */
public class MainPanel extends JPanel {
    private PathTextField pathTextField = new PathTextField(20, this);
    private FileList fileList;
    private JScrollPane scrollPane = new JBScrollPane();
    private Project project;
    private JBPopup popup;

    public JBPopup getPopup() {
        return popup;
    }

    public MainPanel(Project project, JBPopup popup) {
        super();
        DefaultListModel<String> model = new DefaultListModel<>();
        fileList = new FileList(model, this);
        this.project = project;
        this.popup = popup;

        Set<KeyStroke> forwardKeys = new HashSet<KeyStroke>(1);
        forwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.CTRL_MASK));
        Set<KeyStroke> backwardKeys = new HashSet<KeyStroke>(1);
        backwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, InputEvent.CTRL_MASK));
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys);
        setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardKeys);


        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setPreferredSize(new Dimension(500, 500));
        scrollPane.add(fileList);

        this.add(pathTextField);
        this.add(scrollPane);



    }

    public PathTextField getPathTextField() {
        return pathTextField;
    }

    public FileList getFileList() {
        return fileList;
    }

    public Project getProject() {
        return project;
    }
}
