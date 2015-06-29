package javatris.model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import javatris.controller.EcouteurMolette;
import javatris.controller.EcouteurSouris;
import javatris.controller.EcouteurTouche;
import javatris.controller.Lançeur;
import javatris.view.DialogHighscore;
import javatris.view.Frame;

public class Game {
	private static int NB_ROWS = 22;
	private static int NB_COLUMNS = 12;
	// fenetre de jeu
	private Frame frame;
	// grille de jeu
	private Grid grid;
	// score de la partie
	private int score;
	// piece en cours
	private Tetromino en_cours;
	// piece suivante
	private Tetromino suivant;
	// Thread qui fait tourner le jeu
	public Thread t;
	// niveau de la partie
	private int niveau;
	// vrai tant que la partie n'est pas perdu
	private boolean partiePerdue;
	// fichier des highscore
	private File file_hscore;
	// score et niveau des highscores
	private int[][] highscore;
	// pseudo des joueurs ayant faire les highscores
	private String[] name_highscore;
	// boolean pour mettre en pause l'animation
	private boolean animated;
	// savoir si la piece en cours est posée ou si elle peut encore descendre
	private boolean en_cours_posée;
	// ligne effacé
	private int ligne;

	public static int getNB_ROWS() {
		return NB_ROWS;
	}

	public static int getNB_COLUMNS() {
		return NB_COLUMNS;
	}

	public Frame getFrame() {
		return frame;
	}

	public Grid getGrid() {
		return grid;
	}

	public int getScore() {
		return score;
	}

	public Tetromino getEn_cours() {
		return en_cours;
	}

	public Tetromino getSuivant() {
		return suivant;
	}

	public Thread getT() {
		return t;
	}

	public int getNiveau() {
		return niveau;
	}

	public boolean isPartiePerdue() {
		return partiePerdue;
	}

	public File getFile_hscore() {
		return file_hscore;
	}

	public int[][] getHighscore() {
		return highscore;
	}

	public String[] getName_highscore() {
		return name_highscore;
	}

	public boolean isAnimated() {
		return animated;
	}

	public boolean isEn_cours_posée() {
		return en_cours_posée;
	}

