package jp.skr.happycome;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class FlickMineActivity extends Activity {
	private static final int MENU_ID_A = 0;
	private static final int MENU_ID_B = 1;
	private static final int MENU_ID_C = 2;
	private GameView view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		view = new GameView(this);
		setContentView(view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, MENU_ID_A, Menu.NONE, "リセット");
		menu.add(Menu.NONE, MENU_ID_B, Menu.NONE, "終了");
		menu.add(Menu.NONE, MENU_ID_C, Menu.NONE, "設定");

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_ID_A:
			// リセット
			new AlertDialog.Builder(this).setMessage("リセットしますか？")
					.setPositiveButton("OK", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							view.setup();
						}
					}).setNegativeButton("キャンセル", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					}).show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}