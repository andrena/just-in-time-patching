package de.andrena.justintime.application.domain;

import static de.andrena.justintime.application.domain.Daytime.AFTERNOON;
import static de.andrena.justintime.application.domain.Daytime.EVENING;
import static de.andrena.justintime.application.domain.Daytime.MORNING;
import static de.andrena.justintime.application.domain.Daytime.NIGHT;
import static de.andrena.justintime.application.domain.Daytime.NOON;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class LocalDateTimeSource implements DateSource {

	public LocalDateTime date;

	public LocalDateTimeSource(LocalDateTime date) {
		this.date = date;
	}

	public LocalDateTimeSource(int year, int month, int day, int hours) {
		this.date = LocalDateTime.of(year, month, day, hours, 0);
	}

	public LocalDateTimeSource() {
		this(LocalDateTime.now());
	}
	
	@Override
	public long getMillis() {
		return date.toInstant(ZoneOffset.UTC).toEpochMilli();
	}
	
	public LocalDateTime getDate() {
		return date;
	}

	@Override
	public int getMonth() {
		return date.getMonthValue();
	}

	@Override
	public int getDayOfMonth() {
		return date.getDayOfMonth();
	}

	@Override
	public int getHoursOfDay() {
		return date.getHour();
	}

	@Override
	public Season getSeason() {
		int dayOfYear = date.getDayOfYear();
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
		int dayOfWeek = date.getDayOfWeek().getValue() % 7;
		return Weekday.values()[dayOfWeek];
	}

	@Override
	public Daytime getDaytime() {
		int base = date.getHour();
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
