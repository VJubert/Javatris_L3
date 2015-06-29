package javatris.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javatris.model.*;

public class EcouteurSouris implements MouseListener {

	private Game jeu;

	public EcouteurSouris(Game jeu) {
		this.jeu = jeu;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!jeu.isPartiePerdue() && jeu.isAnimated()) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				int[][] coord = jeu.getGrid().getCoord();

				// on cherche la plus petite abcisse et la plus grande abcisse
				// de la pièce courante
				int xMinPiece = coord[0][1];
				int xMaxPiece = coord[0][1];

				for (int i = 0; i < 4; i++) {
					for (int j = 0; j < 2; j++) {
						if (j == 1) {// il y a une abcisse dans cette case
							if (coord[i][j] < xMinPiece) {
								xMinPiece = coord[i][j];
							}
							if (coord[i][j] > xMaxPiece) {
								xMaxPiece = coord[i][j];
							}
						}
					}
				}

				// pour ensuite les comparer avec la position du curseur de la
				// souris
				int xSouris = e.getX() / 30;// 30 est la taille d'un carreau

				if (xSouris < xMinPiece) {
					jeu.getGrid().deplacerPieceCouranteAGauche(
							jeu.getFrame().isSon());
				} else if (xSouris > xMaxPiece) {
					jeu.getGrid().deplacerPieceCouranteADroite(
							jeu.getFrame().isSon());
				}
			}

			if (e.getButton() == MouseEvent.BUTTON2) {// clic molette, descendre
														// la pièce directement
				jeu.ajouter_score(jeu.getGrid().descente());
			}

			if (e.getButton() == MouseEvent.BUTTON3) {// clic droit, pivoter la
														// pièce
				jeu.getGrid().tournerPieceCourante(jeu.getEn_cours(),
						jeu.getFrame().isSon());
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

}
