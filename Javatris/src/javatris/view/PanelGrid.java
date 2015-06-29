package javatris.view;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javatris.model.Grid;
import javatris.model.Tetromino;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class PanelGrid extends JPanel implements Observer {
	//taille par défaut des carreaux
	public static final int TILE_SIZE_DEF = 30;
	//taille des carreaux
	private int tile_size;
	//grille à peindre
	private Grid grid;
	//savoir si on a besoin d'un ajustement (en cas de carré 3*3)
	private boolean ajust;
	/**
	 * création de la grille à la position pox,poy
	 * @param grid grille à afficher
	 * @param pox position x
	 * @param poy position y
	 */
	public PanelGrid(Grid grid, int pox, int poy) {
		this.grid = grid;
		ajust = false;
		//on met la taille des cases à celle par défaut
		tile_size = TILE_SIZE_DEF;
		//on met la taille de la grille par rapport au nombre de cases
		this.setSize(grid.getNb_columns() * tile_size, grid.getNb_rows()
				* tile_size);
		this.setLocation(pox, poy);
	}
	//on change la taille des cellules
	public PanelGrid(Grid grid, int pox, int poy, int tile_size) {
		this.grid = grid;
		this.tile_size = tile_size;
		this.setSize(grid.getNb_columns() * tile_size, grid.getNb_rows()
				* tile_size);
		this.setLocation(pox, poy);
	}
	//avoir la couleur par rapport à l'entier dans la grille
	public Color getColor(int n) {
		switch (n) {
		case -1:
			return Color.DARK_GRAY;
		case 0:
			return Color.WHITE;
		case 1:
			return Color.RED;
		case 2:
			return Color.BLUE;
		case 3:
			return Color.ORANGE;
		case 4:
			return Color.YELLOW;
		case 5:
			return Color.MAGENTA;
		case 6:
			return Color.CYAN;
		case 7:
			return Color.GREEN;
		default:
			return Color.WHITE;
		}
	}
	//fonction utile pour dessiner le composant
	public void paintComponent(Graphics graphics) {
		//couleur de fond = noir
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
		for (int row = 0; row < grid.getNb_rows(); row++) {
			for (int column = 0; column < grid.getNb_columns(); column++) {
				graphics.setColor(this.getColor(grid.getContent(row, column)));
				graphics.fillRect(column * tile_size, row * tile_size,
						tile_size - 1, tile_size - 1);
			}
		}
	}

	@Override
	//si un des objets qui est observé est mit à jour cette méthode s'execute
	public void update(Observable o, Object arg) {
		//si c'est une tetromino (donc la piece suivante)
		if (o instanceof Tetromino) {
			Tetromino arg2 = (Tetromino) o;
			if (arg2.getActif().length == 3)
				//si c'est une piece dans une grille 3*3 on met l'ajustement
				ajust = true;
			//on fait une nouvelle grille avec l'ajustement
			grid = new Grid(arg2.getActif(), ajust);
			ajust = false;
			//on redessine
			this.validate();
			this.repaint();
		}
		//si c'est la grille du panel
		if (o == grid) {
			//on validate et on repeint (ce qui appelle paintcomponent donc met à jour l'affichage)
			this.validate();
			this.repaint();
		}
	}
}
