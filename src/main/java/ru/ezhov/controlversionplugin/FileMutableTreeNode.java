package ru.ezhov.controlversionplugin;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;

/**
 * @author deonisius
 */
public class FileMutableTreeNode extends DefaultMutableTreeNode {

    private File file;

    public FileMutableTreeNode(File file) {
        super(file);
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
