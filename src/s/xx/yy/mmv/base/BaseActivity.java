package s.xx.yy.mmv.base;

import s.xx.yy.mmv.view.SystemBarTintManager;
import ssmc.nsss.scS.mdsa.ieeq.mhdu.R;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.tandong.sa.activity.SmartActivity;


public class BaseActivity extends SmartActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initStatus();
	}

	private void initStatus() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			// Translucent status bar
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			// Translucent navigation bar
			window.setFlags(
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.status);
			// tintManager.setNavigationBarTintEnabled(true);
			// tintManager.setNavigationBarTintResource(R.color.white);
		}
	}
}
