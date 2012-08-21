/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.tree.*;
import oltpbenchadmin.comm.ProxyConnection;
import oltpbenchadmin.commons.Database;
import oltpbenchadmin.commons.DatabaseSystem;
import oltpbenchadmin.commons.ExecuteConfiguration;
import oltpbenchadmin.commons.ResultFile;
import oltpbenchadmin.commons.Workload;
import oltpbenchadmin.commons.commands.*;
import oltpbenchadmin.icons.Icons;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public class MainForm extends JFrame {

    private JMenuBar menuBar;
    private JMenu menuFile, menuProxies, menuDatabase, menuWorkload, menuHelp;
    private JMenuItem menuFileDownload, menuFileExit, menuProxiesAdd, menuProxiesRemove;
    private JMenuItem menuCreateWorkload, menuDropWorkload;
    private JMenuItem menuCreateDatabase, menuDropDatabase;
    private JMenuItem menuCreateDatabaseSchema, menuLoadDatabase;
    private JMenuItem menuAbout;
    private JToolBar toolBar;
    private JButton buttonRefreshBenchmarkProxies;
    private JButton buttonExit;
    private JPanel panelMain;
    private JSplitPane horizontalSplitPane, verticalSplitPane;
    private JPanel panelLeft, panelRight, panelContent, panelLog;
    private JTextPane textLog;
    private JTree treeProxyBenchmarks;
    private DefaultTreeModel modelProxyBenchmarks;
    private JButton buttonAddProxy, buttonRemoveProxy;
    private JButton buttonDropWorkload, buttonCreateWorkload;
    private JButton buttonDropDatabase, buttonCreateDatabase;
    private JButton buttonCreateSchemaDatabase, buttonLoadDatabase;
    private List<ProxyConnection> proxyConnectionList;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyy hh:mm:ss");
    private Style styleTime, styleText, styleErrorTime, styleErrorText;
    private JButton buttonAddExecute, buttonRemoveExecute, buttonExecuteAll;
    private JButton buttonDownloadFile;
    private JTabbedPane tabbedExecutes;
    private List<ExecutePanel> executePanelList;
    private int tabId = 0;

    public MainForm() {
        proxyConnectionList = new ArrayList<ProxyConnection>();
        executePanelList = new ArrayList<ExecutePanel>();
        menuBar = new JMenuBar();
        menuFile = new JMenu("File");
        menuFileDownload = new JMenuItem("Download File");
        menuFileExit = new JMenuItem("Exit");
        menuProxies = new JMenu("Proxies");
        menuProxiesAdd = new JMenuItem("Add Proxy");
        menuProxiesRemove = new JMenuItem("Remove Proxy");
        menuDatabase = new JMenu("Database");
        menuDropDatabase = new JMenuItem("Drop Database");
        menuCreateDatabase = new JMenuItem("Create Database");
        menuWorkload = new JMenu("Workload");
        menuDropWorkload = new JMenuItem("Drop Workload");
        menuCreateWorkload = new JMenuItem("Create Workload");
        menuCreateDatabaseSchema = new JMenuItem("Create Database Schema");
        menuLoadDatabase = new JMenuItem("Load Data in Database");
        menuHelp = new JMenu("Help");
        menuAbout = new JMenuItem("About");
        toolBar = new JToolBar();
        buttonExit = new JButton();
        panelMain = new JPanel(new BorderLayout());
        panelLeft = new JPanel(new BorderLayout());
        panelRight = new JPanel(new BorderLayout());
        panelContent = new JPanel(new BorderLayout());
        panelLog = new JPanel(new BorderLayout());
        horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panelLeft, panelRight);
        verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelContent, panelLog);
        textLog = new JTextPane();
        modelProxyBenchmarks = new DefaultTreeModel(new DefaultMutableTreeNode("benchmark-proxies"));
        treeProxyBenchmarks = new JTree(modelProxyBenchmarks);
        buttonRefreshBenchmarkProxies = new JButton();
        buttonAddProxy = new JButton();
        buttonRemoveProxy = new JButton();
        buttonDropWorkload = new JButton();
        buttonCreateWorkload = new JButton();
        buttonDropDatabase = new JButton();
        buttonCreateDatabase = new JButton();
        buttonCreateSchemaDatabase = new JButton();
        buttonLoadDatabase = new JButton();
        buttonAddExecute = new JButton("Add Execute");
        buttonRemoveExecute = new JButton("Remove Execute");
        buttonExecuteAll = new JButton("Execute All Checked");
        buttonDownloadFile = new JButton();
        tabbedExecutes = new JTabbedPane();
    }

    private void buildFrame() {
        setTitle("oltpbenchadmin - A tool for OLTPBenchmark's administration");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setIconImage(new ImageIcon(Icons.class.getResource("wrench.png")).getImage());
        setJMenuBar(menuBar);
        panelMain.add(toolBar, BorderLayout.NORTH);
        panelMain.add(horizontalSplitPane, BorderLayout.CENTER);
        panelRight.add(verticalSplitPane, BorderLayout.CENTER);
        panelLeft.add(new JScrollPane(treeProxyBenchmarks), BorderLayout.CENTER);
        textLog.setEditable(false);
        panelLog.add(new JLabel("Application's log:"), BorderLayout.NORTH);
        panelLog.add(new JScrollPane(textLog), BorderLayout.CENTER);
        StyledDocument styledDoc = textLog.getStyledDocument();
        styleTime = styledDoc.addStyle("time", null);
        StyleConstants.setBold(styleTime, true);
        StyleConstants.setForeground(styleTime, Color.BLACK);
        styleText = styledDoc.addStyle("text", null);
        StyleConstants.setForeground(styleText, Color.BLACK);
        styleErrorTime = styledDoc.addStyle("errorTime", null);
        StyleConstants.setBold(styleErrorTime, true);
        StyleConstants.setForeground(styleErrorTime, Color.RED);
        styleErrorText = styledDoc.addStyle("errorText", null);
        StyleConstants.setForeground(styleErrorText, Color.RED);
        add(panelMain);
    }

    private void eventsFrame() {
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                end();
            }
        });
    }

    private void buildContent() {
        buttonAddExecute.setIcon(new ImageIcon(Icons.class.getResource("add.png")));
        buttonRemoveExecute.setIcon(new ImageIcon(Icons.class.getResource("delete.png")));
        buttonExecuteAll.setIcon(new ImageIcon(Icons.class.getResource("lightning.png")));

        JPanel panelContentTop = new JPanel(new FlowLayout());
        panelContentTop.add(buttonAddExecute);
        panelContentTop.add(buttonRemoveExecute);

        JPanel panelContentBottom = new JPanel(new FlowLayout());
        panelContentBottom.add(buttonExecuteAll);

        panelContent.add(panelContentTop, BorderLayout.NORTH);
        panelContent.add(tabbedExecutes, BorderLayout.CENTER);
        panelContent.add(panelContentBottom, BorderLayout.SOUTH);
    }

    private void eventsContent() {
        buttonAddExecute.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (proxyConnectionList.size() > 0) {
                    String title = "Execute " + (++tabId);
                    ExecutePanel executePanel = new ExecutePanel(getRef(), title, proxyConnectionList);
                    tabbedExecutes.addTab(executePanel.getTitle(), new ImageIcon(Icons.class.getResource("page_white_lightning.png")), executePanel);
                    executePanelList.add(executePanel);
                } else {
                    JOptionPane.showMessageDialog(getRef(), "There is no an active connection to a proxy", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        buttonRemoveExecute.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (tabbedExecutes.getSelectedComponent() != null) {
                    boolean success = false;
                    ExecutePanel executePanel = (ExecutePanel) tabbedExecutes.getSelectedComponent();
                    for (int i = 0; i < executePanelList.size(); i++) {
                        ExecutePanel executePanelTemp = executePanelList.get(i);
                        if (executePanel.getTitle().equalsIgnoreCase(executePanelTemp.getTitle())) {
                            executePanelList.remove(i);
                            tabbedExecutes.removeTabAt(i);
                            success = true;
                        }
                    }
                    if (success) {
                        JOptionPane.showMessageDialog(getRef(), "Execute tab removed", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(getRef(), "Select a execute tab", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        buttonExecuteAll.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (executePanelList.isEmpty()) {
                    JOptionPane.showMessageDialog(getRef(), "There is no set script for execution", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                for (ExecutePanel executePanel : executePanelList) {
                    if (!executePanel.validateForm()) {
                        JOptionPane.showMessageDialog(getRef(), "All fields of the execution scripts must be completed", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
                List<ExecuteConfiguration> executeConfigurations = new ArrayList<ExecuteConfiguration>();
                for (ExecutePanel ep : executePanelList) {
                    if (ep.getExecuteConfiguration() != null) {
                        executeConfigurations.add(ep.getExecuteConfiguration());
                    }
                }
                if (executeConfigurations.isEmpty()) {
                    JOptionPane.showMessageDialog(getRef(), "There is no set script for execution", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                boolean foundEquals = false;
                for (int i = 0; i < executePanelList.size(); i++) {
                    for (int j = 0; j < executePanelList.size(); j++) {
                        if (i != j
                                && executePanelList.get(i).getExecuteConfiguration().getProxyConnectionString().equals(executePanelList.get(j).getExecuteConfiguration().getProxyConnectionString())) {
                            foundEquals = true;
                            break;
                        }
                    }
                    if (foundEquals) {
                        break;
                    }
                }
                if (foundEquals) {
                    JOptionPane.showMessageDialog(getRef(), "There can not be run scripts for the same proxy", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                List<ExecuteResult> executeResults = new ArrayList<ExecuteResult>();
                List<ExecuteThread> executeThreads = new ArrayList<ExecuteThread>();
                for (ExecuteConfiguration ec : executeConfigurations) {
                    ExecuteThread executeThread = new ExecuteThread(proxyConnectionList, executeResults, ec);
                    executeThreads.add(executeThread);
                }
                for (ExecuteThread et : executeThreads) {
                    et.start();
                }
                LoadingForm loadingForm = new LoadingForm(getRef());
                loadingForm.execute();
                while (executeResults.size() < executeConfigurations.size());
                loadingForm.close();
                for (ExecutePanel ep : executePanelList) {
                    ep.setVisibleDownloadResultFile();
                }
                addLog("Database successfully deleted");
                addLog("The execution scritps were finalized");
                addLog("Total of execution scripts: " + executeResults.size());                
                JOptionPane.showMessageDialog(getRef(), "Database successfully executed", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void buildMenu() {
        menuFile.add(menuFileDownload);
        menuFile.addSeparator();
        menuFile.add(menuFileExit);
        menuProxies.add(menuProxiesAdd);
        menuProxies.add(menuProxiesRemove);
        menuDatabase.add(menuCreateDatabase);
        menuDatabase.add(menuDropDatabase);
        menuWorkload.add(menuCreateWorkload);
        menuWorkload.add(menuDropDatabase);
        menuWorkload.addSeparator();
        menuWorkload.add(menuCreateDatabaseSchema);
        menuWorkload.add(menuLoadDatabase);
        menuHelp.add(menuAbout);

        menuFileDownload.setIcon(new ImageIcon(Icons.class.getResource("page_white_put.png")));
        menuFileExit.setIcon(new ImageIcon(Icons.class.getResource("door_in.png")));
        menuProxiesAdd.setIcon(new ImageIcon(Icons.class.getResource("computer_add.png")));
        menuProxiesRemove.setIcon(new ImageIcon(Icons.class.getResource("computer_delete.png")));
        menuDropWorkload.setIcon(new ImageIcon(Icons.class.getResource("database_delete.png")));
        menuCreateWorkload.setIcon(new ImageIcon(Icons.class.getResource("page_white_wrench_add.png")));
        menuDropDatabase.setIcon(new ImageIcon(Icons.class.getResource("page_white_wrench_delete.png")));
        menuCreateDatabase.setIcon(new ImageIcon(Icons.class.getResource("database_add.png")));
        menuCreateDatabaseSchema.setIcon(new ImageIcon(Icons.class.getResource("database_table.png")));
        menuLoadDatabase.setIcon(new ImageIcon(Icons.class.getResource("database_lightning.png")));

        menuFile.setMnemonic('F');
        menuFileDownload.setMnemonic('D');
        menuFileExit.setMnemonic('x');

        menuProxies.setMnemonic('P');
        menuProxiesAdd.setMnemonic('A');
        menuProxiesRemove.setMnemonic('R');

        menuDatabase.setMnemonic('D');
        menuDropDatabase.setMnemonic('D');
        menuCreateDatabase.setMnemonic('C');

        menuWorkload.setMnemonic('W');
        menuCreateWorkload.setMnemonic('C');
        menuDropWorkload.setMnemonic('D');
        menuCreateDatabaseSchema.setMnemonic('S');
        menuLoadDatabase.setMnemonic('L');

        menuHelp.setMnemonic('H');
        menuAbout.setMnemonic('A');

        menuBar.add(menuFile);
        menuBar.add(menuProxies);
        menuBar.add(menuDatabase);
        menuBar.add(menuWorkload);
        menuBar.add(menuHelp);
    }

    private void eventsMenu() {
        menuFileDownload.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                end();
            }
        });
        menuFileExit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                downloadSelectedFile();
            }
        });
        menuProxiesAdd.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                end();
            }
        });
        menuProxiesRemove.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                end();
            }
        });
        menuDropWorkload.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dropWorkload();
            }
        });
        menuCreateWorkload.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                createWorkload();
            }
        });
        menuDropDatabase.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dropDatabase();
            }
        });
        menuCreateDatabase.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                createDatabase();
            }
        });
        menuCreateDatabaseSchema.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                createDatabaseSchema();
            }
        });
        menuLoadDatabase.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                loadDatabase();
            }
        });
    }

    private void buildToolBar() {
        toolBar.setFloatable(false);
        buttonRefreshBenchmarkProxies.setIcon(new ImageIcon(Icons.class.getResource("arrow_refresh.png")));
        buttonRefreshBenchmarkProxies.setToolTipText("Update Benchmark Proxies Tree");
        buttonAddProxy.setIcon(new ImageIcon(Icons.class.getResource("computer_add.png")));
        buttonAddProxy.setToolTipText("Add Proxy");
        buttonRemoveProxy.setIcon(new ImageIcon(Icons.class.getResource("computer_delete.png")));
        buttonRemoveProxy.setToolTipText("Remove Selected Proxy");
        buttonExit.setIcon(new ImageIcon(Icons.class.getResource("door_in.png")));
        buttonExit.setToolTipText("Exit");
        buttonDropDatabase.setIcon(new ImageIcon(Icons.class.getResource("database_delete.png")));
        buttonDropDatabase.setToolTipText("Drop Database");
        buttonCreateDatabase.setIcon(new ImageIcon(Icons.class.getResource("database_add.png")));
        buttonCreateDatabase.setToolTipText("Create Database");
        buttonDropWorkload.setIcon(new ImageIcon(Icons.class.getResource("page_white_wrench_delete.png")));
        buttonDropWorkload.setToolTipText("Drop Workload");
        buttonCreateWorkload.setIcon(new ImageIcon(Icons.class.getResource("page_white_wrench_add.png")));
        buttonCreateWorkload.setToolTipText("Create Workload");
        buttonCreateSchemaDatabase.setIcon(new ImageIcon(Icons.class.getResource("database_table.png")));
        buttonCreateSchemaDatabase.setToolTipText("Create Database Schema");
        buttonLoadDatabase.setIcon(new ImageIcon(Icons.class.getResource("database_lightning.png")));
        buttonLoadDatabase.setToolTipText("Load Data in Database");
        buttonDownloadFile.setIcon(new ImageIcon(Icons.class.getResource("page_white_put.png")));
        buttonDownloadFile.setToolTipText("Download the Selected File");
        toolBar.add(buttonRefreshBenchmarkProxies);
        toolBar.addSeparator();
        toolBar.add(buttonAddProxy);
        toolBar.add(buttonRemoveProxy);
        toolBar.addSeparator();
        toolBar.add(buttonCreateWorkload);
        toolBar.add(buttonDropWorkload);
        toolBar.addSeparator();
        toolBar.add(buttonCreateDatabase);
        toolBar.add(buttonDropDatabase);
        toolBar.addSeparator();
        toolBar.add(buttonCreateSchemaDatabase);
        toolBar.add(buttonLoadDatabase);
        toolBar.addSeparator();
        toolBar.add(buttonDownloadFile);
        toolBar.addSeparator();
        toolBar.add(buttonExit);
    }

    private void eventsToolBar() {
        buttonRefreshBenchmarkProxies.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                LoadingForm loadingForm = new LoadingForm(getRef());
                loadingForm.execute();
                for (ProxyConnection proxyConnection : proxyConnectionList) {
                    proxyConnection.updateProxy();
                }
                loadingForm.close();
                refreshAllProxyConnections();
                addLog("oltp-proxy list was updated");
                JOptionPane.showMessageDialog(getRef(), "oltp-proxy list was updated", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        buttonExit.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                end();
            }
        });
        buttonAddProxy.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                addProxy();
            }
        });

        buttonRemoveProxy.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                removeProxy();
            }
        });

        buttonDropWorkload.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dropWorkload();
            }
        });

        buttonCreateWorkload.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                createWorkload();
            }
        });

        buttonDropDatabase.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                dropDatabase();
            }
        });

        buttonCreateDatabase.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                createDatabase();
            }
        });

        buttonCreateSchemaDatabase.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                createDatabaseSchema();
            }
        });

        buttonLoadDatabase.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                loadDatabase();
            }
        });
        
        buttonDownloadFile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                downloadSelectedFile();
            }
        });

    }

    private void buildTree() {
        treeProxyBenchmarks.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
    }

    private void eventsTree() {
        treeProxyBenchmarks.setCellRenderer(new DefaultTreeCellRenderer() {

            @Override
            public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
                Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
                JLabel l = (JLabel) c;
                switch (row) {
                    case 0: {
                        l.setIcon(new ImageIcon(Icons.class.getResource("chart_organisation.png")));
                        break;
                    }
                }
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
                Object userObject = node.getUserObject();
                if (userObject instanceof ProxyConnection) {
                    l.setIcon(new ImageIcon(Icons.class.getResource("computer.png")));
                }
                if (userObject instanceof String && userObject.toString().equalsIgnoreCase("workloads")) {
                    l.setIcon(new ImageIcon(Icons.class.getResource("folder_wrench.png")));
                }
                if (userObject instanceof Workload) {
                    l.setIcon(new ImageIcon(Icons.class.getResource("page_white_wrench.png")));
                }
                if (userObject instanceof String && userObject.toString().equalsIgnoreCase("database systems")) {
                    l.setIcon(new ImageIcon(Icons.class.getResource("folder_database.png")));
                }
                if (userObject instanceof DatabaseSystem) {
                    l.setIcon(new ImageIcon(Icons.class.getResource("server_database.png")));
                }
                if (userObject instanceof Database) {
                    l.setIcon(new ImageIcon(Icons.class.getResource("database.png")));
                }
                if (userObject instanceof String && userObject.toString().equalsIgnoreCase("result files")) {
                    l.setIcon(new ImageIcon(Icons.class.getResource("folder_star.png")));
                }
                if (userObject instanceof ResultFile) {
                    l.setIcon(new ImageIcon(Icons.class.getResource("page_white_star.png")));
                }
                return c;
            }
        });
        treeProxyBenchmarks.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    TreePath path = treeProxyBenchmarks.getSelectionPath();
                    if (path == null) {
                        return;
                    }
                    DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                    if (selectedNode.getUserObject() instanceof Workload) {
                        LoadingForm loadingForm = new LoadingForm(getRef());
                        loadingForm.execute();
                        DefaultMutableTreeNode proxyConnectionNode = (DefaultMutableTreeNode) selectedNode.getParent().getParent();
                        ProxyConnection proxyConnection = (ProxyConnection) proxyConnectionNode.getUserObject();
                        loadingForm.close();
                        WorkloadDescriptorForm form = new WorkloadDescriptorForm(getRef(), ((Workload) selectedNode.getUserObject()), proxyConnection);
                        form.execute();
                    }
                }
            }
        });
        treeProxyBenchmarks.addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent event) {
                if (treeProxyBenchmarks.isSelectionEmpty()) {
                    return;
                }
            }
        });
    }

    public void execute() {
        buildFrame();
        eventsFrame();
        buildMenu();
        eventsMenu();
        buildToolBar();
        eventsToolBar();
        buildTree();
        eventsTree();
        buildContent();
        eventsContent();
        setVisible(true);
        panelLeft.setPreferredSize(new Dimension((getWidth() * 25) / 100, 0));
        panelContent.setPreferredSize(new Dimension(0, ((getHeight() * 75) / 100)));
        pack();
        setExtendedState(MAXIMIZED_BOTH);
    }

    public void end() {
        if (JOptionPane.showConfirmDialog(this, "Are you sure you want to exit this application?", "oltpbenchadmin", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public void addLog(String message) {
        StyledDocument doc = textLog.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), "[" + dateFormat.format(new Date()) + "] ", styleTime);
            doc.insertString(doc.getLength(), (message + "\n"), styleText);
            textLog.setCaretPosition(doc.getLength());
        } catch (BadLocationException ex) {
        }
    }

    public void addLogError(String message) {
        StyledDocument doc = textLog.getStyledDocument();
        try {
            doc.insertString(doc.getLength(), "[" + dateFormat.format(new Date()) + "] ", styleErrorTime);
            doc.insertString(doc.getLength(), (message + "\n"), styleErrorText);
            textLog.setCaretPosition(doc.getLength());
        } catch (BadLocationException ex) {
        }
    }

    private void addProxy() {
        BenchmarkProxyForm form = new BenchmarkProxyForm(getRef());
        form.execute();
    }

    private void removeProxy() {
        if (treeProxyBenchmarks.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(getRef(), "Select one tree element", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        TreePath path = treeProxyBenchmarks.getSelectionPath();
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (selectedNode.getUserObject() instanceof ProxyConnection) {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this proxy?", "oltpbenchadmin", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                ((ProxyConnection) selectedNode.getUserObject()).close();
            }
            return;
        }
        JOptionPane.showMessageDialog(getRef(), "Select a proxy element", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
    }

    private void dropWorkload() {
        JOptionPane.showMessageDialog(getRef(), "TO DO", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
    }

    private void createWorkload() {
        JOptionPane.showMessageDialog(getRef(), "TO DO", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
    }

    private void dropDatabase() {
        if (treeProxyBenchmarks.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(getRef(), "Select one tree element", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        TreePath path = treeProxyBenchmarks.getSelectionPath();
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (selectedNode.getUserObject() instanceof Database) {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this database?", "oltpbenchadmin", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                DefaultMutableTreeNode proxyConnectionNode = (DefaultMutableTreeNode) selectedNode.getParent().getParent().getParent();
                ProxyConnection proxyConnection = (ProxyConnection) proxyConnectionNode.getUserObject();
                DropDatabaseCommand command = new DropDatabaseCommand((Database) selectedNode.getUserObject());
                LoadingForm loadingForm = new LoadingForm(getRef());
                loadingForm.execute();
                Result result = proxyConnection.executeCommand(command);
                for (ProxyConnection proxyConn : this.proxyConnectionList) {
                    proxyConn.updateProxy();
                }
                refreshAllProxyConnections();
                loadingForm.close();
                if (result != null && result instanceof DropDatabaseResult && ((DropDatabaseResult) result).isSuccess()) {
                    addLog("Database successfully deleted");
                    JOptionPane.showMessageDialog(getRef(), "Database successfully deleted", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    addLogError("Failed to delete the database");
                    JOptionPane.showMessageDialog(getRef(), "Failed to delete the database", "oltpbenchadmin", JOptionPane.ERROR_MESSAGE);
                }
            }
            return;
        }
        JOptionPane.showMessageDialog(getRef(), "Select a database element", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
    }

    private void createDatabase() {
        if (treeProxyBenchmarks.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(getRef(), "Select one tree element", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        TreePath path = treeProxyBenchmarks.getSelectionPath();
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (selectedNode.getUserObject() instanceof DatabaseSystem) {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to create a database?", "oltpbenchadmin", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                DefaultMutableTreeNode proxyConnectionNode = (DefaultMutableTreeNode) selectedNode.getParent().getParent();
                ProxyConnection proxyConnection = (ProxyConnection) proxyConnectionNode.getUserObject();
                DatabaseSystem databaseSystem = (DatabaseSystem) selectedNode.getUserObject();
                String databaseName = JOptionPane.showInputDialog(getRef(), "Database name: ");
                Database database = new Database(databaseName, databaseSystem.getType());
                CreateDatabaseCommand command = new CreateDatabaseCommand(database);
                LoadingForm loadingForm = new LoadingForm(getRef());
                loadingForm.execute();
                Result result = proxyConnection.executeCommand(command);
                for (ProxyConnection proxyConn : this.proxyConnectionList) {
                    proxyConn.updateProxy();
                }
                refreshAllProxyConnections();
                loadingForm.close();
                if (result != null && result instanceof CreateDatabaseResult && ((CreateDatabaseResult) result).isSuccess()) {
                    addLog("Database successfully created");
                    JOptionPane.showMessageDialog(getRef(), "Database successfully created", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    addLogError("Failed to create the database");
                    JOptionPane.showMessageDialog(getRef(), "Failed to create the database", "oltpbenchadmin", JOptionPane.ERROR_MESSAGE);
                }
            }
            return;
        }
        JOptionPane.showMessageDialog(getRef(), "Select a database system element", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
    }

    private void createDatabaseSchema() {
        if (treeProxyBenchmarks.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(getRef(), "Select one tree element", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        TreePath path = treeProxyBenchmarks.getSelectionPath();
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (selectedNode.getUserObject() instanceof Workload) {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to create database schema for this database?", "oltpbenchadmin", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                DefaultMutableTreeNode proxyConnectionNode = (DefaultMutableTreeNode) selectedNode.getParent().getParent();
                ProxyConnection proxyConnection = (ProxyConnection) proxyConnectionNode.getUserObject();
                Workload workload = (Workload) selectedNode.getUserObject();
                String benchmarkClass = JOptionPane.showInputDialog(getRef(), "Benchmark class: ");
                long samplingWindow = Long.parseLong(JOptionPane.showInputDialog(getRef(), "Sampling window: "));
                String outputFile = JOptionPane.showInputDialog(getRef(), "Output file: ");
                CreateDatabaseSchemaCommand command = new CreateDatabaseSchemaCommand(workload, benchmarkClass, samplingWindow, outputFile);
                LoadingForm loadingForm = new LoadingForm(getRef());
                loadingForm.execute();
                Result result = proxyConnection.executeCommand(command);
                loadingForm.close();
                if (result != null && result instanceof CreateDatabaseSchemaResult && ((CreateDatabaseSchemaResult) result).isSuccess()) {
                    addLog(((CreateDatabaseSchemaResult) result).getConsoleMessage());
                    addLog("Database schema successfully created");
                    JOptionPane.showMessageDialog(getRef(), "Database schema successfully created", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    addLogError("Failed to create the database schema");
                    JOptionPane.showMessageDialog(getRef(), "Failed to create the database schema", "oltpbenchadmin", JOptionPane.ERROR_MESSAGE);
                }
            }
            return;
        }
        JOptionPane.showMessageDialog(getRef(), "Select a workload descritor element", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadDatabase() {
        if (treeProxyBenchmarks.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(getRef(), "Select one tree element", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        TreePath path = treeProxyBenchmarks.getSelectionPath();
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
        if (selectedNode.getUserObject() instanceof Workload) {
            if (JOptionPane.showConfirmDialog(this, "Are you sure you want to load data in this database?", "oltpbenchadmin", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                DefaultMutableTreeNode proxyConnectionNode = (DefaultMutableTreeNode) selectedNode.getParent().getParent();
                ProxyConnection proxyConnection = (ProxyConnection) proxyConnectionNode.getUserObject();
                Workload workload = (Workload) selectedNode.getUserObject();
                String benchmarkClass = JOptionPane.showInputDialog(getRef(), "Benchmark class: ");
                long samplingWindow = Long.parseLong(JOptionPane.showInputDialog(getRef(), "Sampling window: "));
                String outputFile = JOptionPane.showInputDialog(getRef(), "Output file: ");
                LoadDatabaseCommand command = new LoadDatabaseCommand(workload, benchmarkClass, samplingWindow, outputFile);
                LoadingForm loadingForm = new LoadingForm(getRef());
                loadingForm.execute();
                Result result = proxyConnection.executeCommand(command);
                loadingForm.close();
                if (result != null && result instanceof LoadDatabaseResult && ((LoadDatabaseResult) result).isSuccess()) {
                    addLog(((LoadDatabaseResult) result).getConsoleMessage());
                    addLog("Database successfully loaded");
                    JOptionPane.showMessageDialog(getRef(), "Database successfully loaded", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    addLogError("Failed to load the database");
                    JOptionPane.showMessageDialog(getRef(), "Failed to load the database", "oltpbenchadmin", JOptionPane.ERROR_MESSAGE);
                }
            }
            return;
        }
        JOptionPane.showMessageDialog(getRef(), "Select a workload descritor element", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void downloadSelectedFile() {
        if (treeProxyBenchmarks.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(getRef(), "Select a file", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        TreePath path = treeProxyBenchmarks.getSelectionPath();
        DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
    }

    public MainForm getRef() {
        return this;
    }

    public synchronized void addProxyConnection(ProxyConnection connection) {
        LoadingForm loadingForm = new LoadingForm(getRef());
        loadingForm.execute();
        if (!proxyConnectionList.contains(connection)) {
            proxyConnectionList.add(connection);
            refreshAllProxyConnections();
        }
        loadingForm.close();
        addLog("oltp-proxy is connected");
        JOptionPane.showMessageDialog(getRef(), "oltp-proxy is connected", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
    }

    public synchronized void removeProxyConnection(ProxyConnection connection) {
        LoadingForm loadingForm = new LoadingForm(getRef());
        loadingForm.execute();
        if (proxyConnectionList.contains(connection)) {
            proxyConnectionList.remove(connection);
            refreshAllProxyConnections();
        }
        loadingForm.close();
        addLog("oltp-proxy was disconnected and removed");
        JOptionPane.showMessageDialog(getRef(), "oltp-proxy was disconnected and removed", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
    }

    public synchronized void refreshAllProxyConnections() {
        DefaultTreeModel treeModel = (DefaultTreeModel) treeProxyBenchmarks.getModel();
        treeModel.setRoot(new DefaultMutableTreeNode("benchmark-proxies"));
        DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) treeProxyBenchmarks.getModel().getRoot();
        for (ProxyConnection proxyConnection : proxyConnectionList) {
            DefaultMutableTreeNode proxyNode = new DefaultMutableTreeNode(proxyConnection);
            rootNode.add(proxyNode);
            
            DefaultMutableTreeNode workloadsNode = new DefaultMutableTreeNode("workloads");
            proxyNode.add(workloadsNode);
            for (Workload workload : proxyConnection.getWorkloadList()) {
                DefaultMutableTreeNode workloadNode = new DefaultMutableTreeNode(workload);
                workloadsNode.add(workloadNode);
            }
            
            DefaultMutableTreeNode databaseSystemsNode = new DefaultMutableTreeNode("database systems");
            proxyNode.add(databaseSystemsNode);
            for (DatabaseSystem databaseSystem : proxyConnection.getDatabaseSystemList()) {
                DefaultMutableTreeNode databaseSystemNode = new DefaultMutableTreeNode(databaseSystem);
                databaseSystemsNode.add(databaseSystemNode);
                for (Database database : databaseSystem.getDatabaseList()) {
                    DefaultMutableTreeNode databaseNode = new DefaultMutableTreeNode(database);
                    databaseSystemNode.add(databaseNode);
                }
            }
            
            DefaultMutableTreeNode resultFilesNode = new DefaultMutableTreeNode("result files");
            proxyNode.add(resultFilesNode);
            for (ResultFile resultFile : proxyConnection.getResultFileList()) {
                DefaultMutableTreeNode resultFileNode = new DefaultMutableTreeNode(resultFile);
                resultFilesNode.add(resultFileNode);
            }
        }
        for (ExecutePanel executePanel : executePanelList) {
            executePanel.updateForm();
        }
        for (int i = 0; i < treeProxyBenchmarks.getRowCount(); i++) {
            treeProxyBenchmarks.expandRow(i);
        }
    }

    public static void main(String[] args) {
        MainForm mainForm = new MainForm();
        mainForm.execute();
    }

    public void delay(long timeMillis) {
        LoadingForm form = new LoadingForm(this);
        form.execute();
        long i = System.currentTimeMillis();
        long f = i + timeMillis;
        while (i < f) {
            f = System.currentTimeMillis();
        }
        form.close();
    }
}