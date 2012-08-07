/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin;

import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import oltpbenchadmin.comm.ProxyConnection;
import oltpbenchadmin.commons.TransactionType;
import oltpbenchadmin.commons.Work;
import oltpbenchadmin.commons.Workload;
import oltpbenchadmin.commons.commands.Result;
import oltpbenchadmin.commons.commands.SaveWorkloadDescriptorCommand;
import oltpbenchadmin.commons.commands.SaveWorkloadDescriptorResult;
import oltpbenchadmin.icons.Icons;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public class WorkloadDescriptorForm extends JDialog {

    private JComboBox dbType;
    private JTextField driver;
    private JTextField dbUrl;
    private JTextField username;
    private JTextField password;
    private JComboBox isolation;
    private JTextField scaleFactor;
    private JTextField terminals;
    private JTable works;
    private JButton addWork, removeWork;
    private JTable transactionTypes;
    private JButton addTransactionType, removeTransactionType;
    private JButton save, cancel;
    private MainForm mainForm;
    private JPanel panelMain;
    private Workload workload;
    private ProxyConnection proxyConnection;

    public WorkloadDescriptorForm(MainForm mainForm, Workload workload, ProxyConnection proxyConnection) {
        super(mainForm, true);
        this.mainForm = mainForm;
        this.workload = workload;
        this.proxyConnection = proxyConnection;
        panelMain = new JPanel(null);

        dbType = new JComboBox(new String[]{"mysql", "postgres"});
        driver = new JTextField();
        dbUrl = new JTextField();
        username = new JTextField();
        password = new JTextField();
        isolation = new JComboBox(new String[]{"TRANSACTION_SERIALIZABLE"});
        scaleFactor = new JTextField();
        terminals = new JTextField();
        works = new JTable();
        addWork = new JButton("Add");
        removeWork = new JButton("Remove");
        transactionTypes = new JTable();
        addTransactionType = new JButton("Add");
        removeTransactionType = new JButton("Remove");
        save = new JButton("Save");
        cancel = new JButton("Cancel");
    }
    
    @Override
    protected JRootPane createRootPane() {
        ActionListener actionListener = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                end();
            }
        };
        KeyStroke stroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        JRootPane rootPane = new JRootPane();
        rootPane.registerKeyboardAction(actionListener, stroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
        return rootPane;
    }

    private void build() {
        setTitle("Workload Descriptor" + (workload == null ? "" : " - [" + workload.getFileAbsolutePath().substring(workload.getBenchmarkPath().length()) + "]"));
        setSize(800, 630);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setIconImage(new ImageIcon(Icons.class.getResource("page_white_wrench.png")).getImage());

        works.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        works.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultTableModel worksTableModel = (DefaultTableModel) works.getModel();
        worksTableModel.addColumn("Time");
        worksTableModel.addColumn("Rate");
        worksTableModel.addColumn("Weights");

        transactionTypes.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        transactionTypes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        DefaultTableModel transactionTypesTableModel = (DefaultTableModel) transactionTypes.getModel();
        transactionTypesTableModel.addColumn("Name");

        int y = 10;

        JLabel labelTemp;

        labelTemp = new JLabel("Workload Configuration: ");
        labelTemp.setOpaque(true);
        labelTemp.setSize(150, 25);
        labelTemp.setLocation(10, y);
        panelMain.add(labelTemp);

        y += 30;

        labelTemp = new JLabel("Database type: ");
        labelTemp.setOpaque(true);
        labelTemp.setSize(100, 25);
        labelTemp.setLocation(10, y);
        panelMain.add(labelTemp);

        dbType.setSize(300, 25);
        dbType.setLocation(120, y);
        dbType.setEditable(false);
        panelMain.add(dbType);

        y += 30;

        labelTemp = new JLabel("Database driver: ");
        labelTemp.setOpaque(true);
        labelTemp.setSize(100, 25);
        labelTemp.setLocation(10, y);
        panelMain.add(labelTemp);

        driver.setSize(300, 25);
        driver.setLocation(120, y);
        panelMain.add(driver);

        y += 30;

        labelTemp = new JLabel("Database driver: ");
        labelTemp.setOpaque(true);
        labelTemp.setSize(100, 25);
        labelTemp.setLocation(10, y);
        panelMain.add(labelTemp);

        dbUrl.setSize(300, 25);
        dbUrl.setLocation(120, y);
        panelMain.add(dbUrl);

        y += 30;

        labelTemp = new JLabel("Username: ");
        labelTemp.setOpaque(true);
        labelTemp.setSize(100, 25);
        labelTemp.setLocation(10, y);
        panelMain.add(labelTemp);

        username.setSize(150, 25);
        username.setLocation(120, y);
        panelMain.add(username);

        labelTemp = new JLabel("Password: ");
        labelTemp.setOpaque(true);
        labelTemp.setSize(70, 25);
        labelTemp.setLocation(290, y);
        panelMain.add(labelTemp);

        password.setSize(150, 25);
        password.setLocation(370, y);
        panelMain.add(password);

        y += 30;

        labelTemp = new JLabel("Isolation: ");
        labelTemp.setOpaque(true);
        labelTemp.setSize(100, 25);
        labelTemp.setLocation(10, y);
        panelMain.add(labelTemp);

        isolation.setSize(300, 25);
        isolation.setLocation(120, y);
        panelMain.add(isolation);

        y += 30;

        labelTemp = new JLabel("Scale factor: ");
        labelTemp.setOpaque(true);
        labelTemp.setSize(100, 25);
        labelTemp.setLocation(10, y);
        panelMain.add(labelTemp);

        scaleFactor.setSize(150, 25);
        scaleFactor.setLocation(120, y);
        panelMain.add(scaleFactor);

        y += 30;

        labelTemp = new JLabel("Terminals: ");
        labelTemp.setOpaque(true);
        labelTemp.setSize(100, 25);
        labelTemp.setLocation(10, y);
        panelMain.add(labelTemp);

        terminals.setSize(150, 25);
        terminals.setLocation(120, y);
        panelMain.add(terminals);

        y += 30;

        labelTemp = new JLabel("Works: ");
        labelTemp.setOpaque(true);
        labelTemp.setSize(100, 25);
        labelTemp.setLocation(10, y);
        panelMain.add(labelTemp);

        y += 25;

        JScrollPane scrollWorks = new JScrollPane(works);
        scrollWorks.setSize(690, 120);
        scrollWorks.setLocation(10, y);
        panelMain.add(scrollWorks);

        addWork.setSize(80, 55);
        addWork.setLocation(705, y);
        addWork.setIcon(new ImageIcon(Icons.class.getResource("add.png")));
        addWork.setVerticalTextPosition(SwingConstants.BOTTOM);
        addWork.setHorizontalTextPosition(SwingConstants.CENTER);
        panelMain.add(addWork);

        removeWork.setSize(80, 55);
        removeWork.setLocation(705, y + 60);
        removeWork.setIcon(new ImageIcon(Icons.class.getResource("delete.png")));
        removeWork.setVerticalTextPosition(SwingConstants.BOTTOM);
        removeWork.setHorizontalTextPosition(SwingConstants.CENTER);
        panelMain.add(removeWork);

        y += 120;

        labelTemp = new JLabel("Transaction types: ");
        labelTemp.setOpaque(true);
        labelTemp.setSize(150, 25);
        labelTemp.setLocation(10, y);
        panelMain.add(labelTemp);

        y += 25;

        JScrollPane scrollTransactionTypes = new JScrollPane(transactionTypes);
        scrollTransactionTypes.setSize(690, 120);
        scrollTransactionTypes.setLocation(10, y);
        panelMain.add(scrollTransactionTypes);

        addTransactionType.setSize(80, 55);
        addTransactionType.setLocation(705, y);
        addTransactionType.setIcon(new ImageIcon(Icons.class.getResource("add.png")));
        addTransactionType.setVerticalTextPosition(SwingConstants.BOTTOM);
        addTransactionType.setHorizontalTextPosition(SwingConstants.CENTER);
        panelMain.add(addTransactionType);

        removeTransactionType.setSize(80, 55);
        removeTransactionType.setLocation(705, y + 60);
        removeTransactionType.setIcon(new ImageIcon(Icons.class.getResource("delete.png")));
        removeTransactionType.setVerticalTextPosition(SwingConstants.BOTTOM);
        removeTransactionType.setHorizontalTextPosition(SwingConstants.CENTER);
        panelMain.add(removeTransactionType);

        y += 120;

        save.setSize(100, 30);
        save.setLocation(575, y + 20);
        save.setIcon(new ImageIcon(Icons.class.getResource("disk.png")));
        panelMain.add(save);

        cancel.setSize(100, 30);
        cancel.setLocation(685, y + 20);
        cancel.setIcon(new ImageIcon(Icons.class.getResource("cancel.png")));
        panelMain.add(cancel);

        add(panelMain);
    }

    private void events() {
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                end();
            }
        });

        addWork.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel worksTableModel = (DefaultTableModel) works.getModel();
                worksTableModel.addRow(new String[]{"", "", ""});
            }
        });

        removeWork.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel worksTableModel = (DefaultTableModel) works.getModel();
                int selectedRow = works.getSelectedRow();
                if (selectedRow > -1) {
                    worksTableModel.removeRow(selectedRow);
                }
            }
        });

        addTransactionType.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel transactionTypesTableModel = (DefaultTableModel) transactionTypes.getModel();
                transactionTypesTableModel.addRow(new String[]{""});
            }
        });

        removeTransactionType.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel transactionTypesTableModel = (DefaultTableModel) transactionTypes.getModel();
                int selectedRow = transactionTypes.getSelectedRow();
                if (selectedRow > -1) {
                    transactionTypesTableModel.removeRow(selectedRow);
                }
            }
        });

        cancel.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                end();
            }
        });
        save.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (workload != null) {
                    getData();
                    SaveWorkloadDescriptorCommand command = new SaveWorkloadDescriptorCommand(workload);
                    Result result = proxyConnection.executeCommand(command);
                    if (result != null && result instanceof SaveWorkloadDescriptorResult && ((SaveWorkloadDescriptorResult) result).isSaved()) {
                        JOptionPane.showMessageDialog(getRef(), "Workload Descriptor saved successfully", "oltpbtool", JOptionPane.INFORMATION_MESSAGE);
                        end();
                        return;
                    }
                }
                mainForm.addLogError("Failed to save Workload Descriptor");
                JOptionPane.showMessageDialog(getRef(), "Failed to save Workload Descriptor", "oltpbtool", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    public WorkloadDescriptorForm getRef() {
        return this;
    }

    private void getData() {
        if (workload != null) {
            workload.setDbType(this.dbType.getSelectedItem().toString());
            workload.setDriver(this.driver.getText());
            workload.setDbUrl(this.dbUrl.getText());
            workload.setUsername(this.username.getText());
            workload.setPassword(this.password.getText());
            workload.setIsolation(this.isolation.getSelectedItem().toString());
            workload.setScaleFactor(Integer.parseInt(this.scaleFactor.getText()));
            workload.setTerminals(Integer.parseInt(this.terminals.getText()));
            workload.setWorks(new ArrayList<Work>());
            for (int row = 0; row < works.getRowCount(); row++) {
                Work work = new Work();
                work.setTime(Long.parseLong(works.getValueAt(row, 0).toString()));
                work.setRate(Long.parseLong(works.getValueAt(row, 1).toString()));
                work.setWeights(works.getValueAt(row, 2).toString());
                workload.getWorks().add(work);
            }
            workload.setTransactionTypes(new ArrayList<TransactionType>());
            for (int row = 0; row < transactionTypes.getRowCount(); row++) {
                TransactionType transactionType = new TransactionType();
                transactionType.setName(transactionTypes.getValueAt(row, 0).toString());
                workload.getTransactionTypes().add(transactionType);
            }
        }
    }
    
    private void setData() {
        if (workload != null) {
            this.dbType.setSelectedItem(workload.getDbType());
            this.driver.setText(workload.getDriver());
            this.dbUrl.setText(workload.getDbUrl());
            this.username.setText(workload.getUsername());
            this.password.setText(workload.getPassword());
            this.isolation.setSelectedItem(workload.getIsolation());
            this.scaleFactor.setText(String.valueOf(workload.getScaleFactor()));
            this.terminals.setText(String.valueOf(workload.getTerminals()));
            DefaultTableModel worksTableModel = (DefaultTableModel) works.getModel();
            for (Work work : workload.getWorks()) {
                worksTableModel.addRow(new String[]{String.valueOf(work.getTime()), String.valueOf(work.getRate()), work.getWeights()});
            }
            DefaultTableModel transactionTypesTableModel = (DefaultTableModel) transactionTypes.getModel();
            for (TransactionType transactionType : workload.getTransactionTypes()) {
                transactionTypesTableModel.addRow(new String[]{transactionType.getName()});
            }
        }
    }

    public void end() {
        dispose();
    }

    public void execute() {
        build();
        events();
        setData();
        setVisible(true);
    }
}