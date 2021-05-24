package net.landofrails.WeatherApi.api;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.landofrails.WeatherApi.api.WeatherInformation.Intensity;
import net.landofrails.WeatherApi.api.WeatherInformation.Season;
import net.landofrails.WeatherApi.api.WeatherInformation.Weather;
import net.landofrails.WeatherApi.api.WeatherInformation.WeatherIntensity;

public class WeatherGetter {


	public WeatherGetter(IWeatherManagerInfoProvider provider, Consumer<WeatherInformation> consumer) {
		this(WeatherApi.getJson(provider.getURL()), consumer);
	}

	public WeatherGetter(JsonElement weatherJson, Consumer<WeatherInformation> consumer) {

		if (weatherJson != null) {
			JsonElement weatherEntry = weatherJson.getAsJsonObject().get("weather");
			JsonObject weatherDataEntry = weatherEntry.getAsJsonArray().get(0).getAsJsonObject();
			int status = weatherDataEntry.get("id").getAsInt();

			List<WeatherIntensity> weather = statusToWeather(status);

			WeatherInformation wi = new WeatherInformation(Season.EARLY_SUMMER, null, weather);
			consumer.accept(wi);
		}

	}

	public List<WeatherIntensity> statusToWeather(int statusCode) {
		char[] status = ("" + statusCode).toCharArray();
		
		List<WeatherIntensity> wi = new ArrayList<>();
		
		// 2XX
		if (status[0] == '2') {
			Weather w1 = Weather.THUNDER;
			Intensity i1 = Intensity.NORMAL;

			// 20X
			if (status[1] == '0') {
				Weather w2 = Weather.RAIN;
				Intensity i2 = Intensity.NORMAL;

				// 200
				if (status[2] == '0')
					i2 = Intensity.LIGHT;

				// 202
				else if (status[2] == '2')
					i2 = Intensity.HEAVY;

				wi.add(new WeatherIntensity(w2, i2));

				// 21X
			} else if (status[1] == '1') {

				// 210
				if (status[2] == '0')
					i1 = Intensity.LIGHT;
				// 212
				else if (status[2] == '2')
					i1 = Intensity.HEAVY;

				// 23X
			} else if (status[1] == '3') {
				Weather w2 = Weather.DRIZZLE;
				Intensity i2 = Intensity.NORMAL;

				// 230
				if (status[2] == '0')
					i2 = Intensity.LIGHT;

				// 232
				else if (status[2] == '2')
					i2 = Intensity.HEAVY;

				wi.add(new WeatherIntensity(w2, i2));
			}

			wi.add(new WeatherIntensity(w1, i1));

			// 3XX
		} else if (status[0] == '3') {

			Weather w1 = Weather.DRIZZLE;
			Intensity i1 = Intensity.NORMAL;

			// 30X
			if (status[1] == '0') {

				// 300
				if (status[2] == '0')
					i1 = Intensity.LIGHT;

				// 302
				else if (status[2] == '2')
					i1 = Intensity.HEAVY;

			// 31X
			} else if (status[1] == '1') {

				Weather w2 = Weather.RAIN;
				Intensity i2 = Intensity.NORMAL;

				// 310
				if (status[2] == '0') {
					i1 = Intensity.LIGHT;
					i2 = Intensity.LIGHT;

					// 312 || 314
				} else if (status[2] == '2' || status[2] == '4') {
					i1 = Intensity.HEAVY;
					i2 = Intensity.HEAVY;

				}

				wi.add(new WeatherIntensity(w2, i2));

			}

			// TODO: 5XX Rain
			// TODO: 6XX Snow
			// TODO: 7XX Misc
			// TODO: 800 Clear
			// TODO: 80X Clouds

			wi.add(new WeatherIntensity(w1, i1));

		}

		return wi;
	}

}
