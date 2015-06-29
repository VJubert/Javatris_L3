package javatris.model;

import java.util.Observable;
import java.util.Random;


public class Tetromino extends Observable{
	protected int[][] actif;
	private int codeColor;
	private int rotation;
	private int pivot;
	
	private static final int[][][] I_TETROMINO =
         {
 			{
 				{0, 0, 0, 0},
 				{1, 1, 1, 1},
 				{0, 0, 0, 0},
 				{0, 0, 0, 0}
 			},
 			{
 				{0, 0, 1, 0},
 				{0, 0, 1, 0},
 				{0, 0, 1, 0},
 				{0, 0, 1, 0}
 			},
 			{
 				{0, 0, 0, 0},
 				{0, 0, 0, 0},
 				{1, 1, 1, 1},
 				{0, 0, 0, 0}
 			},
 			{
 				{0, 1, 0, 0},
 				{0, 1, 0, 0},
 				{0, 1, 0, 0},
 				{0, 1, 0, 0}
 			}
         };

	private static final int[][][] J_TETROMINO =
         {
 			{
 				{2, 0, 0},
 				{2, 2, 2},
 				{0, 0, 0}
 			},
 			{
 				{0, 2, 2},
 				{0, 2, 0},
 				{0, 2, 0}
 			},
 			{
 				{0, 0, 0},
 				{2, 2, 2},
 				{0, 0, 2}
 			},
 			{
 				{0, 2, 0},
 				{0, 2, 0},
 				{2, 2, 0}
 			}
         };

	private static final int[][][] L_TETROMINO =
         {
 			{
 				{0, 0, 3},
 				{3, 3, 3},
 				{0, 0, 0}
 			},
 			{
 				{0, 3, 0},
 				{0, 3, 0},
 				{0, 3, 3}
 			},
 			{
 				{0, 0, 0},
 				{3, 3, 3},
 				{3, 0, 0}
 			},
 			{
 				{3, 3, 0},
 				{0, 3, 0},
 				{0, 3, 0}
 			}
         };

	private static final int[][][] O_TETROMINO =
         {
 			{
 				{0, 0, 0, 0},
 				{0, 4, 4, 0},
 				{0, 4, 4, 0},
 				{0, 0, 0, 0}
 			}
         };

	private static final int[][][] S_TETROMINO =
         {
 			{
 				{0, 5, 5},
 				{5, 5, 0},
 				{0, 0, 0}
 			} ,
 			{
 				{0, 5, 0},
 				{0, 5, 5},
 				{0, 0, 5}
 			},
 			{
 				{0, 0, 0},
 				{0, 5, 5},
 				{5, 5, 0}
 			},
 			{
 				{5, 0, 0},
 				{5, 5, 0},
 				{0, 5, 0}
 			}
         };

