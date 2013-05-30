package fr.days.calendarview;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;


public class Day implements Serializable {

	private static final long serialVersionUID = -3165059533607342767L;

	private static final DateFormat DATE_FORMAT = DateFormat.getDateInstance();

	public static final int[] FILTER_WEEKENDS = new int[] { Calendar.SUNDAY, Calendar.SATURDAY };

	protected final Calendar date;

	/**
	 * Create a new {@link Day} for today
	 */
	public Day() {
		this(Calendar.getInstance());
	}

	/**
	 * Create a new copy of given {@link Day}
	 */
	public Day(Day day) {
		this(day.date);
	}

	/**
	 * Create a {@link Day} for the given {@link Calendar}
	 */
	public Day(Calendar date) {
		this.date = date;
	}

	/**
	 * Create a nw {@link Day} for the given {@link Month} and day of this month
	 * 
	 * @param month
	 * @param day
	 */
	public Day(Month month, int day) {
		Calendar date = Calendar.getInstance();
		date.set(Calendar.YEAR, month.getYear());
		date.set(Calendar.MONTH, month.getMonth());
		date.set(Calendar.DAY_OF_MONTH, day);
		this.date = date;
	}

	@Override
	public String toString() {
		return DATE_FORMAT.format(date.getTime());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Day other = (Day) obj;

		if (getYear() != other.getYear())
			return false;
		if (getMonth() != other.getMonth())
			return false;
		if (getDayOfMonth() != other.getDayOfMonth())
			return false;

		return true;
	}

	/**
	 * Return a copy of this instance plus the given number of days
	 * 
	 * @param numberOfDays
	 */
	public Day plusDays(int numberOfDays) {
		Calendar newDate = (Calendar) date.clone();
		newDate.add(Calendar.DAY_OF_MONTH, numberOfDays);
		return new Day(newDate);
	}

	/**
	 * Return a copy of this instance plus the given number of days.
	 * <p/>
	 * During increment this method will also skip days according to the given list of weekdays to skip. So if we ask to add 2 days from a {@link Calendar#FRIDAY} but skipping the week-end, the result will be {@link Calendar#TUESDAY}.
	 * 
	 * @param numberOfDays
	 */
	public Day plusDays(int numberOfDays, int[] weekDaysToSkip) {
		boolean[] filteringArray = weekDaysFilteringArray(weekDaysToSkip);

		Calendar newDate = (Calendar) date.clone();
		int numberOfDaysLeft = numberOfDays;
		while (numberOfDaysLeft > 0) {
			newDate.add(Calendar.DAY_OF_MONTH, 1);

			if (!filteringArray[newDate.get(Calendar.DAY_OF_WEEK)]) {
				numberOfDaysLeft--;
			}
		}
		return new Day(newDate);
	}

	private boolean[] weekDaysFilteringArray(int[] weekDaysToSkip) {
		boolean[] filter = new boolean[8];
		for (int weekDayToSkip : weekDaysToSkip) {
			filter[weekDayToSkip] = true;
		}
		return filter;
	}

	public Day minusDays(int numberOfDays) {
		return plusDays(-numberOfDays);
	}

	public boolean isWeekend() {
		int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
		return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
	}

	public boolean isBefore(Day day) {
		return date.before(day.date);
	}

	public boolean isAfter(Day day) {
		return date.after(day.date);
	}

	public int getDayOfMonth() {
		return date.get(Calendar.DAY_OF_MONTH);
	}

	public int getWeekDay() {
		return date.get(Calendar.DAY_OF_WEEK);
	}

	public int getWeekOfMonth() {
		return date.get(Calendar.WEEK_OF_MONTH);
	}

	public int getYear() {
		return date.get(Calendar.YEAR);
	}

	public int getMonth() {
		return date.get(Calendar.MONTH);
	}

	public Calendar getDate() {
		return date;
	}

}
