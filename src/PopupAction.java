import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopupFactory;
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
    JBList fileList;
    PathTextField pathTexField;
    VirtualFile currentFile;

    @Override
    public void actionPerformed(AnActionEvent e) {

        panel = new MainPanel(DataKeys.PROJECT.getData(e.getDataContext()));
        currentFile = DataKeys.VIRTUAL_FILE.getData(e.getDataContext());
        pathTexField = panel.getPathTextField();
        fileList = panel.getFileList();
        pathTexField.setText(currentFile.getPath());

        Editor myEditor = DataKeys.EDITOR.getData(e.getDataContext());

        JBPopupFactory.getInstance().createComponentPopupBuilder(panel, pathTexField).createPopup().showInBestPositionFor(e.getData(PlatformDataKeys.EDITOR));


//        System.out.println("file path " + DataKeys.VIRTUAL_FILE.getData(e.getDataContext()).getPath());
//        VirtualFile currentFile = LocalFileSystem.getInstance().findFileByPath("/home/adam/IdeaProjects/test-project/src/com/company");
//        System.out.println("parent path " + currentFile.getParent().getPath());
//        Project myProject = DataKeys.PROJECT.getData(e.getDataContext());
//        VirtualFile sibling = LocalFileSystem.getInstance().findFileByPath("/home/adam/IdeaProjects/test-project/src/com/company/Test.java");
//        OpenFileDescriptor ofd = new OpenFileDescriptor(myProject, sibling);
//        ofd.navigate(false);
    }
}

