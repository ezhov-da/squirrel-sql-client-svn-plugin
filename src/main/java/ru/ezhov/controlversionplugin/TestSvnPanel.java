package ru.ezhov.controlversionplugin;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestSvnPanel {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    try {
                        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    } catch (Throwable ex) {
                        //
                    }
                    JFrame frame = new JFrame("_________");
                    frame.add(new PanelFileTree(null));
                    frame.setSize(1000, 600);
                    frame.setLocationRelativeTo(null);
                    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    frame.setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(TestSvnPanel.class.getName()).log(Level.SEVERE, null, ex);
                    //
                }
            }
        });
    }
}
