package ro.tuc.dsrl.m2o.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * 
 * @Author: Technical University of Cluj-Napoca, Romania Distributed Systems
 *          Research Laboratory, http://dsrl.coned.utcluj.ro/
 * @Project: FP7 GEYSER Project, http://www.geyser-project.eu/
 * @Module: rest-client
 * @Since: Dec 17, 2014
 * @Description:
 *
 */
public final class PropertiesLoader {
	private static final Logger LOGGER = Logger
			.getLogger(PropertiesLoader.class);
	private static String CONFIG_FILE = "/src/main/resources/owl-config.config";
	private static Properties prop;

	private PropertiesLoader() {
	}

	private static void loadProperties() {
		prop = new Properties();
		try {
			final Path path = Paths.get(CONFIG_FILE);
			if (Files.exists(path, LinkOption.NOFOLLOW_LINKS)) {
				prop.load(new FileInputStream(CONFIG_FILE));
			} else {
				prop.load(PropertiesLoader.class
						.getResourceAsStream(CONFIG_FILE));
			}
		} catch (IOException e) {
			LOGGER.error("", e);
		}
	}

	public static String getProperty(ConfigurationProperties configProp) {
		if (prop == null) {
			loadProperties();
		}
		return prop.getProperty(configProp.value());
	}

	/**
	 * @param prop
	 *            the prop to set
	 */
	public static void setProp(Properties prop) {
		PropertiesLoader.prop = prop;
	}
	public static void setConfigFile(String file){CONFIG_FILE = file;}
}
