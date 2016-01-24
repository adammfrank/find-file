import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBList;
import com.intellij.openapi.actionSystem.DataKeys;


import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

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
    }

    Action listFiles = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ApplicationManager.getApplication().invokeLater(new Runnable() {
                public void run() {
                    //fileList.setEmptyText("");
                    VirtualFile currentPathFile = LocalFileSystem.getInstance().findFileByPath(getText());
                    if(currentPathFile != null) {
                        VirtualFile[] children = currentPathFile.getChildren();
                        if(children.length != 0) {
                            if(children.length == 1) {
                                setText(children[0].getPath());
                            }
                            else{
                                String[] listData = new String[children.length];
                                DefaultListModel listModel = (DefaultListModel) parentPanel.getFileList().getModel();
                                listModel.removeAllElements();
                                for(int i = 0; i < children.length; ++i) {
                                    listModel.addElement(children[i].getPath());
                                }
                                //parentPanel.add(PathTextField.this, 1);

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
}
