package de.andrena.justintime.application.esoterics;

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

	public boolean badStellarConstellation(DateSource date) {
		updateSourceOfKnowledge();
		return date.getHoursOfDay() % sourceOfKnowledge() == 0;
	}

	public Weather applyEsotericKnowledge(DateSource date, Weather weather) {
		if (date.getDayOfMonth() == 13 && date.getWeekday() == Weekday.FRIDAY && badStellarConstellation(date)) {
			return new Weather(
				weather.getPrecipitation().worse(),
				weather.getTemperature().worse(),
				weather.getWind().worse());
		} else {
			return weather;
		}
	}

	public void updateSourceOfKnowledge() {
		sourceOfKnowledge = (sourceOfKnowledge + 1) % 7;
	}

	public int sourceOfKnowledge() {
		return sourceOfKnowledge;
	}

}
