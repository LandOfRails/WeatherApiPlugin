package net.landofrails.WeatherApi.api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class WeatherApi {

	public static final String WEATHERMAP_URL = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";

	public static String getContent(String urlToRead) throws Exception {
		StringBuilder result = new StringBuilder();
		URL url = new URL(urlToRead);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
			for (String line; (line = reader.readLine()) != null;) {
				result.append(line);
			}
		}
		return result.toString();
	}

	public static JsonObject getJson(String urlToRead) {
		try {
			return new Gson().fromJson(getContent(urlToRead), JsonObject.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}
