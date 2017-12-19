package ro.tuc.dsrl.m2o.ontology.utility;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ro.tuc.dsrl.m2o.datamodel.OntologyClass;
import ro.tuc.dsrl.m2o.parser.OntologyClassGeneration;
import ro.tuc.dsrl.m2o.utility.ConfigurationProperties;
import ro.tuc.dsrl.m2o.utility.PropertiesLoader;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntDocumentManager;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.XSD;

/**
 * @Author: Technical University of Cluj-Napoca, Romania Distributed Systems
 * Research Laboratory, http://dsrl.coned.utcluj.ro/
 */
public class JenaUtility implements OntologyUtility {

    private static final Log LOGGER = LogFactory.getLog(JenaUtility.class);

    public static final String OWL_FILE = PropertiesLoader
            .getProperty(ConfigurationProperties.OWL_FILE);
    public static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd hh_mm_ss";
    public static final String AUTOGENERATE = PropertiesLoader
            .getProperty(ConfigurationProperties.AUTO_GEN);

    public static final String OWL_URI = PropertiesLoader
            .getProperty(ConfigurationProperties.URI);
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
    private static final String INT = "int";
    private static final String FLOAT = "float";
    private static final String DOUBLE = "double";

    private OntModel ontModel;

    private static volatile JenaUtility instance;

    private JenaUtility() {
        if (TRUE.equals(AUTOGENERATE)) {
            generateOwlClasses();
        }
        loadOntology();
    }

    public static JenaUtility getInstance() {
        if (instance == null) {
            synchronized (JenaUtility.class) {
                if (instance == null) {
                    instance = new JenaUtility();
                }
            }
        }
        return instance;
    }

    private void insertId() {
        OntClass ontClass = ontModel
                .getOntClass("http://www.w3.org/2002/07/owl#Thing");
        DatatypeProperty datatypeProperty = ontModel
                .createDatatypeProperty(JenaUtility.OWL_URI + "hasId");
        datatypeProperty.setDomain(ontClass);
        datatypeProperty.setRange(XSD.xlong);
    }

    private void insertClasses(List<OntologyClass> owlClasses) {
        for (OntologyClass oc : owlClasses) {
            String className = oc.getClassName();
            String superClassName = oc.getSuperClass();
            if (!(THING.equals(superClassName))) {
                OntClass ontClass = ontModel.createClass(JenaUtility.OWL_URI
                        + className);
                OntClass ontSuperClass = ontModel
                        .createClass(JenaUtility.OWL_URI + superClassName);
                ontModel.add(ontClass, RDFS.subClassOf, ontSuperClass);
            } else {
                ontModel.createClass(JenaUtility.OWL_URI + className);
            }
        }
    }

    private void insertDatatypeProperties(List<OntologyClass> owlClasses) {
        for (OntologyClass oc : owlClasses) {
            String className = oc.getClassName();

            OntClass ontClass = ontModel.getOntClass(JenaUtility.OWL_URI
                    + className);

            Iterator<Entry<String, String>> it = oc.getFields().entrySet()
                    .iterator();

            while (it.hasNext()) {
                @SuppressWarnings("rawtypes")
                Map.Entry pair = (Map.Entry) it.next();

                DatatypeProperty datatypeProperty = ontModel
                        .createDatatypeProperty(JenaUtility.OWL_URI
                                + pair.getKey());
                datatypeProperty.setDomain(ontClass);

                if (DATE_CLASS.equals(pair.getValue())) {
                    datatypeProperty.setRange(XSD.dateTime);
                } else if (INTEGER_CLASS.equals(pair.getValue())) {
                    datatypeProperty.setRange(XSD.xint);
                } else if (STRING_CLASS.equals(pair.getValue())) {
                    datatypeProperty.setRange(XSD.xstring);
                } else if (LONG_CLASS.equals(pair.getValue())) {
                    datatypeProperty.setRange(XSD.xlong);
                } else if (DOUBLE_CLASS.equals(pair.getValue())) {
                    datatypeProperty.setRange(XSD.xdouble);
                } else if (FLOAT_CLASS.equals(pair.getValue())) {
                    datatypeProperty.setRange(XSD.xfloat);
                } else if (BOOLEAN_CLASS.equals(pair.getValue())) {
                    datatypeProperty.setRange(XSD.xboolean);
                } else if (BOOLEAN.equals(pair.getValue())) {
                    datatypeProperty.setRange(XSD.xboolean);
                } else if (LONG.equals(pair.getValue())) {
                    datatypeProperty.setRange(XSD.xlong);
                } else if (INT.equals(pair.getValue())) {
                    datatypeProperty.setRange(XSD.xint);
                } else if (FLOAT.equals(pair.getValue())) {
                    datatypeProperty.setRange(XSD.xfloat);
                } else if (DOUBLE.equals(pair.getValue())) {
                    datatypeProperty.setRange(XSD.xdouble);
                }

                it.remove();
            }

        }
    }

