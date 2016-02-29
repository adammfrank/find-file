import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.components.JBList;
import org.jdesktop.swingx.action.ActionManager;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
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

    }

    /**
     *
     * @param files ArrayList of files paths to be checked
     * @param filePrefix The characters that matching file paths need to start with
     * @return The maximum common characters that all files share with each other and filePrefix
     * eg If files are "Main.java, MainFake.java, and Sibling.java" and filePrefix is "M",
     * return is "Main".
     */
    private String matchingFiles(ArrayList<String> files, String filePrefix) {
        System.out.println("filePrefix " + filePrefix);
        Pattern prefixPattern = Pattern.compile("^" + Pattern.quote(filePrefix) + ".*");
        ArrayList<String> filesWithPrefix = new ArrayList<String>();
        for(String f : files) {
            String truncF = f.substring(f.lastIndexOf("/"));
            System.out.println(truncF);

            if(prefixPattern.matcher(truncF).matches()) {
                filesWithPrefix.add(truncF);
                System.out.println("has prefix " + truncF);
            }
        }
        int stringLen = filesWithPrefix.get(0).length();
        int filesLength = filesWithPrefix.size();
        char match;
        StringBuffer beginning = new StringBuffer();


        for(int i = 0; i < stringLen; i++) {
            match = filesWithPrefix.get(0).charAt(i);
            for(int j = 0; j < filesLength; j++) {
                if(filesWithPrefix.get(j).length() < i + 1 || filesWithPrefix.get(j).charAt(i) != match) {
                    System.out.println("beginning inside " + beginning.toString());
                    return beginning.toString();
                }
            }
            beginning.append(match);
        }
        System.out.println("beginning outside " + beginning.toString());
        return beginning.toString();
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

                    if(currentPathFile != null) {
                        if(currentPathFile.isDirectory()) {
                            System.out.println("Directory");
                            if(pathText.charAt(pathText.length() -1) != '/') {
                                setText(pathText + '/');
                            }
                            else {
                                VirtualFile[] children = currentPathFile.getChildren();
                                System.out.println("Children size" + children.length);
                                if(children.length == 1) {
                                    System.out.println("First child " + children[0].getPath());
                                    if(children[0].isDirectory()) {
                                        setText(children[0].getPath() + '/');
                                    }
                                    else {
                                        setText(children[0].getPath());
                                    }
                                }
                                else if(children.length == 0) {
                                    listModel.addElement(".");
                                    listModel.addElement("..");
                                }

                                else {

                                    for(int i = 0; i < children.length; ++i) {
                                        listModel.addElement(children[i].getPath().substring(children[i].getPath().lastIndexOf("/")));
                                    }

                                    parentPanel.remove(1);
                                    parentPanel.add(new FileList(listModel, parentPanel));
                                    parentPanel.setMinimumSize(new Dimension(400, children.length * 20));
                                    parentPanel.revalidate();
                                    parentPanel.repaint();
                                }
                            }
                        }
                    }
                    else if(LocalFileSystem.getInstance().findFileByPath(directory) != null){
                        System.out.println("Not a directory");
                        VirtualFile directoryFile = LocalFileSystem.getInstance().findFileByPath(directory);
                        VirtualFile[] children = directoryFile.getChildren();

                        Pattern childPattern = Pattern.compile(pathText + ".*");
                        ArrayList<String> childPaths = new ArrayList<String>();
                        for(int i = 0; i < children.length; ++i) {
                            if(childPattern.matcher(children[i].getPath()).matches()) {
                                listModel.addElement(children[i].getPath().substring(children[i].getPath().lastIndexOf("/")));
                                childPaths.add(children[i].getPath());
                            }
                        }

                        String newPathText = matchingFiles(childPaths, filePrefix);
                        System.out.println("newPathText " + newPathText);

                        setText(directory + newPathText);

                    }
                    else {
                        //parentPanel.getFileList().setEmptyText("");

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
                parentPanel.getPopup().closeOk(null);

            }
            else {

            }
        }
    };
}
