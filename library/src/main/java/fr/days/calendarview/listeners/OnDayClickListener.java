package fr.days.calendarview.listeners;

import org.joda.time.LocalDate;

import android.view.View;

public interface OnDayClickListener {

	void onClick(View v, LocalDate date);

}
