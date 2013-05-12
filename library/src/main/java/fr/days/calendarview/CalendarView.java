package fr.days.calendarview;

import static android.view.View.MeasureSpec.EXACTLY;
import static android.view.View.MeasureSpec.makeMeasureSpec;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import fr.days.calendarview.listeners.OnDayClickListener;
import fr.days.calendarview.listeners.OnDayLongClickListener;

public class CalendarView extends LinearLayout implements OnClickListener, OnLongClickListener {

	private CalendarHeaderView headerView;
	private CalendarGridView gridLayout;

	private CalendarAdapter adapter;
	private DataSetObserver dataSetObserver;

	private OnDayClickListener onDayClickListener;
	private OnDayLongClickListener onDayLongClickListener;

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

	private void init() {
		setOrientation(VERTICAL);

		headerView = new CalendarHeaderView(getContext());
		gridLayout = new CalendarGridView(getContext());

		addView(headerView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		addView(gridLayout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
	}

	public void setAdapter(CalendarAdapter calendarAdapter) {
		if (adapter != null && dataSetObserver != null)
			adapter.unregisterDataSetObserver(dataSetObserver);

		if (calendarAdapter != null) {
			adapter = calendarAdapter;

			dataSetObserver = new AdapterDataSetObserver();
			adapter.registerDataSetObserver(dataSetObserver);

			adapter.notifyDataSetChanged();
		}
	}

	public CalendarAdapter getAdapter() {
		return adapter;
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

	/**
	 * Represent and manage the "body" of the calendar with all days
	 * 
	 * @author dvilleneuve
	 * 
	 */
	class CalendarGridView extends ViewGroup {

		public CalendarGridView(Context context) {
			super(context);
		}

		public CalendarGridView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		public CalendarGridView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs);
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			LogWrapper.trace("CalendarGridView : onMeasure(" + MeasureSpec.toString(widthMeasureSpec) + ", " + MeasureSpec.toString(heightMeasureSpec) + ")");

			int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
			int totalHeight = MeasureSpec.getSize(heightMeasureSpec);

			if (adapter != null) {
				final int cellWidthSpec = makeMeasureSpec(totalWidth / adapter.getFinalColumnCount(), EXACTLY);
				final int cellHeightSpec = makeMeasureSpec(totalHeight / adapter.getFinalRowCount(), EXACTLY);
				final int childCount = getChildCount();

				for (int i = 0; i < childCount; i++) {
					measureChild(getChildAt(i), cellWidthSpec, cellHeightSpec);
				}
			}
			setMeasuredDimension(totalWidth, totalHeight);
		}

		@Override
		protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
			LogWrapper.trace("CalendarGridView : onLayout(" + left + ", " + top + ", " + right + ", " + bottom + ")");

			if (adapter != null) {
				final int daysInWeek = adapter.getFinalColumnCount();
				int x = 0;
				int y = 0;
				for (int i = 0, numChildren = getChildCount(); i < numChildren; i++) {
					final View child = getChildAt(i);
					child.setVisibility(View.VISIBLE);
					child.layout(x * child.getMeasuredWidth(), y * child.getMeasuredHeight(), //
							(x + 1) * child.getMeasuredWidth(), (y + 1) * child.getMeasuredHeight());

					if (++x == daysInWeek) {
						x = 0;
						y++;
					}
				}
			}
		}

	}

	/**
	 * Represent and manage the header with weedays names
	 * 
	 * @author dvilleneuve
	 * 
	 */
	class CalendarHeaderView extends View {

		private final Paint paint = new Paint();

		private int backgroundColor = Color.WHITE;

		private int textColor = Color.rgb(150, 150, 150);
		private float textSize;

		private int width;
		private int height;
		private int cellWidth;
		private float topMargin;
		private float bottomMargin;
		private float rightMargin;

		public CalendarHeaderView(Context context) {
			super(context);

			textSize = convertDpiToPixels(14);
			topMargin = convertDpiToPixels(3);
			bottomMargin = convertDpiToPixels(3);
			rightMargin = convertDpiToPixels(3);
		}

		public void setTextSize(int textSize) {
			this.textSize = convertDpiToPixels(textSize);
			invalidate();
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			LogWrapper.trace("CalendarHeaderView : onMeasure(%s, %s)", MeasureSpec.toString(widthMeasureSpec), MeasureSpec.toString(heightMeasureSpec));

			setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), (int) (textSize + topMargin + bottomMargin));
		}

		@Override
		public void layout(int l, int t, int r, int b) {
			super.layout(l, t, r, b);
			LogWrapper.trace("CalendarHeaderView : layout(%d, %d, %d, %d)", l, t, r, b);

			if (adapter != null) {
				width = getWidth();
				height = getHeight();

				cellWidth = width / adapter.getFinalColumnCount();
			}
		}

		@Override
		protected void onDraw(Canvas canvas) {
			drawBackground(canvas);
			drawText(canvas);
		}

		private void drawBackground(Canvas canvas) {
			paint.setStyle(Style.FILL);
			paint.setColor(backgroundColor);
			canvas.drawRect(0, 0, width, height, paint);
		}

		private void drawText(Canvas canvas) {
			if (adapter != null) {
				paint.setColor(textColor);
				paint.setTextAlign(Align.RIGHT);
				paint.setTextSize(textSize);

				for (int i = 0; i < adapter.getFinalColumnCount(); i++) {
					canvas.drawText(adapter.getShortWeekdays(i), cellWidth * (i + 1) - rightMargin, height - bottomMargin, paint);
				}
			}
		}

		private float convertDpiToPixels(int dpi) {
			return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpi, getResources().getDisplayMetrics());
		}

	}

	/**
	 * The {@link DataSetObserver} responsible to create / remove childrend of {@link CalendarGridView}.
	 * 
	 * @author dvilleneuve
	 * 
	 */
	class AdapterDataSetObserver extends DataSetObserver {
		@Override
		public void onChanged() {
			int count = adapter.getCount();
			int currentCount = gridLayout.getChildCount();
			LogWrapper.trace("CalendarGridView.onChanged : count = %d", count);

			for (int i = 0; i < count; i++) {
				View convertView = null;
				if (i < currentCount) {
					convertView = gridLayout.getChildAt(i);
				}

				View cellView = adapter.getView(i, convertView, gridLayout);
				// If the cellView wasn't a recycled one, add it to the gridLayout. Elsewhere just invalide it's content
				if (convertView == null) {
					gridLayout.addView(cellView);
				} else {
					cellView.invalidate();
				}
			}

			if (count > currentCount) {
				LogWrapper.trace("CalendarGridView.onChanged : count (%d) > getChildCount (%d)", count, currentCount);

				for (int i = count; i < getChildCount(); i++) {
					gridLayout.removeViewAt(i);
				}
			}

			gridLayout.requestLayout();
			headerView.requestLayout();
		}
	}

}
