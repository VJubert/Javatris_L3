package javatris.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javatris.model.Game;

public class EcouteurTouche implements KeyListener {

	private Game jeu;

	public EcouteurTouche(Game jeu) {
		this.jeu = jeu;
	}

	public void keyPressed(KeyEvent keyEvent) {
		if (!jeu.isPartiePerdue() && jeu.isAnimated()) {
			switch (keyEvent.getKeyCode()) {
			case 40:
				jeu.getGrid().deplacerPieceCouranteEnBas();
				break;
			case 39:
				jeu.getGrid().deplacerPieceCouranteADroite(
						jeu.getFrame().isSon());
				break;
			case 37:
				jeu.getGrid().deplacerPieceCouranteAGauche(
						jeu.getFrame().isSon());
				break;
			case 38:
				jeu.getGrid().tournerPieceCourante(jeu.getEn_cours(),
						jeu.getFrame().isSon());
				break;
			case 32: // barre_espace
				jeu.ajouter_score(jeu.getGrid().descente());
				break;
			}
		}
	}

	public void keyReleased(KeyEvent keyEvent) {
	}

	public void keyTyped(KeyEvent keyEvent) {
	}

}
