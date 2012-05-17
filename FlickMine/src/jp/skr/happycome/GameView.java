package jp.skr.happycome;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author mudwell
 * 
 */
public class GameView extends View {

	private Board board;
	private int row;
	private int column;
	private int cellSize;

	/**
	 * @param context
	 */
	public GameView(Context context) {
		super(context);
		row = 7;
		column = 7;
		board = new Board(column, row);
		board.setup(10);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		cellSize = Math.min(getWidth() / column, getHeight() / row);
		System.out.println(cellSize);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint paint = new Paint();
		// 矩形を描画する

		// ボードを描画する
		for (int r = 0; r < row; r++) {
			for (int c = 0; c < column; c++) {
				if (board.isOpen(c, r)) {
					drawBlock(canvas, c, r, paint, 0xff808080);
					// 開かれている場合
					if (board.getMine(c, r)) {
						// 地雷を描画する
						// paint.setColor(0xffff0000);
						// paint.setStyle(Style.FILL);
						// canvas.drawCircle(c * cellSize + cellSize / 2, r
						// * cellSize + cellSize / 2, cellSize / 2, paint);
					} else if (board.getMineAround(c, r) > 0) {
						// 周囲の地雷数を描画する
						paint.setColor(0xff000000);
						paint.setTextSize(cellSize);
						paint.setTextAlign(Align.CENTER);
						canvas.drawText(
								Integer.toString(board.getMineAround(c, r)), c
										* cellSize + cellSize / 2, (r + 1)
										* cellSize, paint);
					} else {
						// 地雷がない場合は背景を描画する
					}
					// paint.setColor(Color.rgb(0xff, 0, 0));
				} else {
					paint.setColor(Color.rgb(0, 0, 0));
					drawBlock(canvas, c, r, paint, 0xffc9c9c9);
				}
				if (!board.isPlaying() && board.getMine(c, r)) {
					// 地雷を描画する
					paint.setColor(0xffff0000);
					paint.setStyle(Style.FILL);
					canvas.drawCircle(c * cellSize + cellSize / 2, r * cellSize
							+ cellSize / 2, cellSize / 2, paint);

				}
			}
		}
	}

	/**
	 * マスを描画する.
	 * 
	 * @param canvas
	 * @param x
	 * @param y
	 * @param paint
	 * @param bgcolor
	 *            背景色(ARGB)
	 */
	private void drawBlock(Canvas canvas, int x, int y, Paint paint, int bgcolor) {

		// マスを描画する
		paint.setColor(bgcolor);
		paint.setStyle(Style.FILL);
		canvas.drawRect(x * cellSize, y * cellSize, (x + 1) * cellSize, (y + 1)
				* cellSize, paint);
		paint.setStyle(Style.STROKE);
		paint.setColor(Color.rgb(128, 128, 128));
		canvas.drawRect(x * cellSize, y * cellSize, (x + 1) * cellSize, (y + 1)
				* cellSize, paint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x = (int) (event.getX() / cellSize);
		int y = (int) (event.getY() / cellSize);
		if (x < column && y < row && !board.isOpen(x, y)) {
			board.open(x, y);
			invalidate();
		}
		return true;
	}
}