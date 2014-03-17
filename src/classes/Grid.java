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
	private int redRow;
	
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
			while ((s = reader.readLine()) != null) {
				list.add(s);
				System.out.println(s);
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createRequiredButtons() {
		Random r = new Random(System.currentTimeMillis());
		buttonGrid = new Button[size][size];
		String[] req = new String[] { "1x2", "1x3", "2x1", "3x1" };

		// initialize grid to blank tiles
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				Button b = new Button(i, j, "blank");
				b.setBlank(true);
				b.addMouseListener(this);
				buttonGrid[i][j] = b;
			}
		}

		int redI = r.nextInt(size / 2) + (size / 2);
		int redJ1 = r.nextInt(size / 2 - 1);
		int redJ2;
		if (redJ1 == size - 1) {
			redJ2 = redJ1 - 1;
		} else {
			redJ2 = redJ1 + 1;
		}
		Button redCar1 = buttonGrid[redI][redJ1];
		Button redCar2 = buttonGrid[redI][redJ2];
		redCar1.setAttributes("redcar", "1x2", (redJ2 > redJ1) ? 0 : 1, new Color(255, 0, 0), false);
		redCar2.setAttributes("redcar", "1x2", (redJ2 > redJ1) ? 1 : 0, new Color(255, 0, 0), false);
		redRow = redI;

		for (String s : req) {
			String[] dims = s.split("x");
			int dimI = Integer.parseInt(dims[0]);
			int dimJ = Integer.parseInt(dims[1]);

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
		int startI = selectedButton.getI();
		int startJ = selectedButton.getJ();
		int endI = destI;
		int endJ = destJ;

	}

	public void selectButton(Button b, boolean val) {
		if (val == true) {
			selectedButton = b;
		}
		selectedButton.setSelected(val);
		if (b.getType().equals("1x2")) {
			int j = (b.getPosition() == 0) ? (b.getJ() + 1) : (b.getJ() - 1);
			Button other = buttonGrid[b.getI()][j];
			other.setSelected(val);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Button b = (Button) e.getSource();

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
