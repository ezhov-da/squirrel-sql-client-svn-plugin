package ru.ezhov.controlversionplugin.components;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import java.util.Vector;

/**
 * Source from
 * http://www.java2s.com/Code/Java/Swing-JFC/FileTreewithPopupMenu.htm
 *
 * @author deonisius
 */
public class PanelFile
        extends JPanel {

    public static final ImageIcon ICON_COMPUTER
            = new ImageIcon(PanelFile.class.getResource("/computer.png"));
    public static final ImageIcon ICON_DISK
            = new ImageIcon(PanelFile.class.getResource("/drive.png"));
    public static final ImageIcon ICON_FOLDER
            = new ImageIcon(PanelFile.class.getResource("/folder.png"));
    public static final ImageIcon ICON_EXPANDEDFOLDER
            = new ImageIcon(PanelFile.class.getResource("/epandedfolder.png"));
    public static final ImageIcon ICON_SQL
            = new ImageIcon(PanelFile.class.getResource("/sql.png"));
    public static final ImageIcon ICON_TXT
            = new ImageIcon(PanelFile.class.getResource("/txt.png"));

    private final JButton buttonOpenFile = new JButton(new ActionOpenFile());
    private final JFileChooser fileChooser = new JFileChooser();

    protected JTree tree;
    protected DefaultTreeModel model;
    protected JTextField textFieldShow;

    // NEW
    protected JPopupMenu popupMenu;
    protected Action m_action;
    protected TreePath m_clickedPath;

    public PanelFile() {
        super(new BorderLayout());
        DefaultMutableTreeNode top = new DefaultMutableTreeNode(
                new IconData(ICON_COMPUTER, null, "Folders"));

        DefaultMutableTreeNode node;
        File[] roots = File.listRoots();
        for (int k = 0; k < roots.length; k++) {
            node = new DefaultMutableTreeNode(new IconData(ICON_DISK,
                    null, new FileNode(roots[k])));
            top.add(node);
            node.add(new DefaultMutableTreeNode(new Boolean(true)));
        }

        model = new DefaultTreeModel(top);
        tree = new JTree(model);
        tree.putClientProperty("JTree.lineStyle", "Angled");

        TreeCellRenderer renderer = new IconCellRenderer();
        tree.setCellRenderer(renderer);
        tree.addTreeExpansionListener(new DirExpansionListener());
        tree.addTreeSelectionListener(new DirSelectionListener());
        tree.getSelectionModel().setSelectionMode(
                TreeSelectionModel.SINGLE_TREE_SELECTION);
        tree.setShowsRootHandles(true);
        tree.setEditable(false);

        JScrollPane s = new JScrollPane();
        s.getViewport().add(tree);
        add(s, BorderLayout.CENTER);

        JPanel panel = new JPanel(new BorderLayout());
        textFieldShow = new JTextField();
        textFieldShow.setEditable(false);

        panel.add(buttonOpenFile, BorderLayout.WEST);
        panel.add(textFieldShow, BorderLayout.CENTER);

        add(panel, BorderLayout.NORTH);

// NEW
        popupMenu = new JPopupMenu();
        m_action = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (m_clickedPath == null) {
                    return;
                }
                if (tree.isExpanded(m_clickedPath)) {
                    tree.collapsePath(m_clickedPath);
                } else {
                    tree.expandPath(m_clickedPath);
                }
            }
        };
        popupMenu.add(m_action);
        popupMenu.addSeparator();

        Action a1 = new AbstractAction("Delete") {
            public void actionPerformed(ActionEvent e) {
                tree.repaint();
                JOptionPane.showMessageDialog(PanelFile.this,
                        "Delete option is not implemented",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        popupMenu.add(a1);

        Action a2 = new AbstractAction("Rename") {
            public void actionPerformed(ActionEvent e) {
                tree.repaint();
                JOptionPane.showMessageDialog(PanelFile.this,
                        "Rename option is not implemented",
                        "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        };
        popupMenu.add(a2);
        tree.add(popupMenu);
        tree.addMouseListener(new PopupTrigger());
    }

    DefaultMutableTreeNode getTreeNode(TreePath path) {
        return (DefaultMutableTreeNode) (path.getLastPathComponent());
    }

    FileNode getFileNode(DefaultMutableTreeNode node) {
        if (node == null) {
            return null;
        }
        Object obj = node.getUserObject();
        if (obj instanceof IconData) {
            obj = ((IconData) obj).getObject();
        }
        if (obj instanceof FileNode) {
            return (FileNode) obj;
        } else {
            return null;
        }
    }

    // NEW
    class PopupTrigger extends MouseAdapter {

        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                int x = e.getX();
                int y = e.getY();
                TreePath path = tree.getPathForLocation(x, y);
                if (path != null) {
                    if (tree.isExpanded(path)) {
                        m_action.putValue(Action.NAME, "Collapse");
                    } else {
                        m_action.putValue(Action.NAME, "Expand");
                    }
                    popupMenu.show(tree, x, y);
                    m_clickedPath = path;
                }
            }
        }
    }

    // Make sure expansion is threaded and updating the tree model
    // only occurs within the event dispatching thread.
    class DirExpansionListener implements TreeExpansionListener {

        public void treeExpanded(TreeExpansionEvent event) {
            final DefaultMutableTreeNode node = getTreeNode(
                    event.getPath());
            final FileNode fnode = getFileNode(node);

            Thread runner = new Thread() {
                public void run() {
                    if (fnode != null && fnode.expand(node)) {
                        Runnable runnable = new Runnable() {
                            public void run() {
                                model.reload(node);
                            }
                        };
                        SwingUtilities.invokeLater(runnable);
                    }
                }
            };
            runner.start();
        }

        public void treeCollapsed(TreeExpansionEvent event) {
        }
    }

    class DirSelectionListener
            implements TreeSelectionListener {

        public void valueChanged(TreeSelectionEvent event) {
            DefaultMutableTreeNode node = getTreeNode(
                    event.getPath());
            FileNode fnode = getFileNode(node);
            if (fnode != null) {
                textFieldShow.setText(fnode.getFile().
                        getAbsolutePath());
            } else {
                textFieldShow.setText("");
            }
        }
    }

    /**
     * это action для кнопки открытия файлов
     */
    private class ActionOpenFile extends AbstractAction {

        {
            putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/folder_add.png")));
            putValue(Action.SHORT_DESCRIPTION, "Open folder");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int open = fileChooser.showOpenDialog(null);
            if (open != JFileChooser.OPEN_DIALOG) {
                return;
            }
            File file = fileChooser.getSelectedFile();
            //setNodes(file, null);
        }
    }
}

class IconCellRenderer
        extends JLabel
        implements TreeCellRenderer {

    protected Color m_textSelectionColor;
    protected Color m_textNonSelectionColor;
    protected Color m_bkSelectionColor;
    protected Color m_bkNonSelectionColor;
    protected Color m_borderSelectionColor;

    protected boolean m_selected;

    public IconCellRenderer() {
        super();
        m_textSelectionColor = UIManager.getColor(
                "Tree.selectionForeground");
        m_textNonSelectionColor = UIManager.getColor(
                "Tree.textForeground");
        m_bkSelectionColor = UIManager.getColor(
                "Tree.selectionBackground");
        m_bkNonSelectionColor = UIManager.getColor(
                "Tree.textBackground");
        m_borderSelectionColor = UIManager.getColor(
                "Tree.selectionBorderColor");
        setOpaque(false);
    }

    public Component getTreeCellRendererComponent(JTree tree,
                                                  Object value, boolean sel, boolean expanded, boolean leaf,
                                                  int row, boolean hasFocus) {
        DefaultMutableTreeNode node
                = (DefaultMutableTreeNode) value;
        Object obj = node.getUserObject();
        setText(obj.toString());

        if (obj instanceof Boolean) {
            setText("Retrieving data...");
        }

        if (obj instanceof IconData) {
            IconData idata = (IconData) obj;
            if (expanded) {
                setIcon(idata.getExpandedIcon());
            } else {
                setIcon(idata.getIcon());
            }
        } else {
            setIcon(null);
        }

        setFont(tree.getFont());
        setForeground(sel ? m_textSelectionColor
                : m_textNonSelectionColor);
        setBackground(sel ? m_bkSelectionColor
                : m_bkNonSelectionColor);
        m_selected = sel;
        return this;
    }

    public void paintComponent(Graphics g) {
        Color bColor = getBackground();
        Icon icon = getIcon();

        g.setColor(bColor);
        int offset = 0;
        if (icon != null && getText() != null) {
            offset = (icon.getIconWidth() + getIconTextGap());
        }
        g.fillRect(offset, 0, getWidth() - 1 - offset,
                getHeight() - 1);

        if (m_selected) {
            g.setColor(m_borderSelectionColor);
            g.drawRect(offset, 0, getWidth() - 1 - offset, getHeight() - 1);
        }

        super.paintComponent(g);
    }
}

class IconData {

    protected Icon m_icon;
    protected Icon m_expandedIcon;
    protected Object m_data;

    public IconData(Icon icon, Object data) {
        m_icon = icon;
        m_expandedIcon = null;
        m_data = data;
    }

    public IconData(Icon icon, Icon expandedIcon, Object data) {
        m_icon = icon;
        m_expandedIcon = expandedIcon;
        m_data = data;
    }

    public Icon getIcon() {
        return m_icon;
    }

    public Icon getExpandedIcon() {
        return m_expandedIcon != null ? m_expandedIcon : m_icon;
    }

    public Object getObject() {
        return m_data;
    }

    public String toString() {
        return m_data.toString();
    }
}

class FileNode {

    protected File m_file;

    public FileNode(File file) {
        m_file = file;
    }

    public File getFile() {
        return m_file;
    }

    public String toString() {
        return m_file.getName().length() > 0 ? m_file.getName()
                : m_file.getPath();
    }

    public boolean expand(DefaultMutableTreeNode parent) {
        DefaultMutableTreeNode flag
                = (DefaultMutableTreeNode) parent.getFirstChild();
        if (flag == null) // No flag
        {
            return false;
        }
        Object obj = flag.getUserObject();
        if (!(obj instanceof Boolean)) {
            return false;      // Already expanded
        }
        parent.removeAllChildren();  // Remove Flag

        File[] files = listFiles();
        if (files == null) {
            return true;
        }

        Vector v = new Vector();

        for (int k = 0; k < files.length; k++) {
            File f = files[k];
//            if (!(f.isDirectory())) {
//                continue;
//            }

            FileNode newNode = new FileNode(f);

            boolean isAdded = false;
            for (int i = 0; i < v.size(); i++) {
                FileNode nd = (FileNode) v.elementAt(i);
                if (newNode.compareTo(nd) < 0) {
                    v.insertElementAt(newNode, i);
                    isAdded = true;
                    break;
                }
            }
            if (!isAdded) {
                v.addElement(newNode);
            }
        }

        for (int i = 0; i < v.size(); i++) {
            FileNode nd = (FileNode) v.elementAt(i);

            IconData idata = null;
            File fileFrom = nd.getFile();
            if (fileFrom.isDirectory()) {
                idata = new IconData(PanelFile.ICON_FOLDER,
                        PanelFile.ICON_EXPANDEDFOLDER, nd);
            } else {
                String name = fileFrom.getName();
                if (name.endsWith("sql")) {
                    idata = new IconData(PanelFile.ICON_SQL,
                            PanelFile.ICON_EXPANDEDFOLDER, nd);
                } else {
                    idata = new IconData(PanelFile.ICON_TXT,
                            PanelFile.ICON_EXPANDEDFOLDER, nd);
                }
            }

            DefaultMutableTreeNode node = new DefaultMutableTreeNode(idata);
            parent.add(node);

            if (nd.hasSubDirs()) {
                node.add(new DefaultMutableTreeNode(
                        new Boolean(true)));
            }
        }

        return true;
    }

    public boolean hasSubDirs() {
        File[] files = listFiles();
        return files != null;
    }

    public int compareTo(FileNode toCompare) {
        return m_file.getName().compareToIgnoreCase(
                toCompare.m_file.getName());
    }

    protected File[] listFiles() {
        if (!m_file.isDirectory()) {
            return null;
        }
        try {

            File[] files = m_file.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    String name = pathname.getName();
                    boolean isShow = pathname.isDirectory() || name.endsWith(".sql") || name.endsWith(".txt");

                    System.out.println(name + " :" + isShow);

                    return isShow;
                }
            });

            return files;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    "Error reading directory " + m_file.getAbsolutePath(),
                    "Warning", JOptionPane.WARNING_MESSAGE);
            return null;
        }
    }

}
