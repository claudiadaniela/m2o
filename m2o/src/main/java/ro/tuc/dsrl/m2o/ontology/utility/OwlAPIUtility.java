package ro.tuc.dsrl.m2o.ontology.utility;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDatatype;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.OWLSubClassOfAxiom;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.ShortFormProvider;
import org.semanticweb.owlapi.util.SimpleShortFormProvider;

import ro.tuc.dsrl.m2o.datamodel.OntologyClass;
import ro.tuc.dsrl.m2o.ontology.access.owlapi.DLQueryEngine;
import ro.tuc.dsrl.m2o.parser.OntologyClassGeneration;
import ro.tuc.dsrl.m2o.utility.ConfigurationProperties;
import ro.tuc.dsrl.m2o.utility.PropertiesLoader;

/**
 * @Author: Technical University of Cluj-Napoca, Romania Distributed Systems
 * Research Laboratory, http://dsrl.coned.utcluj.ro/
 */
public class OwlAPIUtility implements OntologyUtility {

    private static final Log LOGGER = LogFactory.getLog(OwlAPIUtility.class);

    public static final String OWL_FILE = PropertiesLoader.getProperty(ConfigurationProperties.OWL_FILE);
    public static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd hh_mm_ss";

    public static final String OWL_URI = PropertiesLoader.getProperty(ConfigurationProperties.URI);

    public static final String AUTOGENERATE = PropertiesLoader.getProperty(ConfigurationProperties.AUTO_GEN);

    private static final String THING = "Thing";
    private static final String TRUE = "true";
    private static final String STRING_CLASS = "java.lang.String";
    private static final String DATE_CLASS = "java.util.Date";
    private static final String INTEGER_CLASS = "java.lang.Integer";
    private static final String LONG_CLASS = "java.lang.Long";
    private static final String DOUBLE_CLASS = "java.lang.Double";
    private static final String FLOAT_CLASS = "java.lang.Float";
    private static final String BOOLEAN_CLASS = "java.lang.Boolean";
    private static final String BOOLEAN = "boolean";
    private static final String LONG = "long";
    private static final String INTEGER = "int";
    private static final String FLOAT = "float";
    private static final String DOUBLE = "double";

    private static OWLOntology owlOntology;
    private static OWLOntologyManager manager;
    private static OWLDataFactory factory;
    private static DLQueryEngine dlQueryEngine;

    private static volatile OwlAPIUtility instance;

    private OwlAPIUtility() {
        manager = OWLManager.createOWLOntologyManager();
        factory = manager.getOWLDataFactory();
        if (TRUE.equals(AUTOGENERATE)) {
            generateOwlClasses();
        } else {
            loadOntology();
        }

        OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
        OWLReasoner reasoner = reasonerFactory.createReasoner(owlOntology);

        ShortFormProvider shortFormProvider = new SimpleShortFormProvider();
        dlQueryEngine = new DLQueryEngine(reasoner, shortFormProvider);

        LOGGER.info("Ontology " + OWL_URI + " loaded");
    }

    public static OwlAPIUtility getInstance() {
        if (instance == null) {
            synchronized (OwlAPIUtility.class) {
                if (instance == null) {
                    instance = new OwlAPIUtility();
                }
            }
        }
        return instance;
    }

    private void insertId() {

        PrefixManager pm = new DefaultPrefixManager(OwlAPIUtility.OWL_URI);
        OWLDatatype dt = factory.getOWLDatatype("xsd:long", pm);

        OWLClass c = factory.getOWLClass(IRI.create("http://www.w3.org/2002/07/owl#Thing"));
        OWLAxiom declareC = factory.getOWLDeclarationAxiom(c);
        manager.addAxiom(owlOntology, declareC);

        OWLDataProperty property = factory.getOWLDataProperty(IRI.create(OwlAPIUtility.OWL_URI + "hasId"));

        OWLDataPropertyDomainAxiom dataPropertyDomainAxiom = factory.getOWLDataPropertyDomainAxiom(property, c);
        OWLDataPropertyRangeAxiom dataPropertyRangeAxiom = factory.getOWLDataPropertyRangeAxiom(property, dt);

        manager.addAxiom(owlOntology, dataPropertyDomainAxiom);
        manager.addAxiom(owlOntology, dataPropertyRangeAxiom);

    }

