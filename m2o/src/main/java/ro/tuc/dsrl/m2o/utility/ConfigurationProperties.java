package ro.tuc.dsrl.m2o.utility;

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
public enum ConfigurationProperties {
	OWL_FILE("ONT_FILE"), 
	URI("ONT_URI"), 
	API_TYPE("API_TYPE"), 
	ENTITIES_PACKAGE("ENTITIES_PACKAGE"), 
	AUTO_GEN("AUTO_GEN");

	private String value;

	private ConfigurationProperties(String url) {
		this.value = url;
	}

	public String value() {
		return value;
	}
}