	private static final int[][][] T_TETROMINO =
 		{
 			{
 				{0, 6, 0},
 				{6, 6, 6},
 				{0, 0, 0}
 			},
 			{
 				{0, 6, 0},
 				{0, 6, 6},
 				{0, 6, 0}
 			},
 			{
 				{0, 0, 0},
 				{6, 6, 6},
 				{0, 6, 0}
 			},
 			{
 				{0, 6, 0},
 				{6, 6, 0},
             	{0, 6, 0}
 			}
 		};
	private static final int[][][] Z_TETROMINO =
			{
				{
					{7, 7, 0},
					{0, 7, 7},
					{0, 0, 0}
				},
				{
					{0, 0, 7},
					{0, 7, 7},
					{0, 7, 0}
				},
				{
					{0, 0, 0},
					{7, 7, 0},
					{0, 7, 7}
				},
				{
					{0, 7, 0},
					{7, 7, 0},
					{7, 0, 0}
				}
				
			};
	/**
	 * le pivot est défini suivant la définition srs de witetriski
	 */
	public void genererPiece() {
		Random r = new Random();
		int r2 = r.nextInt(7) + 1;
		switch (r2) {
		case 1:
			actif = I_TETROMINO[0];
			pivot=6;
			break;
		case 2:
			actif = J_TETROMINO[0];
			pivot = 3;
			break;
		case 3:
			actif = L_TETROMINO[0];
			pivot = 3;
			break;
		case 4:
			actif = O_TETROMINO[0];
			pivot = 0;
			break;
		case 5:
			actif = S_TETROMINO[0];
			pivot = 4;
			break;
		case 6:
			actif = T_TETROMINO[0];
			pivot = 3;
			break;
		case 7:
			actif = Z_TETROMINO[0];
			pivot = 3;
			break;
		}
		codeColor = r2;
		rotation=0;
		//on notifie que la piece a changé
		setChanged();
		notifyObservers();
	}
	public Tetromino() {
		Random r = new Random();
		int r2 = r.nextInt(7) + 1;
		switch (r2) {
		case 1:
			actif = I_TETROMINO[0];
			pivot=6;
			break;
		case 2:
			actif = J_TETROMINO[0];
			pivot = 3;
			break;
		case 3:
			actif = L_TETROMINO[0];
			pivot = 3;
			break;
		case 4:
			actif = O_TETROMINO[0];
			pivot = 0;
			break;
		case 5:
			actif = S_TETROMINO[0];
			pivot = 4;
			break;
		case 6:
			actif = T_TETROMINO[0];
			pivot = 3;
			break;
		case 7:
			actif = Z_TETROMINO[0];
			pivot = 3;
			break;
		}
		codeColor = r2;
		rotation=0;
	}
	/*on crée une copie de la piece)*/
    public Tetromino(int[][] actif2, int codeColor2, int rotation2, int pivot2) {
		codeColor=codeColor2;
		rotation=rotation2;
		pivot=pivot2;
		actif=new int[actif2.length][actif2[0].length];
		for (int i=0;i<actif.length;i++){
			for (int j=0;j<actif[i].length;j++){
				actif[i][j]=actif2[i][j];
			}
		}
	}
	public int[][] getActif() {
		return actif;
	}
	//donner la prochaine rotation en mettant à jour le pivot
	// on voit les résultats des fonctions en fonction de rotation (0/1/2/3)
    public int[][] next_rotate() {
		switch (codeColor) {
		case 1:
			// 3 3 2 2
			pivot = (3 - (((rotation + 1) % (I_TETROMINO.length)) / 2)) + 3;
			return I_TETROMINO[(rotation + 1) % (I_TETROMINO.length)];
		case 2:
			// 3 3 2 2
			pivot = 3 - (((rotation + 1) % (J_TETROMINO.length)) / 2);
			return J_TETROMINO[(rotation + 1) % (J_TETROMINO.length)];
		case 3:
			// 3 2 2 3
			pivot = Math.max(
					2 + 1 - ((rotation + 1) % (L_TETROMINO.length)) % 3, 2);
			return L_TETROMINO[(rotation + 1) % (L_TETROMINO.length)];
		case 4:
			return O_TETROMINO[0];
		case 5:
			// 4 2 1 3
			pivot = (4 - (((rotation + 1) % (S_TETROMINO.length)) % 3) - (((rotation + 1) % (S_TETROMINO.length)) > 0 ? 1
					: 0));
			return S_TETROMINO[(rotation + 1) % (S_TETROMINO.length)];
		case 6:
			// 3 2 2 3
			pivot = Math.max(
					2 + 1 - ((rotation + 1) % (T_TETROMINO.length)) % 3, 2);
			return T_TETROMINO[(rotation + 1) % (T_TETROMINO.length)];
		case 7:
			// 3 2 2 3
			pivot = Math.max(
					2 + 1 - ((rotation + 1) % (Z_TETROMINO.length)) % 3, 2);
			return Z_TETROMINO[(rotation + 1) % (Z_TETROMINO.length)];
		}
		return null;
	}
    public Tetromino clone(){
    	return new Tetromino(actif,codeColor,rotation,pivot);
    }
    //on modifie rotation et actif (rotation ne va pas au dela de trois) (modulo 4)
	public void rotation_validé() {
		switch (codeColor) {
		case 1:
			rotation = (rotation + 1) % (I_TETROMINO.length);
			actif = I_TETROMINO[(rotation + 1) % (I_TETROMINO.length)];
			break;
		case 2:
			rotation = (rotation + 1) % (J_TETROMINO.length);
			actif = J_TETROMINO[(rotation + 1) % (J_TETROMINO.length)];
			break;
		case 3:
			rotation = (rotation + 1) % (L_TETROMINO.length);
			actif = L_TETROMINO[(rotation + 1) % (L_TETROMINO.length)];
			break;
		case 4:
			break;
		case 5:
			rotation = (rotation + 1) % (S_TETROMINO.length);
			actif = S_TETROMINO[(rotation + 1) % (S_TETROMINO.length)];
			break;
		case 6:
			rotation = (rotation + 1) % (T_TETROMINO.length);
			actif = T_TETROMINO[(rotation + 1) % (T_TETROMINO.length)];
			break;
		case 7:
			rotation = (rotation + 1) % (Z_TETROMINO.length);
			actif = Z_TETROMINO[(rotation + 1) % (Z_TETROMINO.length)];
			break;
		}
	}
	public int getPivot() {
		return pivot;
	}
	public void setPivot(int pivot) {
		this.pivot = pivot;
	}
	public int getRotation() {
		return rotation;
	}
}
