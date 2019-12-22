package de.andrena.justintime.application.domain;

public interface WeatherSource {

	Weather getWeather(DateSource date);

}
