package de.andrena.justintime.application.domain;

import static de.andrena.justintime.application.domain.Daytime.AFTERNOON;
import static de.andrena.justintime.application.domain.Daytime.EVENING;
import static de.andrena.justintime.application.domain.Daytime.MORNING;
import static de.andrena.justintime.application.domain.Daytime.NIGHT;
import static de.andrena.justintime.application.domain.Daytime.NOON;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class CalendarDateSource implements DateSource {

	private Calendar date;

	public CalendarDateSource(Calendar date) {
		this.date = date;
	}

	public CalendarDateSource(int year, int month, int day, int hours) {
		this.date = Calendar.getInstance();
		date.set(year, month - 1, day, hours, 0);
	}

	public CalendarDateSource() {
		this(new GregorianCalendar());
	}

	@Override
	public int getMonth() {
		return date.get(Calendar.MONTH) + 1;
	}

	@Override
	public int getDayOfMonth() {
		return date.get(Calendar.DAY_OF_MONTH);
	}

	@Override
	public int getHoursOfDay() {
		return date.get(Calendar.HOUR_OF_DAY);
	}

	public String getHours() {
		return String.valueOf(date.get(Calendar.HOUR_OF_DAY));
	}

	public String getDate() {
		DateFormat format = DateFormat.getDateInstance(DateFormat.MEDIUM);
		return format.format(date.getTime());
	}

	@Override
	public Season getSeason() {
		int dayOfYear = date.get(Calendar.DAY_OF_YEAR);
		if (dayOfYear < 81) {
			return Season.WINTER;
		} else if (dayOfYear < 162) {
			return Season.SPRING;
		} else if (dayOfYear < 243) {
			return Season.SUMMER;
		} else if (dayOfYear < 324) {
			return Season.FALL;
		} else {
			return Season.WINTER;
		}
	}

	@Override
	public Weekday getWeekday() {
		int dayOfWeek = date.get(Calendar.DAY_OF_WEEK) - 1;
		return Weekday.values()[dayOfWeek];
	}

	@Override
	public Daytime getDaytime() {
		int base = date.get(Calendar.HOUR_OF_DAY);
		int time = (int) (base % 24);
		if (time < 6) {
			return NIGHT;
		} else if (time < 11) {
			return MORNING;
		} else if (time < 14) {
			return NOON;
		} else if (time < 18) {
			return AFTERNOON;
		} else if (time < 22) {
			return EVENING;
		} else {
			return NIGHT;
		}
	}

}
