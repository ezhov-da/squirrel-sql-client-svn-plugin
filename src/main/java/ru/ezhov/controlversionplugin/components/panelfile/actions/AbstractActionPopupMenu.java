package ru.ezhov.controlversionplugin.components.panelfile.actions;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * @author deonisius
 */
public class AbstractActionPopupMenu extends AbstractAction {

    protected JTree tree;

    public AbstractActionPopupMenu(JTree tree) {
        this.tree = tree;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

}
