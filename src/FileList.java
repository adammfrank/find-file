import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileSystemView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Created by adam on 2/24/16.
 */
public class FileList extends JBList implements ListSelectionListener {
    private MainPanel parentPanel;
    public FileList(ListModel model, MainPanel parentPanel) {
        super(model);
        this.addListSelectionListener(this);
        valueChanged(new ListSelectionEvent(this, 0, 1, true));
        this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.parentPanel = parentPanel;
    }




    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(!e.getValueIsAdjusting()) {
            FileList list = (FileList) e.getSource();

            String filePath = parentPanel.getPathTextField().getText() + list.getSelectedValue();
            VirtualFile file = LocalFileSystem.getInstance().findFileByPath(filePath);
            System.out.println("SelectionListener! " + filePath);
            OpenFileDescriptor ofd = new OpenFileDescriptor(parentPanel.getProject(), file);
            ofd.navigate(false);
            parentPanel.getPopup().closeOk(null);
        }

    }

    
}
