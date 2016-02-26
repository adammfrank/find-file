import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
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
    public MainPanel(Project project) {
        super();
        DefaultListModel<String> model = new DefaultListModel<>();
//        model.addElement("first");
//        model.addElement("second");
//        model.addElement("third");
        fileList = new FileList(model);
        this.project = project;

        Set<KeyStroke> forwardKeys = new HashSet<KeyStroke>(1);
        forwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, InputEvent.CTRL_MASK));
        Set<KeyStroke> backwardKeys = new HashSet<KeyStroke>(1);
        backwardKeys.add(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, InputEvent.CTRL_MASK));
        setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys);
        setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS, backwardKeys);


        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.setPreferredSize(new Dimension(500, 500));
        scrollPane.add(fileList);
        //this.add(fileList);
        this.add(pathTextField);
        this.add(scrollPane);

//        fileList.addListSelectionListener(new ListSelectionListener() {
//            @Override
//            public void valueChanged(ListSelectionEvent e) {
//                System.out.println("Value changed");
//                String selectedFile = fileList.getSelectedValue().toString();
//                System.out.println("Selected file " + selectedFile);
//                VirtualFile destination = LocalFileSystem.getInstance().findFileByPath(pathTextField.getText()
//                        + selectedFile);
//
//                if (destination.isDirectory()) {
//                    pathTextField.setText(destination.getPath());
//                } else {
//                    OpenFileDescriptor ofd = new OpenFileDescriptor(project, destination);
//                    ofd.navigate(false);
//                }
//            }
//        });


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
