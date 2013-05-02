package fr.days.calendarview;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

public class CalendarHeaderView extends View {

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

	private int firstDayOfWeekToShow = Calendar.MONDAY;
	private String[] shortWeekdays = new String[7];
	private boolean showWeekends;

	public CalendarHeaderView(Context context) {
		super(context);
		init();
	}

	public CalendarHeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CalendarHeaderView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		init();
	}

	private void init() {
		SimpleDateFormat weekDateFormat = new SimpleDateFormat("EEE");

		Calendar date = Calendar.getInstance();
		for (int i = 0; i < 7; i++) {
			date.set(Calendar.DAY_OF_WEEK, i);
			shortWeekdays[getDayOfWeekColumn(i)] = weekDateFormat.format(date.getTime());
		}

		textSize = convertDpiToPixels(14);
		topMargin = convertDpiToPixels(3);
		bottomMargin = convertDpiToPixels(3);
		rightMargin = convertDpiToPixels(3);
	}

	// TODO: Refactor with CalendarGridView.getDayOfWeekColumn
	protected int getDayOfWeekColumn(int dayOfWeek) {
		int result = dayOfWeek - firstDayOfWeekToShow;
		if (result < 0)
			result += 7;
		return result;
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

		width = getWidth();
		height = getHeight();

		cellWidth = width / numberOfDaysToShow();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawBackground(canvas);
		drawText(canvas);
	}

	public void setShowWeekends(boolean showWeekends) {
		if (this.showWeekends == showWeekends)
			return;

		this.showWeekends = showWeekends;
		requestLayout();
	}

	private int numberOfDaysToShow() {
		return showWeekends ? 7 : 5;
	}

	private void drawBackground(Canvas canvas) {
		paint.setStyle(Style.FILL);
		paint.setColor(backgroundColor);
		canvas.drawRect(0, 0, width, height, paint);
	}

	private void drawText(Canvas canvas) {
		paint.setColor(textColor);
		paint.setTextAlign(Align.RIGHT);
		paint.setTextSize(textSize);

		for (int i = 0; i < numberOfDaysToShow(); i++) {
			canvas.drawText(shortWeekdays[i], cellWidth * (i + 1) - rightMargin, height - bottomMargin, paint);
		}
	}

	private float convertDpiToPixels(int dpi) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpi, getResources().getDisplayMetrics());
	}

}
