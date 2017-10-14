package ru.ezhov.controlversionplugin;

import java.io.File;

/**
 * @author deonisius
 */
public interface FileTree {

    public void addFile(File file);

    public void reloadFolder(File file);
}
