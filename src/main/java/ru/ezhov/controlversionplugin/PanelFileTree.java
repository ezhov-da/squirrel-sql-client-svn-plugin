package ru.ezhov.controlversionplugin;

import net.sourceforge.squirrel_sql.client.gui.session.SQLInternalFrame;
import net.sourceforge.squirrel_sql.client.session.ISession;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.mozilla.universalchardet.UniversalDetector;
import ru.ezhov.controlversionplugin.components.PanelFile;
import ru.ezhov.controlversionplugin.svn.Command;
import ru.ezhov.controlversionplugin.svn.Svn;
import ru.ezhov.controlversionplugin.svn.SvnLoad;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author ezhov_da
 */
public class PanelFileTree extends JPanel {

    private static final Logger LOG = Logger.getLogger(PanelFileTree.class.getName());

    private final JLabel labelTextSvn = new JLabel("Control version:");
    private JComboBox comboBoxSvn;
    private final JLabel labelCommanSvn = new JLabel("commands:");
    /**
     * комбобокс, который отвечает за комманды svn
     */
    private final JComboBox comboBoxCommandSvn = new JComboBox();
    /**
     * кнопка применить комманду
     */
    private final JButton buttonCommandComboBox = new JButton(new ActionExecuteSvnCommand());
    private final JFileChooser fileChooser = new JFileChooser();
    private final JToolBar toolBar = new JToolBar(JToolBar.HORIZONTAL);

    private PanelFile panelFile;

    private RSyntaxTextArea textPaneViewFile = new RSyntaxTextArea();

    private ISession is;

    public PanelFileTree(ISession is) throws Exception {
        super(new BorderLayout());
        this.is = is;
        this.panelFile = new PanelFile();
        loadComboBoxSvn();  //загружаем комбобокс с svn
        setToolBar();
        //  setModelTree(); // ставим модель
        add(new JScrollPane(toolBar), BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        splitPane.setLeftComponent(new JScrollPane(panelFile));

        RTextScrollPane rTextScrollPane = new RTextScrollPane(textPaneViewFile);
        textPaneViewFile.setAnimateBracketMatching(true);
        textPaneViewFile.setCodeFoldingEnabled(true);
        textPaneViewFile.setHighlightCurrentLine(true);
        rTextScrollPane.setLineNumbersEnabled(true);

        splitPane.setRightComponent(rTextScrollPane);

        splitPane.setDividerLocation(0.5);
        splitPane.setResizeWeight(0.5);

        add(splitPane, BorderLayout.CENTER);
    }

    /**
     * загружаем список доступных svn
     */
    private void loadComboBoxSvn() throws Exception {
        //SvnTreatment svnTreatment = new SvnTreatment();
        //SvnLoad svnLoad = svnTreatment.loadList();

        SvnLoad svnLoad = new SvnLoad();
        comboBoxSvn = new JComboBox(svnLoad.getListSvn().toArray());
        //comboBoxSvn.addItemListener(new OwnListenerChangeSvn());
        fillCommandComboBox();
    }

    /**
     * наполняем командный комбобокс командами выбранного svn-а
     */
    private void fillCommandComboBox() {
        Svn svn = (Svn) comboBoxSvn.getSelectedItem();
        svn.getCommands();
        comboBoxCommandSvn.setModel(new DefaultComboBoxModel(svn.getCommands().toArray()));
    }

    /**
     * настраиваем toolbar
     */
    private void setToolBar() {
        //-----------------------------------------------------------------
        Dimension dimensionLabel = new Dimension(30, 20);
        labelTextSvn.setPreferredSize(dimensionLabel);
        labelTextSvn.setMinimumSize(dimensionLabel);
        labelTextSvn.setMaximumSize(dimensionLabel);
        Dimension dimensionLabelCommanSvn = new Dimension(65, 20);
        labelCommanSvn.setPreferredSize(dimensionLabelCommanSvn);
        labelCommanSvn.setMinimumSize(dimensionLabelCommanSvn);
        labelCommanSvn.setMaximumSize(dimensionLabelCommanSvn);
        Dimension dimensionComboBox = new Dimension(100, 20);
        comboBoxSvn.setPreferredSize(dimensionComboBox);
        comboBoxSvn.setMinimumSize(dimensionComboBox);
        comboBoxSvn.setMaximumSize(dimensionComboBox);
        comboBoxCommandSvn.setPreferredSize(dimensionComboBox);
        comboBoxCommandSvn.setMinimumSize(dimensionComboBox);
        comboBoxCommandSvn.setMaximumSize(dimensionComboBox);
        comboBoxSvn.setAlignmentX(JComponent.LEFT_ALIGNMENT);
        //-----------------------------------------------------------------
        toolBar.setFloatable(false);
        toolBar.setLayout(new BoxLayout(toolBar, BoxLayout.X_AXIS));
        toolBar.add(Box.createHorizontalStrut(5));
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(labelTextSvn);
        toolBar.add(comboBoxSvn);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(labelCommanSvn);
        toolBar.add(comboBoxCommandSvn);
        toolBar.add(Box.createHorizontalStrut(5));
        toolBar.add(buttonCommandComboBox);
        toolBar.add(Box.createHorizontalGlue());
    }

    /**
     * добавляем дерево
     * <p>
     *
     * @param file
     * @param defaultMutableTreeNode
     */
    private void setNodes(File file, DefaultMutableTreeNode defaultMutableTreeNode) {
//        if (defaultMutableTreeNode == null)
//        {
//            DefaultMutableTreeNode defaultMutableTreeNodeRoot =
//                    (DefaultMutableTreeNode) defaultTreeModelFile.getRoot();
//            defaultMutableTreeNodeRoot.add(getRecursionFileTree(file));
//            defaultTreeModelFile.reload(defaultMutableTreeNodeRoot);
//        }
    }

    /**
     * выполняем команду SVN на выбранном файле для дерева
     * <p>
     *
     * @param command выбранная команда
     * @param file    - выбранный файл для работы
     */
    private void executeSvn(
            Command command,
            File file)
            throws NoSuchMethodException,
            IllegalAccessException,
            IllegalArgumentException,
            InvocationTargetException {
        Method method = file.getClass().getMethod(command.getMethodFile());
        Object o = method.invoke(file);
        String[] commandsForCmd = new String[]{
                command.getPathToCommand(),
                command.getArgument(),
                o.toString()
        };
        String resultStr = String.format("%s%s\"%s\"", commandsForCmd);
        try {
            Runtime.getRuntime().exec(resultStr);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, null, ex);
        }
    }

