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
	
}
