import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.JBPopupListener;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.ui.components.JBList;


import javax.swing.*;



/**
 * Created by adam on 1/1/16.
 */
public class PopupAction extends AnAction {
    MainPanel panel;
    FileList fileList;
    PathTextField pathTexField;
    VirtualFile currentFile;

    @Override
    public void actionPerformed(AnActionEvent e) {
        JPanel outerPanel = new JPanel();
        JBPopup popup = JBPopupFactory.getInstance().createComponentPopupBuilder(outerPanel, pathTexField).createPopup();

        panel = new MainPanel(DataKeys.PROJECT.getData(e.getDataContext()), popup);
        currentFile = DataKeys.VIRTUAL_FILE.getData(e.getDataContext());
        pathTexField = panel.getPathTextField();
        fileList = panel.getFileList();
        pathTexField.setText(currentFile.getPath());
        outerPanel.add(panel);




        popup.showInBestPositionFor(e.getData(PlatformDataKeys.EDITOR));


    }
}