    private void insertClasses(List<OntologyClass> owlClasses) {
        for (OntologyClass oc : owlClasses) {
            String className = oc.getClassName();
            String superClassName = oc.getSuperClass();
            if (!(THING.equals(superClassName))) {

                OWLClass owlClass = factory.getOWLClass(IRI.create(OwlAPIUtility.OWL_URI + className));
                OWLClass owlSuperClass = factory.getOWLClass(IRI.create(OwlAPIUtility.OWL_URI + superClassName));

                OWLSubClassOfAxiom owlSubClassOfAxiom = factory.getOWLSubClassOfAxiom(owlClass, owlSuperClass);
                manager.addAxiom(owlOntology, owlSubClassOfAxiom);
            } else {

                OWLClass c = factory.getOWLClass(IRI.create(OwlAPIUtility.OWL_URI + className));
                OWLAxiom declareC = factory.getOWLDeclarationAxiom(c);
                manager.addAxiom(owlOntology, declareC);

            }
        }
    }

    private void insertDatatypeProperties(List<OntologyClass> owlClasses) {
        for (OntologyClass oc : owlClasses) {
            String className = oc.getClassName();

            OWLClass owlClass = factory.getOWLClass(IRI.create(OwlAPIUtility.OWL_URI + className));

            Iterator<Entry<String, String>> it = oc.getFields().entrySet().iterator();

            while (it.hasNext()) {
                @SuppressWarnings("rawtypes")
                Map.Entry pair = (Map.Entry) it.next();

                OWLDataProperty property = factory
                        .getOWLDataProperty(IRI.create(OwlAPIUtility.OWL_URI + pair.getKey()));

                OWLDataPropertyDomainAxiom dataPropertyDomainAxiom = factory.getOWLDataPropertyDomainAxiom(property,
                        owlClass);

                manager.addAxiom(owlOntology, dataPropertyDomainAxiom);

                PrefixManager pm = new DefaultPrefixManager(OwlAPIUtility.OWL_URI);
                OWLDatatype dt = null;

                if (DATE_CLASS.equals(pair.getValue())) {
                    dt = factory.getOWLDatatype("xsd:dateTime", pm);
                } else if (STRING_CLASS.equals(pair.getValue())) {
                    dt = factory.getOWLDatatype("xsd:string", pm);
                } else if (LONG_CLASS.equals(pair.getValue())) {
                    dt = factory.getOWLDatatype("xsd:long", pm);
                } else if (INTEGER_CLASS.equals(pair.getValue())) {
                    dt = factory.getOWLDatatype("xsd:int", pm);
                } else if (FLOAT_CLASS.equals(pair.getValue())) {
                    dt = factory.getOWLDatatype("xsd:float", pm);
                } else if (DOUBLE_CLASS.equals(pair.getValue())) {
                    dt = factory.getOWLDatatype("xsd:double", pm);
                } else if (BOOLEAN_CLASS.equals(pair.getValue())) {
                    dt = factory.getOWLDatatype("xsd:boolean", pm);
                } else if (LONG.equals(pair.getValue())) {
                    dt = factory.getOWLDatatype("xsd:long", pm);
                } else if (INTEGER.equals(pair.getValue())) {
                    dt = factory.getOWLDatatype("xsd:integer", pm);
                } else if (BOOLEAN.equals(pair.getValue())) {
                    dt = factory.getOWLDatatype("xsd:boolean", pm);
                } else if (FLOAT.equals(pair.getValue())) {
                    dt = factory.getOWLDatatype("xsd:float", pm);
                } else if (DOUBLE.equals(pair.getValue())) {
                    dt = factory.getOWLDatatype("xsd:double", pm);
                }

                OWLDataPropertyRangeAxiom dataPropertyRangeAxiom = factory.getOWLDataPropertyRangeAxiom(property, dt);
                manager.addAxiom(owlOntology, dataPropertyRangeAxiom);

                it.remove();
            }

        }
    }

