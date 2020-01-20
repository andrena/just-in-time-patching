package de.andrena.justintime.application.fake;

import static de.andrena.justintime.application.domain.Precipitation.DRIZZLE;
import static de.andrena.justintime.application.domain.Precipitation.DRY;
import static de.andrena.justintime.application.domain.Precipitation.MIST;
import static de.andrena.justintime.application.domain.Precipitation.RAIN;
import static de.andrena.justintime.application.domain.Precipitation.SNOW;
import static de.andrena.justintime.application.domain.Temperature.COLD;
import static de.andrena.justintime.application.domain.Temperature.COOL;
import static de.andrena.justintime.application.domain.Temperature.FREEZING;
import static de.andrena.justintime.application.domain.Temperature.HOT;
import static de.andrena.justintime.application.domain.Temperature.MODERATE;
import static de.andrena.justintime.application.domain.Temperature.WARM;

import de.andrena.justintime.application.domain.DateSource;
import de.andrena.justintime.application.domain.Precipitation;
import de.andrena.justintime.application.domain.Season;
import de.andrena.justintime.application.domain.Temperature;
import de.andrena.justintime.application.domain.Weather;
import de.andrena.justintime.application.domain.WeatherSource;
import de.andrena.justintime.application.domain.Wind;

public class SimulatedWeatherSource implements WeatherSource {

	private Waves precipitation;
	private Waves temperature;
	private Waves wind;

	public SimulatedWeatherSource() {
		this.precipitation = new Waves(1.0, 5, 15.0);
		this.temperature = new Waves(1.5, 3.5, 10.0);
		this.wind = new Waves(1.0, 4.0, 10.0);
	}

	@Override
	public Weather getWeather(DateSource date) {
		long hours = date.getMillis() / 1000 / 60 / 60;
		Season season = date.getSeason();

		return new Weather(
			precipitation.value(hours, precipitationForSeason(season)),
			temperature.value(hours, temperatureForSeason(season)),
			wind.value(hours, windForSeason(season)));
	}

	public static Precipitation[] precipitationForSeason(Season season) {
		switch (season) {
		case SPRING:
			return new Precipitation[] { DRY, DRY, DRY, DRY, MIST, MIST, MIST, DRIZZLE, DRIZZLE, RAIN };
		case SUMMER:
			return new Precipitation[] { DRY, DRY, DRY, DRY, DRY, DRY, DRY, DRY, DRIZZLE, RAIN };
		case FALL:
			return new Precipitation[] { DRY, DRY, DRY, MIST, MIST, MIST, DRIZZLE, DRIZZLE, RAIN, RAIN };
		case WINTER:
		default:
			return new Precipitation[] { DRY, DRY, DRY, MIST, MIST, DRIZZLE, RAIN, SNOW, SNOW, SNOW };
		}
	}

	public static Temperature[] temperatureForSeason(Season season) {
		switch (season) {
		case SPRING:
			return new Temperature[] { WARM, MODERATE, COOL, COLD };
		case SUMMER:
			return new Temperature[] { HOT, WARM, MODERATE, COOL };
		case FALL:
			return new Temperature[] { WARM, MODERATE, COOL, COLD };
		case WINTER:
		default:
			return new Temperature[] { MODERATE, COOL, COLD, FREEZING };
		}
	}

	public static Wind[] windForSeason(Season season) {
		return Wind.values();
	}

	private static class Waves {

		private double f1;
		private double f2;
		private double f3;

		public Waves(double f1, double f2, double f3) {
			this.f1 = f1;
			this.f2 = f2;
			this.f3 = f3;
		}

		public <T> T value(double base, T[] values) {
			double value = Math.sin(base / f1 + Math.PI) * f1
				+ Math.sin(base / f2 + 2 * Math.PI) * f2
				+ Math.sin(base / f3 + 3 * Math.PI) * f3;

			double max = f1 + f2 + f3;
			double min = -max;

			double relativeValue = (value - min) / (max - min);
			int index = (int) (relativeValue * (double) values.length);

			return values[index];
		}

	}
}
