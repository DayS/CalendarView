package fr.days.calendarview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.util.TypedValue;
import android.view.View;

public class CalendarHeaderView extends View {

	// TODO: Hard-coded for now. Try to fix it
	private static final String[] SHORT_WEEKDAYS = new String[] { "LUN.", "MAR.", "MER.", "JEU.", "VEN.", "SAM.", "DIM." };

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

	private boolean showWeekends;

	public CalendarHeaderView(Context context) {
		super(context);
		init();
	}

	private void init() {
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
		LogWrapper.debug("CalendarHeaderView : onMeasure(" + MeasureSpec.toString(widthMeasureSpec) + "," + MeasureSpec.toString(heightMeasureSpec) + ")");

		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), (int) (textSize + topMargin + bottomMargin));
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		LogWrapper.debug("CalendarHeaderView : onSizeChanged: :" + w + ", " + h);

		width = getWidth();
		height = getHeight();

		cellWidth = width / numberOfDaysToShow();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		LogWrapper.debug("CalendarHeaderView : onDraw()");

		drawBackground(canvas);
		drawText(canvas);
	}

	public void setShowWeekends(boolean showWeekends) {
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
			canvas.drawText(SHORT_WEEKDAYS[i], cellWidth * (i + 1) - rightMargin, height - bottomMargin, paint);
		}
	}

	private float convertDpiToPixels(int dpi) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpi, getResources().getDisplayMetrics());
	}

}