    /**
     * получаем рекурчивно дерево из выьранной папки
     * <p>
     *
     * @param file - выбранная папка
     *             <p>
     * @return дерево папок
     */
    private DefaultMutableTreeNode getRecursionFileTree(File file) {
        DefaultMutableTreeNode defaultMutableTreeNode = new DefaultMutableTreeNode(new OwnChildTree(file));
        File[] files = file.listFiles();
        for (File f : files) {
            if (f.isDirectory()) {
                defaultMutableTreeNode.add(getRecursionFileTree(f));
            } else {
                defaultMutableTreeNode.add(new DefaultMutableTreeNode(new OwnChildTree(f)));
            }
        }
        return defaultMutableTreeNode;
    }

    /**
     * это action для кнопки открытия файлов
     */
    private class ActionExecuteSvnCommand extends AbstractAction {

        {
            putValue(Action.SMALL_ICON, new ImageIcon(getClass().getResource("/execute.png")));
            putValue(Action.SHORT_DESCRIPTION, "выполнить команду для выбранного файла или папки");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
//            try
//            {
//                DefaultMutableTreeNode defaultMutableTreeNode = (DefaultMutableTreeNode) treeFile.getSelectionPath().getLastPathComponent();
//                Object o = defaultMutableTreeNode.getUserObject();
//                if (o instanceof OwnChildTree)
//                {
//                    OwnChildTree childTree = (OwnChildTree) defaultMutableTreeNode.getUserObject();
//                    executeSvn(((Command) comboBoxCommandSvn.getSelectedItem()), childTree.getFile());
//                }
//            } catch (Exception ex)
//            {
//                LOG.log(Level.SEVERE, null, ex);
//            }
        }
    }

