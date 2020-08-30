package Tetris;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Tetris extends JPanel implements KeyListener {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Tetris instance
	 */
	public static Tetris game;

	/**
	 * JFrame insance
	 */
	JFrame jf;

	/**
	 * playing grid
	 */
	int[][] grid = new int[10][20];

	/**
	 * shape of the blocks
	 */
	Block[] templates = { new Block(new int[] { 4, 4, 5, 5 }, new int[] { 0, 1, 1, 0 }, 0),
			new Block(new int[] { 4, 5, 6, 7 }, new int[] { 0, 0, 0, 0 }, 0),
			new Block(new int[] { 4, 5, 5, 6 }, new int[] { 0, 0, 1, 1 }, 0),
			new Block(new int[] { 6, 5, 5, 4 }, new int[] { 0, 0, 1, 1 }, 0),
			new Block(new int[] { 4, 5, 6, 6 }, new int[] { 0, 0, 0, 1 }, 0),
			new Block(new int[] { 4, 4, 5, 6 }, new int[] { 1, 0, 0, 0 }, 0),
			new Block(new int[] { 4, 5, 5, 6 }, new int[] { 0, 0, 1, 0 }, 0) };

	/**
	 * current block
	 */
	Block current;

	/**
	 * next block
	 */
	Block next;

	/**
	 * saved block
	 */
	Block save;

	/**
	 * is there a saved block?
	 */
	boolean hasSave = false;

	/**
	 * wait
	 */
	boolean wait = false;

	/**
	 * score
	 */
	int score = 0;

	/**
	 * array of colors
	 */
	Color[] colors = { Color.WHITE, Color.YELLOW, Color.RED, Color.ORANGE, Color.BLUE, Color.RED, Color.MAGENTA,
			Color.GREEN, Color.PINK };

	/**
	 * Random instance
	 */
	Random random = new Random();

	/**
	 * Tetris constructor
	 */
	public Tetris() {
		addKeyListener(this);
		setFocusable(true);
	}

	/**
	 * start method
	 */
	public void start() {
		int index = random.nextInt(templates.length);
		int color = random.nextInt(colors.length - 1) + 1;
		current = new Block(templates[index].x, templates[index].y, color);
		index = random.nextInt(templates.length);
		color = random.nextInt(colors.length - 1) + 1;
		next = new Block(templates[index].x, templates[index].y, color);
		jf = new JFrame("tetris");
		jf.setSize(getToolkit().getScreenSize());
		jf.setVisible(true);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.add(game);
		int time = 0;
		while (true) {
			time++;
			if (time % 10 == 0)
				game.update();
			jf.repaint();
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {

			}
		}
	}

	public void next() {
		int index = random.nextInt(templates.length);
		int color = random.nextInt(colors.length - 1) + 1;
		current = next;
		next = new Block(templates[index].x, templates[index].y, color);
	}

	/**
	 * updates the game
	 */
	public void update() {
		if (current.canFall())
			current.fall();
		else
			wait = true;
		if (wait) {
			for (int i = 0; i < 4; i++)
				grid[current.x[i]][current.y[i]] = current.color;
			next();
			wait = false;
		}
		for (int j = 19; j >= 0; j--) {
			boolean empty = false;
			for (int i = 0; i < 10; i++)
				if (grid[i][j] == 0)
					empty = true;

			if (!empty) {
				score++;
				for (int k = j - 1; k >= 0; k--)
					for (int i = 0; i < 10; i++)
						grid[i][k + 1] = grid[i][k];

				for (int i = 0; i < 10; i++)
					grid[i][0] = 0;
			}
		}
	}

	/**
	 * paint function, called every frame
	 */
	public void paint(Graphics g) {
		g.drawOval(590, 28, 15, 15);
		g.drawString("C", 593, 40);
		g.drawString("Jonathan Guo", 610, 40);
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 20; j++) {
				int xCoordinate = 500 + 25 * i;
				int yCoordinate = 100 + 25 * j;
				g.setColor(colors[grid[i][j]]);
				g.fillRect(xCoordinate, yCoordinate, 25, 25);
				g.setColor(Color.BLACK);
				g.drawRect(xCoordinate, yCoordinate, 25, 25);
			}
		}
		for (int i = 0; i < 4; i++) {
			int xCoordinate = current.x[i] * 25 + 500;
			int yCoordinate = current.y[i] * 25 + 100;
			g.setColor(colors[current.color]);
			g.fillRect(xCoordinate, yCoordinate, 25, 25);
			g.setColor(Color.BLACK);
			g.drawRect(xCoordinate, yCoordinate, 25, 25);
			int nextXCoordinate = next.x[i] * 25 + 100;
			int nextYCoordinate = next.y[i] * 25 + 100;
			g.setColor(colors[next.color]);
			g.fillRect(nextXCoordinate, nextYCoordinate, 25, 25);
			if (hasSave) {
				int saveXCoordinate = save.x[i] * 25 + 100;
				int saveYCoordinate = save.y[i] * 25 + 300;
				g.setColor(colors[save.color]);
				g.fillRect(saveXCoordinate, saveYCoordinate, 25, 25);
			}
		}
		g.setColor(Color.BLACK);
		g.drawString("" + score, 1100, 100);
		g.drawString("next", 100, 70);
		g.drawString("save", 100, 270);
	}

	/**
	 * main method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		game = new Tetris();
		game.start();
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		String bob = KeyEvent.getKeyText(e.getKeyCode());
		if (bob.equals("A"))
			current.move(-1);
		if (bob.equals("D"))
			current.move(1);
		if (bob.equals("Q") && current.canFall()) {
			current.fall();
			if (current.canFall())
				current.fall();
		}
		if (bob.equals("S"))
			current.rotate(1);
		if (bob.equals("W"))
			current.rotate(-1);
		if (bob.equals("E")) {
			if (!hasSave) {
				save = current;
				hasSave = true;
				next();
			} else {
				Block swap = save;
				save = current;
				current = swap;
			}
			int minX = 1000;
			int minY = 1000;
			for (int i = 0; i < 4; i++) {
				if (save.x[i] < minX)
					minX = save.x[i];
				if (save.y[i] < minY)
					minY = save.y[i];
			}
			for (int i = 0; i < 4; i++) {
				save.x[i] -= minX;
				save.y[i] -= minY;
				save.x[i] += 4; // move down 4 spaces
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