	/**
	 * 
	 * @param f
	 *            fenetre de jeu
	 * @param lvl_dep
	 *            niveau de départ
	 * @param dens_dep
	 *            nombre de case pré-rempli sur une ligne
	 * @param dens_ligne_dep
	 *            nombre de ligne pré-rempli
	 */
	public Game(Frame f, int lvl_dep, int dens_dep, int dens_ligne_dep) {
		recup_highscore();
		animated = true;
		en_cours_posée = true;
		grid = new Grid(NB_ROWS, NB_COLUMNS);
		score = 0;
		niveau = lvl_dep;
		partiePerdue = false;
		suivant = new Tetromino();
		frame = f;
		frame.init_grid(grid, score, suivant, niveau);
		if (dens_dep != 0 && dens_ligne_dep != 0)
			// si l'un des deux est à 0 c'est inutile de pré-remplir
			// soit on pré-rempli x ligne à 0 (donc vide)
			// soit on rempli 0 ligne à xx%
			grid.pre_remplissage(dens_dep, dens_ligne_dep);
		suivant.addObserver(frame.getSuivantGrid());
		grid.addObserver(frame.getPanelGrid());
		// lancer le jeu
		lancer();
	}
/**
 * fonction principal
 * gère la partie donnée du jeu
 */
	public void dep() {
		//initialisation de ligne_combo (servira à savoir combien on a fait de ligne de suite)
		int ligne_combo = 0;
		//on initialise l'anc_lvl au lvl actuel
		int anc_lvl = niveau;
		//on lance l'animation
		animated = true;
		try {
			while (animated) {
				//tant qu'on n'est aps en pause
				if (en_cours_posée) {
					//si la pièce en cours est posée, on prend la piece suivante
					this.en_cours = suivant.clone();
					if (!grid.genererPiece(en_cours)) {
						//si on peut pas génerer on a perdu
						partiePerdue = true;
						break;
					}
					//on génère la piece suivante
					suivant.genererPiece();
				}
				do {
					//la pause dans l'animation (de base 500 millisecondes, -10 par niveau jusqu'a un minimum de 50 millisecondes
					if (niveau < 45)
						Thread.sleep(500 - niveau * 10);
					else
						Thread.sleep(50);
					//on fait ça tant qu'on est pas en pause et qu'on puisse descendre la piece
				} while (animated && grid.deplacerPieceCouranteEnBas());
				if (grid.peutAllerEnBas()) {
					//si la piece peut encore descendre c'est qu'elle est pas encore posée
					en_cours_posée = false;
				} else {
					//si elle ne peut plus descendre c'est qu'elle est posée
					en_cours_posée = true;
					if (frame.isSon())
						//on joue le son s'il est activé
						Sound.playSound(1);
				}
				//on regarde combien on a fait de ligne
				ligne_combo = grid.effacer_ligne();
				//on ajoute au total de ligne
				ligne += ligne_combo;
				//on sauvegarde l'ancien niveau
				anc_lvl = niveau;
				//on met à jour le niveau
				niveau = ligne / 4;
				if (anc_lvl != niveau)
					if (frame.isSon())
						//si le son est activé et qu'on a level up on joue le son
						Sound.playSound(9);
				//on met à jour le score
				score += (1 + (niveau / 10)) * point_par_ligne(ligne_combo);
				//on fait savoir à la fenetre qu'il faut mettre à jour le score
				frame.update_score(score, niveau);
			}
			if (partiePerdue) {
				if (frame.isSon())
					//si la partie est parti et que le son est activé on joue le son
					Sound.playSound(7);
				//on lance la gestion des highscores
				gestion_highscore();
			}
		} catch (InterruptedException e) {
			//si le thread est interrupted alors sleep renvoie cette erreur et on recoupe le thread en cours
			Thread.currentThread().interrupt();
		}
	}
/**
 * initialisation des listeners
 */
	private void initialiserJeu() {
		frame.getPanelGrid().addKeyListener(new EcouteurTouche(this));
		frame.getPanelGrid().addMouseListener(new EcouteurSouris(this));
		frame.getPanelGrid().addMouseWheelListener(new EcouteurMolette(this));
		ligne = niveau * 4;
		// on prend le focus pour que le clavier fonctionne
		frame.getPanelGrid().requestFocus();
	}

	private int point_par_ligne(int ligne_combo) {
		switch (ligne_combo) {
		case 1:
			// 40 points quand on fait qu'un ligne
			if (frame.isSon())
				Sound.playSound(ligne_combo + 2);
			return 40;
		case 2:
			// 100 points quand on fait 2 lignes à la fois
			if (frame.isSon())
				Sound.playSound(ligne_combo + 2);
			return 100;
		case 3:
			// 300 points quand on fait 3 lignes à la fois
			if (frame.isSon())
				Sound.playSound(ligne_combo + 2);
			return 300;
		case 4:
			// 1200 qunad on 4 lignes à la fois
			if (frame.isSon())
				Sound.playSound(ligne_combo + 2);
			return 1200;
		}
		return 0;
	}

