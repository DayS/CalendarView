package fr.days.calendarview.sample;

import org.joda.time.LocalDate;
import org.joda.time.YearMonth;

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

	private LocalDate startDate;

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

		if (startDate != null) {
			if (startDate.getMonthOfYear() == currentMonth.getMonthOfYear() && date.getMonthOfYear() == currentMonth.getMonthOfYear()) {
				showSelectDialog(date);
			}
		}
	}

	@Override
	public boolean onLongClick(View v, LocalDate date) {
		LogWrapper.info("Long click on date %s", date.toString());

		startDate = date;
		return true;
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

	private void showSelectDialog(LocalDate endDate) {
		if (startDate.isAfter(endDate)) {
			LocalDate tmp = startDate;
			startDate = endDate;
			endDate = tmp;
		}
		final LocalDate finalEndDate = endDate;

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

	private void selectDates(LocalDate startDate, LocalDate endDate, boolean selected) {
		calendarView.setSelectedDate(startDate, endDate, selected);
	}

}
