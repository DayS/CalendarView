package fr.days.calendarview.listeners;

import org.joda.time.LocalDate;

import android.view.View;

public interface OnDayLongClickListener {

	boolean onLongClick(View v, LocalDate date);

}