    private void insertObjectProperties(List<OntologyClass> owlClasses) {
        for (OntologyClass oc : owlClasses) {
            String className = oc.getClassName();

            OWLClass owlClass = factory.getOWLClass(IRI.create(OwlAPIUtility.OWL_URI + className));

            Iterator<Entry<String, String>> it = oc.getObjectProperties().entrySet().iterator();

            while (it.hasNext()) {
                @SuppressWarnings("rawtypes")
                Map.Entry pair = (Map.Entry) it.next();

                OWLObjectProperty property = factory.getOWLObjectProperty(IRI.create(OwlAPIUtility.OWL_URI
                        + pair.getKey()));

                String range = (String) pair.getValue();
                OWLClass owlRangeClass = factory.getOWLClass(IRI.create(OwlAPIUtility.OWL_URI + range));

                OWLObjectPropertyDomainAxiom objectPropertyDomainAxiom = factory.getOWLObjectPropertyDomainAxiom(
                        property, owlClass);
                OWLObjectPropertyRangeAxiom objectPropertyRangeAxiom = factory.getOWLObjectPropertyRangeAxiom(property,
                        owlRangeClass);

                manager.addAxiom(owlOntology, objectPropertyDomainAxiom);
                manager.addAxiom(owlOntology, objectPropertyRangeAxiom);

                it.remove();
            }

        }
    }

    public void generateOwlClasses() {

        try {
            owlOntology = manager.createOntology(IRI.create(OWL_URI));
        } catch (OWLOntologyCreationException e) {
            LOGGER.error(e);
        }

        List<OntologyClass> owlClasses = OntologyClassGeneration.generateOwlClasses();

        insertId();
        insertClasses(owlClasses);
        insertDatatypeProperties(owlClasses);
        insertObjectProperties(owlClasses);

        saveOntology();
    }

    // @Override
    private void loadOntology() {

        File file = new File(OWL_FILE);

        try {
            owlOntology = manager.loadOntologyFromOntologyDocument(file);
        } catch (OWLOntologyCreationException e) {
            LOGGER.error("", e);
        }

    }

    @Override
    public void saveOntology() {

        String fileName = PropertiesLoader.getProperty(ConfigurationProperties.OWL_FILE);
        File file = new File(fileName);

        OWLOntologyFormat format = manager.getOntologyFormat(owlOntology);

        OWLXMLOntologyFormat owlxmlFormat = new OWLXMLOntologyFormat();
        if (format.isPrefixOWLOntologyFormat()) {
            owlxmlFormat.copyPrefixesFrom(format.asPrefixOWLOntologyFormat());
        }
        try {
            manager.saveOntology(owlOntology, owlxmlFormat, IRI.create(file.toURI()));
        } catch (OWLOntologyStorageException e) {
            LOGGER.error(e);
        }

    }

    @Override
    public void saveSnapshot(Date date) {

        File file = new File(getSnapshotName(date));
        OWLOntologyFormat format = manager.getOntologyFormat(owlOntology);
        OWLXMLOntologyFormat owlxmlFormat = new OWLXMLOntologyFormat();
        if (format.isPrefixOWLOntologyFormat()) {
            owlxmlFormat.copyPrefixesFrom(format.asPrefixOWLOntologyFormat());
        }
        try {
            manager.saveOntology(owlOntology, owlxmlFormat, IRI.create(file.toURI()));
        } catch (OWLOntologyStorageException e) {
            LOGGER.error(e);
        }
    }

    @Override
    public void reset() {
        loadOntology();
    }

    public OWLOntology getOntology() {
        return owlOntology;
    }

    public OWLOntologyManager getManager() {
        return manager;
    }

    public DLQueryEngine getDlQueryEngine() {
        return dlQueryEngine;
    }

    public OWLDataFactory getFactory() {
        return factory;
    }

    public void setDlQueryEngine(DLQueryEngine dlQueryEngine) {
        OwlAPIUtility.dlQueryEngine = dlQueryEngine;
    }

    private static String getSnapshotName(Date date) {
        int i = OWL_FILE.lastIndexOf(".");
        String[] a = {OWL_FILE.substring(0, i), OWL_FILE.substring(i)};

        SimpleDateFormat dt1 = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        String timestamp = dt1.format(date);

        return a[0] + timestamp + a[1];
    }

}
