package javatris.view;

import javax.swing.JButton;

@SuppressWarnings("serial")
public class Button extends JButton {

	public Button(String text, int width, int height, int xPos, int yPos) {
		this.setText(text);
		this.setSize(width, height);
		this.setLocation(xPos, yPos);
	}

}
