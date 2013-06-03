package fr.dvilleneuve.calendarview;

import android.util.Log;

public class LogWrapper {

	private static final String TAG = "CalendarView";

	public static void trace(String msg) {
		if (Log.isLoggable(TAG, Log.VERBOSE))
			Log.v(TAG, msg);
	}

	public static void trace(String msg, Object... args) {
		if (Log.isLoggable(TAG, Log.VERBOSE))
			Log.v(TAG, String.format(msg, args));
	}

	public static void trace(String msg, Throwable throwable) {
		if (Log.isLoggable(TAG, Log.VERBOSE))
			Log.v(TAG, msg, throwable);
	}

	public static void debug(String msg) {
		if (Log.isLoggable(TAG, Log.DEBUG))
			Log.d(TAG, msg);
	}

	public static void debug(String msg, Object... args) {
		if (Log.isLoggable(TAG, Log.DEBUG))
			Log.d(TAG, String.format(msg, args));
	}

	public static void debug(String msg, Throwable throwable) {
		if (Log.isLoggable(TAG, Log.DEBUG))
			Log.d(TAG, msg, throwable);
	}

	public static void info(String msg) {
		if (Log.isLoggable(TAG, Log.INFO))
			Log.i(TAG, msg);
	}

	public static void info(String msg, Object... args) {
		if (Log.isLoggable(TAG, Log.INFO))
			Log.i(TAG, String.format(msg, args));
	}

	public static void info(String msg, Throwable throwable) {
		if (Log.isLoggable(TAG, Log.INFO))
			Log.i(TAG, msg, throwable);
	}

	public static void error(String msg) {
		if (Log.isLoggable(TAG, Log.ERROR))
			Log.e(TAG, msg);
	}

	public static void error(String msg, Object... args) {
		if (Log.isLoggable(TAG, Log.ERROR))
			Log.e(TAG, String.format(msg, args));
	}

	public static void error(String msg, Throwable throwable) {
		if (Log.isLoggable(TAG, Log.ERROR))
			Log.e(TAG, msg, throwable);
	}

}
