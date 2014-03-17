package classes;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class Button extends JLabel {

	private static final long serialVersionUID = 1L;

	private int i, j;
	private int position;
	private int h;
	private int w;
	private String tag;
	private Color c;
	private boolean selected;
	private boolean blank;

	public Button(int i, int j, int h, int w) {
		this.i = i;
		this.j = j;
		this.h = h;
		this.w = w;

		tag = "blank";
		selected = false;
		blank = false;

		setOpaque(true);
	}
	
	public void setAttributes(String tag, int h, int w, int pos, Color c, boolean blank) {
		this.tag = tag;
		this.h = h;
		this.w = w;
		this.position = pos;
		this.c = c;
		this.blank = blank;

		setBackground(c);
		if (blank == false) {
			setBorder(null);
		}
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
			if (h == 1) {
				if (position == 0) {
					setBorder(BorderFactory.createMatteBorder(2, 2, 2, 0, Color.black));
				} else if (position == w - 1) {
					setBorder(BorderFactory.createMatteBorder(2, 0, 2, 2, Color.black));
				} else {
					setBorder(BorderFactory.createMatteBorder(2, 0, 2, 0, Color.black));
				}
			} else if (w == 1) {
				if (position == 0) {
					setBorder(BorderFactory.createMatteBorder(2, 2, 0, 2, Color.black));
				} else if (position == h - 1) {
					setBorder(BorderFactory.createMatteBorder(0, 2, 2, 2, Color.black));
				} else {
					setBorder(BorderFactory.createMatteBorder(0, 2, 0, 2, Color.black));
				}
			}

		} else if (blank == true) {
			setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
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
			setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		}
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public int getH() {
		return h;
	}

	public void setH(int h) {
		this.h = h;
	}

	public int getW() {
		return w;
	}

	public void setW(int w) {
		this.w = w;
	}
}
