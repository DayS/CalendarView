package fr.dvilleneuve.calendarview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class CalendarCellView extends View implements OnTouchListener {

	private static final Day CURRENT_DAY = new Day();

	private final Paint paint = new Paint();

	private static int backgroundColorCurrentDay = Color.rgb(215, 235, 245);
	private static int backgroundColorNormalDay = Color.WHITE;
	private static int backgroundColorWeekEndDay = Color.rgb(230, 230, 230);
	private static int backgroundColorSelectedDay = Color.rgb(255, 255, 153);
	private static int backgroundColorHighlightedDay = Color.rgb(145, 210, 234);

	private static int overlayColorOutOfMonthDay = Color.argb(30, 127, 127, 127);

	private static int borderColor = Color.rgb(194, 194, 194);
	private static int borderHighlightedColor = backgroundColorHighlightedDay;

	private static int textColorNormaDay = Color.rgb(100, 100, 100);
	private static int textColorOutOfMonth = Color.rgb(155, 155, 155);
	private static float textSize;
	private static float textMargin;

	private int width;
	private int height;

	private Month displayedMonth;
	private Day date;
	private boolean isWeekend;
	private boolean selected = false;
	private boolean highlighted = false;

	public CalendarCellView(Context context) {
		super(context);
		init();
	}

	public CalendarCellView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CalendarCellView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		init();
	}

	private void init() {
		if (textSize == 0)
			textSize = convertDpiToPixels(15);
		if (textMargin == 0)
			textMargin = convertDpiToPixels(5);

		setOnTouchListener(this);
	}

	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
		invalidate();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			LogWrapper.trace("Action down");
			highlighted = true;
			invalidate();
			return !(isClickable() || isLongClickable());
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			LogWrapper.trace("Action up");
			highlighted = false;
			invalidate();
			return !(isClickable() || isLongClickable());
		}
		LogWrapper.trace("action %d", event.getAction());
		return false;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		width = getWidth();
		height = getHeight();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawBackground(canvas);
		drawCellHeader(canvas);
		drawOutOfMonthOverlay(canvas);
		drawCellBorder(canvas);
	}

	public Day getDay() {
		return date;
	}

	public void setDate(Month currentMonth, Day day) {
		this.displayedMonth = currentMonth;
		this.date = day;
		this.isWeekend = day.isWeekend();
	}

	public boolean isWeekEnd() {
		return isWeekend;
	}

	private void drawBackground(Canvas canvas) {
		paint.setStyle(Style.FILL);
		if (highlighted) {
			paint.setColor(backgroundColorHighlightedDay);
		} else if (date.equals(CURRENT_DAY)) {
			paint.setColor(backgroundColorCurrentDay);
		} else if (selected) {
			paint.setColor(backgroundColorSelectedDay);
		} else if (isWeekend) {
			paint.setColor(backgroundColorWeekEndDay);
		} else {
			paint.setColor(backgroundColorNormalDay);
		}
		canvas.drawRect(0, 0, width, height, paint);
	}

	private void drawCellHeader(Canvas canvas) {
		paint.setStyle(Style.FILL);

		// Day number
		if (isOutOfMonth()) {
			paint.setColor(textColorOutOfMonth);
		} else {
			paint.setColor(textColorNormaDay);
		}
		paint.setTextAlign(Align.RIGHT);
		paint.setTextSize(textSize);
		canvas.drawText(String.valueOf(date.getDayOfMonth()), width - textMargin, textSize + textMargin, paint);
	}

	private void drawOutOfMonthOverlay(Canvas canvas) {
		if (isOutOfMonth()) {
			paint.setColor(overlayColorOutOfMonthDay);
			canvas.drawRect(0, 0, width, height, paint);
		}
	}

	private void drawCellBorder(Canvas canvas) {
		paint.setStyle(Style.STROKE);
		if (highlighted) {
			paint.setColor(borderHighlightedColor);
		} else {
			paint.setColor(borderColor);
		}
		canvas.drawRect(0, 0, width, height, paint);
	}

	private boolean isOutOfMonth() {
		return !displayedMonth.contains(date);
	}

	private float convertDpiToPixels(int dpi) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpi, getResources().getDisplayMetrics());
	}

}
