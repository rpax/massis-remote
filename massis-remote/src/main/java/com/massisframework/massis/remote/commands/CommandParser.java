package com.massisframework.massis.remote.commands;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import io.gsonfire.GsonFireBuilder;
import io.gsonfire.TypeSelector;

@SuppressWarnings("unchecked")
public class CommandParser {
	private static CommandParser INSTANCE;
	private final Logger logger;
	private Gson gson;
	private final Map<String, Class<? extends AbstractCommand<?>>> classMap;

	public static CommandParser getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CommandParser();
		return INSTANCE;
	}

	private CommandParser() {
		this.classMap = loadClassMap();
		this.logger = Logger.getLogger(CommandParser.class.getName());
		GsonFireBuilder builder = new GsonFireBuilder().registerTypeSelector(
				AbstractCommand.class, new TypeSelector<AbstractCommand>() {
					@SuppressWarnings("rawtypes")
					@Override
					public Class<? extends AbstractCommand> getClassForElement(
							JsonElement readElement) {
						String type = readElement.getAsJsonObject().get("type")
								.getAsString();
						return classMap.get(type);
					}
				});
		this.gson = builder.createGson();
	}

	private Map<String, Class<? extends AbstractCommand<?>>> loadClassMap() {
		final ClassLoader classLoader = CommandParser.class.getClassLoader();
		final Map<String, Class<? extends AbstractCommand<?>>> classMap = new HashMap<>();
		final URL resource = classLoader.getResource("config/commandImpl.json");
		try (InputStreamReader rd = new InputStreamReader(
				resource.openStream())) {
			HashMap<String, String> mapS = new Gson().fromJson(rd,
					HashMap.class);
			mapS.forEach((k, v) -> {
				try {
					classMap.put(k, (Class<? extends AbstractCommand<?>>) Class
							.forName(v));

				} catch (Exception e) {
					logger.log(Level.SEVERE, "Exception when loading class", e);
				}
			});
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Exception when reading config file", e);
		}
		return classMap;

	}

	public <T> AbstractCommand<T> parseCommand(String jsonData) {
		return this.gson.fromJson(jsonData, AbstractCommand.class);
	}
}
