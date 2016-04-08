import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
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

        this.setFocusable(true);
        this.grabFocus();

    }
    /**
     *
     * @param files ArrayList of file names to be checked
     * @param filePrefix The characters that matching file names need to start with
     * @return The maximum common characters that all files share with each other and filePrefix
     * eg If files are "Main.java, MainFake.java, and Sibling.java" and filePrefix is "M",
     * return is "Main".
     */
    private String matchingFiles(ArrayList<String> files, String filePrefix) {
        System.out.println("Files to be matched: " + files.toString());
        int filePrefixLen = filePrefix.length();
        Pattern prefixPattern = Pattern.compile("^" + Pattern.quote(filePrefix) + ".*");
        ArrayList<String> filesWithPrefix = new ArrayList<>();
        DefaultListModel model = new DefaultListModel<>();
        // Find all files which match filePrefix
        for(String f : files) {
            String truncF = f.substring(f.lastIndexOf("/"));

            if(prefixPattern.matcher(truncF).matches()) {
                filesWithPrefix.add(truncF);
                model.addElement(truncF);
            }
        }
        int filesWithPrefixLength = filesWithPrefix.size();

        if(filesWithPrefixLength > 0) {
            int stringLen = filesWithPrefix.get(0).length();
            char match;
            StringBuffer beginning = new StringBuffer(filePrefix);

            // Figure out if the matching files share any characters after
            // the prefix
            for(int i = filePrefixLen; i < stringLen; i++) {
                match = filesWithPrefix.get(0).charAt(i);
                for(int j = 0; j < filesWithPrefixLength; j++) {
                    if(i >= filesWithPrefix.get(j).length() || filesWithPrefix.get(j).charAt(i) != match) {
                        return beginning.toString();
                    }
                }
                beginning.append(match);
            }

            return beginning.toString();
        }
        else {

            return filePrefix;
        }

    }

    Action listFiles = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ApplicationManager.getApplication().invokeLater(new Runnable() {
                public void run() {
                    String directory = getText().substring(0, getText().lastIndexOf("/"));
                    String filePrefix = getText().substring(getText().lastIndexOf("/"));
                    String pathText = getText();
                    VirtualFile currentPathFile = LocalFileSystem.getInstance().findFileByPath(pathText);

                    DefaultListModel listModel = (DefaultListModel) parentPanel.getFileList().getModel();
                    listModel.removeAllElements();

                    listModel.addElement("./");
                    listModel.addElement("../");

                    if(currentPathFile != null) {
                        if(currentPathFile.isDirectory()) {
                            System.out.println("Is directory");
                            if(pathText.charAt(pathText.length() -1) != '/') {
                                setText(pathText + '/');
                            }
                            else {
                                VirtualFile[] children = currentPathFile.getChildren();
                                if(children.length == 1) {
                                    if(children[0].isDirectory()) {
                                        setText(children[0].getPath() + '/');
                                    }
                                    else {
                                        setText(children[0].getPath());
                                    }
                                }

                                else {

                                    for(int i = 0; i < children.length; ++i) {
                                        listModel.addElement("." + children[i].getPath().substring(children[i].getPath().lastIndexOf("/")));
                                    }
                                    System.out.println("Children of directory: " + listModel.toString());
                                    parentPanel.resetFileList(listModel);
                                }
                            }
                        }
                    }
                    else if(LocalFileSystem.getInstance().findFileByPath(directory) != null){
                        System.out.println("Is not directory");
                        VirtualFile directoryFile = LocalFileSystem.getInstance().findFileByPath(directory);
                        VirtualFile[] children = directoryFile.getChildren();

                        Pattern childPattern = Pattern.compile(directoryFile.getPath() + filePrefix + ".*");
                        ArrayList<String> childPaths = new ArrayList<String>();
                        for(int i = 0; i < children.length; ++i) {
                            if(childPattern.matcher(children[i].getPath()).matches()) {
                                listModel.addElement("." + children[i].getPath().substring(children[i].getPath().lastIndexOf("/")));
                                childPaths.add(children[i].getPath());
                            }
                        }
                        String newPathText = matchingFiles(childPaths, filePrefix);
                        if(newPathText.equals(filePrefix)) {
                            parentPanel.resetFileList(listModel);
                        }
                        else {
                            setText(directory + newPathText);
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
            System.out.println("PATH: " + path);
            VirtualFile file = LocalFileSystem.getInstance().findFileByPath(path);
            if(file != null) {
                OpenFileDescriptor ofd = new OpenFileDescriptor(parentPanel.getProject(), file);
                ofd.navigate(false);
                parentPanel.getPopup().closeOk(null);

            }
            else {
                System.out.println("===ENTER=== File was null");
            }
        }
    };
}