	/**
	 * Ajout de score (lors du hard drop)
	 * @param descente
	 *            nombre de ligne descendu
	 */
	public void ajouter_score(int descente) {
		// quand on fait la descente on ajoute 5 points par lignes descendus
		score += 5 * descente;
	}
/*
 * fonction d'insertion des scores dans les highscores
 */
	private void gestion_highscore() {
		DialogHighscore h = new DialogHighscore(frame);
		int i = highscore.length;
		//on replace le score dans le tableau des highscores
		while (i > 0 && score >= highscore[i - 1][0]) {
			if (score == highscore[i - 1][0]) {
				if (niveau > highscore[i - 1][1]) {
					//si les highscores sont égaux ils sont départagés au lvl
					i--;
				}
			} else {
				if (score > highscore[i - 1][0]) {
					i--;
				}
			}
		}
		//ici i vaut soit 10 si le score n'est pas un highscore, sinon il vaut l'emplacement du score
		for (int j = 8; j >= i; j--) {
			//on décale les highscores si on a besoin d'insérer le notre
			highscore[j + 1][0] = highscore[j][0];
			highscore[j + 1][1] = highscore[j][1];
			name_highscore[j + 1] = name_highscore[j];
		}
		if (i >= highscore.length) {
			//si on n'a pas fait de highscore on affiche la fenetre des highscores
			h.high(i, highscore, name_highscore);
		} else {
			//si on a fait un highscore on affiche et et récupère son pseudo
			highscore[i][0] = score;
			highscore[i][1] = niveau;
			name_highscore[i] = h.high(i, highscore, name_highscore);
		}
		//on lance l'écriture
		write_highscore();
	}
/*
 * fonction d'écriture des highscore
 */
	private void write_highscore() {
		try {
			//on reprend la possibilité d'écrire sur le fichier
			file_hscore.setWritable(true);
			file_hscore = new File("res", "hscore.javatris");
			//on crée le fichier (ne le crée pas s'il existe déjà)
			file_hscore.createNewFile();
			FileWriter fw = new FileWriter(file_hscore);
			for (int i = 0; i < highscore.length; i++) {
				if (!(name_highscore[i] == null)) {
					if (i > 0) {
						//saut de ligne apres la première ligne
						fw.write(System.getProperty("line.separator"));
					}
					//format: abc 10 10
					fw.write(name_highscore[i] + " " + highscore[i][0] + " "
							+ highscore[i][1]);
				}
			}
			fw.close();
			//on empeche l'écriture sur le fichier
			file_hscore.setWritable(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*fonction de récuparation de highscore dans le fichier*/
	private void recup_highscore() {
		try {
			file_hscore = new File("res", "hscore.javatris");
			//10 highscore, la première dimension est pour le score, la seconde pour le niveau
			highscore = new int[10][2];
			name_highscore = new String[10];
			//on crée le fichier (ne le crée pas s'il existe déjà)
			file_hscore.createNewFile();
			Scanner lr = new Scanner(file_hscore);
			int i = 0;
			while (lr.hasNextLine()) {
				name_highscore[i] = lr.next();
				highscore[i][0] = lr.nextInt();
				highscore[i][1] = lr.nextInt();
				i++;
			}
			lr.close();
		} catch (IOException e) {
			System.err.println("Problème de fichier");
		} catch (InputMismatchException e2) {
			System.err.println("Problème dans la lecture des highscore");
		}
	}
	/*ouvrir la fenetre pour voir les highscores*/
	public void consulter_highscore() {
		DialogHighscore h = new DialogHighscore(frame);
		h.voirHighScore(highscore, name_highscore);
	}
	/*lancer le jeu*/
	public void lancer() {
		t = new Thread(new Lançeur(this));
		initialiserJeu();
		if (frame.isSon())
			Sound.playSound(8);
		t.start();
	}
	/*mettre le jeu en pause*/
	public void pause() {
		animated = false;
	}
	/*reprendre le jeu*/
	public void reprendre() {
		animated = true;
		if (!partiePerdue) {
			t = new Thread(new Lançeur(this));
			t.start();
		}
	}
	/*fonction pour vider le tableau des highscores*/
	public void vider_highscore() {
		for (int i = 0; i < 10; i++) {
			highscore[i][0] = 0;
			highscore[i][1] = 0;
			name_highscore[i] = null;
		}
		write_highscore();
	}
	/**
	 * Méthode utilisé lors de la nouvelle partie
	 * @param lvl_dep niveau de départ
	 * @param dens_dep nombre de case pré-rempli dans 1 ligne 
	 * @param dens_ligne_dep nombre de ligne pré-rempli dans la grille
	 */
	public void reset_jeu(int lvl_dep, int dens_dep, int dens_ligne_dep) {
		couper();
		niveau = lvl_dep;
		score = 0;
		ligne = niveau * 4;
		frame.update_score(score, niveau);
		partiePerdue = false;
		suivant.genererPiece();
		grid.reset();
		grid.pre_remplissage(dens_dep, dens_ligne_dep);
		t = new Thread(new Lançeur(this));
		if (frame.isSon())
			Sound.playSound(8);
		t.start();
	}
	/*méthohde pour couper l'affichage*/
	public void couper() {
		t.interrupt();
		animated = false;
		en_cours_posée = true;
	}
}