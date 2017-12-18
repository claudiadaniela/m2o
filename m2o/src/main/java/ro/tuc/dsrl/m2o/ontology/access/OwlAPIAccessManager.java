package ro.tuc.dsrl.m2o.ontology.access;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyExpression;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.util.OWLEntityRemover;

import ro.tuc.dsrl.m2o.datamodel.OntologyIndividual;
import ro.tuc.dsrl.m2o.datamodel.OntologyIndividual.RangeData;
import ro.tuc.dsrl.m2o.ontology.utility.OntologyUtilityFactory;
import ro.tuc.dsrl.m2o.ontology.utility.OwlAPIUtility;
import ro.tuc.dsrl.m2o.parser.EntityReflectionParser;
import uk.ac.manchester.cs.owl.owlapi.OWLLiteralImpl;

public class OwlAPIAccessManager implements OntologyAccessManager {
	
	private static final String LIST_INTERFACE = "interface java.util.List";
	private static final Log LOGGER = LogFactory.getLog(OwlAPIAccessManager.class);
	private OwlAPIUtility owlUtility;

	private static volatile OwlAPIAccessManager instance;

	public OwlAPIAccessManager() {
		owlUtility = (OwlAPIUtility) OntologyUtilityFactory.getInstance();
	}

	static OwlAPIAccessManager getInstance() {
		if (instance == null) {
			synchronized (OwlAPIAccessManager.class) {
				if (instance == null) {
					instance = new OwlAPIAccessManager();
				}
			}
		}
		return instance;
	}

	/**
	 * @param cls
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	@Override
	public <T> List<T> getIndividuals(Class<T> cls) {
		Set<OWLNamedIndividual> namedIndividuals = owlUtility.getDlQueryEngine()
				.getInstances(cls.getSimpleName(), true);
		Set<OWLIndividual> individuals = new HashSet<OWLIndividual>();
		Iterator<OWLNamedIndividual> it = namedIndividuals.iterator();
		while (it.hasNext()) {
			individuals.add(it.next());
		}
		List<T> objects = new ArrayList<T>();

		try {
			getIndividualsRecursive(cls, individuals, objects);
		} catch (InstantiationException e) {
			LOGGER.error("", e);
		} catch (IllegalAccessException e) {
			LOGGER.error("", e);
		} catch (ClassNotFoundException e) {
			LOGGER.error("", e);
		} catch (ParseException e) {
			LOGGER.error("", e);
		}
		return objects;
	}

	/**
	 * @param cls
	 * @param windPowerSourceId
	 * @return
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@Override
	public <T> T getIndividual(Class<T> cls, Long id) {
		List<T> individuals = getIndividuals(cls);
		Field field = getField(cls, "id");
		field.setAccessible(true);
		for (T ind : individuals) {
			try {
				if (field.get(ind) == id) {
					return ind;
				}
			} catch (IllegalArgumentException e) {
				LOGGER.error("", e);
			} catch (IllegalAccessException e) {
				LOGGER.error("", e);
			}
		}
		return null;
	}

	/**
	 * @param cls
	 * @param individuals
	 * @param objects
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 * @throws ParseException
	 * @throws IllegalArgumentException
	 */

	private <T> void getIndividualsRecursive(Class<?> cls, Set<OWLIndividual> individuals, List<T> objects)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException,
			ParseException {
		if (!individuals.isEmpty()) {
			for (OWLIndividual individ : individuals) {
				Class<?> c = cls;
				String className = individ.asOWLNamedIndividual().getIRI().toString()
						.replace(OwlAPIUtility.OWL_URI, "");
				className = className.replaceAll("\\_{1,2}[0-9]*", "");
				if (!cls.getSimpleName().equals(className)) {
					c = Class.forName(cls.getPackage().getName() + "." + className);
				}
				@SuppressWarnings("unchecked")
				T clsInstance = (T) c.newInstance();
				populateDataPropertyFields(c, individ, clsInstance);
				populateObjectPropertyFields(c, individ, clsInstance);
				objects.add(clsInstance);
			}
		}
	}

	/**
	 * @param cls
	 * @param individ
	 * @param clsInstance
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ParseException
	 * @throws IllegalArgumentException
	 */

