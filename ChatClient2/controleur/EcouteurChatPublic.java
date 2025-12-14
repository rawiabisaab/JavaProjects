package controleur;

import modele.ClientChat;
import vue.PanneauChat;
import java.awt.event.*;

public class EcouteurChatPublic implements ActionListener {

    protected ClientChat client;
    protected PanneauChat panneau;

    public EcouteurChatPublic(ClientChat client, PanneauChat panneau) {
        this.client = client;
        this.panneau = panneau;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = panneau.getChampSaisie().getText().trim();
        if (!msg.isEmpty()) {
            client.envoyer("MSG " + msg);
            panneau.ajouterMessage("MOI>> " + msg);
            panneau.getChampSaisie().setText("");
        }
    }
}