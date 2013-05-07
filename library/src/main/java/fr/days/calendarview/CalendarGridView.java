package fr.days.calendarview;

import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.makeMeasureSpec;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import fr.days.calendarview.listeners.OnDayClickListener;
import fr.days.calendarview.listeners.OnDayLongClickListener;

public class CalendarGridView extends ViewGroup implements OnClickListener, OnLongClickListener {

	private CalendarAdapter adapter;
	private DataSetObserver dataSetObserver;

	private OnDayClickListener onDayClickListener;
	private OnDayLongClickListener onDayLongClickListener;

	public CalendarGridView(Context context) {
		super(context);
	}

	public CalendarGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CalendarGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
	}

	public void setAdapter(CalendarAdapter adapter) {
		if (this.adapter != null && dataSetObserver != null)
			this.adapter.unregisterDataSetObserver(dataSetObserver);

		if (adapter != null) {
			this.adapter = adapter;

			this.dataSetObserver = new AdapterDataSetObserver();
			this.adapter.registerDataSetObserver(dataSetObserver);

			requestLayout();
		}
	}

	public CalendarAdapter getAdapter() {
		return adapter;
	}

	class AdapterDataSetObserver extends DataSetObserver {
		@Override
		public void onChanged() {
			int count = adapter.getCount();
			LogWrapper.trace("CalendarGridView.onChanged : count = %d", count);

			for (int i = 0; i < count; i++) {
				View convertView = null;
				if (i < getChildCount()) {
					convertView = getChildAt(i);
				}

				View cellView = adapter.getView(i, convertView, CalendarGridView.this);
				if (convertView == null) {
					addView(cellView);
				}
			}

			if (count > getChildCount()) {
				LogWrapper.trace("CalendarGridView.onChanged : count (%d) > getChildCount (%d)", count, getChildCount());

				for (int i = count; i < getChildCount(); i++) {
					removeViewAt(i);
				}
			}

			requestLayout();
		}

		@Override
		public void onInvalidated() {
			super.onInvalidated();
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		LogWrapper.trace("CalendarGridView : onMeasure(" + MeasureSpec.toString(widthMeasureSpec) + ", " + MeasureSpec.toString(heightMeasureSpec) + ")");

		int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
		int totalHeight = MeasureSpec.getSize(heightMeasureSpec);
		final int cellWidthSpec = makeMeasureSpec(totalWidth / adapter.getFinalColumnCount(), EXACTLY);
		final int cellHeightSpec = makeMeasureSpec(totalHeight / adapter.getFinalRowCount(), EXACTLY);

		if (adapter != null) {
			int childCount = getChildCount();
			for (int i = 0; i < childCount; i++) {
				final CalendarCellView child = (CalendarCellView) getChildAt(i);
				// if (adapter.isShowWeekends() || !child.isWeekEnd()) {
				measureChild(child, cellWidthSpec, cellHeightSpec);
				// }
			}
		}
		setMeasuredDimension(totalWidth, totalHeight);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		LogWrapper.trace("CalendarGridView : onLayout(" + left + ", " + top + ", " + right + ", " + bottom + ")");

		int daysInWeek = adapter.getFinalColumnCount();
		int x = 0;
		int y = 0;
		for (int i = 0, numChildren = getChildCount(); i < numChildren; i++) {
			final CalendarCellView child = (CalendarCellView) getChildAt(i);
			// if (adapter.isShowWeekends() || !child.isWeekEnd()) {
			child.setVisibility(View.VISIBLE);
			child.layout(x * child.getMeasuredWidth(), y * child.getMeasuredHeight(), //
					(x + 1) * child.getMeasuredWidth(), (y + 1) * child.getMeasuredHeight());

			if (++x == daysInWeek) {
				x = 0;
				y++;
			}
			// } else {
			// child.setVisibility(View.GONE);
			// }
		}
	}

	@Override
	public void onClick(View v) {
		if (v instanceof CalendarCellView && onDayClickListener != null) {
			CalendarCellView cellView = (CalendarCellView) v;
			onDayClickListener.onClick(v, cellView.getDay());
		}
	}

	@Override
	public boolean onLongClick(View v) {
		if (v instanceof CalendarCellView && onDayLongClickListener != null) {
			CalendarCellView cellView = (CalendarCellView) v;
			return onDayLongClickListener.onLongClick(v, cellView.getDay());
		}
		return false;
	}

	public void setOnDayClickListener(OnDayClickListener l) {
		// If no listener was set before, we should set listener for each cells. Otherwise this is not necessary
		if (onDayClickListener == null) {
			for (int i = 0; i < getChildCount(); i++) {
				getChildAt(i).setOnClickListener(this);
			}
		}
		onDayClickListener = l;
	}

	public void setOnDayLongClickListener(OnDayLongClickListener l) {
		// If no listener was set before, we should set listener for each cells. Otherwise this is not necessary
		if (onDayLongClickListener == null) {
			for (int i = 0; i < getChildCount(); i++) {
				getChildAt(i).setOnLongClickListener(this);
			}
		}
		onDayLongClickListener = l;
	}

}
