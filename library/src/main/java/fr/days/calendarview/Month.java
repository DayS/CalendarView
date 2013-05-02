package fr.days.calendarview;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Month implements Serializable {

	private static final long serialVersionUID = -7678210275754915223L;

	private final Calendar date;
	private SimpleDateFormat monthDateFormat;

	public Month() {
		this(-1);
	}

	public Month(int month) {
		this(-1, month);
	}

	public Month(int year, int month) {
		date = Calendar.getInstance();
		date.set(Calendar.DAY_OF_MONTH, 1);
		if (year > -1)
			date.set(Calendar.YEAR, year);
		if (month > -1)
			date.set(Calendar.MONTH, month - 1);
	}

	@Override
	public String toString() {
		return SimpleDateFormat.getInstance().format(date.getTime());
	}

	public int getYear() {
		return date.get(Calendar.YEAR);
	}

	public int getMonth() {
		return date.get(Calendar.MONTH) + 1;
	}

	public int getFirstDayOfFirstWeek() {
		return date.get(Calendar.DAY_OF_WEEK);
	}

	public int getNumberOfDays() {
		return date.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	public Calendar getFirstDayOfMonth() {
		Calendar firstDate = (Calendar) date.clone();
		firstDate.set(Calendar.DAY_OF_MONTH, 1);
		return firstDate;
	}

	public boolean contains(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return contains(cal);
	}

	public boolean contains(Calendar date) {
		return date.get(Calendar.YEAR) == this.date.get(Calendar.YEAR) && date.get(Calendar.MONTH) == this.date.get(Calendar.MONTH);
	}

	public String getMonthName() {
		if (monthDateFormat == null) {
			monthDateFormat = new SimpleDateFormat("MMM", Locale.getDefault());
		}
		return monthDateFormat.format(date.getTime());
	}

	public Month previousMonth() {
		int month = getMonth();
		if (month == 1) {
			return new Month(getYear() - 1, 12);
		} else {
			return new Month(getYear(), getMonth() - 1);
		}
	}

	public Month nextMonth() {
		int month = getMonth();
		if (month == 12) {
			return new Month(getYear() + 1, 1);
		} else {
			return new Month(getYear(), getMonth() + 1);
		}
	}

}
