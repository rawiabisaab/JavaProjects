package vue;

import javax.swing.*;
import java.awt.*;

public class PanneauChat extends JPanel {

    protected JTextArea zoneChat;
    protected JTextField champSaisie;

    public PanneauChat() {
        setLayout(new BorderLayout());
        zoneChat = new JTextArea();
        zoneChat.setEditable(false);
        add(new JScrollPane(zoneChat), BorderLayout.CENTER);
        champSaisie = new JTextField();
        add(champSaisie, BorderLayout.SOUTH);
    }

    public void ajouterMessage(String msg) {
        zoneChat.append(msg + "\n");
    }

    public JTextField getChampSaisie() {
        return champSaisie;
    }

    public void setEcouteur(java.awt.event.ActionListener e) {
        champSaisie.addActionListener(e);
    }
}