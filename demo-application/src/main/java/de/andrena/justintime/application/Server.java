package de.andrena.justintime.application;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import de.andrena.justintime.application.domain.LocalDateTimeSource;
import de.andrena.justintime.application.domain.WeatherSource;
import de.andrena.justintime.application.fake.SimulatedWeatherSource;
import de.andrena.justintime.application.impl.EsotericWeatherSource;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
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

	public void start() {
		this.engine = createEngine();
		this.router = Router.router(vertx);
		router.route("/static/*").handler(StaticHandler.create("src/main/resources"));
		router.route("/day/:year/:month/:day/:hours").handler(this::predict);
		router.route().handler(this::show);

		HttpServer server = vertx.createHttpServer();
		server.requestHandler(router).listen(8080);
	}

	private TemplateEngine createEngine() {
		HandlebarsTemplateEngine engine = HandlebarsTemplateEngine.create(vertx);
		engine.setMaxCacheSize(0);
		return engine;
	}

	public void show(RoutingContext context) {
 		LocalDateTimeSource time = new LocalDateTimeSource();
		context.data().put("nexthour", nextHourLink(time.getDate()));
		context.data().put("prevhour", prevHourLink(time.getDate()));
		context.data().put("date", mediumDate(time.getDate()));
		context.data().put("hours", time.getHoursOfDay());
		context.data().put("season", time.getSeason());
		context.data().put("weekday", time.getWeekday());
		context.data().put("daytime", time.getDaytime());
		context.data().put("weather", weather.getWeather(time));

		engine.render(context.data(), "src/main/resources/index.html", res -> {
			if (res.succeeded()) {
				context.response().putHeader(HttpHeaders.CONTENT_TYPE, "text/html").end(res.result());
			} else {
				context.fail(res.cause());
			}
		});
	}

	public void predict(RoutingContext context) {
		int year = Integer.parseInt(context.request().getParam("year"));
		int month = Integer.parseInt(context.request().getParam("month"));
		int day = Integer.parseInt(context.request().getParam("day"));
		int hours = Integer.parseInt(context.request().getParam("hours"));

		LocalDateTimeSource time = new LocalDateTimeSource(year, month, day, hours);
		context.data().put("nexthour", nextHourLink(time.getDate()));
		context.data().put("prevhour", prevHourLink(time.getDate()));
		context.data().put("date", mediumDate(time.getDate()));
		context.data().put("hours", time.getHoursOfDay());
		context.data().put("season", time.getSeason());
		context.data().put("weekday", time.getWeekday());
		context.data().put("daytime", time.getDaytime());
		context.data().put("weather", weather.getWeather(time));

		engine.render(context.data(), "src/main/resources/index.html", res -> {
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
