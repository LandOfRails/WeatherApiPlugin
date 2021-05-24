package net.landofrails.WeatherApi.api;

import java.util.List;

public class WeatherInformation {

	private WeatherIntensity[] weather;
	private Integer temp;
	private Season season;

	public WeatherInformation(Season season, Integer temp, WeatherIntensity... weather) {
		this.weather = weather;
		this.temp = temp;
		this.season = season;
	}

	public WeatherInformation(Season season, Integer temp, List<WeatherIntensity> weather) {
		this(season, temp, weather.toArray(new WeatherIntensity[0]));
	}

	public WeatherIntensity[] getWeather() {
		return weather;
	}

	public Season getSeason() {
		return season;
	}

	public Integer getTemp() {
		return temp;
	}

	public enum Weather {
		// @formatter:off
		THUNDER, DRIZZLE, RAIN, SNOW, TORNADO, CLEAR, CLOUDS, SANDSTORM;

	}
	
	public enum Intensity {
		LIGHT, NORMAL, HEAVY;
	}
	
	public static class WeatherIntensity {
		
		private Weather weather;
		private Intensity intensity;
		
		public WeatherIntensity(Weather weather, Intensity intensity) {
			this.weather = weather;
			this.intensity = intensity;
		}

		public Weather getWeather() {
			return weather;
		}
		
		public Intensity getIntensity() {
			return intensity;
		}
		
	}

	public enum Season {

		// @formatter:off
		EARLY_SPRING, MID_SPRING, LATE_SPRING,
		EARLY_SUMMER, MID_SUMMER, LATE_SUMMER,
		EARLY_AUTUMN, MID_AUTUMN, LATE_AUTUMN,
		EARLY_WINTER, MID_WINTER, LATE_WINTER;
		// @formatter:on

	}


}