    private void insertObjectProperties(List<OntologyClass> owlClasses) {
        for (OntologyClass oc : owlClasses) {
            String className = oc.getClassName();

            OntClass ontClass = ontModel.getOntClass(JenaUtility.OWL_URI
                    + className);

            Iterator<Entry<String, String>> it = oc.getObjectProperties()
                    .entrySet().iterator();

            while (it.hasNext()) {
                @SuppressWarnings("rawtypes")
                Map.Entry pair = (Map.Entry) it.next();

                ObjectProperty objectProperty = ontModel
                        .createObjectProperty(JenaUtility.OWL_URI
                                + pair.getKey());
                objectProperty.setDomain(ontClass);

                String range = (String) pair.getValue();
                OntClass rangeClass = ontModel.getOntClass(JenaUtility.OWL_URI
                        + range);

                objectProperty.setRange(rangeClass);

                it.remove();
            }

        }
    }

    public void generateOwlClasses() {

        OntDocumentManager dm = new OntDocumentManager();

        OntModelSpec s = new OntModelSpec(OntModelSpec.OWL_DL_MEM);
        s.setDocumentManager(dm);

        ontModel = ModelFactory.createOntologyModel(s, null);

        List<OntologyClass> owlClasses = OntologyClassGeneration
                .generateOwlClasses();

        insertId();
        insertClasses(owlClasses);
        insertDatatypeProperties(owlClasses);
        insertObjectProperties(owlClasses);

        saveOntology();
    }

    @Override
    public void saveOntology() {
        String fileName = PropertiesLoader
                .getProperty(ConfigurationProperties.OWL_FILE);

        try {
            File file = new File(fileName);
            ontModel.write(new FileOutputStream(file));

        } catch (IOException e) {
            LOGGER.error("", e);
        }
    }

    // @Override
    private void loadOntology() {
        ontModel = getDataModel();
    }

    public OntModel getDataModel() {

        OntDocumentManager dm = new OntDocumentManager();

        OntModelSpec s = new OntModelSpec(OntModelSpec.OWL_DL_MEM);
        s.setDocumentManager(dm);
        OntModel m = ModelFactory.createOntologyModel(s, null);

        InputStream in = null;

        in = FileManager.get().open(OWL_FILE);
        if (in == null) {
            LOGGER.error("", new IllegalArgumentException("File: " + OWL_FILE
                    + " not found"));
        }

        try {
            m.read(in, "");
        } catch (Exception e) {
            LOGGER.error("", e);
        }

        return m;
    }

    public OntModel getOntModel() {
        return ontModel;
    }

    public void setOntModel(OntModel ontModel) {
        this.ontModel = ontModel;
    }

    @Override
    public void saveSnapshot(Date date) {
        File file = new File(getSnapshotName(date));

        try {
            ontModel.write(new FileOutputStream(file));

        } catch (IOException e) {
            LOGGER.error("", e);
        }
    }

    @Override
    public void reset() {
        loadOntology();
    }

    private static String getSnapshotName(Date date) {
        int i = OWL_FILE.lastIndexOf(".");
        String[] a = {OWL_FILE.substring(0, i), OWL_FILE.substring(i)};

        SimpleDateFormat dt1 = new SimpleDateFormat(SIMPLE_DATE_FORMAT);
        String timestamp = dt1.format(date);

        return a[0] + timestamp + a[1];
    }

}
