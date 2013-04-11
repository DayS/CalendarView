package fr.days.calendarview;

import android.util.Log;

public class LogWrapper {

	private static final String TAG = "CalendarView";

	public static void debug(String msg) {
		Log.d(TAG, msg);
	}

	public static void debug(String msg, Throwable throwable) {
		Log.d(TAG, msg, throwable);
	}

	public static void info(String msg) {
		Log.i(TAG, msg);
	}

	public static void info(String msg, Throwable throwable) {
		Log.i(TAG, msg, throwable);
	}

	public static void error(String msg) {
		Log.e(TAG, msg);
	}

	public static void error(String msg, Throwable throwable) {
		Log.e(TAG, msg, throwable);
	}

}
