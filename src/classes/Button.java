package classes;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class Button extends JLabel {

	private int i, j;
	private Color c;
	private boolean selected;
	private boolean blank;

	public Button(int i, int j, Color c) {
		this.i = i;
		this.j = j;
		this.c = c;

		selected = false;
		blank = false;

		setOpaque(true);
		setBackground(c);
	}

	public int getI() {
		return i;
	}

	public void setI(int i) {
		this.i = i;
	}

	public int getJ() {
		return j;
	}

	public void setJ(int j) {
		this.j = j;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
		if (selected == true) {
			setBorder(BorderFactory.createLineBorder(Color.black, 2));
		} else {
			setBorder(null);
		}
	}

	public Color getC() {
		return c;
	}

	public void setC(Color c) {
		this.c = c;
		setBackground(c);
	}

	public boolean isBlank() {
		return blank;
	}

	public void setBlank(boolean blank) {
		this.blank = blank;
		if (blank == true) {
			setC(new Color(210, 210, 210));
		}
	}

}
