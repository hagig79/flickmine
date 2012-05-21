package jp.skr.happycome;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author mudwell
 * 
 */
public class GameView extends View implements
		GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

	private Board board;
	private int row;
	private int column;
	private int cellSize;
	private GestureDetector gesDetect;
	private Bitmap mine;
	private Bitmap flag;
	private Context context;

	/**
	 * @param context
	 */
	public GameView(Context context) {
		super(context);
		this.context = context;

		row = 8;
		column = 8;
		board = new Board(column, row);
		board.setup(10);

		mine = BitmapFactory.decodeResource(getResources(), R.drawable.mine);
		flag = BitmapFactory.decodeResource(getResources(), R.drawable.flag);

		gesDetect = new GestureDetector(context, this);
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
					if (board.getMineAround(c, r) > 0) {
						// 周囲の地雷数を描画する
						paint.setColor(0xff000000);
						paint.setTextSize(cellSize);
						paint.setTextAlign(Align.CENTER);
						canvas.drawText(
								Integer.toString(board.getMineAround(c, r)), c
										* cellSize + cellSize / 2, (r + 1)
										* cellSize, paint);

					}
				} else if (board.isFlag(c, r)) {
					canvas.drawBitmap(flag, c * cellSize, r * cellSize, paint);
				} else {
					paint.setColor(Color.rgb(0, 0, 0));
					drawBlock(canvas, c, r, paint, 0xffc9c9c9);
				}
				if (board.isGameOver() && board.getMine(c, r)) {
					// 地雷を描画する
					canvas.drawBitmap(mine, c * cellSize, r * cellSize, paint);

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
		gesDetect.onTouchEvent(event);
		return true;
	}

	public void setup() {
		board.setup(10);
		invalidate();
	}

	@Override
	public boolean onDoubleTap(MotionEvent event) {
		int x = (int) (event.getX() / cellSize);
		int y = (int) (event.getY() / cellSize);
		if (x < column && y < row && board.isOpen(x, y)) {
			board.openAround(x, y);
			if (board.isGameClear()) {
				new AlertDialog.Builder(context).setMessage("ゲームクリア！").show();
			}
			invalidate();
		}
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// float th = -10;
		// // if (velocityY <= th) {
		int x = (int) (e1.getX() / cellSize);
		int y = (int) (e1.getY() / cellSize);
		if (x < column && y < row) {
			if (velocityY < 0) {
				// 上向き
				// 旗を抜く
				board.setFlag(x, y, false);
			} else {
				// 下向き
				// 旗を立てる
				board.setFlag(x, y, true);
			}
			//
			// blockData[y * COL_NUM + x] = !blockData[y *
			// COL_NUM
			// + x];
			invalidate();

		}

		// }
		return true;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent event) {
		int x = (int) (event.getX() / cellSize);
		int y = (int) (event.getY() / cellSize);
		if (!board.isPlaying()) {
			return false;
		}
		if (x < column && y < row && !board.isOpen(x, y) && !board.isFlag(x, y)) {
			board.open(x, y);
			if (board.isGameClear()) {
				new AlertDialog.Builder(context).setMessage("ゲームクリア！").show();
			}
			invalidate();
		}
		return false;
	}
}