    /**
     * класс реализует слушателя на изменение элемента svn
     */
    private class OwnListenerChangeSvn implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent e) {
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    fillCommandComboBox();
                }
            });
        }
    }

    /**
     * данный класс хранит информацию об узле дерева
     */
    class OwnChildTree {

        private final File file;

        public OwnChildTree(File file) {
            this.file = file;
        }

        @Override
        public String toString() {
            return file.getName();
        }

        public File getFile() {
            return file;
        }
    }

    public File getSelectedFile(JTree jt) {
        if (jt.getSelectionPath() == null) {
            return null;
        }
        DefaultMutableTreeNode defaultMutableTreeNode
                = (DefaultMutableTreeNode) jt.getSelectionPath().getLastPathComponent();
        Object o = defaultMutableTreeNode.getUserObject();
        if (o instanceof OwnChildTree) {
            OwnChildTree childTree = (OwnChildTree) defaultMutableTreeNode.getUserObject();
            File file = childTree.getFile();
            if (!file.isDirectory()) {
                return file;
            }
        }
        return null;
    }

    private String readOpenFile(File file)
            throws FileNotFoundException,
            UnsupportedEncodingException,
            IOException {
        String charsetFile = guessEncoding(file);    //получаем кодировку файла
        charsetFile = ("".equals(charsetFile)) ? "UTF-8" : charsetFile;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, charsetFile);
            Scanner scanner = new Scanner(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder(10000);
            while (scanner.hasNext()) {
                stringBuilder.append(scanner.nextLine());
                stringBuilder.append("\n");
            }
            scanner.close();
            return stringBuilder.toString();

        } catch (Exception ex) {
            LOG.log(Level.SEVERE, null, ex);
            return "NOT READ FILE";
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, null, ex);
            }
        }
    }

    public String guessEncoding(File file) throws FileNotFoundException, IOException {
        byte[] buf = new byte[4096];
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            // (1)
            UniversalDetector detector = new UniversalDetector(null);
            // (2)
            int nread;
            while ((nread = fis.read(buf)) > 0 && !detector.isDone()) {
                detector.handleData(buf, 0, nread);
            }
            // (3)
            detector.dataEnd();
            // (4)
            String encoding = detector.getDetectedCharset();
            // (5)
            detector.reset();
            if (encoding != null) {
                return encoding;
            } else {
                return "";
            }
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
    }

    /**
     * класс для открытия файла по нажатию enter
     */
    class OwnKeyListener extends KeyAdapter {

        private final JTree tr;

        public OwnKeyListener(JTree tr) {
            this.tr = tr;
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() != KeyEvent.VK_ENTER) {
                return;
            }
            getSelectedFile(tr);
        }
    }

    /**
     * класс для открытия файла по двойному клику
     */
    class OwnMouseListener extends MouseAdapter {

        private final JTree tr;

        public OwnMouseListener(JTree tr) {
            this.tr = tr;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1
                    && e.getClickCount() == 1) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            File file = getSelectedFile(tr);
                            if (file != null) {

                                RSyntaxTextArea syntaxTextArea = PanelFileTree.this.textPaneViewFile;

                                String name = file.getName();
                                int lastIndex = name.lastIndexOf(".");
                                if (lastIndex != -1) {
                                    String ext = name.substring(lastIndex + 1, name.length());
                                    String resultExt = "text/" + ext.toLowerCase();
                                    syntaxTextArea.setSyntaxEditingStyle(resultExt);
                                } else {
                                    syntaxTextArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
                                }
                                String textFromFile = readOpenFile(file);
                                syntaxTextArea.setText(textFromFile);
                            }
                        } catch (UnsupportedEncodingException ex) {
                            LOG.log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            LOG.log(Level.SEVERE, null, ex);
                        }
                    }
                });

            } else if (e.getButton() == MouseEvent.BUTTON1
                    && e.getClickCount() == 2) {
                final File file = getSelectedFile(tr);
                if (file != null && is != null) {
                    SwingUtilities.invokeLater(new Runnable() {

                        @Override
                        public void run() {
                            SQLInternalFrame sQLInternalFrame
                                    = is
                                    .getApplication()
                                    .getWindowManager()
                                    .createSQLInternalFrame(is);

                            sQLInternalFrame.getSQLPanelAPI().fileOpen(file);
                        }
                    });

                }
            }
        }
    }

}
