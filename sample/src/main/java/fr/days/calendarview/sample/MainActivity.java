package fr.days.calendarview.sample;

import org.joda.time.YearMonth;

import android.app.Activity;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

import fr.days.calendarview.CalendarView;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity {

	@ViewById
	CalendarView calendarView;

	@ViewById
	TextView titleView;

	@AfterViews
	void init() {
		setCurrentMonth(YearMonth.now());
	}

	@Click
	void previousButtonClicked() {
		setCurrentMonth(calendarView.getCurrentMonth().minusMonths(1));
	}

	@Click
	void nextButtonClicked() {
		setCurrentMonth(calendarView.getCurrentMonth().plusMonths(1));
	}

	void setCurrentMonth(YearMonth currentMonth) {
		calendarView.setCurrentMonth(currentMonth);
		titleView.setText(currentMonth.monthOfYear().getAsText());
	}

}
