package de.andrena.justintime.application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Map;

import de.andrena.justintime.application.domain.LocalDateTimeSource;
import de.andrena.justintime.application.domain.Weather;
import de.andrena.justintime.application.domain.WeatherSource;
import de.andrena.justintime.application.fake.SimulatedWeatherSource;
import de.andrena.justintime.application.impl.EsotericWeatherSource;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.common.template.TemplateEngine;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.templ.handlebars.HandlebarsTemplateEngine;

public class Server extends AbstractVerticle {

	private TemplateEngine engine;
	private Router router;

	private WeatherSource weather;

	public Server() {
		weather = new EsotericWeatherSource(new SimulatedWeatherSource());
	}

	@Override
	public void start() {
		this.engine = createEngine();
		this.router = Router.router(vertx);
		router.route("/static/*").handler(StaticHandler.create("src/main/resources"));
		router.route("/day/:year/:month/:day/:hours").handler(this::predict);
		router.route().handler(this::show);
		router.errorHandler(500, this::error);

		HttpServer server = vertx.createHttpServer();
		server.requestHandler(router).listen(8080);

		System.out.println("Weather application started");
	}

	private TemplateEngine createEngine() {
		HandlebarsTemplateEngine engine = HandlebarsTemplateEngine.create(vertx);
		engine.setMaxCacheSize(0);
		return engine;
	}

	public void show(RoutingContext context) {
		Weather w = weather.getWeather(new LocalDateTimeSource());
		fillResponse(context.data(), new LocalDateTimeSource(), w);
		render(context, "src/main/resources/index.html");
	}

	public void predict(RoutingContext context) {
		LocalDateTimeSource time = extractTimeFromRequest(context.request());
		Weather w = weather.getWeather(time);
		fillResponse(context.data(), time, w);
		render(context, "src/main/resources/index.html");
	}

	public void error(RoutingContext context) {
		render(context, "src/main/resources/error.html");
	}

	private LocalDateTimeSource extractTimeFromRequest(HttpServerRequest request) {
		int year = Integer.parseInt(request.getParam("year"));
		int month = Integer.parseInt(request.getParam("month"));
		int day = Integer.parseInt(request.getParam("day"));
		int hours = Integer.parseInt(request.getParam("hours"));

		return new LocalDateTimeSource(year, month, day, hours);
	}

	private void fillResponse(Map<String, Object> response, LocalDateTimeSource time, Weather w) {
		response.put("nexthour", nextHourLink(time.getDate()));
		response.put("prevhour", prevHourLink(time.getDate()));
		response.put("date", mediumDate(time.getDate()));
		response.put("hours", time.getHoursOfDay());
		response.put("season", time.getSeason());
		response.put("weekday", time.getWeekday());
		response.put("daytime", time.getDaytime());
		response.put("weather", w);
	}

	private void render(RoutingContext context, String template) {
		engine.render(context.data(), template, res -> {
			if (res.succeeded()) {
				context.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/html").end(res.result());
			} else {
				context.fail(res.cause());
			}
		});
	}

	private String nextHourLink(LocalDateTime date) {
		LocalDateTime next = date.plusHours(1);
		return "/day/" + next.getYear() + "/" + next.getMonthValue() + "/" + next.getDayOfMonth() + "/" + next.getHour();
	}

	private String prevHourLink(LocalDateTime date) {
		LocalDateTime next = date.minusHours(1);
		return "/day/" + next.getYear() + "/" + next.getMonthValue() + "/" + next.getDayOfMonth() + "/" + next.getHour();
	}

	public static void main(String[] args) {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new Server());
	}

	public static String mediumDate(LocalDateTime date) {
		return DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).format(date);
	}
}
