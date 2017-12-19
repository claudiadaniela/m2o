package ro.tuc.dsrl.m2o.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

import ro.tuc.dsrl.m2o.datamodel.OntologyClass;
import ro.tuc.dsrl.m2o.utility.ConfigurationProperties;
import ro.tuc.dsrl.m2o.utility.PropertiesLoader;

/**
 * @Author: Technical University of Cluj-Napoca, Romania Distributed Systems
 * Research Laboratory, http://dsrl.coned.utcluj.ro/
 */
public class OntologyClassGeneration {
    private static final String PACKAGE = PropertiesLoader
            .getProperty(ConfigurationProperties.ENTITIES_PACKAGE);
    private static final String AUTO = PropertiesLoader
            .getProperty(ConfigurationProperties.AUTO_GEN);

    private OntologyClassGeneration() {
    }

    public static List<OntologyClass> generateOwlClasses() {
        if (AUTO == null || AUTO.equals("false")) {
            return new ArrayList<OntologyClass>();
        }
        Reflections reflections = new Reflections(PACKAGE);
        Set<Class<?>> annotated = reflections
                .getTypesAnnotatedWith(ro.tuc.dsrl.m2o.annotations.OntologyEntity.class);

        List<OntologyClass> ontologyClasses = new ArrayList<OntologyClass>();
        for (Class<?> c : annotated) {
            ontologyClasses.add(EntityReflectionParser.getOntologyClass(c));
        }
        return ontologyClasses;

    }

}