	private <T> void populateObjectPropertyFields(Class<?> cls, OWLIndividual individ, T clsInstance)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException,
			ParseException {
		String propName;
		// get object property values
		Map<OWLObjectPropertyExpression, Set<OWLIndividual>> objectPropertyValues = individ
				.getObjectPropertyValues(owlUtility.getOntology());

		for (OWLObjectPropertyExpression opv : objectPropertyValues.keySet()) {

			Set<OWLIndividual> newIndiv = objectPropertyValues.get(opv);
			propName = opv.asOWLObjectProperty().getIRI().getFragment();
			propName = propName.replace("has", "");

			List<T> nObjects = new ArrayList<T>();

			getIndividualsRecursive(cls, newIndiv, nObjects);

			propName = WordUtils.uncapitalize(propName);
			Field field = getField(cls, propName);

			if (field != null) {
				if (field.getType().isInterface() && LIST_INTERFACE.equals(field.getType().toString())) {
					field.setAccessible(true);
					field.set(clsInstance, nObjects);
				} else {
					field.setAccessible(true);
					field.set(clsInstance, nObjects.get(0));
				}
			}
		}
	}

	/**
	 * @param cls
	 * @param individ
	 * @param clsInstance
	 * @throws IllegalAccessException
	 * @throws ParseException
	 * @throws IllegalArgumentException
	 */

	private <T> void populateDataPropertyFields(Class<?> cls, OWLIndividual individ, T clsInstance)
			throws IllegalAccessException, IllegalArgumentException, ParseException {
		String propName;
		// get data property values
		Map<OWLDataPropertyExpression, Set<OWLLiteral>> dataPropertyValues = individ.getDataPropertyValues(owlUtility
				.getOntology());

		for (OWLDataPropertyExpression dpv : dataPropertyValues.keySet()) {

			Set<OWLLiteral> literalSet = dataPropertyValues.get(dpv);
			Iterator<OWLLiteral> it = literalSet.iterator();

			propName = dpv.asOWLDataProperty().getIRI().getFragment();
			propName = propName.replace("has", "");
			propName = propName.replace("Component", "");
			propName = WordUtils.uncapitalize(propName);

			Field field = getField(cls, propName);

			if (field != null) {
				field.setAccessible(true);

				if (it.hasNext()) {
					OWLLiteral lit = it.next();
					if (field.getType().getSimpleName().equals(String.class.getSimpleName())) {
						field.set(clsInstance, lit.getLiteral());
					} else if (field.getType().getSimpleName().equals(Long.class.getSimpleName())) {
						field.set(clsInstance, Long.parseLong(lit.getLiteral()));
					} else if (field.getType().getSimpleName().equals(Integer.class.getSimpleName())) {
						field.set(clsInstance, Integer.parseInt(lit.getLiteral()));
					} else if (field.getType().getSimpleName().equals(Float.class.getSimpleName())) {
						field.set(clsInstance, Float.parseFloat(lit.getLiteral()));
					} else if (field.getType().getSimpleName().equals(Double.class.getSimpleName())) {
						field.set(clsInstance, Double.parseDouble(lit.getLiteral()));
					} else if (field.getType().getSimpleName().equals(Boolean.class.getSimpleName())) {
						field.set(clsInstance, lit.parseBoolean());
					} else if (field.getType().getSimpleName().equals(Date.class.getSimpleName())) {
						SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S a z");
						field.set(clsInstance, dt.parse(lit.getLiteral()));
					} else if (field.getType().getSimpleName().equals(long.class.getSimpleName())) {
						field.setLong(clsInstance, Long.parseLong(lit.getLiteral()));
					} else if (field.getType().getSimpleName().equals(double.class.getSimpleName())) {
						field.setDouble(clsInstance, lit.parseDouble());
					} else if (field.getType().getSimpleName().equals(int.class.getSimpleName())) {
						field.setInt(clsInstance, lit.parseInteger());
					} else if (field.getType().getSimpleName().equals(float.class.getSimpleName())) {
						field.setFloat(clsInstance, lit.parseFloat());
					} else if (field.getType().getSimpleName().equals(boolean.class.getSimpleName())) {
						field.setBoolean(clsInstance, lit.parseBoolean());
					}
				}
			}
		}
	}

	/**
	 * @param cls
	 * @param name
	 * @return
	 */

	private Field getField(Class<?> cls, String name) {
		Class<?> clazz = cls;
		while (clazz != null) {
			for (Field field : clazz.getDeclaredFields()) {
				if (field.getName().equals(name)) {
					return field;
				}
			}
			clazz = clazz.getSuperclass();
		}
		return null;
	}

