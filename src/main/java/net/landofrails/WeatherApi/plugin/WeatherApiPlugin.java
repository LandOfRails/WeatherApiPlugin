package net.landofrails.WeatherApi.plugin;

import java.text.MessageFormat;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import net.landofrails.WeatherApi.api.IWeatherManagerInfoProvider;
import net.landofrails.WeatherApi.api.WeatherGetter;

public class WeatherApiPlugin extends JavaPlugin {

	private Logger logger = Logger.getLogger(WeatherApiPlugin.class.getName());
	private InfoProvider provider;
	private WeatherHandler handler;

	@Override
	public void onLoad() {
		logger.info(String.format("Loading WeatherApiPlugin v%s", getDescription().getVersion()));

		this.reloadConfig();
		this.getConfig().options().copyDefaults(true);
		this.saveConfig();

		provider = new InfoProvider(this);
		handler = new WeatherHandler();
	}

	@Override
	public void onEnable() {

		long secondInTicks = 20l;
		long seconds = 60l * 15l;

		if (getConfig().contains("weatherapiplugin_interval"))
			seconds = 60l * getConfig().getLong("weatherapiplugin_interval");

		Runnable r = () -> new WeatherGetter(provider, handler::modifyWeather);

		Bukkit.getScheduler().runTaskTimerAsynchronously(this, r, 0, secondInTicks * seconds);

	}
	
	@Override
	public void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
	}

	public static class InfoProvider implements IWeatherManagerInfoProvider {

		private JavaPlugin plugin;

		public InfoProvider(JavaPlugin plugin) {
			this.plugin = plugin;
		}

		@Override
		public String getURL() {
			String url = "https://api.openweathermap.org/data/2.5/weather?q={0}&appid={1}";
			return MessageFormat.format(url, getCity(), getAPIKey());
		}

		@Override
		public String getAPIKey() {
			return plugin.getConfig().getString("openweathermap_apikey");
		}

		@Override
		public String getCity() {
			return plugin.getConfig().getString("openweathermap_city");
		}

	}
	
}
