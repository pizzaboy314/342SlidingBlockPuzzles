package classes;

import java.awt.Color;
import java.util.Arrays;

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
	private int[] Umoves;
	private int[] Dmoves;
	private int[] Lmoves;
	private int[] Rmoves;

	/**
	 * Constructor for this button; sets its i,j location in the grid as well as
	 * its height (h) and width (w).
	 * 
	 * @param i
	 * @param j
	 * @param h
	 * @param w
	 */
	public Button(int i, int j, int h, int w) {
		this.i = i;
		this.j = j;
		this.h = h;
		this.w = w;
		this.position = -1;

		tag = "blank";
		selected = false;
		blank = false;

		setOpaque(true);
	}

	/**
	 * Manually sets the attributes of this button: its tag, height, width,
	 * position in multi-tile piece, color, and whether this button is a blank
	 * tile.
	 * 
	 * @param tag
	 * @param h
	 * @param w
	 * @param pos
	 * @param c
	 * @param blank
	 */
	public void setAttributes(String tag, int h, int w, int pos, Color c, boolean blank) {
		this.tag = tag;
		this.h = h;
		this.w = w;
		this.position = pos;
		this.c = c;
		setBlank(blank);

		setBackground(c);
		if (blank == false) {
			setBorder(null);
		}
	}

	/**
	 * Getter for i coordinate.
	 * 
	 * @return
	 */
	public int getI() {
		return i;
	}

	/**
	 * Setter for i coordinate.
	 * 
	 * @return
	 */
	public void setI(int i) {
		this.i = i;
	}

	/**
	 * Getter for j coordinate.
	 * 
	 * @return
	 */
	public int getJ() {
		return j;
	}

	/**
	 * Setter for j coordinate.
	 * 
	 * @return
	 */
	public void setJ(int j) {
		this.j = j;
	}

	/**
	 * Getter for boolean variable selected.
	 * 
	 * @return
	 */
	public boolean isSelected() {
		return selected;
	}

	/**
	 * Setter for boolean variable selected, which indicates whether or not this
	 * button is selected. This function also sets the border around the sides
	 * of the button so the gui shows that the button is selected.
	 * @param selected
	 */
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

	/**
	 * Getter for this button's color.
	 * 
	 * @return
	 */
	public Color getC() {
		return c;
	}

	/**
	 * Setter for this button's color. Also sets the button's background to this
	 * color.
	 * 
	 * @param c
	 */
	public void setC(Color c) {
		this.c = c;
		setBackground(c);
	}

	/**
	 * Getter for boolean variable blank.
	 * 
	 * @return
	 */
	public boolean isBlank() {
		return blank;
	}

	/**
	 * Setter for boolean variable blank, which indicates whether or not this
	 * button is a blank tile in the grid. Also sets a light border to
	 * distinguish blank tiles from each other.
	 * @param blank
	 */
	public void setBlank(boolean blank) {
		this.blank = blank;
		if (blank == true) {
			setC(new Color(210, 210, 210));
			setBorder(BorderFactory.createLineBorder(Color.lightGray, 1));
		}
	}

	/**
	 * Getter for this button's position in a larger piece.
	 * 
	 * @return
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Setter for this button's position in a larger piece.
	 * 
	 * @return
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * Getter for this button's string tag.
	 * 
	 * @return
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Setter for this button's string tag.
	 * 
	 * @param tag
	 */
	public void setTag(String tag) {
		this.tag = tag;
	}

	/**
	 * Getter for this button's height.
	 * 
	 * @return
	 */
	public int getH() {
		return h;
	}

	/**
	 * Setter for this button's height.
	 * 
	 * @param h
	 */
	public void setH(int h) {
		this.h = h;
	}

	/**
	 * Getter for this button's width.
	 * 
	 * @return
	 */
	public int getW() {
		return w;
	}

	/**
	 * Setter for this button's width.
	 * 
	 * @param w
	 */
	public void setW(int w) {
		this.w = w;
	}

	/**
	 * Sets all possible moves in all directions.
	 * 
	 * @param Upmoves
	 * @param Dnmoves
	 * @param Ltmoves
	 * @param Rtmoves
	 * @param arraySize
	 */
	public void setAllMoves(int [] Upmoves, int [] Dnmoves, int [] Ltmoves, int [] Rtmoves, int arraySize){

		this.Umoves = new int [arraySize-1];
		this.Dmoves = new int [arraySize-1];
		this.Lmoves = new int [arraySize-1];
		this.Rmoves = new int [arraySize-1];

		for(int i=0; i<arraySize-1; i++){
			this.Umoves[i] = Upmoves[i];
		}
		for(int i=0; i<arraySize-1; i++){
			this.Dmoves[i] = Dnmoves[i];
		}
		for(int i=0; i<arraySize-1; i++){
			this.Lmoves[i] = Ltmoves[i];
		}
		for(int i=0; i<arraySize-1; i++){
			this.Rmoves[i] = Rtmoves[i];
		}
	}

	/**
	 * Sets the possible moves upwards.
	 * 
	 * @param Upmoves
	 * @param arraySize
	 */
	public void setUMoves(int[] Upmoves, int arraySize) {

		this.Umoves = new int[arraySize - 1];

		for (int i = 0; i < arraySize - 1; i++) {
			this.Umoves[i] = Upmoves[i];
		}
	}

	/**
	 * Sets the possible moves downwards.
	 * 
	 * @param Dnmoves
	 * @param arraySize
	 */
	public void setDMoves(int[] Dnmoves, int arraySize) {

		this.Dmoves = new int[arraySize - 1];

		for (int i = 0; i < arraySize - 1; i++) {
			this.Dmoves[i] = Dnmoves[i];
		}
	}

	/**
	 * Sets the possible moves leftwards.
	 * 
	 * @param Ltmoves
	 * @param arraySize
	 */
	public void setLMoves(int[] Ltmoves, int arraySize) {

		this.Lmoves = new int[arraySize - 1];

		for (int i = 0; i < arraySize - 1; i++) {
			this.Lmoves[i] = Ltmoves[i];
		}
	}

	/**
	 * Sets the possible moves rightwards.
	 * 
	 * @param Rtmoves
	 * @param arraySize
	 */
	public void setRMoves(int[] Rtmoves, int arraySize) {

		this.Rmoves = new int[arraySize - 1];

		for (int i = 0; i < arraySize - 1; i++) {
			this.Rmoves[i] = Rtmoves[i];
		}
	}

	/**
	 * Getter for this button's possible moves upwards.
	 * 
	 * @return
	 */
	public int [] getUMoves(){
		return this.Umoves;
	}

	/**
	 * Getter for this button's possible moves downwards.
	 * 
	 * @return
	 */
	public int [] getDMoves(){
		return this.Dmoves;
	}

	/**
	 * Getter for this button's possible moves leftwards.
	 * 
	 * @return
	 */
	public int [] getLMoves(){
		return this.Lmoves;
	}

	/**
	 * Getter for this button's possible moves rightwards.
	 * 
	 * @return
	 */
	public int [] getRMoves(){
		return Rmoves;
	}

	/**
	 * Used for debugging, simply prints all possible moves.
	 */
	public void printMoves(){
		System.out.println("UMoves: " + Arrays.toString(this.Umoves));
		System.out.println("DMoves: " + Arrays.toString(this.Dmoves));
		System.out.println("LMoves: " + Arrays.toString(this.Lmoves));
		System.out.println("RMoves: " + Arrays.toString(this.Rmoves));
	}
}
