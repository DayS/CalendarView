package fr.days.calendarview.sample;

import org.joda.time.YearMonth;

import android.app.Activity;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

import fr.days.calendarview.CalendarView;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

	@ViewById
	CalendarView calendarView;

	private YearMonth currentMonth;

	@AfterViews
	void init() {
		currentMonth = YearMonth.now();
		calendarView.setCurrentMonth(currentMonth);
	}

	@Click
	void previousButtonClicked() {
		currentMonth = currentMonth.minusMonths(1);
		calendarView.setCurrentMonth(currentMonth);
	}

	@Click
	void nextButtonClicked() {
		currentMonth = currentMonth.plusMonths(1);
		calendarView.setCurrentMonth(currentMonth);
	}

}
