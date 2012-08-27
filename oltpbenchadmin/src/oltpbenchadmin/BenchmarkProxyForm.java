/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin;

import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;
import oltpbenchadmin.comm.ProxyConnection;
import oltpbenchadmin.icons.Icons;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public class BenchmarkProxyForm extends JDialog {

    private JTextField host;
    private JTextField port;
    private JButton connect, cancel;
    private MainForm mainForm;
    private JPanel panelMain;

    public BenchmarkProxyForm(MainForm mainForm) {
        super(mainForm, true);
        this.mainForm = mainForm;
        panelMain = new JPanel(null);
        host = new JTextField();
        port = new JTextField();
        connect = new JButton("Connect");
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
        setTitle("Benchmark Proxy Connect");
        setSize(435, 190);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setIconImage(new ImageIcon(Icons.class.getResource("computer.png")).getImage());

        int y = 10;
        JLabel labelTemp;
        labelTemp = new JLabel("Benchmark Proxy Configuration: ");

        labelTemp.setOpaque(true);
        labelTemp.setSize(300, 25);
        labelTemp.setLocation(10, y);
        panelMain.add(labelTemp);
        y += 30;
        labelTemp = new JLabel("Host: ");

        labelTemp.setOpaque(true);
        labelTemp.setSize(100, 25);
        labelTemp.setLocation(10, y);
        panelMain.add(labelTemp);

        host.setSize(300, 25);
        host.setLocation(120, y);
        panelMain.add(host);
        y += 30;
        labelTemp = new JLabel("Port: ");

        labelTemp.setOpaque(true);
        labelTemp.setSize(100, 25);
        labelTemp.setLocation(10, y);
        panelMain.add(labelTemp);

        port.setSize(100, 25);
        port.setLocation(120, y);
        panelMain.add(port);
        y += 30;

        connect.setSize(110, 30);
        connect.setLocation(190, y + 20);
        connect.setIcon(new ImageIcon(Icons.class.getResource("connect.png")));
        panelMain.add(connect);

        cancel.setSize(110, 30);
        cancel.setLocation(310, y + 20);
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

        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                end();
            }
        });
        connect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    end();
                    ProxyConnection connection = new ProxyConnection(host.getText(), Integer.parseInt(port.getText()), mainForm);
                    mainForm.addProxyConnection(connection);
                    connection.start();
                } catch (IOException ex) {
                    mainForm.addLogError(ex.getMessage());
                    JOptionPane.showMessageDialog(getRef(), ex.getMessage(), "oltpbenchadmin", JOptionPane.ERROR_MESSAGE);
                    setVisible(true);
                }
            }
        });
    }

    public void end() {
        dispose();
    }

    public BenchmarkProxyForm getRef() {
        return this;
    }

    public void execute() {
        build();
        events();
        setVisible(true);
    }
}