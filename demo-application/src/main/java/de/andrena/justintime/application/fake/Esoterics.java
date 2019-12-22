package de.andrena.justintime.application.fake;

import de.andrena.justintime.application.domain.DateSource;
import de.andrena.justintime.application.domain.Weather;
import de.andrena.justintime.application.domain.Weekday;

public class Esoterics {

	public static boolean badStellarConfiguration(DateSource date) {
		return date.getDayOfMonth() % (date.getMonth() % date.getHoursOfDay()) == 0;  
	}

	public static Weather predictWithEsotericKnowledge(DateSource date, Weather weather) {
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
