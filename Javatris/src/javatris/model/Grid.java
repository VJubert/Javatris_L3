package javatris.model;

import java.util.Observable;
import java.util.Random;

public class Grid extends Observable {

	private int nb_rows;
	private int nb_columns;
	//la grille de jeu
	private int[][] grid;
	//les coordonnées de la pièce en cours
	private int[][] coord;// en 0 c'est la ligne, en 1 la colonne
	
	/*creation d'une grille avec le nombre de ligne et colones*/
	public Grid(int nB_ROWS, int nB_COLUMNS) {
		nb_rows = nB_ROWS;
		nb_columns = nB_COLUMNS;
		grid = new int[nB_ROWS][nB_COLUMNS];
		//4 cases pour une piece, 2 coordonnées (x et y)
		coord = new int[4][2];
		this.initialize();
	}

	public int[][] getGrid() {
		return grid;
	}

	public int[][] getCoord() {
		return coord;
	}
	/**
	 * Création d'une grille en recopie d'une autre grille
	 * La fonction est utilisé pour faire les grilles des pièces suivantes
	 * @param actif la grille à recopier
	 * @param ajus l'ajustement à faire (si la pièce est dans une grille 3*3 on crée une grille 4*4 pour uniformiser l'affichage)
	 */
	public Grid(int[][] actif, boolean ajus) {
		int i = 0;
		if (ajus)
			i = 1;
		//nombre de ligne et de colone + les 2 bords + l'ajustement si besoin
		nb_rows = actif.length + 2 + i;
		nb_columns = actif[0].length + 2 + i;
		grid = new int[nb_rows][nb_columns];
		// faire la bordure
		makeBorder();
		//faire l'intérieur (avec l'ajustement si besoin)
		for (int row = 1; row < nb_rows - 1 - i; row++) {
			for (int column = 1; column < nb_columns - 1 - i; column++) {
				this.grid[row + i][column + i] = actif[row - 1][column - 1];
			}
		}
		//si on a eu besoin de l'ajustement il faut remplir les cases en plus
		if (ajus) {
			for (int j = 1; i < nb_rows - 2; i++) {
				grid[j][1] = 0;
				grid[1][j] = 0;
			}
		}
	}

	public int getContent(int row, int column) {
		return this.grid[row][column];
	}

    /* Initialisation de la grille */
	public void initialize() {
         /* Initialisation de la grille en mettant un marqueur neutre partout càd 0 */
		for (int row = 0; row < nb_rows; row++) {
			for (int column = 0; column < nb_columns; column++) {
				this.grid[row][column] = 0;
			}
		}
        // Délimitation de la bordure autour de la grille
		makeBorder();
	}

    // Place -1 sur le périmètre de la grille pour délimiter les limites de la grille
	public void makeBorder() {
		for (int column = 0; column < nb_columns; column++) {
			this.grid[0][column] = -1;
			this.grid[nb_rows - 1][column] = -1;
		}
		for (int row = 0; row < nb_rows; row++) {
			this.grid[row][0] = -1;
			this.grid[row][nb_columns - 1] = -1;
		}
	}

	public int getNb_rows() {
		return nb_rows;
	}

	public int getNb_columns() {
		return nb_columns;
	}

