package jp.skr.happycome;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.View;

public class GameView extends View {

	private Board board;
	private int row;
	private int column;
	private int cellSize;

	public GameView(Context context) {
		super(context);
		row = 11;
		column = 10;
		board = new Board(row, column);
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
				if (board.getMine(c, r)) {
					// 地雷があれば赤
					paint.setColor(Color.rgb(0xff, 0, 0));
				} else {
					// 地雷がなければ黒
					paint.setColor(Color.rgb(0, 0, 0));
				}
				// マスを描画する
				paint.setStyle(Style.FILL);
				canvas.drawRect(c * cellSize, r * cellSize, (c + 1) * cellSize,
						(r + 1) * cellSize, paint);
				paint.setStyle(Style.STROKE);
				paint.setColor(Color.rgb(128, 128, 128));
				canvas.drawRect(c * cellSize, r * cellSize, (c + 1) * cellSize,
						(r + 1) * cellSize, paint);
			}
		}
	}

}
