package de.andrena.justintime.application.fake;

import static de.andrena.justintime.application.fake.Esoterics.predictWithEsotericKnowledge;

import de.andrena.justintime.application.domain.DateSource;
import de.andrena.justintime.application.domain.Weather;
import de.andrena.justintime.application.domain.WeatherSource;

public class EsotericWeatherSource implements WeatherSource {

	private WeatherSource source;

	public EsotericWeatherSource(WeatherSource source) {
		this.source = source;
	}

	@Override
	public Weather getWeather(DateSource date) {
		Weather weather = source.getWeather(date);
		return predictWithEsotericKnowledge(date, weather);
	}
}
