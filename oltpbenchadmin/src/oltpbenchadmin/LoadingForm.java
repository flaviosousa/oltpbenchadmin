/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import oltpbenchadmin.icons.Icons;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public class LoadingForm extends JDialog {

    private MainForm mainForm;
    private JPanel panelMain;
    private boolean closed = false;

    public LoadingForm(MainForm mainForm) {
        super(mainForm, true);
        this.mainForm = mainForm;
        panelMain = new JPanel(new BorderLayout());
    }

    public void close() {
        Cursor cursor = Cursor.getDefaultCursor();
        mainForm.setCursor(cursor);
    }

    public void execute() {
        Cursor cursor = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
        mainForm.setCursor(cursor);
    }

    private void build() {
        setTitle("oltpbtool - Loading...");
        setSize(500, 130);
        setAlwaysOnTop(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setIconImage(new ImageIcon(Icons.class.getResource("wrench.png")).getImage());

        JLabel labelTemp;

        labelTemp = new JLabel(new ImageIcon(Icons.class.getResource("loading.gif")));
        labelTemp.setOpaque(true);
        panelMain.add(labelTemp, BorderLayout.CENTER);

        labelTemp = new JLabel("Loading...");
        labelTemp.setOpaque(true);
        labelTemp.setHorizontalAlignment(JLabel.CENTER);
        labelTemp.setFont(new Font(Font.DIALOG, Font.PLAIN, 22));
        panelMain.add(labelTemp, BorderLayout.SOUTH);

        add(panelMain);
    }

    private void events() {
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

    }
}