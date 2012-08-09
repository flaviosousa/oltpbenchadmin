/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import oltpbenchadmin.comm.ProxyConnection;
import oltpbenchadmin.commons.Constants;
import oltpbenchadmin.commons.ExecuteConfiguration;
import oltpbenchadmin.commons.Workload;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public class ExecutePanel extends JPanel {

    private String title;
    private JCheckBox execute;
    private JComboBox proxy;
    private JComboBox workload;
    private JComboBox benchmarkClass;
    private JTextField samplingWindow;
    private JTextField outputFile;
    private List<ProxyConnection> proxyConnectionList;
    private MainForm mainForm;
    private JButton downloadResultFile;

    public ExecutePanel(MainForm mainForm, String title, List<ProxyConnection> proxyConnectionList) {
        super(null);
        this.mainForm = mainForm;
        this.title = title;
        this.proxyConnectionList = proxyConnectionList;
        proxy = new JComboBox();
        execute = new JCheckBox();
        benchmarkClass = new JComboBox();
        workload = new JComboBox();
        samplingWindow = new JTextField();
        outputFile = new JTextField();
        downloadResultFile = new JButton("Download Result File");
        build();
        events();
    }

    private void build() {
        int y = 10;

        JLabel labelTemp;

        labelTemp = new JLabel("Execute Configuration: ");
        labelTemp.setOpaque(true);
        labelTemp.setSize(150, 25);
        labelTemp.setLocation(10, y);
        add(labelTemp);

        y += 35;

        labelTemp = new JLabel("Execute: ");
        labelTemp.setOpaque(true);
        labelTemp.setSize(100, 25);
        labelTemp.setLocation(10, y);
        add(labelTemp);

        execute.setSize(300, 25);
        execute.setLocation(150, y);
        execute.setSelected(true);
        add(execute);

        y += 35;

        labelTemp = new JLabel("Proxy: ");
        labelTemp.setOpaque(true);
        labelTemp.setSize(100, 25);
        labelTemp.setLocation(10, y);
        add(labelTemp);

        proxy.setSize(400, 25);
        proxy.setLocation(150, y);
        add(proxy);

        y += 35;

        labelTemp = new JLabel("Workload: ");
        labelTemp.setOpaque(true);
        labelTemp.setSize(100, 25);
        labelTemp.setLocation(10, y);
        add(labelTemp);

        workload.setSize(400, 25);
        workload.setLocation(150, y);
        add(workload);

        y += 35;

        labelTemp = new JLabel("Benchmark class: ");
        labelTemp.setOpaque(true);
        labelTemp.setSize(120, 25);
        labelTemp.setLocation(10, y);
        add(labelTemp);

        benchmarkClass.setSize(300, 25);
        benchmarkClass.setLocation(150, y);
        add(benchmarkClass);

        y += 35;

        labelTemp = new JLabel("Sampling window: ");
        labelTemp.setOpaque(true);
        labelTemp.setSize(120, 25);
        labelTemp.setLocation(10, y);
        add(labelTemp);

        samplingWindow.setSize(300, 25);
        samplingWindow.setLocation(150, y);
        add(samplingWindow);

        y += 35;

        labelTemp = new JLabel("Output file: ");
        labelTemp.setOpaque(true);
        labelTemp.setSize(120, 25);
        labelTemp.setLocation(10, y);
        add(labelTemp);

        outputFile.setSize(300, 25);
        outputFile.setLocation(150, y);
        add(outputFile);

        y += 35;

        downloadResultFile.setSize(300, 25);
        downloadResultFile.setLocation(150, y);
        downloadResultFile.setVisible(false);
        add(downloadResultFile);

        for (String bc : Constants.BENCHMARK_CLASSES) {
            benchmarkClass.addItem(bc);
        }
        updateForm();
    }

    public void updateForm() {
        downloadResultFile.setVisible(false);
        execute.setSelected(true);
        proxy.removeAllItems();
        while (proxy.getItemCount() > 0) {
            proxy.removeItemAt(0);
        }
        for (ProxyConnection pc : proxyConnectionList) {
            proxy.addItem(pc);
        }
        workload.removeAllItems();
        while (workload.getItemCount() > 0) {
            workload.removeItemAt(0);
        }
        ProxyConnection pc = (ProxyConnection) proxy.getSelectedItem();
        for (Workload w : pc.getWorkloadList()) {
            workload.addItem(w);
        }
        benchmarkClass.setSelectedIndex(0);
        samplingWindow.setText("");
        outputFile.setText("");
    }

    private void events() {
        proxy.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                workload.removeAllItems();
                while (workload.getItemCount() > 0) {
                    workload.removeItemAt(0);
                }
                ProxyConnection pc = (ProxyConnection) proxy.getSelectedItem();
                for (Workload w : pc.getWorkloadList()) {
                    workload.addItem(w);
                }
            }
        });

        downloadResultFile.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });
    }

    public String getTitle() {
        return title;
    }

    public ExecuteConfiguration getExecuteConfiguration() {
        downloadResultFile.setVisible(false);
        if (execute.isSelected()) {
            ExecuteConfiguration ec = new ExecuteConfiguration();
            ec.setProxyConnectionString(((ProxyConnection) proxy.getSelectedItem()).toString());
            ec.setWorkload((Workload) workload.getSelectedItem());
            ec.setBenchmarkClass(benchmarkClass.getSelectedItem().toString());
            ec.setSamplingWindow(Long.parseLong(samplingWindow.getText()));
            ec.setOutputFile(outputFile.getText());
            return ec;
        }
        return null;
    }

    public boolean validateForm() {
        downloadResultFile.setVisible(false);
        if (samplingWindow.getText().trim().length() == 0 || !samplingWindow.getText().matches("[0-9]+")) {
            JOptionPane.showMessageDialog(mainForm, "Sampling window is obligatory and should be a number", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        if (outputFile.getText().trim().length() == 0) {
            JOptionPane.showMessageDialog(mainForm, "Output file is obligatory", "oltpbenchadmin", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        return true;
    }
    
    public void setVisibleDownloadResultFile() {
        downloadResultFile.setVisible(true);
    }
}