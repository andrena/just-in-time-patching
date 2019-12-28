package de.andrena.justintime.application.fake;

import de.andrena.justintime.application.domain.DateSource;
import de.andrena.justintime.application.domain.Weather;
import de.andrena.justintime.application.domain.WeatherSource;

public class EsotericWeatherSource implements WeatherSource {

	private WeatherSource source;
	private Esoterics esoterics;

	public EsotericWeatherSource(WeatherSource source) {
		this.source = source;
		this.esoterics = new Esoterics();
	}

	@Override
	public Weather getWeather(DateSource date) {
		Weather weather = source.getWeather(date);
		return esoterics.applyEsotericKnowledge(date, weather);
	}
}
