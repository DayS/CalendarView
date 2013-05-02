package fr.days.calendarview;

import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import fr.days.calendarview.listeners.OnDayClickListener;
import fr.days.calendarview.listeners.OnDayLongClickListener;

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

	public void setOnDayClickListener(OnDayClickListener l) {
		gridLayout.setOnDayClickListener(l);
	}

	public void setOnDayLongClickListener(OnDayLongClickListener l) {
		gridLayout.setOnDayLongClickListener(l);
	}

	public Month getCurrentMonth() {
		return gridLayout.getCurrentMonth();
	}

	public void setCurrentMonth(Month currentMonth) {
		gridLayout.setCurrentMonth(currentMonth);
	}

	public boolean isShowWeekend() {
		return gridLayout.isShowWeekends();
	}

	public void setShowWeekend(boolean showWeekends) {
		gridLayout.setShowWeekends(showWeekends);
		headerView.setShowWeekends(showWeekends);
	}

	public void setSelectedDate(Date startDate, Date endDate, boolean selected) {
		gridLayout.setSelectedDate(startDate, endDate, selected);
	}

}
