package controleur;

import com.chat.client.ClientChat;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EcouteurTicTacToe implements ActionListener {

    private ClientChat clientChat;

    public EcouteurTicTacToe(ClientChat clientChat) {
        this.clientChat = clientChat;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton bouton = (JButton) e.getSource();
        String frappe = bouton.getActionCommand();
        int i, j;
        String[] parties = frappe.split(" ");
        i = Integer.parseInt(parties[0]);
        j = Integer.parseInt(parties[1]);

        clientChat.envoyer("COUP " + i + " " + j);


    }
}
