package de.andrena.justintime.application.impl;

import de.andrena.justintime.application.domain.DateSource;
import de.andrena.justintime.application.domain.Weather;
import de.andrena.justintime.application.domain.Weekday;

public class Esoterics {

	private int sourceOfKnowledge;

	public Esoterics() {
	}

	public Esoterics(int sourceOfKnowledge) {
		this.sourceOfKnowledge = sourceOfKnowledge;
	}

	public boolean badStellarConfiguration(DateSource date) {
		sourceOfKnowledge = (sourceOfKnowledge + 1) % 7;
		return date.getHoursOfDay() % sourceOfKnowledge == 0;
	}

	public Weather applyEsotericKnowledge(DateSource date, Weather weather) {
		if (date.getDayOfMonth() == 13 && date.getWeekday() == Weekday.FRIDAY && badStellarConfiguration(date)) {
			return new Weather(
				weather.getPrecipitation().worse(),
				weather.getTemperature().worse(),
				weather.getWind().worse());
		} else {
			return weather;
		}
	}

}
