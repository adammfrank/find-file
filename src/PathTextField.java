import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBList;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.regex.*;

/**
 * Created by adam on 1/13/16.
 */
public class PathTextField extends JTextField{
    private MainPanel parentPanel;
    public PathTextField(int width, MainPanel parentPanel) {
        super(width);
        this.parentPanel = parentPanel;
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0), "tab");
        this.getActionMap().put("tab", listFiles);
        this.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
        this.getActionMap().put("enter", loadFile);
    }

    Action listFiles = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ApplicationManager.getApplication().invokeLater(new Runnable() {
                public void run() {
                    String directory = getText().substring(0, getText().lastIndexOf("/"));
                    String filePrefix = getText().substring(getText().lastIndexOf("/"));
                    VirtualFile currentPathFile = LocalFileSystem.getInstance().findFileByPath(directory);
                    if(currentPathFile != null) {
                        if(currentPathFile.isDirectory()) {
                            VirtualFile[] children = currentPathFile.getChildren();
                            if(children.length == 1) {
                                setText(children[0].getPath());
                            }
                            else {
                                DefaultListModel listModel = (DefaultListModel) parentPanel.getFileList().getModel();
                                listModel.removeAllElements();
                                for(int i = 0; i < children.length; ++i) {
                                    Pattern childPattern = Pattern.compile(directory + filePrefix + ".*");
                                    if(childPattern.matcher(children[i].getPath()).matches()) {
                                        listModel.addElement(children[i].getPath());
                                    }

                                }
                                parentPanel.remove(1);
                                parentPanel.add(new JBList(listModel));
                                parentPanel.setMinimumSize(new Dimension(400, children.length * 20));
                                parentPanel.revalidate();
                                parentPanel.repaint();

                            }
                        }
                    }
                    else {
                        parentPanel.getFileList().setEmptyText("");
                    }
                }
            });
        }
    };

    Action loadFile = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String path = getText();
            VirtualFile file = LocalFileSystem.getInstance().findFileByPath(path);
            if(file != null) {
                OpenFileDescriptor ofd = new OpenFileDescriptor(parentPanel.getProject(), file);
                ofd.navigate(false);
            }
        }
    };
}
