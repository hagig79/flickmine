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
	private static final int FLAG = 2;
	private int row;
	private int column;
	private int mineN;
	private boolean[] mineData;
	private int[] mineNums;
	private int[] panel;
	private int openN;
	private boolean gameFailed;

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
		gameFailed = false;
	}

	/**
	 * 地雷の総数を返す.
	 * 
	 * @return 地雷の総数
	 */
	public int getMineN() {
		return mineN;
	}

	/**
	 * 指定した位置の回りにある地雷の数を返す.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int getMineAround(int x, int y) {
		return mineNums[x + y * column];
	}

	/**
	 * 指定した位置に地雷があるかどうかを返す.
	 * 
	 * @param x
	 * @param y
	 * @return 地雷があればtrue.なければfalse.
	 */
	public boolean getMine(int x, int y) {
		return mineData[x + y * this.column];
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
			gameFailed = true;
		} else {
			openRecursive(x, y);
		}
		openN = 0;
		for (int i = 0; i < panel.length; i++) {
			if (panel[i] == OPEN) {
				openN++;
			}
		}
		if (row * column - mineN == openN) {
			playing = false;
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

	/**
	 * @param x
	 * @param y
	 * @param flag
	 */
	public void setFlag(int x, int y, boolean flag) {
		if (flag && panel[x + y * column] == CLOSE) {
			panel[x + y * column] = FLAG;
		} else if (!flag && panel[x + y * column] == FLAG) {
			panel[x + y * column] = CLOSE;
		}
	}

	/**
	 * 周囲を開く.
	 * 
	 * (x, y)がすでに開かれていて地雷数と同数の旗が周囲に立っている場合、周囲の閉じているマスを全て開く.
	 * 
	 * @param x
	 * @param y
	 */
	public void openAround(int x, int y) {
		if (!isOpen(x, y) || getMineAround(x, y) == 0) {
			// まだ開かれていないか周囲に地雷が無い場合は何もしない
			return;
		}
		// 旗の数と地雷の数が同じかどうか
		if (getMineAround(x, y) != countFlagAround(x, y)) {
			return;
		}
		if (x > 0) {
			if (y > 0 && !isOpen(x - 1, y - 1) && !isFlag(x - 1, y - 1)) {
				open(x - 1, y - 1);
			}
			if (!isOpen(x - 1, y) && !isFlag(x - 1, y)) {
				open(x - 1, y);
			}
			if (y < row - 1 && !isOpen(x - 1, y + 1) && !isFlag(x - 1, y + 1)) {
				open(x - 1, y + 1);
			}
		}
		if (x < column - 1) {
			if (y > 0 && !isOpen(x + 1, y - 1) && !isFlag(x + 1, y - 1)) {
				open(x + 1, y - 1);
			}
			if (!isOpen(x + 1, y) && !isFlag(x + 1, y)) {
				open(x + 1, y);
			}
			if (y < row - 1 && !isOpen(x + 1, y + 1) && !isFlag(x + 1, y + 1)) {
				open(x + 1, y + 1);
			}
		}
		if (y > 0 && !isOpen(x, y - 1) && !isFlag(x, y - 1)) {
			open(x, y - 1);
		}
		if (y < row - 1 && !isOpen(x, y + 1) && !isFlag(x, y + 1)) {
			open(x, y + 1);
		}
	}

	private int countFlagAround(int x, int y) {
		int count = 0;
		if (x > 0) {
			if (panel[(x - 1) + y * column] == FLAG) {
				count++;
			}
			if (y > 0) {
				if (panel[(x - 1) + (y - 1) * column] == FLAG) {
					count++;
				}
			}
			if (y < row - 1) {
				if (panel[(x - 1) + (y + 1) * column] == FLAG) {
					count++;
				}
			}
		}
		if (x < column - 1) {
			if (panel[(x + 1) + y * column] == FLAG) {
				count++;
			}
			if (y > 0) {
				if (panel[(x + 1) + (y - 1) * column] == FLAG) {
					count++;
				}
			}
			if (y < row - 1) {
				if (panel[(x + 1) + (y + 1) * column] == FLAG) {
					count++;
				}
			}
		}
		if (y > 0) {
			if (panel[x + (y - 1) * column] == FLAG) {
				count++;
			}
		}
		if (y < row - 1) {
			if (panel[x + (y + 1) * column] == FLAG) {
				count++;
			}
		}
		return count;
	}

	public boolean isFlag(int c, int r) {
		return panel[c + r * column] == FLAG;
	}

	public boolean isGameOver() {
		return !playing && gameFailed;
	}

	public boolean isGameClear() {
		return !playing && !gameFailed;
	}
}