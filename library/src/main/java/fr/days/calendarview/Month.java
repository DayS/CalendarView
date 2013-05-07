package fr.days.calendarview;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * This class represent a month for a given year. It offers some methods to easily retrieve the first day of the month and wich day of week is this day.
 */
public class Month implements Serializable {

	private static final long serialVersionUID = -7678210275754915223L;

	private final Calendar date;
	private SimpleDateFormat monthDateFormat;

	/**
	 * Create a new {@link Month} for the current month and year
	 */
	public Month() {
		this(-1);
	}

	/**
	 * Create a new {@link Month} for the the given month and current year
	 * 
	 * @param month
	 */
	public Month(int month) {
		this(-1, month);
	}

	/**
	 * Create a new {@link Month} for the given month and year
	 * 
	 * @param year
	 * @param month
	 */
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

	/**
	 * Return the actual number of days in this month. This handle odd year.
	 */
	public int getNumberOfDays() {
		return date.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Return the week day of the first day of first week in this month.
	 * 
	 * @return return a value between {@link Calendar#MONDAY} and {@link Calendar#SUNDAY}
	 */
	public int getFirstWeekDayOfFirstWeek() {
		return date.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * Return the first day of this month.
	 */
	public Day getFirstDayOfMonth() {
		Calendar firstDate = (Calendar) date.clone();
		firstDate.set(Calendar.DAY_OF_MONTH, 1);
		return new Day(firstDate);
	}

	/**
	 * Check if the given date is on this month (ie: day is in the range of this month and the month is equals to this one)
	 * 
	 * @param day
	 * @return <code>true</code> if the date is in this month, <code>false</code> elsewhere
	 * @see Month#contains(Calendar)
	 */
	public boolean contains(Day day) {
		return date.get(Calendar.YEAR) == day.getYear() && date.get(Calendar.MONTH) == day.getMonth();
	}

	/**
	 * Return the current month's full name for the current {@link Locale}.
	 */
	public String getMonthName() {
		if (monthDateFormat == null) {
			monthDateFormat = new SimpleDateFormat("MMM", Locale.getDefault());
		}
		return monthDateFormat.format(date.getTime());
	}

	/**
	 * Return a new instance of {@link Month} which is the previous month. If current month is {@link Calendar#JANUARY}, the new instance will be {@link Calendar#DECEMBER}
	 */
	public Month previousMonth() {
		int month = getMonth();
		if (month == 1) {
			return new Month(getYear() - 1, 12);
		} else {
			return new Month(getYear(), getMonth() - 1);
		}
	}

	/**
	 * Return a new instance of {@link Month} which is the next month. If current month is {@link Calendar#DECEMBER}, the new instance will be {@link Calendar#JANUARY}
	 */
	public Month nextMonth() {
		int month = getMonth();
		if (month == 12) {
			return new Month(getYear() + 1, 1);
		} else {
			return new Month(getYear(), getMonth() + 1);
		}
	}

}
