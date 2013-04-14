package fr.days.calendarview;

import org.joda.time.LocalDate;
import org.joda.time.YearMonth;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public class CalendarView extends LinearLayout {

	private CalendarHeaderView headerView;
	private CalendarGridView gridLayout;

	public CalendarView(Context context) {
		super(context);
		init();
	}

	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CalendarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		init();
	}

	void init() {
		setOrientation(VERTICAL);

		headerView = new CalendarHeaderView(getContext());
		gridLayout = new CalendarGridView(getContext());

		addView(headerView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		addView(gridLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		gridLayout.setOnClickListener(l);
	}

	@Override
	public void setOnLongClickListener(OnLongClickListener l) {
		gridLayout.setOnLongClickListener(l);
	}

	public YearMonth getCurrentMonth() {
		return gridLayout.getCurrentMonth();
	}

	public void setCurrentMonth(YearMonth currentMonth) {
		gridLayout.setCurrentMonth(currentMonth);
	}

	public void setShowWeekend(boolean showWeekends) {
		gridLayout.setShowWeekends(showWeekends);
		headerView.setShowWeekends(showWeekends);
	}

	public void setSelectedDate(LocalDate startDate, LocalDate endDate, boolean selected) {
		gridLayout.setSelectedDate(startDate, endDate, selected);
	}

}
