package javatris.controller;

import javatris.model.Game;

public class Lançeur implements Runnable {
	private Game g;

	public Lançeur(Game g) {
		this.g = g;
	}

	@Override
	public void run() {
		//lancer le jeu quand le Thread démarre
		g.dep();

	}

}
