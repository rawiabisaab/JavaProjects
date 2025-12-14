package controleur;


import modele.ClientChat;
import javax.swing.*;
import java.awt.event.*;

public class EcouteurListeConnectes extends MouseAdapter {

    private ClientChat client;
    private JList<String> liste;

    public EcouteurListeConnectes(ClientChat client, JList<String> liste) {
        this.client = client;
        this.liste = liste;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            String alias = liste.getSelectedValue();
            if (alias != null) {
                client.envoyer("JOIN " + alias);
            }
        }
    }
}