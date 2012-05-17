package jp.skr.happycome;

import static org.junit.Assert.*;

import org.junit.Test;

public class BoardTest {

	@Test
	public void testInitialize() {
		int row = 10;
		int col = 10;
		Board board = new Board(row, col);
		assertEquals(row, board.getRowN());
		assertEquals(col, board.getColumnN());
	}

	@Test
	public void testSetMine() throws Exception {
		Board board = createBoard();

		int mines = 10;
		// 地雷をセットする
		board.setup(mines);

		assertEquals(mines, board.getMineN());
	}

	private Board createBoard() {
		int row = 10;
		int col = 10;
		Board board = new Board(row, col);
		return board;
	}

	@Test
	public void testPlaying() throws Exception {
		int row = 10;
		int col = 10;
		Board board = new Board(row, col);

		int mines = 10;
		// 地雷をセットする
		board.setup(mines);

		// ゲームプレイ中
		assertTrue(board.isPlaying());

		// 一部を開く
		board.open(0, 0);
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (board.getMine(j, i)) {
					// 地雷のある場所を開く
					board.open(j, i);
				}
			}
		}
		// ゲーム終了
		assertFalse(board.isPlaying());
	}
}