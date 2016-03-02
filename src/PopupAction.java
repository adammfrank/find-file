import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.vfs.VirtualFile;
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