	@Override
	public <T> void addIndividual(T t) {

		OntologyIndividual ontologyData = EntityReflectionParser.getOntologyData(t);

		OWLDataFactory factory = owlUtility.getFactory();
		OWLOntologyManager manager = owlUtility.getManager();
		OWLOntology ontology = owlUtility.getOntology();

		String className = ontologyData.getClassName();
		Long id = ontologyData.getId();

		OWLClass owlClass = factory.getOWLClass(IRI.create(OwlAPIUtility.OWL_URI + className));

		OWLNamedIndividual owlIndividual = factory.getOWLNamedIndividual(IRI.create(OwlAPIUtility.OWL_URI + className
				+ "_" + id));

		OWLClassAssertionAxiom classAssertion = factory.getOWLClassAssertionAxiom(owlClass, owlIndividual);

		manager.addAxiom(ontology, classAssertion);

		for (Map.Entry<String, Object> entry : ontologyData.getParams().entrySet()) {
			String dataProperty = entry.getKey();
			Object value = entry.getValue();

			OWLDataProperty property = factory.getOWLDataProperty(IRI.create(OwlAPIUtility.OWL_URI + dataProperty));
			OWLDataPropertyAssertionAxiom dataPropertyAssertionAxiom = null;
			if (value instanceof Double) {
				dataPropertyAssertionAxiom = factory.getOWLDataPropertyAssertionAxiom(property, owlIndividual,
						(Double) value);
			}
			if (value instanceof String) {
				dataPropertyAssertionAxiom = factory.getOWLDataPropertyAssertionAxiom(property, owlIndividual,
						(String) value);
			}
			if (value instanceof Integer) {
				dataPropertyAssertionAxiom = factory.getOWLDataPropertyAssertionAxiom(property, owlIndividual,
						(Integer) value);
			}
			if (value instanceof Long) {
				dataPropertyAssertionAxiom = factory.getOWLDataPropertyAssertionAxiom(property, owlIndividual,
						(String) value.toString());
			}
			if (value instanceof Boolean) {
				dataPropertyAssertionAxiom = factory.getOWLDataPropertyAssertionAxiom(property, owlIndividual,
						(Boolean) value);
			}
			if (value instanceof Float) {
				dataPropertyAssertionAxiom = factory.getOWLDataPropertyAssertionAxiom(property, owlIndividual,
						(Float) value);
			}
			if (value instanceof Date) {
				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S a z");
				String dateStringValue = dt.format((Date) value);
				OWLLiteral literal = new OWLLiteralImpl(dateStringValue, "", factory.getOWLDatatype(IRI
						.create("http://www.w3.org/2001/XMLSchema#dateTime")));

				dataPropertyAssertionAxiom = factory.getOWLDataPropertyAssertionAxiom(property, owlIndividual, literal);
			}
			manager.addAxiom(ontology, dataPropertyAssertionAxiom);
		}

		for (Map.Entry<String, List<RangeData>> entry : ontologyData.getForeignKeys().entrySet()) {

			OWLObjectProperty property = factory
					.getOWLObjectProperty(IRI.create(OwlAPIUtility.OWL_URI + entry.getKey()));

			for (RangeData rangeData : entry.getValue()) {

				String individualClassName = rangeData.getClassName();
				long individualId = rangeData.getId();

				OWLNamedIndividual subject = factory.getOWLNamedIndividual(IRI.create(OwlAPIUtility.OWL_URI
						+ individualClassName + "_" + individualId));

				OWLObjectPropertyAssertionAxiom objectPropertyAssertion = factory.getOWLObjectPropertyAssertionAxiom(
						property,  owlIndividual, subject);

				manager.addAxiom(ontology, objectPropertyAssertion);
			}

		}

		OWLDataProperty property = factory.getOWLDataProperty(IRI.create(OwlAPIUtility.OWL_URI + "hasId"));

		OWLLiteral literal = new OWLLiteralImpl(id.toString(), "", factory.getOWLDatatype(IRI
				.create("http://www.w3.org/2001/XMLSchema#long")));

		OWLDataPropertyAssertionAxiom dataPropertyAssertionAxiom = factory.getOWLDataPropertyAssertionAxiom(property,
				owlIndividual, literal);
		manager.addAxiom(ontology, dataPropertyAssertionAxiom);

	}

	@Override
	public <T> void updateIndividual(T t) {
		OntologyIndividual data = EntityReflectionParser.getOntologyData(t);
		Long id = data.getId();
		deleteIndividual(t.getClass(), id);
		addIndividual(t);
	}

	@Override
	public <T> void deleteIndividual(Class<T> cls, Long id) {

		OWLDataFactory factory = owlUtility.getFactory();
		OWLOntologyManager manager = owlUtility.getManager();
		OWLOntology ontology = owlUtility.getOntology();

		String className = cls.getSimpleName();

		OWLNamedIndividual owlIndividual = factory.getOWLNamedIndividual(IRI.create(OwlAPIUtility.OWL_URI + className
				+ "_" + id));

		OWLEntityRemover remover = new OWLEntityRemover(manager, Collections.singleton(ontology));
		remover.visit(owlIndividual);
		manager.applyChanges(remover.getChanges());

	}

}
