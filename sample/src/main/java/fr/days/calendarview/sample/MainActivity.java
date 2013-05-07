package fr.days.calendarview.sample;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnDismissListener;
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

import fr.days.calendarview.CalendarAdapter;
import fr.days.calendarview.CalendarView;
import fr.days.calendarview.Day;
import fr.days.calendarview.LogWrapper;
import fr.days.calendarview.Month;
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
	Month currentMonth;

	private CalendarAdapter adapter;
	
	private MenuItem actionShowWeekends;

	private Day startDate;

	@AfterInject
	void init() {
		currentMonth = new Month();
		adapter = new CalendarAdapter(this);
	}

	@AfterViews
	void initViews() {
		calendarView.setOnDayClickListener(this);
		calendarView.setOnDayLongClickListener(this);
		calendarView.setAdapter(adapter);
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
		actionShowWeekends.setChecked(adapter.isShowWeekends());
		return result;
	}

	@Override
	public void onClick(View v, Day day) {
		LogWrapper.info("Click on date %s", day.toString());

		if (startDate != null) {
			if (currentMonth.contains(startDate) && currentMonth.contains(day)) {
				showSelectDialog(day);
			}
		}
	}

	@Override
	public boolean onLongClick(View v, Day day) {
		LogWrapper.info("Long click on date %s", day.toString());

		startDate = day;
		return true;
	}

	@Click
	void previousButtonClicked() {
		setCurrentMonth(adapter.getCurrentMonth().previousMonth());
	}

	@Click
	void nextButtonClicked() {
		setCurrentMonth(adapter.getCurrentMonth().nextMonth());
	}

	@OptionsItem
	void actionShowWeekends() {
		boolean showWeekend = !adapter.isShowWeekends();
		setShowWeekend(showWeekend);
	}

	private void setCurrentMonth(Month currentMonth) {
		this.currentMonth = currentMonth;

		adapter.setCurrentMonth(currentMonth);
		titleView.setText(currentMonth.getMonthName() + " " + currentMonth.getYear());
	}

	private void setShowWeekend(boolean showWeekend) {
		this.showWeekend = showWeekend;

		if (actionShowWeekends != null) {
			actionShowWeekends.setChecked(showWeekend);
		}
		adapter.setShowWeekends(showWeekend);
	}

	private void showSelectDialog(Day endDate) {
		if (startDate.isAfter(endDate)) {
			Day tmp = startDate;
			startDate = endDate;
			endDate = tmp;
		}
		final Day finalEndDate = endDate;

		AlertDialog selectDialog = new AlertDialog.Builder(this) //
				.setTitle(R.string.dialog_select_dates_title) //
				.setMessage(getString(R.string.dialog_select_dates_message, startDate.toString(), endDate.toString())) //
				.setPositiveButton(R.string.dialog_select_dates_yes, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						selectDates(startDate, finalEndDate, true);
						dialog.dismiss();
					}
				}) //
				.setNeutralButton(R.string.dialog_select_dates_cancel, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}) //
				.setNegativeButton(R.string.dialog_select_dates_no, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						selectDates(startDate, finalEndDate, false);
						dialog.cancel();
					}
				}) //
				.create();

		// Builder version was introduced in API 17
		selectDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				startDate = null;
			}
		});
		selectDialog.show();
	}

	private void selectDates(Day startDate, Day endDate, boolean selected) {
		adapter.setSelectedDate(startDate, endDate, selected);
	}

}
