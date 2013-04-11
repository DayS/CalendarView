package fr.days.calendarview;

import org.joda.time.LocalDate;
import org.joda.time.YearMonth;

import android.content.Context;
import android.view.View;
import android.widget.GridLayout;

public class CalendarGridView extends GridLayout {

	private static final int DAYS_COUNT = 35;
	private static final int DAYS_COUNT_EXTRA = DAYS_COUNT + 7;

	private YearMonth currentMonth;
	private LocalDate firstDayOfView;
	private int firstDayOfMonthWeekIndex;
	private boolean showExtraRow;
	private boolean showWeekends;

	public CalendarGridView(Context context) {
		super(context);
		init();
	}

	private void init() {
		setColumnCount(7);
		setRowCount(6);
		createCellViews();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		LogWrapper.debug("CalendarView : onSizeChanged: :" + w + ", " + h);

		int cellWidth = (int) (getWidth() / getFinalColumnCount());
		int cellHeight = (int) (getHeight() / getFinalRowCount());

		for (int i = 0; i < DAYS_COUNT_EXTRA; i++) {
			CalendarCellView calendarCellView = (CalendarCellView) getChildAt(i);
			android.view.ViewGroup.LayoutParams layoutParams = calendarCellView.getLayoutParams();
			layoutParams.width = cellWidth;
			layoutParams.height = cellHeight;

			if (!showExtraRow && i >= DAYS_COUNT) {
				calendarCellView.setVisibility(GONE);
			} else if (!showWeekends && i % 7 >= 5) {
				calendarCellView.setVisibility(GONE);
			} else {
				calendarCellView.setVisibility(VISIBLE);
			}
		}
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		for (int i = 0; i < DAYS_COUNT_EXTRA; i++) {
			getChildAt(i).setOnClickListener(l);
		}
	}

	@Override
	public void setOnLongClickListener(OnLongClickListener l) {
		for (int i = 0; i < DAYS_COUNT_EXTRA; i++) {
			getChildAt(i).setOnLongClickListener(l);
		}
	}

	public int getFinalColumnCount() {
		return showWeekends ? 7 : 5;
	}

	public int getFinalRowCount() {
		return showExtraRow ? 6 : 5;
	}
	
	public YearMonth getCurrentMonth() {
		return currentMonth;
	}

	public void setCurrentMonth(YearMonth currentMonth) {
		this.currentMonth = currentMonth;

		LogWrapper.debug("set current month to " + currentMonth.toString());

		LocalDate firstDayOfMonth = currentMonth.toLocalDate(1);
		firstDayOfMonthWeekIndex = firstDayOfMonth.getDayOfWeek();
		firstDayOfView = firstDayOfMonth.minusDays(firstDayOfMonthWeekIndex - 1);

		showExtraRow = firstDayOfMonth.dayOfMonth().getMaximumValue() + firstDayOfMonthWeekIndex - 1 > 35;

		requestLayout();
		configureCells();
	}

	public void setShowWeekends(boolean showWeekends) {
		this.showWeekends = showWeekends;
		requestLayout();
	}

	public void setSelectedDate(LocalDate startDate, LocalDate endDate, boolean selected) {
		if (startDate.getMonthOfYear() != currentMonth.getMonthOfYear())
			throw new IllegalArgumentException();
		if (endDate.getMonthOfYear() != currentMonth.getMonthOfYear())
			throw new IllegalArgumentException();

		int startDay = startDate.getDayOfMonth();
		int endDay = endDate.getDayOfMonth();

		for (int i = startDay; i <= endDay; i++) {
			View cellView = getChildAt(i + firstDayOfMonthWeekIndex - 2);
			cellView.setSelected(selected);
		}
	}

	private void configureCells() {
		LogWrapper.debug("CalendarView : resetWeeks");

		for (int i = 0; i < getChildCount(); i++) {
			CalendarCellView calendarCellView = (CalendarCellView) getChildAt(i);
			calendarCellView.setDate(currentMonth, firstDayOfView.plusDays(i));
			calendarCellView.invalidate();
		}
	}

	private void createCellViews() {
		if (getChildCount() > 0)
			return;

		int y = 0;
		int x = 0;
		for (int dayIndex = 0; dayIndex < DAYS_COUNT_EXTRA; dayIndex++) {
			LayoutParams layoutParams = new LayoutParams(GridLayout.spec(y), GridLayout.spec(x));
			CalendarCellView calendarCellView = new CalendarCellView(getContext());

			addView(calendarCellView, layoutParams);

			if (++x == getColumnCount()) {
				y++;
				x = 0;
			}
		}
	}
}
