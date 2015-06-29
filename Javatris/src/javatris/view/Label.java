package javatris.view;

import javax.swing.JLabel;

@SuppressWarnings("serial")
public class Label extends JLabel {

	public Label(String text, int width, int height, int xPos, int yPos) {
		this.setText(text);
		this.setSize(width, height);
		this.setLocation(xPos, yPos);
	}
}
