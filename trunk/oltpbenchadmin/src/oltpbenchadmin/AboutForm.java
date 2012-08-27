/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package oltpbenchadmin;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;
import oltpbenchadmin.icons.Icons;

/**
 *
 * @author Leonardo Oliveira Moreira
 */
public class AboutForm extends JDialog {

    private JButton close;
    private MainForm mainForm;
    private JPanel panelMain;

    public AboutForm(MainForm mainForm) {
        super(mainForm, true);
        this.mainForm = mainForm;
        panelMain = new JPanel(new GridLayout(5, 1));
        close = new JButton("Close");
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
        setTitle("About");
        setSize(400, 200);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setIconImage(new ImageIcon(Icons.class.getResource("wrench.png")).getImage());
        
        JLabel applicationName = new JLabel("oltpbenchadmin");
        applicationName.setHorizontalAlignment(JLabel.CENTER);
        Font applicationNameFont = new Font(applicationName.getFont().getFontName(), Font.BOLD, 16);
        applicationName.setFont(applicationNameFont);
        
        JLabel applicationDescription = new JLabel("A tool for OLTPBenchmark's administration");
        applicationDescription.setHorizontalAlignment(JLabel.CENTER);
        Font applicationDescriptionFont = new Font(applicationDescription.getFont().getFontName(), Font.ITALIC, 16);
        applicationDescription.setFont(applicationDescriptionFont);
        
        JLabel version = new JLabel("Version: 0.1");
        version.setHorizontalAlignment(JLabel.CENTER);
        
        JLabel contact = new JLabel("Contact by cloud@lia.ufc.br");
        contact.setHorizontalAlignment(JLabel.CENTER);
        
        panelMain.add(applicationName);
        panelMain.add(applicationDescription);
        panelMain.add(version);
        panelMain.add(contact);
        JPanel panelClose = new JPanel(new FlowLayout());
        panelClose.add(close);
        panelMain.add(panelClose);
        
        add(panelMain);
    }

    private void events() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                end();
            }
        });
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                end();
            }
        });
    }

    public void end() {
        dispose();
    }

    public AboutForm getRef() {
        return this;
    }

    public void execute() {
        build();
        events();
        setVisible(true);
    }
}