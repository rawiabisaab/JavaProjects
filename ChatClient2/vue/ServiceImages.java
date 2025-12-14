package vue;


import javax.swing.*;
/**
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-10-01
 */
public class ServiceImages {
	public static ImageIcon getIconePourSymbole(char symbole) {
		char couleur;
		String nomFichier = null;
		if (symbole >='a' && symbole <='z')
			couleur = 'n';
		else {
			couleur = 'b';
			symbole += 32; //conversion en minuscule
		}
		switch (symbole) {
			case 'x':
			case 'X':
				nomFichier = "x.png";
				break;
			case 'o':
			case 'O':
				nomFichier = "o.png";
				break;
		}
		System.out.println("Nom fichier de l'image : "+nomFichier);
		if (nomFichier==null)
			return null;
		return new ImageIcon("imgs/"+nomFichier);
	}
	public static ImageIcon getImage(String nomFichier) {
		return new ImageIcon("imgs/"+nomFichier);
	}
}
