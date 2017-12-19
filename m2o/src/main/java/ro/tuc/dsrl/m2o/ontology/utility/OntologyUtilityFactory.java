package ro.tuc.dsrl.m2o.ontology.utility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ro.tuc.dsrl.m2o.types.ApiType;
import ro.tuc.dsrl.m2o.utility.ConfigurationProperties;
import ro.tuc.dsrl.m2o.utility.PropertiesLoader;

/**
 * @Author: Technical University of Cluj-Napoca, Romania Distributed Systems
 * Research Laboratory, http://dsrl.coned.utcluj.ro/
 */
public class OntologyUtilityFactory {
    private static final Log LOGGER = LogFactory
            .getLog(OntologyUtilityFactory.class);

    private static final String TYPE = PropertiesLoader
            .getProperty(ConfigurationProperties.API_TYPE);
    private static volatile OntologyUtility instance;

    private OntologyUtilityFactory() {
    }

    public static OntologyUtility getInstance() {
        if (instance == null) {
            synchronized (OntologyUtility.class) {
                if (instance == null) {
                    switch (ApiType.valueOf(TYPE)) {
                        case JENA: {
                            instance = JenaUtility.getInstance();
                            break;
                        }
                        case OWLAPI: {
                            instance = OwlAPIUtility.getInstance();
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
