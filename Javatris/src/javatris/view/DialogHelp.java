package javatris.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 * classe qui sert à afficher l'aide contenu dans le fichier "aide.javatris"
 * @author ValFac
 *
 */
@SuppressWarnings("serial")
public class DialogHelp extends JDialog {

	public DialogHelp(Frame parent) {
		super(parent, "Aide", true);
		//on centre sur le parent
		this.setLocationRelativeTo(parent);
		//on empeche le redimensionement
		this.setResizable(false);
		try {
			//on ouvre le fichier
			File f = new File("res"+System.getProperty("file.separator")+"aide.javatris");
			//on le crée s'il n'est pas déjà présent
			f.createNewFile();
			Scanner lire = new Scanner(f);
			String s = "";
			while (lire.hasNext()) {
				//on affiche chaque ligne + un saut de ligne
				s += lire.nextLine() + "<br>";
			}
			add(new JLabel("<html>" + s + "</html>"), BorderLayout.CENTER);
			lire.close();
		} catch (FileNotFoundException e){
			//en cas de probleme on affiche fichier non trouvé
			add(new JLabel("Fichier non trouvé"),
					BorderLayout.CENTER);
		} catch (IOException e) {
			//en cas de probleme on affiche erreur de lecture
			add(new JLabel("Erreur lors de la lecture du fichier aide"),
					BorderLayout.CENTER);
		}
		//on ajoute le bouton retour qui ferme la fenetre
		JButton b = new JButton("Retour");
		b.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);

			}
		});
		JPanel p = new JPanel();
		p.add(b);
		add(p, BorderLayout.SOUTH);
		//on redimensionne et on affiche
		pack();
		setVisible(true);

	}
}
