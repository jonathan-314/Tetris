package Tetris;

/**
 * Block class
 * 
 * @author jonguo6
 */
public class Block {
	/**
	 * color of block
	 */
	int color;

	/**
	 * x coordinates of block
	 */
	int[] x = new int[4];

	/**
	 * y coordinates of block
	 */
	int[] y = new int[4];

	/**
	 * Block constructor
	 * 
	 * @param x     x coordinates
	 * @param y     y coordinates
	 * @param color color
	 */
	public Block(int[] x, int[] y, int color) {
		for (int i = 0; i < 4; i++) {
			this.x[i] = x[i];
			this.y[i] = y[i];
		}
		this.color = color;
	}

	/**
	 * can this block fall
	 * 
	 * @return whether or not this block can fall
	 */
	public boolean canFall() {
		for (int i = 0; i < 4; i++) {
			if (y[i] + 1 == 20)
				return false;
			if (Tetris.game.grid[x[i]][y[i] + 1] != 0)
				return false;
		}
		return true;
	}

	/**
	 * makes this block fall
	 */
	public void fall() {
		for (int i = 0; i < 4; i++)
			y[i]++;
	}

	/**
	 * moves this block horizontally
	 * 
	 * @param dir direction to move
	 */
	public void move(int dir) {
		for (int i = 0; i < 4; i++) {
			int nextX = x[i] + dir;
			if (nextX < 0 || nextX >= 10)
				return;
			if (Tetris.game.grid[nextX][y[i]] != 0)
				return;
		}
		for (int i = 0; i < 4; i++)
			x[i] += dir;
	}

	/**
	 * rotates this block
	 * 
	 * @param dir direction to rotate
	 */
	public void rotate(int dir) {
		int minX = 100;
		int minY = 100;
		for (int i = 0; i < 4; i++) {
			if (x[i] < minX)
				minX = x[i];
			if (y[i] < minY)
				minY = y[i];
		}
		for (int i = 0; i < 4; i++) {
			x[i] -= minX;
			y[i] -= minY;
			int swap = x[i];
			x[i] = dir * y[i];
			y[i] = -1 * dir * swap;
		}
		int newMinX = 100;
		int newMinY = 100;
		for (int i = 0; i < 4; i++) {
			if (x[i] < newMinX)
				newMinX = x[i];
			if (y[i] < newMinY)
				newMinY = y[i];
		}
		for (int i = 0; i < 4; i++) {
			x[i] += minX;
			x[i] -= newMinX;
			y[i] += minY;
			y[i] -= newMinY;
		}
		while (true) {
			boolean outOfBounds = false;
			for (int i = 0; i < 4; i++) {
				if (x[i] < 0) {
					outOfBounds = true;
					move(1);
				} else if (x[i] >= 10) {
					move(-1);
					outOfBounds = true;
				}
				if (y[i] < 0) {
					outOfBounds = true;
					fall();
				}
			}
			if (!outOfBounds)
				break;
		}
	}
}
