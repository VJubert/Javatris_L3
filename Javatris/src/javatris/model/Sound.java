package javatris.model;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;

public class Sound {

	// Code son
	public static final String pieceRotateFail = "SFX_PieceRotateFail.wav"; // 0
	public static final String pieceLockDown = "SFX_PieceLockdown.wav"; // 1
	public static final String pieceRotateLR = "SFX_PieceRotateLR.wav"; // 2
	public static final String specialLineClearSingle = "SFX_SpecialLineClearSingle.wav"; // 3
	public static final String specialLineClearDouble = "SFX_SpecialLineClearDouble.wav"; // 4
	public static final String specialLineClearTriple = "SFX_SpecialLineClearTriple.wav"; // 5
	public static final String specialTetris = "SFX_SpecialTetris.wav"; // 6
	public static final String gameOver = "SFX_GameOver.wav"; // 7
	public static final String gameStart = "SFX_GameStart.wav"; // 8
	public static final String levelUp = "SFX_LevelUp.wav"; // 9

	public static void playSound(int i) {
		String soundFile = "";
		switch (i) {
		case 0:
			soundFile = pieceRotateFail;
			break;
		case 1:
			soundFile = pieceLockDown;
			break;
		case 2:
			soundFile = pieceRotateLR;
			break;
		case 3:
			soundFile = specialLineClearSingle;
			break;
		case 4:
			soundFile = specialLineClearDouble;
			break;
		case 5:
			soundFile = specialLineClearTriple;
			break;
		case 6:
			soundFile = specialTetris;
			break;
		case 7:
			soundFile = gameOver;
			break;
		case 8:
			soundFile = gameStart;
			break;
		case 9:
			soundFile = levelUp;
			break;
		}
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(new File("res\\Sounds\\"
					+ soundFile)));
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
