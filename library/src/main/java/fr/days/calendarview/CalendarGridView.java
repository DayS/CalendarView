package fr.days.calendarview;

import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.makeMeasureSpec;

import org.joda.time.LocalDate;
import org.joda.time.YearMonth;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import fr.days.calendarview.listeners.OnDayClickListener;
import fr.days.calendarview.listeners.OnDayLongClickListener;

public class CalendarGridView extends ViewGroup implements OnClickListener, OnLongClickListener {

	private static final int DAYS_COUNT = 35;
	private static final int DAYS_COUNT_EXTRA = DAYS_COUNT + 7;

	private YearMonth currentMonth;
	private LocalDate firstDayOfView;
	private int firstDayOfMonthWeekIndex;
	private boolean showExtraRow;
	private boolean showWeekends;

	private OnDayClickListener onDayClickListener;
	private OnDayLongClickListener onDayLongClickListener;

	public CalendarGridView(Context context) {
		super(context);
		init();
	}

	public CalendarGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CalendarGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		init();
	}

	private void init() {
		LogWrapper.trace("init()");

		for (int dayIndex = 0; dayIndex < DAYS_COUNT_EXTRA; dayIndex++) {
			addView(new CalendarCellView(getContext()));
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		LogWrapper.trace("CalendarGridView : onMeasure(" + MeasureSpec.toString(widthMeasureSpec) + ", " + MeasureSpec.toString(heightMeasureSpec) + ")");

		int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
		int totalHeight = MeasureSpec.getSize(heightMeasureSpec);
		final int cellWidthSpec = makeMeasureSpec(totalWidth / getFinalColumnCount(), EXACTLY);
		final int cellHeightSpec = makeMeasureSpec(totalHeight / getFinalRowCount(), EXACTLY);

		for (int i = 0, numChildren = getChildCount(); i < numChildren; i++) {
			final CalendarCellView child = (CalendarCellView) getChildAt(i);
			if (showWeekends || !child.isWeekEnd()) {
				measureChild(child, cellWidthSpec, cellHeightSpec);
			}
		}
		setMeasuredDimension(totalWidth, totalHeight);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		LogWrapper.trace("CalendarGridView : onLayout(" + left + ", " + top + ", " + right + ", " + bottom + ")");

		int daysInWeek = getFinalColumnCount();
		int x = 0;
		int y = 0;
		for (int i = 0, numChildren = getChildCount(); i < numChildren; i++) {
			final CalendarCellView child = (CalendarCellView) getChildAt(i);
			if (showWeekends || !child.isWeekEnd()) {
				child.setVisibility(View.VISIBLE);
				child.layout(x * child.getMeasuredWidth(), y * child.getMeasuredHeight(), //
						(x + 1) * child.getMeasuredWidth(), (y + 1) * child.getMeasuredHeight());

				if (++x == daysInWeek) {
					x = 0;
					y++;
				}
			} else {
				child.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v instanceof CalendarCellView && onDayClickListener != null) {
			CalendarCellView cellView = (CalendarCellView) v;
			onDayClickListener.onClick(v, cellView.getDate());
		}
	}

	@Override
	public boolean onLongClick(View v) {
		if (v instanceof CalendarCellView && onDayLongClickListener != null) {
			CalendarCellView cellView = (CalendarCellView) v;
			return onDayLongClickListener.onLongClick(v, cellView.getDate());
		}
		return false;
	}

	public void setOnDayClickListener(OnDayClickListener l) {
		// If no listener was set before, we should set listener for each cells. Otherwise this is not necessary
		if (onDayClickListener == null) {
			for (int i = 0; i < DAYS_COUNT_EXTRA; i++) {
				getChildAt(i).setOnClickListener(this);
			}
		}
		onDayClickListener = l;
	}

	public void setOnDayLongClickListener(OnDayLongClickListener l) {
		// If no listener was set before, we should set listener for each cells. Otherwise this is not necessary
		if (onDayLongClickListener == null) {
			for (int i = 0; i < DAYS_COUNT_EXTRA; i++) {
				getChildAt(i).setOnLongClickListener(this);
			}
		}
		onDayLongClickListener = l;
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
		if (currentMonth == null)
			throw new IllegalArgumentException("currentMonth musn't be null");
		if (currentMonth.equals(this.currentMonth))
			return;

		this.currentMonth = currentMonth;

		LogWrapper.debug("set current month to %s", currentMonth.toString());

		LocalDate firstDayOfMonth = currentMonth.toLocalDate(1);
		firstDayOfMonthWeekIndex = firstDayOfMonth.getDayOfWeek();
		firstDayOfView = firstDayOfMonth.minusDays(firstDayOfMonthWeekIndex - 1);

		showExtraRow = firstDayOfMonth.dayOfMonth().getMaximumValue() + firstDayOfMonthWeekIndex - 1 > 35;

		requestLayout();
		configureCells();
	}

	public boolean isShowWeekends() {
		return showWeekends;
	}

	public void setShowWeekends(boolean showWeekends) {
		if (this.showWeekends == showWeekends)
			return;

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

}
