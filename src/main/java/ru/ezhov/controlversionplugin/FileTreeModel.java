package ru.ezhov.controlversionplugin;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.io.File;

/**
 * @author deonisius
 */
public class FileTreeModel extends DefaultTreeModel implements FileTree {

    public FileTreeModel(TreeNode root) {
        super(root);
    }

    @Override
    public void addFile(File file) {
        FileMutableTreeNode defaultMutableTreeNode
                = new FileMutableTreeNode(file);
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                addFile(f);
            }
            defaultMutableTreeNode.add(new FileMutableTreeNode(f));

        }

    }

    @Override
    public void reloadFolder(File file) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
