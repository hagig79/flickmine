package jp.skr.happycome;

/**
 * 盤クラス.
 * 
 * @author mudwell
 * 
 */
public class Board {
	private static final int OPEN = 0;
	private static final int CLOSE = 1;
	private int row;
	private int column;
	private int mineN;
	private boolean[] mineData;
	private int[] mineNums;
	private int[] panel;
	private int openN;

	/**
	 * ゲーム中かどうか.
	 */
	private boolean playing;

	/**
	 * @param column
	 *            列数
	 * @param row
	 *            行数
	 */
	public Board(int column, int row) {
		this.row = row;
		this.column = column;
		this.mineData = new boolean[row * column];
		this.mineNums = new int[row * column];
		this.panel = new int[row * column];
		for (int i = 0; i < panel.length; i++) {
			panel[i] = CLOSE;
		}
	}

	/**
	 * 列数を返す.
	 * 
	 * @return 列数
	 */
	public int getColumnN() {
		return column;
	}

	/**
	 * 行数を返す.
	 * 
	 * @return 行数
	 */
	public int getRowN() {
		return row;
	}

	/**
	 * 地雷の数を設定する.
	 * 
	 * @param mines
	 */
	public void setup(int mines) {
		this.mineN = mines;

		for (int i = 0; i < mineN; i++) {
			mineData[i] = true;
		}

		for (int i = mineN; i < mineData.length; i++) {
			mineData[i] = false;
		}

		for (int i = 0; i < mineN; i++) {
			int index = (int) (Math.random() * (mineData.length - 1));
			boolean temp = mineData[index];
			mineData[index] = mineData[i];
			mineData[i] = temp;
		}

		for (int i = 0; i < panel.length; i++) {
			panel[i] = CLOSE;
		}

		for (int i = 0; i < mineNums.length; i++) {
			mineNums[i] = 0;
		}
		openN = 0;
		playing = true;
	}

	/**
	 * @return
	 */
	public int getMineN() {
		return mineN;
	}

	/**
	 * @param col
	 * @param row
	 * @return
	 */
	public int getMineAround(int col, int row) {
		return mineNums[col + row * column];
	}

	/**
	 * @param col
	 * @param row
	 * @return
	 */
	public boolean getMine(int col, int row) {
		return mineData[col + row * this.column];
	}

	/**
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isOpen(int x, int y) {
		return panel[x + y * column] == OPEN;
	}

	/**
	 * @param x
	 * @param y
	 */
	public void open(int x, int y) {
		if (!playing) {
			return;
		}
		int index = x + y * column;
		panel[index] = OPEN;
		if (openN == 0) {
			// 初めて開く
			System.arraycopy(mineData, index, mineData, index + 1,
					mineData.length - index - 1);
			mineData[index] = false;

			// 回りにある地雷の個数を計算する
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < column; j++) {
					mineNums[j + i * column] = countMineAround(j, i);
				}
			}
			openRecursive(x, y);
		} else if (mineData[index]) {
			// ゲーム終了
			playing = false;
		} else {
			openRecursive(x, y);
		}
		openN = 0;
		for (int i = 0; i < panel.length; i++) {
			if (panel[i] == OPEN) {
				openN++;
			}
		}
	}

	/**
	 * 再帰的にパネルを開く.
	 * 
	 * @param x
	 *            開く開始パネルX座標(0 <= x < column).
	 * @param y
	 *            開く開始パネルY座標(0 <= y < row).
	 */
	private void openRecursive(int x, int y) {
		panel[x + y * column] = OPEN;
		if (mineNums[x + y * column] == 0) {
			if (x > 0 && panel[(x - 1) + y * column] == CLOSE) {
				openRecursive(x - 1, y);
			}
			if (x < column - 1 && panel[(x + 1) + y * column] == CLOSE) {
				openRecursive(x + 1, y);
			}
			if (y > 0 && panel[x + (y - 1) * column] == CLOSE) {
				openRecursive(x, y - 1);
			}
			if (y < row - 1 && panel[x + (y + 1) * column] == CLOSE) {
				openRecursive(x, y + 1);
			}

			if (x > 0 && y > 0 && panel[(x - 1) + (y - 1) * column] == CLOSE) {
				openRecursive(x - 1, y - 1);
			}
			if (x > 0 && y < row - 1
					&& panel[(x - 1) + (y + 1) * column] == CLOSE) {
				openRecursive(x - 1, y + 1);
			}

			if (x < column - 1 && y > 0
					&& panel[(x + 1) + (y - 1) * column] == CLOSE) {
				openRecursive(x + 1, y - 1);
			}
			if (x < column - 1 && y < row - 1
					&& panel[(x + 1) + (y + 1) * column] == CLOSE) {
				openRecursive(x + 1, y + 1);
			}
		}

	}

	/**
	 * 周囲の地雷数を返す.
	 * 
	 * (x, y)の回りにある地雷数を返す. (x, y)に地雷は数えない.
	 * 
	 * @param x
	 * @param y
	 * @return 地雷数(0～8)
	 */
	private int countMineAround(int x, int y) {
		int count = 0;
		if (x > 0) {
			if (mineData[(x - 1) + y * column]) {
				count++;
			}
			if (y > 0) {
				if (mineData[(x - 1) + (y - 1) * column]) {
					count++;
				}
			}
			if (y < row - 1) {
				if (mineData[(x - 1) + (y + 1) * column]) {
					count++;
				}
			}
		}
		if (x < column - 1) {
			if (mineData[(x + 1) + y * column]) {
				count++;
			}
			if (y > 0) {
				if (mineData[(x + 1) + (y - 1) * column]) {
					count++;
				}
			}
			if (y < row - 1) {
				if (mineData[(x + 1) + (y + 1) * column]) {
					count++;
				}
			}
		}
		if (y > 0) {
			if (mineData[x + (y - 1) * column]) {
				count++;
			}
		}
		if (y < row - 1) {
			if (mineData[x + (y + 1) * column]) {
				count++;
			}
		}
		return count;
	}

	/**
	 * @return
	 */
	public boolean isPlaying() {
		return playing;
	}
}