package s.xx.yy.mmv.utils;

import s.xx.yy.mmv.constants.Conf;
import android.util.Log;

public class MyLog {

	public static void i(String tag, String msg) {
		if (Conf.LOG_MODE && msg != null) {
			Log.i(tag, msg);
		} else {

		}
	}

	public static void e(String tag, String msg) {
		if (Conf.LOG_MODE && msg != null) {
			Log.e(tag, msg);
		} else {

		}
	}
}
