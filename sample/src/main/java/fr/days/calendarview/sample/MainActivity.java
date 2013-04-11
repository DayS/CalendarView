package fr.days.calendarview.sample;

import org.joda.time.YearMonth;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

import fr.days.calendarview.CalendarView;

import android.app.Activity;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

	@ViewById
	CalendarView calendarView;

	@AfterViews
	void init() {
		calendarView.setCurrentMonth(YearMonth.now());
	}
	
}
