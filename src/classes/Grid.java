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
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class Grid implements MouseListener {
	
	private int size = 6; // SIZE OF BOARD
	private int boardSize;
	
	private JFrame mainFrame; // main window
	private JPanel content; // to contain the reset/hint bar and main board
	private JPanel bar; // contains reset/hint/solution
	private Button[][] buttonGrid; // grid of JLabels
	private Button selectedButton;
	private Color[] colors;
	private List<String> locations;
	private String redLoc;
	
	public Grid(String windowLabel) {
		mainFrame = new JFrame(windowLabel);
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		create_menu();
		init();
	}
	
	public void create_menu() {

		JMenuBar menu = new JMenuBar();

		JMenu Game = new JMenu("Game"), Help = new JMenu("Help");

		JMenuItem eXit = new JMenuItem("eXit"), help = new JMenuItem("Help"), about = new JMenuItem("About");

		eXit.setMnemonic('X');
		help.setMnemonic('H');
		about.setMnemonic('A');

		Game.setMnemonic('G');
		Game.add(eXit);

		Help.setMnemonic('H');
		Help.add(help);
		Help.add(about);

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
		mainFrame.setJMenuBar(menu);
	}
	
	public void init(){
		createColors();
		readFile();

		boardSize = size * 80;

		JPanel panel = new JPanel(new GridLayout(size, size));
		panel.setPreferredSize(new Dimension(boardSize, boardSize));

		createRequiredButtons();

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				Button b = buttonGrid[i][j];
				panel.add(b);
			}
		}

		mainFrame.add(panel);
		mainFrame.pack();
		mainFrame.setVisible(true);

	}

	public void readFile() {
		File file = new File("proj3a.data"); // using sample data for now
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		List<String> list = new ArrayList<String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader("proj3a.data"));
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

	public void createRequiredButtons() {
		Random r = new Random(System.currentTimeMillis());
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

		String[] redCarData = redLoc.split(" ");
		int redI = Integer.parseInt(redCarData[0]) - 1;
		int redJ1 = Integer.parseInt(redCarData[1]) - 1;
		int redJ2 = Integer.parseInt(redCarData[1]);
		int redH = Integer.parseInt(redCarData[2]);
		int redW = Integer.parseInt(redCarData[3]);

		Button redCar1 = buttonGrid[redI][redJ1];
		Button redCar2 = buttonGrid[redI][redJ2];
		redCar1.setAttributes(redCarData[4], redH, redW, 0, new Color(255, 0, 0), false);
		redCar2.setAttributes(redCarData[4], redH, redW, 1, new Color(255, 0, 0), false);

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
					counter++;
				}
			}
			color = (color >= 10) ? 0 : (color + 1);
		}
	}

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

	}

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
			}
		}
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
}
