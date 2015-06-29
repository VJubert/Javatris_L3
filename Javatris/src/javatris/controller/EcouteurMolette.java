package javatris.controller;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javatris.model.Game;

public class EcouteurMolette implements MouseWheelListener {

	private Game jeu;

	public EcouteurMolette(Game jeu) {
		this.jeu = jeu;
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		if (!jeu.isPartiePerdue() && jeu.isAnimated()) {
			if (e.getWheelRotation() > 0) {// scroll en bas
				jeu.getGrid().deplacerPieceCouranteEnBas();
			}
		}
	}

}
