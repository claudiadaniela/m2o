package ro.tuc.dsrl.m2o.ontology.access;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ro.tuc.dsrl.m2o.types.ApiType;
import ro.tuc.dsrl.m2o.utility.ConfigurationProperties;
import ro.tuc.dsrl.m2o.utility.PropertiesLoader;

public class OntologyAccessManagerFactory {
	private static final Log LOGGER = LogFactory
			.getLog(OntologyAccessManagerFactory.class);

	private static final String TYPE = PropertiesLoader
			.getProperty(ConfigurationProperties.API_TYPE);
	private static volatile OntologyAccessManager instance;

	private OntologyAccessManagerFactory() {
	}

	public static OntologyAccessManager getInstance() {
		if (instance == null) {
			synchronized (OntologyAccessManagerFactory.class) {
				if (instance == null) {
					switch (ApiType.valueOf(TYPE)) {
					case JENA: {
						instance = JenaAccessManager.getInstance();
						break;
					}
					case OWLAPI: {
						instance = OwlAPIAccessManager.getInstance();
						break;
					}
					default: {
						LOGGER.error("The specified API type is not available.");
						break;
					}
					}
				}

			}
		}
		return instance;
	}
}