	public boolean genererPiece(Tetromino en_cours) {
		int[][] a_mettre = en_cours.getActif();
		int k = 4;
		int l = 1;
		int m = 0;
		boolean g = false;
		boolean possible = true;
		for (int i = 0; i < a_mettre.length; i++) {
			for (int j = 0; j < a_mettre[i].length; j++) {
				if (a_mettre[i][j] >= 1) {
					if (grid[l][k] != 0) {
						possible = false;
					}
					m++;
					g = true;
				}
				k++;
			}
			if (g) {
				l++;
			}
			k = 4;
		}
		if (!possible) {
			return false;
		}
		k = 4;
		l = 1;
		m = 0;
		g = false;
		for (int i = 0; i < a_mettre.length; i++) {
			for (int j = 0; j < a_mettre[i].length; j++) {
				if (a_mettre[i][j] >= 1) {
					if (possible) {
						grid[l][k] = a_mettre[i][j];
						coord[m][0] = l;
						coord[m][1] = k;
					}
					m++;
					g = true;
				}
				k++;
			}
			if (g) {
				l++;
			}
			k = 4;
		}
		setChanged();
		notifyObservers();
		return possible;

	}
/**
 * déplacement de la piece vers le bas
 * @return retourne faux si on n'a pas pu (vrai sinon)
 */
	public boolean deplacerPieceCouranteEnBas() {
		boolean g = true;
		//on sauvegarde la couleur de piece
		int couleur = grid[coord[0][0]][coord[0][1]];
		// effacement piece en cours
		for (int i = 0; i < coord.length; i++) {
			grid[coord[i][0]][coord[i][1]] = 0;
		}
		// test si la piece peut aller en bas
		for (int i = 0; i < coord.length; i++) {
			if (grid[coord[i][0] + 1][coord[i][1]] != 0) {
				g = false;// elle peut pas
			}
		}
		if (g) {
			// déplacement en bas
			for (int i = 0; i < coord.length; i++) {
				grid[coord[i][0] + 1][coord[i][1]] = couleur;
				coord[i][0]++;
			}
		} else {
			// remise en place
			for (int i = 0; i < coord.length; i++) {
				grid[coord[i][0]][coord[i][1]] = couleur;
			}
		}
		//on notifie que la grille a changé
		setChanged();
		notifyObservers();
		return g;

	}
/**
 * Test si la piece peut aller vers le bas
 * @return retourne vrai si elle peut, faux sinon
 */
	public boolean peutAllerEnBas() {
		boolean g = true;
		int couleur = grid[coord[0][0]][coord[0][1]];
		// effacement piece en cours
		for (int i = 0; i < coord.length; i++) {
			grid[coord[i][0]][coord[i][1]] = 0;
		}
		// test si la piece peut aller en bas
		for (int i = 0; i < coord.length; i++) {
			if (grid[coord[i][0] + 1][coord[i][1]] != 0) {
				g = false;// elle peut pas
			}
		}
		// remise en place
		for (int i = 0; i < coord.length; i++) {
			grid[coord[i][0]][coord[i][1]] = couleur;
		}
		return g;

	}
/**
 * Fonction de translation de la piece en cours vers la gauche
 * @param son savoir si le son doit être joué ou pas
 */
	public void deplacerPieceCouranteAGauche(boolean son) {
		boolean g = true;
		int couleur = grid[coord[0][0]][coord[0][1]];
		// effacement piece en cours
		for (int i = 0; i < coord.length; i++) {
			grid[coord[i][0]][coord[i][1]] = 0;
		}
		// test si la piece peut aller à gauche
		for (int i = 0; i < coord.length; i++) {
			if (grid[coord[i][0]][coord[i][1] - 1] != 0) {
				g = false;// elle peut pas
			}
		}
		if (g) {
			// déplacement à gauche
			for (int i = 0; i < coord.length; i++) {
				grid[coord[i][0]][coord[i][1] - 1] = couleur;
				coord[i][1]--;
			}
		} else {
			if (son)
				//si on doit jouer le son on le joue
				Sound.playSound(0);
			// remise en place
			for (int i = 0; i < coord.length; i++) {
				grid[coord[i][0]][coord[i][1]] = couleur;
			}
		}
		//on notifie que la grille a changé
		setChanged();
		notifyObservers();
	}
	/**
	 * Fonction de translation de la piece en cours vers la droite
	 * @param son savoir si le son doit être joué ou pas
	 */
	public void deplacerPieceCouranteADroite(boolean son) {
		boolean g = true;
		int couleur = grid[coord[0][0]][coord[0][1]];
		// efface piece en cours
		for (int i = coord.length - 1; i >= 0; i--) {
			grid[coord[i][0]][coord[i][1]] = 0;
		}
		// test si la piece peut aller à droite
		for (int i = coord.length - 1; i >= 0; i--) {
			if (grid[coord[i][0]][coord[i][1] + 1] != 0) {
				g = false; // elle peut pas
			}
		}
		// déplacement à droite
		if (g) {
			for (int i = coord.length - 1; i >= 0; i--) {
				grid[coord[i][0]][coord[i][1] + 1] = couleur;
				coord[i][1]++;
			}
		} else {
			if (son)
				Sound.playSound(0);
			// remise en place de la piece
			for (int i = coord.length - 1; i >= 0; i--) {
				grid[coord[i][0]][coord[i][1]] = couleur;
			}
		}
		//on notifie que la grille a changé
		setChanged();
		notifyObservers();
	}
	/**
	 * Fonction de descente instantané
	 * @return nombre de ligne descendu
	 */
	public int descente() {
		int i = 0;
		while (deplacerPieceCouranteEnBas())
			i++;
		return i;
	}
	/**
	 * Fonction de rotation de la piece en cours
	 * voir la page SRS du witetriski pour la définition des pivots
	 * @param p_cours piece en cours à retourner
	 * @param son savoir si le son doit être joué
	 */
	public void tournerPieceCourante(Tetromino p_cours, boolean son) {
		if (p_cours.getPivot() == 0) { // si c'est un carré on fait rien
			return;
		}
		//sauvegarde de la couleur de la piece en cours
		int couleur = grid[coord[0][0]][coord[0][1]];
		//sauvgarde de l'ancien pivot de la piece
		int anc_pi = p_cours.getPivot();
		//avoir la rotation suivante de la piece en cours
		int[][] a_mettre = p_cours.next_rotate();
		if (anc_pi >= 5) {// traitement spécial pour la barre
			//le pivot de la barre est décalé de 3 pour savoir qu'on a la barre à rotate
			anc_pi -= 3;
			// définition des décalages de l'origine de la grille 4*4 qui contient la piece
			int r = p_cours.getRotation();
			int r2 = ((r % 3) == 0) ? 1 : 2;
			int r3 = (r > 1) ? 1 : 2;
			int r4 = r2 == 1 ? 2 : 1;
			int r5 = r3 == 1 ? 2 : 1;
			// fin def
			int k = coord[anc_pi - 1][0]; // coord x du pivot
			int l = coord[anc_pi - 1][1]; // coord y du pivot
			//m et n servent aux déplacements dans la grille a_mettre
			int m = 0;
			int n = 0;
			boolean g = true;
			// effacement
			for (int i = 0; i < coord.length; i++) {
				grid[coord[i][0]][coord[i][1]] = 0;
			}
			// test
			for (int i = k - r2; i <= k + r4; i++) {
				for (int j = l - r3; j <= l + r5; j++) {
					//déplacement à partir du pivot suivant un carré 4*4 (avec les décalages)
					if (a_mettre[m][n] >= 1) {
						try {
							if (grid[i][j] != 0) {
								// peut pas
								g = false;
							}
						} catch (ArrayIndexOutOfBoundsException e) {
							//si on sort de la grille on peut pas
							g = false;
						}
					}
					n++;
				}
				if (!g)
					//si on peut pas on stop
					break;
				m++;
				n = 0;
			}
			m = 0;
			n = 0; 
			int u = 0;
			if (g) {
				// mise en place
				for (int i = k - r2; i <= k + r4; i++) {
					for (int j = l - r3; j <= l + r5; j++) {
						if (a_mettre[m][n] >= 1) {
							grid[i][j] = couleur;
							coord[u][0] = i;
							coord[u][1] = j;
							u++;
						}
						n++;
					}
					m++;
					n = 0;
				}
				if (son)
					//on joue le son si on doit
					Sound.playSound(2);
				//on notifie que la grille a changé, et on valide la rotation
				p_cours.rotation_validé();
				setChanged();
				notifyObservers();
			} else {
				//si on peut pas on remet la piece telle qu'elle était
				if (son)
					Sound.playSound(0);
				// remise en place
				for (int i = 0; i < coord.length; i++) {
					grid[coord[i][0]][coord[i][1]] = couleur;
				}
				p_cours.setPivot(anc_pi + 3);
			}
			return;
		}
		int k = coord[anc_pi - 1][0];
		int l = coord[anc_pi - 1][1];
		// efface piece en cours
		for (int i = 0; i < coord.length; i++) {
			grid[coord[i][0]][coord[i][1]] = 0;
		}
		//idem que pour la barre
		int m = 0;
		int n = 0;
		boolean g = true;
		for (int i = k - 1; i <= k + 1; i++) {
			for (int j = l - 1; j <= l + 1; j++) {
				//parcours de la grille à partir du pivot suivant un carré 3*3
				if (a_mettre[m][n] >= 1) {
					//si on a quelque chose à mettre on test si on peut le mettre à cette palce
					try {
						if (grid[i][j] != 0) {
							//si il y a déjà quelque chose on peut pas
							g = false;
						}
					} catch (ArrayIndexOutOfBoundsException e) {
						//si on sort de la grille on peut pas
						g = false;
					}
				}
				//on avance dans a_mettre
				n++;
			}
			if (!g)
				break;
			//on avance dans les lignes de a_mettre mais on repart de 0 pour les colonnes
			m++;
			n = 0;
		}
		m = 0;
		n = 0;
		int u = 0;
		if (g) {
			//si on peut faire la rotation
			for (int i = k - 1; i <= k + 1; i++) {
				for (int j = l - 1; j <= l + 1; j++) {
					//parcout de la grille à partir de pivot (suivant un carré 3*3)
					if (a_mettre[m][n] >= 1) {
						//s'il y a quelque chose à mettre on le met
						grid[i][j] = couleur;
						coord[u][0] = i;
						coord[u][1] = j;
						u++;
					}
					n++;
				}
				m++;
				n = 0;
			}
			if (son)
				Sound.playSound(2);
			p_cours.rotation_validé();
			setChanged();
			notifyObservers();
		} else {
			//si on peut pas on remet la piece telle qu'elle était
			if (son)
				Sound.playSound(0);
			//remise en place
			for (int i = 0; i < coord.length; i++) {
				grid[coord[i][0]][coord[i][1]] = couleur;
			}
			//on remet le pivot si on a pas pu faire la rotation
			p_cours.setPivot(anc_pi);
		}
	}
/**
 * Fonction pour effacer les lignes
 * @return on retourne le nombre de ligne qu'on a pu effacer
 */
	public int effacer_ligne() {
		//compteur de ligne
		int l = 0;
		//boolean pour savoir si on doit effacer une ligne
		boolean g = true;
		//on parcourt la grille
		for (int i = 1; i < grid.length - 1; i++) {
			for (int j = 1; j < grid[i].length - 1; j++) {
				if (grid[i][j] == 0) {
					//s'il y a une case vide on l'efface pas
					g = false;
					break;
				}
			}
			if (g) {
				//si on doit l'effacer on fait descendre toutes les lignes au dessus d'elle
				descendre_ligne(i);
				//on a effacé une ligne de plus
				l++;
			}
			g = true;
		}
		//on a changé la grille, on le dit
		setChanged();
		notifyObservers();
		//on retourne le nombre de ligne effacé
		return l;
	}
/**
 * Fonction de descente de ligne
 * @param i descendre les lignes à partir de i
 */
	private void descendre_ligne(int i) {
		//à partir de i en remontant jusqu'a la penultième ligne
		for (int j = i; j > 1; j--) {
			for (int l = 1; l < grid[j].length - 1; l++) {
				//la case prend ce qu'il y avait la case au dessus
				//du coup on efface la ligne en cours
				grid[j][l] = grid[j - 1][l];
				//on efface la case au dessus
				grid[j - 1][l] = 0;
			}
		}
		//on remplit de 0 la dernière ligne (elle est forcement vide)
		for (int l = 1; l < grid[1].length - 1; l++) {
			grid[1][l] = 0;
		}

	}
	/**
	 * Reset de l'espace de jeu (en cas de nouvelle partie)
	 */
	public void reset() {
		for (int i = 1; i < grid.length - 1; i++) {
			for (int j = 1; j < grid[i].length - 1; j++) {
				grid[i][j] = 0;
			}
		}
	}
	/**
	 * Fonction de pré-remplissage de la grille
	 * @param dens_dep Nombre de case pré-rempli sur 1 ligne
	 * @param dens_ligne_dep Nombre de ligne à pré-remplir
	 */
	public void pre_remplissage(int dens_dep, int dens_ligne_dep) {
		//compteur de case sur une ligne
		int j = 0;
		//case à remplir
		int k;
		Random r = new Random();
		//boucle allant de bas en haut (en parcourant le bon nombre de case)
		for (int i = grid.length - 2; i >= grid.length - 1 - dens_ligne_dep; i--) {
			//on reset le compteur de case à 0;
			j = 0;
			//tant qu'on a le bon nombre de case
			while ((j * 10) < dens_dep) {
				//on génère aléatoirement la deuxieme coordonnées (la colonne) où mettre la piece
				k = r.nextInt(10) + 1;
				if (grid[i][k] == 0) {
					//si la case est vide on peut mettre
					grid[i][k] = r.nextInt(7) + 1;
					//on a rempli une case
					j++;
				}
			}
		}

	}
}
