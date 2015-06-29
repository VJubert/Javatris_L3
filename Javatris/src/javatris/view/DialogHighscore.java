package javatris.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
/**
 * classe qui sert à l'affichage des highscores dans une fenetre de dialogue (jdialog)
 * on supprime les espaces des pseudo pour la normalisation du fichier highscore où les espaces sont les séparateurs
 * @author ValFac
 *
 */
@SuppressWarnings("serial")
public class DialogHighscore extends JDialog {
	/*savoir s'il faut envoyé les données*/
	boolean a_envoye;
//constructeur de la jdialog
	public DialogHighscore(Frame parent) {
		super(parent, "Highscore", true);
		//on centre la fenetre sur le centre de la fenetre parent
		this.setLocationRelativeTo(parent);
		//on empeche le redimensionnement
		this.setResizable(false);
		a_envoye = false;
	}
	//méthode d'affichage des highscores
	public void voirHighScore(int[][] highscore, String[] name) {
		//on appelle la 2eme fonction qui fait
		high(highscore.length, highscore, name);
	}
	/**
	 * méthode d'affichage des highscores
	 * @param k la position du score
	 * @param highscore les scores
	 * @param name les noms
	 * @return le nom du joueur ou "abc"
	 */
	public String high(int k, int[][] highscore, String[] name) {
		//le champ de texte qui demande le nom avec comme nom par défaut abc
		final JTextField ask_name = new JTextField("Abc");
		JPanel cont_name = new JPanel();
		cont_name.setLayout(new GridLayout(10, 1));
		cont_name.setPreferredSize(new Dimension(80, 200));
		//compteur pour savoir combien de highscore il y a
		int i = 0;
		cont_name.setBorder(BorderFactory.createTitledBorder("Pseudo :"));
		for (int j = 0; j < name.length; j++) {
			if (j == k) {
				//on ajoute le champ de texte
				cont_name.add(ask_name);
				//on compte le nombre de noms qu'il y a
				i++;
			} else {
				if (name[j] != null) {
					//on compte le nombre de noms qu'il y a
					i++;
					//on ajoute le nom dans un label qu'on ajoute dans le panel
					cont_name.add(new JLabel(name[j]));
				}
			}
		}
		JPanel cont_score = new JPanel();
		cont_score.setLayout(new GridLayout(10, 1));
		cont_score.setPreferredSize(new Dimension(80, 200));
		cont_score.setBorder(BorderFactory.createTitledBorder("Score :"));
		JLabel l;
		//on affiche les scores (jusqu'a i)
		for (int j = 0; j < i; j++) {
			l = new JLabel(highscore[j][0] + "");
			l.setSize(25, 300);
			cont_score.add(l);
		}
		JPanel cont_lvl = new JPanel();
		cont_lvl.setLayout(new GridLayout(10, 1));
		cont_lvl.setPreferredSize(new Dimension(80, 200));
		cont_lvl.setBorder(BorderFactory.createTitledBorder("Niveau :"));
		//on affiche les scores (jusqu'a i)
		for (int j = 0; j < i; j++) {
			cont_lvl.add(new JLabel(highscore[j][1] + ""));
		}
		//bouton ok qui envoie les noms
		JButton okBouton = new JButton("Ok");
		okBouton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean g = true;
				if (espace(ask_name.getText())) {
					//s'il y a un espace dans le nom on affiche un avertissement
					g = avertissement();
				}
				if (g) {
					a_envoye = true;
					setVisible(false);
				}
			}
		});
		//bouton annuler qui sort de la fenetre sans rien faire
		JButton cancelBouton = new JButton("Annuler");
		cancelBouton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}
		});
		JPanel cont = new JPanel();
		cont.add(cont_name);
		cont.add(cont_score);
		cont.add(cont_lvl);
		JPanel button = new JPanel();
		button.add(okBouton);
		button.add(cancelBouton);
		this.add(cont, BorderLayout.CENTER);
		this.add(button, BorderLayout.SOUTH);
		//on redimensionne et on rend visible
		this.pack();
		this.setVisible(true);
		//si on veut envoyer on supprime les espaces du noms et on envoie, sinon on envoie "abc"
		if (a_envoye)
			return del_space(ask_name.getText());
		else
			return "Abc";

	}
	//remplace les espaces par des underscores
	private static String del_space(String text) {
		return text.replace(' ', '_');
	}
	//teste si une string contient un espace
	private static boolean espace(String t) {
		return t.contains(" ");
	}
	// affiche un avertissement pour savoir s'il veut garder son pseudo avec espace
	private boolean avertissement() {
		int option = JOptionPane.showConfirmDialog(
				this,
				"Votre pseudo contient des espaces qui seront remplacés par des underscore."
						+ System.getProperty("line.separator")
						+ System.getProperty("line.separator")
						+ "Voulez vous continuez ?", "Confirmation",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
		return option == JOptionPane.OK_OPTION;
	}
}
