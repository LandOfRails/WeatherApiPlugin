package net.landofrails.WeatherApiPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Optional;
import java.util.Scanner;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import junit.framework.TestCase;
import net.landofrails.WeatherApi.api.WeatherApi;
import net.landofrails.WeatherApi.api.WeatherGetter;

public class WeatherApiTest extends TestCase {

	public void testConnection() {

		Optional<String> apiKey = getWeatherApiKey();

		// Is the OpenWeatherMap key available?
		assert apiKey.isPresent();

		tryConnection(apiKey.get());

	}

	private void tryConnection(String apiKey) {
		final String url = WeatherApi.WEATHERMAP_URL;
		final String cityName = "Nuremberg";
		String finalUrl = null;
		try {
			finalUrl = String.format(url, URLEncoder.encode(cityName, "UTF-8"), URLEncoder.encode(apiKey, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			assert false;
		}

		try {
			WeatherApi.getContent(finalUrl);
			assert true;
		} catch (Exception e) {
			System.err.println("Connection failed.");
			e.printStackTrace();
			assert false;
		}

	}


	private Optional<String> getWeatherApiKey() {
		File myObj = new File("weatherapi.secure");
		try (Scanner myReader = new Scanner(myObj)) {
			if (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				return Optional.of(data);
			}
			return Optional.empty();
		} catch (FileNotFoundException e) {
			return Optional.empty();
		}

	}

	public void testJsons() {
		String content = readFile("src/test/resources/clear.json");
		JsonElement json = JsonParser.parseString(content);
		new WeatherGetter(json, System.out::println);

	}

	public String readFile(String fileName) {
		String content = "";
		try (Scanner myReader = new Scanner(new File(fileName))) {
			while (myReader.hasNextLine()) {
				content += myReader.nextLine() + "\n";
			}
			return content.trim();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return "";
		}
	}

}
