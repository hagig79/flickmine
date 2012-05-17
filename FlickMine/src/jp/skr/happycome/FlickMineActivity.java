package jp.skr.happycome;

import android.app.Activity;
import android.os.Bundle;

public class FlickMineActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GameView view = new GameView(this);
		setContentView(view);
	}
}