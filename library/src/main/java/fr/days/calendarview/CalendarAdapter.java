package fr.days.calendarview;

import java.util.Calendar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CalendarAdapter extends BaseAdapter {

	private static final int DAYS_COUNT = 35;
	private static final int DAYS_COUNT_EXTRA = DAYS_COUNT + 7;

	private Context context;

	private int firstDayOfWeekToShow = Calendar.MONDAY;
	private boolean[] selectedCells = new boolean[DAYS_COUNT_EXTRA];

	private Month currentMonth;
	private Day firstDayOfView;
	private int firstWeekDayOfFirstWeek;
	private boolean showExtraRow;
	private boolean showWeekends;

	public CalendarAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return getFinalColumnCount() * getFinalRowCount();
	}

	@Override
	public Day getItem(int position) {
		if (isShowWeekends()) {
			return firstDayOfView.plusDays(position);
		} else {
			return firstDayOfView.plusDays(position, Day.FILTER_WEEKENDS);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CalendarCellView calendarCellView;
		if (convertView == null) {
			calendarCellView = new CalendarCellView(context);
		} else {
			calendarCellView = (CalendarCellView) convertView;
		}

		calendarCellView.setDate(currentMonth, getItem(position));
		calendarCellView.setSelected(selectedCells[position]);
		calendarCellView.invalidate();
		return calendarCellView;
	}

	public Month getCurrentMonth() {
		return currentMonth;
	}

	public void setCurrentMonth(Month currentMonth) {
		if (currentMonth == null)
			throw new IllegalArgumentException("currentMonth musn't be null");
		if (currentMonth.equals(this.currentMonth))
			return;

		this.currentMonth = currentMonth;

		LogWrapper.debug("set current month to %s", currentMonth.toString());

		firstWeekDayOfFirstWeek = currentMonth.getFirstWeekDayOfFirstWeek();
		firstDayOfView = currentMonth.getFirstDayOfMonth();
		firstDayOfView = firstDayOfView.minusDays(getDayOfWeekColumn(firstWeekDayOfFirstWeek));

		showExtraRow = currentMonth.getNumberOfDays() + getDayOfWeekColumn(firstWeekDayOfFirstWeek) > 35;

		notifyDataSetChanged();
	}

	public void setSelectedDate(Day startDate, Day endDate, boolean selected) {
		if (!currentMonth.contains(startDate))
			throw new IllegalArgumentException();
		if (!currentMonth.contains(endDate))
			throw new IllegalArgumentException();

		int startDay = startDate.getDayOfMonth();
		int endDay = endDate.getDayOfMonth();

		for (int i = startDay; i <= endDay; i++) {
			selectedCells[i] = selected;
		}

		notifyDataSetChanged();
	}

	public void setFirstDayOfWeekToShow(int firstDayOfWeekToShow) {
		if (this.firstDayOfWeekToShow == firstDayOfWeekToShow)
			return;

		this.firstDayOfWeekToShow = firstDayOfWeekToShow;
		notifyDataSetChanged();
	}

	public void setShowWeekends(boolean showWeekends) {
		if (this.showWeekends == showWeekends)
			return;

		this.showWeekends = showWeekends;
		notifyDataSetChanged();
	}

	public boolean isShowWeekends() {
		return showWeekends;
	}

	public int getFinalColumnCount() {
		return showWeekends ? 7 : 5;
	}

	public int getFinalRowCount() {
		return showExtraRow ? 6 : 5;
	}

	private int getDayOfWeekColumn(int dayOfWeek) {
		int result = dayOfWeek - firstDayOfWeekToShow;
		if (result < 0)
			result += 7;
		return result;
	}

}
