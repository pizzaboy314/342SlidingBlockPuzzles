package classes;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Grid implements MouseListener {

	private int size = 6; // SIZE OF BOARD
	private int boardSize;

	private String windowLabel;
	private JFrame mainFrame; // main window
	private JPanel content; // to contain the reset/hint bar and main board
	private JPanel bar; // contains reset/hint/solution
	private Button[][] buttonGrid; // grid of JLabels
	private Button selectedButton;
	private List<TagColor> buttons;
	private Color[] colors;
	private List<String> locations;
	private int redRow;
	private String redLoc;
	private String Snapshot;	//Snapshot of the grid 
	private int fileNum = 0;
	private int fileCounter = 1;

	TimerTask solveAnim;
	Timer animTimer;
	List<String> soln;

	public Grid(String windowLabel) {
		this.windowLabel = windowLabel;
		mainFrame = new JFrame(windowLabel + " - Level " + fileCounter);
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		create_menu();
		countFiles();

		buttonGrid = new Button[size][size];

		// initialize grid to blank tiles
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				Button b = new Button(i, j, 1, 1);
				b.setBlank(true);
				b.addMouseListener(this);
				buttonGrid[i][j] = b;
			}
		}
		boardSize = size * 80;
		JPanel panel = new JPanel(new GridLayout(size, size));
		panel.setPreferredSize(new Dimension(boardSize, boardSize));
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				Button b = buttonGrid[i][j];
				panel.add(b);
			}
		}

		mainFrame.add(panel);
		mainFrame.pack();
		mainFrame.setVisible(true);

		init();
	}

	/**
	 * Creates the JMenu that contains buttons Exit, Help, About, Show Hint,
	 * Show Solution, and Reset Board
	 */
	public void create_menu() {

		JMenuBar menu = new JMenuBar();

		JMenu Game = new JMenu("Game"), Help = new JMenu("Help");

		JMenuItem eXit = new JMenuItem("eXit"), help = new JMenuItem("Help"), about = new JMenuItem("About");
		JMenuItem hint = new JMenuItem("Show Hint"), soln = new JMenuItem("Show Solution"), reset = new JMenuItem("Reset Board");

		eXit.setMnemonic('X');
		help.setMnemonic('H');
		about.setMnemonic('A');

		Game.setMnemonic('G');
		Game.add(eXit);

		Help.setMnemonic('H');
		Help.add(help);
		Help.add(about);
		Help.add(hint);
		Help.add(soln);
		Help.add(reset);

		menu.add(Game);
		menu.add(Help);

		eXit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}

		});

		help.addActionListener(new ActionListener() {
			class helpWindow {
				public helpWindow() {
					JLabel msg = new JLabel();

					msg.setText("Slide the vehicles until the red block reaches the right side.");
					msg.setBorder(new EmptyBorder(10, 10, 10, 10));

					JFrame about = new JFrame("Help");

					about.getContentPane().setLayout(new BorderLayout());
					about.getContentPane().add(msg, "Center");
					about.pack();
					about.setVisible(true);
				}
			}
			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unused")
				helpWindow help = new helpWindow();
			}
		});

		about.addActionListener(new ActionListener() {
			class aboutWindow {
				public aboutWindow() {
					JLabel msg = new JLabel();

					msg.setText("Programmers: George Saldaña, Bryan Spahr");
					msg.setBorder(new EmptyBorder(10, 10, 10, 10));

					JFrame about = new JFrame("About");

					about.getContentPane().setLayout(new BorderLayout());
					about.getContentPane().add(msg, "Center");
					about.pack();
					about.setVisible(true);
				}

			}

			@Override
			public void actionPerformed(ActionEvent e) {
				@SuppressWarnings("unused")
				aboutWindow about = new aboutWindow();
			}
		});
		hint.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				showHint();
			}

		});
		soln.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				showSoln();
			}

		});
		reset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				init();
			}

		});
		mainFrame.setJMenuBar(menu);
	}

	/**
	 * Reads level file and initializes the board to a playable state.
	 */
	public void init(){
		createColors();
		readFile();

		mainFrame.setTitle(windowLabel + " - Level " + fileCounter);

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				buttonGrid[i][j].setAttributes("blank", 1, 1, -1, new Color(210, 210, 210), true);
			}
		}
		createRequiredButtons();
		mainFrame.repaint();

		Snapshot = getSnapshot();
		System.out.println("len: " + Snapshot.length() + "| " + Snapshot);
	}

	/**
	 * Counts the number of level files in the project directory.
	 */
	public void countFiles() {
		File[] files = new File(System.getProperty("user.dir")).listFiles();
		Pattern p = Pattern.compile("lvl\\d\\.data");

		for (File file : files) {
			Matcher m = p.matcher(file.getName());
			if (file.isFile() && m.find()) {
				fileNum++;
			}
		}
	}

	/**
	 * Reads the current level file and parses the locations for all the game
	 * pieces.
	 */
	public void readFile() {
		File file = new File("lvl" + fileCounter + ".data");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		List<String> list = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader("lvl" + fileCounter + ".data"));
			String s;
			if ((s = reader.readLine()) != null) {
				size = Integer.parseInt(s.substring(0, 1));
				System.out.println(size);
			}
			if ((s = reader.readLine()) != null) {
				redLoc = s;
				System.out.println(s);
			}
			while ((s = reader.readLine()) != null) {
				list.add(s);
				System.out.println(s);
			}
			reader.close();
			locations = new ArrayList<String>(list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates the game pieces from the locations listed in the level file and
	 * updates the locations in the grid.
	 */
	public void createRequiredButtons() {
		Random r = new Random(System.currentTimeMillis());
		buttons = new ArrayList<TagColor>();

		String[] redCarData = redLoc.split(" ");
		int redI = Integer.parseInt(redCarData[0]) - 1;
		int redJ1 = Integer.parseInt(redCarData[1]) - 1;
		int redJ2 = Integer.parseInt(redCarData[1]);
		int redH = Integer.parseInt(redCarData[2]);
		int redW = Integer.parseInt(redCarData[3]);
		redRow = redI;

		Button redCar1 = buttonGrid[redI][redJ1];
		Button redCar2 = buttonGrid[redI][redJ2];
		redCar1.setAttributes(redCarData[4], redH, redW, 0, new Color(255, 0, 0), false);
		redCar2.setAttributes(redCarData[4], redH, redW, 1, new Color(255, 0, 0), false);
		buttons.add(new TagColor(redCar1.getTag(), redCar1.getC()));

		int color = 0;
		for (String s : locations) {
			Color c = colors[color];

			String[] vehicleData = s.split(" ");
			int vi = Integer.parseInt(vehicleData[0]) - 1;
			int vj = Integer.parseInt(vehicleData[1]) - 1;
			int vh = Integer.parseInt(vehicleData[2]);
			int vw = Integer.parseInt(vehicleData[3]);

			int counter = 0;
			for (int i = vi; i < (vi + vh); i++) {
				for (int j = vj; j < (vj + vw); j++) {
					Button vehicle = buttonGrid[i][j];
					vehicle.setAttributes(vehicleData[4], vh, vw, counter, c, false);
					if (counter == 0) {
						buttons.add(new TagColor(vehicle.getTag(), vehicle.getC()));
					}
					counter++;
				}
			}
			color = (color >= 10) ? 0 : (color + 1);
		}
	}

	/**
	 * Gets the shortest solution path and performs the first move in the resulting list of moves.
	 */
	public void showHint() {
		mainFrame.setTitle(windowLabel + " - Level " + fileCounter + " - Calculating...");

		List<String> snaps = getSolutionPath();
		snaps.remove(snaps.get(0));

		mainFrame.setTitle(windowLabel + " - Level " + fileCounter);

		System.out.println("Next Step");
		List<String> locs = parseSnapshot(snaps.get(0));

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				buttonGrid[i][j].setAttributes("blank", 1, 1, -1, new Color(210, 210, 210), true);
			}
		}

		for (String s : locs) {
			System.out.println(s);
			String[] vehicleData = s.split(" ");
			int vi = Integer.parseInt(vehicleData[0]) - 1;
			int vj = Integer.parseInt(vehicleData[1]) - 1;
			int vh = Integer.parseInt(vehicleData[2]);
			int vw = Integer.parseInt(vehicleData[3]);

			int counter = 0;
			for (int i = vi; i < (vi + vh); i++) {
				for (int j = vj; j < (vj + vw); j++) {
					Button vehicle = buttonGrid[i][j];
					String tag = vehicleData[4];
					Color c = null;
					for (TagColor t : buttons) {
						if (t.getTag().equals(tag)) {
							c = t.getC();
						}
					}

					vehicle.setAttributes(tag, vh, vw, counter, c, false);
					counter++;
				}
			}
		}
	}

	/**
	 * Gets the shortest solution path and performs all the moves in the
	 * resulting list of moves by starting a TimerTask that performs one move
	 * per second.
	 */
	public void showSoln() {
		mainFrame.setTitle(windowLabel + " - Level " + fileCounter + " - Calculating...");

		soln = getSolutionPath();
		soln.remove(soln.get(0));

		mainFrame.setTitle(windowLabel + " - Level " + fileCounter);

		solveAnim = new repaintTask();
		animTimer = new Timer(true);
		animTimer.scheduleAtFixedRate(solveAnim, 0, 1000);

	}

	/**
	 * Takes a single-line snapshot string and parses individual button
	 * locations from the snapshot's grid.
	 * 
	 * @param s
	 * @return A list of button locations parsed from snapshot s.
	 */
	public List<String> parseSnapshot(String s) {
		List<String> locs = new ArrayList<String>();
		snapChar[][] ingrid = new snapChar[size][size];
		int k = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				ingrid[i][j] = new snapChar(s.charAt(k), false);
				k++;
			}
		}
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				System.out.print(ingrid[i][j].getC() + " ");
				if (j == size - 1) {
					System.out.println();
				}
			}
		}
		System.out.println("New Button Locations");
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (ingrid[i][j].getC() != 'B' && ingrid[i][j].isChecked() == false) {
					StringBuilder line = new StringBuilder();
					line.append((i + 1) + " " + (j + 1) + " ");
					char c = ingrid[i][j].getC();
					int w = 0;
					int h = 0;

					ingrid[i][j].setChecked(true);

					k = j + 1;
					while (k < size && ingrid[i][k].getC() == ingrid[i][j].getC()) {
						h = 1;
						ingrid[i][k].setChecked(true);
						k++;
					}
					if (h == 1) {
						w = k - j;
					}

					k = i + 1;
					while (k < size && ingrid[k][j].getC() == ingrid[i][j].getC()) {
						w = 1;
						ingrid[k][j].setChecked(true);
						k++;
					}
					if (w == 1) {
						h = k - i;
					}

					line.append(h + " " + w + " " + c);
					locs.add(line.toString());
				}
			}
		}

		return locs;
	}

	/**
	 * Manually sets the color palette from which button colors will be picked.
	 */
	public void createColors() {
		colors = new Color[10];
		colors[0] = new Color(0, 255, 255);
		colors[1] = new Color(0, 0, 255);
		colors[2] = new Color(138, 43, 226);
		colors[3] = new Color(127, 255, 0);
		colors[4] = new Color(0, 100, 0);
		colors[5] = new Color(255, 40, 147);
		colors[6] = new Color(255, 215, 0);
		colors[7] = new Color(0, 255, 0);
		colors[8] = new Color(255, 69, 0);
		colors[9] = new Color(148, 0, 211);
	}

	/**
	 * Moves the selected button to the indicated location (destI, destJ).
	 * 
	 * @param destI
	 * @param destJ
	 */
	public void performMove(int destI, int destJ) {
		Button startB = selectedButton;
		int h = startB.getH();
		int w = startB.getW();
		int startI = startB.getI();
		int startJ = startB.getJ();
		int endI = destI;
		int endJ = destJ;

		if (startI == endI && h == 1) {
			if (endJ > startJ) {
				int k = startJ + w;
				while ((k <= endJ) && buttonGrid[startI][k].isBlank() == true) {
					Button toMove = null;
					Button dest = null;
					for (int m = 0; m < w; m++) {
						toMove = buttonGrid[startI][startJ + w - 1 - m];
						dest = buttonGrid[startI][k - m];

						dest.setAttributes(toMove.getTag(), toMove.getH(), toMove.getW(), toMove.getPosition(), toMove.getC(), toMove.isBlank());
						toMove.setAttributes("blank", 1, 1, -1, new Color(210, 210, 210), true);
						toMove.setSelected(false);
					}
					k++;
					startB = dest;
					startJ = startB.getJ();
				}
				selectedButton = startB;
			} else {
				int k = startJ - 1;
				while ((k >= endJ) && buttonGrid[startI][k].isBlank() == true) {
					Button toMove = null;
					Button dest = null;
					for (int m = 0; m < w; m++) {
						toMove = buttonGrid[startI][startJ + m];
						dest = buttonGrid[startI][k + m];

						dest.setAttributes(toMove.getTag(), toMove.getH(), toMove.getW(), toMove.getPosition(), toMove.getC(), toMove.isBlank());
						toMove.setAttributes("blank", 1, 1, -1, new Color(210, 210, 210), true);
						toMove.setSelected(false);
						if (m == 0) {
							startB = dest;
						}
					}
					k--;
					startJ = startB.getJ();
				}
				selectedButton = startB;
			}
		} else if (startJ == endJ && w == 1) {
			if (endI > startI) {
				int k = startI + h;
				while ((k <= endI) && buttonGrid[k][startJ].isBlank() == true) {
					Button toMove = null;
					Button dest = null;
					for (int m = 0; m < h; m++) {
						toMove = buttonGrid[startI + h - 1 - m][startJ];
						dest = buttonGrid[k - m][startJ];

						dest.setAttributes(toMove.getTag(), toMove.getH(), toMove.getW(), toMove.getPosition(), toMove.getC(), toMove.isBlank());
						toMove.setAttributes("blank", 1, 1, -1, new Color(210, 210, 210), true);
						toMove.setSelected(false);
					}
					k++;
					startB = dest;
					startI = startB.getI();
				}
				selectedButton = startB;
			} else {
				int k = startI - 1;
				while ((k >= endI) && buttonGrid[k][startJ].isBlank() == true) {
					Button toMove = null;
					Button dest = null;
					for (int m = 0; m < h; m++) {
						toMove = buttonGrid[startI + m][startJ];
						dest = buttonGrid[k + m][startJ];

						dest.setAttributes(toMove.getTag(), toMove.getH(), toMove.getW(), toMove.getPosition(), toMove.getC(), toMove.isBlank());
						toMove.setAttributes("blank", 1, 1, -1, new Color(210, 210, 210), true);
						toMove.setSelected(false);
						if (m == 0) {
							startB = dest;
						}
					}
					k--;
					startI = startB.getI();
				}
				selectedButton = startB;
			}
		}
		Button win = buttonGrid[redRow][size - 2];
		if (win.getTag().equals("z") && win.getPosition() == 0) {
			if (fileCounter == fileNum) {
				endLevel("End of last level. Click OK to close dialog.", false);
			} else {
				endLevel("End of level. Click OK to start the next level.", true);
			}
		}

	}

	/**
	 * Selects the indicated button and adjacent tiles that belong to the same
	 * piece.
	 * 
	 * @param b
	 * @param val
	 */
	public void selectButton(Button b, boolean val) {

		if (b.getH() == 1) {
			int i = b.getI();
			int jstart = b.getJ()-b.getPosition();

			if (val == true) {
				selectedButton = buttonGrid[i][jstart];
			}
			selectedButton.setSelected(val);

			for (int j = jstart; j < (selectedButton.getW() + jstart); j++) {
				Button other = buttonGrid[i][j];
				other.setSelected(val);
			}
		} else if (b.getW() == 1) {
			int istart = b.getI() - b.getPosition();
			int j = b.getJ();

			if (val == true) {
				selectedButton = buttonGrid[istart][j];
			}
			selectedButton.setSelected(val);

			for (int i = istart; i < (selectedButton.getH() + istart); i++) {
				Button other = buttonGrid[i][j];
				other.setSelected(val);
			}
		}
	}

	/**
	 * Shows end-of-level popup dialog and advances the level to the next one if
	 * there are more level files remaining in the project directory.
	 * 
	 * @param message
	 * @param nextLvl
	 */
	public void endLevel(String message, boolean nextLvl) {
		JOptionPane.showMessageDialog(null, message, "End Of Level", JOptionPane.INFORMATION_MESSAGE);
		if (nextLvl == true) {
			fileCounter++;
			printGrid();
			init();
			printGrid();
		}
	}

	/**
	 * Mouse handler for all Buttons in the grid. Calls selectButton when
	 * appropriate.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		Button b = (Button) e.getSource();
		System.out.print(b.getTag() + " i:" + b.getI() + " j:" + b.getJ() + " pos:" + b.getPosition());
		System.out.println(" h:" + b.getH() + " w:" + b.getW());

		if (b.isBlank() == false) {
			if (selectedButton != null) {
				selectButton(selectedButton, false);
			}
			selectButton(b, true);
		} else {
			if (selectedButton != null) {
				performMove(b.getI(), b.getJ());
				if (gameWonSnap(this.Snapshot) == true) {
					System.out.println("GAME WON!!!!");
				}
			}
		}
	}

	/**
	 * Initial function to determine whether or not the user has won. Currently
	 * not in use.
	 * 
	 * @param SnapShot
	 * @return
	 */
	private boolean gameWonSnap(String SnapShot){
		int stringLength = SnapShot.length();
		for(int i=0; i<stringLength; i++){
			if(i % this.size == (this.size -1) && SnapShot.charAt(i) == 'z'){
				return true;
			}
		}
		return false;
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * Gets the snapshot of the current board.
	 * 
	 * @return
	 */
	public String getSnapshot(){
		String temp;
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < this.size; i++) {
			for (int j = 0; j < this.size; j++) {
				if (this.buttonGrid[i][j].getTag() == "blank") {
					stringBuilder.append("B");
				} else
					stringBuilder.append(this.buttonGrid[i][j].getTag());
			}
		}
		temp = stringBuilder.toString();
		return temp;
	}

	/**
	 * Gets the shortest solution path from the algorithm in Solutions.
	 * 
	 * @return
	 */
	public List<String> getSolutionPath() {
		this.Snapshot = getSnapshot();
		Solutions currentSolution = new Solutions(this.Snapshot, this.buttonGrid, this.size);
		List<String> solns = currentSolution.getSolution();
		Collections.reverse(solns);
		return solns;
	}

	/**
	 * Utility function to print out grid values.
	 */
	public void printGrid(){
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (buttonGrid[i][j].getTag() == "blank") {
					System.out.print("B ");
				}
				else
					System.out.print(buttonGrid[i][j].getTag() + " ");
			}
			System.out.println();
		}
	}

	/**
	 * Utility data structure for parseSnapshot method to keep track of which
	 * grid locations have been checked.
	 * 
	 * @author bryan
	 * 
	 */
	private class snapChar {
		private char c;
		private boolean checked;

		public snapChar(char c, boolean checked) {
			this.c = c;
			this.checked = checked;
		}

		public char getC() {
			return c;
		}

		public void setC(char c) {
			this.c = c;
		}

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}
	}

	/**
	 * Utility data structure to associate each board piece's tag with its color
	 * so pieces keep their color when moved by showHint or showSoln.
	 * 
	 * @author bryan
	 * 
	 */
	private class TagColor {
		private String tag;
		private Color c;

		public TagColor(String tag, Color c) {
			this.tag = tag;
			this.c = c;
		}

		public String getTag() {
			return tag;
		}

		public void setTag(String tag) {
			this.tag = tag;
		}

		public Color getC() {
			return c;
		}

		public void setC(Color c) {
			this.c = c;
		}
	}

	/**
	 * Scheduled task (once per second) that performs the solve animation for
	 * every move in the solution list.
	 * 
	 * @author bryan
	 * 
	 */
	private class repaintTask extends TimerTask {
		int i = 0;
		@Override
		public void run() {
			String snap = soln.get(0);
			System.out.println("Next Step");
			List<String> locs = parseSnapshot(snap);

			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					buttonGrid[i][j].setAttributes("blank", 1, 1, -1, new Color(210, 210, 210), true);
				}
			}

			for (String s : locs) {
				System.out.println(s);
				String[] vehicleData = s.split(" ");
				int vi = Integer.parseInt(vehicleData[0]) - 1;
				int vj = Integer.parseInt(vehicleData[1]) - 1;
				int vh = Integer.parseInt(vehicleData[2]);
				int vw = Integer.parseInt(vehicleData[3]);

				int counter = 0;
				for (int i = vi; i < (vi + vh); i++) {
					for (int j = vj; j < (vj + vw); j++) {
						Button vehicle = buttonGrid[i][j];
						String tag = vehicleData[4];
						Color c = null;
						for (TagColor t : buttons) {
							if (t.getTag().equals(tag)) {
								c = t.getC();
							}
						}

						vehicle.setAttributes(tag, vh, vw, counter, c, false);
						counter++;
					}
				}
			}
			mainFrame.repaint();

			soln.remove(soln.get(0));
			if (soln.size() <= 0) {
				animTimer.cancel();
				if (fileCounter == fileNum) {
					endLevel("End of last level. Click OK to close dialog.", false);
				} else {
					endLevel("End of level. Click OK to start the next level.", true);
				}
			}
		}
	}

}
