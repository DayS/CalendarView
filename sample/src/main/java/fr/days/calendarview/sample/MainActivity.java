package fr.days.calendarview.sample;

import org.joda.time.LocalDate;
import org.joda.time.YearMonth;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.InstanceState;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.ViewById;

import fr.days.calendarview.CalendarView;
import fr.days.calendarview.LogWrapper;
import fr.days.calendarview.listeners.OnDayClickListener;
import fr.days.calendarview.listeners.OnDayLongClickListener;

@OptionsMenu(R.menu.main)
@EActivity(R.layout.activity_main)
public class MainActivity extends Activity implements OnDayClickListener, OnDayLongClickListener {

	@ViewById
	CalendarView calendarView;

	@ViewById
	TextView titleView;

	@InstanceState
	boolean showWeekend = false;

	@InstanceState
	YearMonth currentMonth;

	private MenuItem actionShowWeekends;

	@AfterInject
	void init() {
		currentMonth = YearMonth.now();
	}

	@AfterViews
	void initViews() {
		calendarView.setOnDayClickListener(this);
		calendarView.setOnDayLongClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setCurrentMonth(currentMonth);
		setShowWeekend(showWeekend);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		actionShowWeekends = menu.findItem(R.id.action_show_weekends);
		actionShowWeekends.setChecked(calendarView.isShowWeekend());
		return result;
	}

	@Override
	public void onClick(View v, LocalDate date) {
		LogWrapper.info("Click on date %s", date.toString());
	}

	@Override
	public boolean onLongClick(View v, LocalDate date) {
		LogWrapper.info("Long click on date %s", date.toString());
		return false;
	}

	@Click
	void previousButtonClicked() {
		setCurrentMonth(calendarView.getCurrentMonth().minusMonths(1));
	}

	@Click
	void nextButtonClicked() {
		setCurrentMonth(calendarView.getCurrentMonth().plusMonths(1));
	}

	@OptionsItem
	void actionShowWeekends() {
		boolean showWeekend = !calendarView.isShowWeekend();
		setShowWeekend(showWeekend);
	}

	private void setCurrentMonth(YearMonth currentMonth) {
		this.currentMonth = currentMonth;

		calendarView.setCurrentMonth(currentMonth);
		titleView.setText(currentMonth.monthOfYear().getAsText() + " " + currentMonth.getYear());
	}

	private void setShowWeekend(boolean showWeekend) {
		this.showWeekend = showWeekend;

		if (actionShowWeekends != null) {
			actionShowWeekends.setChecked(showWeekend);
		}
		calendarView.setShowWeekend(showWeekend);
	}

}
