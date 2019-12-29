package de.andrena.justintime.application.domain;

public interface DateSource {

	Season getSeason();
	Weekday getWeekday();
	Daytime getDaytime();
	int getMonth();
	int getDayOfMonth();
	int getHoursOfDay();
	long getMillis();

}
