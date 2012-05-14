package jp.skr.happycome;

public class Board {
	private int row;
	private int column;
	private int mineN;
	private boolean[] mineData;
	private int[] mineNums;
	private int[] panel;

	public Board(int row, int col) {
		this.row = row;
		this.column = col;
		this.mineData = new boolean[row * col];
		this.mineNums = new int[row * col];
		this.panel = new int[row * col];
	}

	public int getColumnN() {
		return column;
	}

	public int getRowN() {
		return row;
	}

	/**
	 * 地雷の数を設定する。
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
			panel[i] = 0;
		}

		for (int i = 0; i < mineNums.length; i++) {
			mineNums[i] = 0;
		}
	}

	public int getMineN() {
		// TODO Auto-generated method stub
		return mineN;
	}

	public boolean getMine(int col, int row) {

		return mineData[col + row * this.column];
	}

}
