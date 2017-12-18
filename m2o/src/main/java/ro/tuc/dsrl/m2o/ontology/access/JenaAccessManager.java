package ro.tuc.dsrl.m2o.ontology.access;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ro.tuc.dsrl.m2o.datamodel.OntologyIndividual;
import ro.tuc.dsrl.m2o.datamodel.OntologyIndividual.RangeData;
import ro.tuc.dsrl.m2o.ontology.utility.JenaUtility;
import ro.tuc.dsrl.m2o.ontology.utility.OntologyUtilityFactory;
import ro.tuc.dsrl.m2o.parser.EntityReflectionParser;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class JenaAccessManager implements OntologyAccessManager {

	private static final Log LOGGER = LogFactory.getLog(JenaAccessManager.class);

	private static final String LIST_INTERFACE = "interface java.util.List";
	private JenaUtility jenaUtility;
	private static volatile JenaAccessManager instance;

	private JenaAccessManager() {
		jenaUtility = (JenaUtility) OntologyUtilityFactory.getInstance();
	}

	static JenaAccessManager getInstance() {
		if (instance == null) {
			synchronized (JenaAccessManager.class) {
				if (instance == null) {
					instance = new JenaAccessManager();
				}
			}
		}
		return instance;
	}

	@Override
	public <T> void addIndividual(T t) {
		OntologyIndividual data = EntityReflectionParser.getOntologyData(t);

		String className = data.getClassName();
		Long id = data.getId();

		OntClass ontClass = jenaUtility.getOntModel().getOntClass(JenaUtility.OWL_URI + className);
		Individual individual = jenaUtility.getOntModel().createIndividual(JenaUtility.OWL_URI + className + "_" + id,
				ontClass);

		for (Map.Entry<String, Object> entry : data.getParams().entrySet()) {
			String dataProperty = entry.getKey();
			Object value = entry.getValue();
			DatatypeProperty datatypeProperty = jenaUtility.getOntModel().getDatatypeProperty(
					JenaUtility.OWL_URI + dataProperty);
			if (value instanceof Double) {
				individual.addProperty(datatypeProperty, value.toString(), XSDDatatype.XSDdouble);
			}
			if (value instanceof String) {
				individual.addProperty(datatypeProperty, value.toString(), XSDDatatype.XSDstring);
			}
			if (value instanceof Integer) {
				individual.addProperty(datatypeProperty, value.toString(), XSDDatatype.XSDint);
			}
			if (value instanceof Long) {
				individual.addProperty(datatypeProperty, value.toString(), XSDDatatype.XSDlong);
			}
			if (value instanceof Boolean) {
				individual.addProperty(datatypeProperty, value.toString(), XSDDatatype.XSDboolean);
			}
			if (value instanceof Float) {
				individual.addProperty(datatypeProperty, value.toString(), XSDDatatype.XSDfloat);
			}
			if (value instanceof Date) {
				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S a z");
				String dateStringValue = dt.format((Date) value);
				individual.addProperty(datatypeProperty, dateStringValue, XSDDatatype.XSDdateTime);
			}
		}

		for (Map.Entry<String, List<RangeData>> entry : data.getForeignKeys().entrySet()) {

			String objectPropertyName = entry.getKey();

			ObjectProperty objectProperty = jenaUtility.getOntModel().getObjectProperty(
					JenaUtility.OWL_URI + objectPropertyName);

			for (RangeData rangeData : entry.getValue()) {
				String individualClassName = rangeData.getClassName();
				long individualId = rangeData.getId();

				Individual subject = jenaUtility.getOntModel().getIndividual(
						JenaUtility.OWL_URI + individualClassName + "_" + individualId);

				individual.addProperty(objectProperty, subject);
			}

		}

		// insert the id

		DatatypeProperty hasId = jenaUtility.getOntModel().getDatatypeProperty(JenaUtility.OWL_URI + "hasId");

		individual.addProperty(hasId, id.toString(), XSDDatatype.XSDlong);

	}

	@Override
	public <T> void deleteIndividual(Class<T> cls, Long id) {
		String className = cls.getSimpleName();

		OntClass ontClass = jenaUtility.getOntModel().getOntClass(JenaUtility.OWL_URI + className);

		Individual individual = jenaUtility.getOntModel().createIndividual(JenaUtility.OWL_URI + className + "_" + id,
				ontClass);
		individual.remove();
	}

	@Override
	public <T> void updateIndividual(T t) {
		OntologyIndividual data = EntityReflectionParser.getOntologyData(t);
		Long id = data.getId();
		deleteIndividual(t.getClass(), id);
		addIndividual(t);
	}

	@Override
	public <T> List<T> getIndividuals(Class<T> cls) {
		OntClass ontClass = jenaUtility.getOntModel().getOntClass(JenaUtility.OWL_URI + cls.getSimpleName());
		List<Individual> individuals = new ArrayList<Individual>();
		ExtendedIterator<Individual> iterator = jenaUtility.getOntModel().listIndividuals(ontClass);

		while (iterator.hasNext()) {
			Individual individual = iterator.next();
			individuals.add(individual);
		}

		List<T> objects = new ArrayList<T>();

		try {
			getIndividualsRecursive(cls, individuals, objects);
		} catch (ClassNotFoundException e) {
			LOGGER.error("", e);
		} catch (InstantiationException e) {
			LOGGER.error("", e);
		} catch (IllegalAccessException e) {
			LOGGER.error("", e);
		}

		return objects;
	}

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

	private <T> void getIndividualsRecursive(Class<?> cls, List<Individual> individuals, List<T> objects)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		if (!individuals.isEmpty()) {
			for (Individual individual : individuals) {
				Class<?> c = cls;
				String className = individual.getURI().replace(JenaUtility.OWL_URI, "");
				className = className.replaceAll("\\_{1,2}[0-9]*", "");

				if (!cls.getSimpleName().equals(className)) {
					c = Class.forName(cls.getPackage().getName() + "." + className);
				}

				@SuppressWarnings("unchecked")
				T clsInstance = (T) c.newInstance();
				populateDataPropertyFields(c, individual, clsInstance);
				populateObjectPropertyFields(c, individual, clsInstance);
				objects.add(clsInstance);
			}
		}
	}

	public <T> void populateObjectPropertyFields(Class<?> cls, Individual individ, T clsInstance)
			throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		String propName;

		ExtendedIterator<ObjectProperty> it = jenaUtility.getOntModel().listObjectProperties();

		while (it.hasNext()) {
			ObjectProperty op = it.next();
			if (individ.getProperty(op) != null) {
				propName = op.getURI().replace(JenaUtility.OWL_URI, "");
				propName = propName.replace("has", "");
				propName = WordUtils.uncapitalize(propName);

				List<T> nObjects = new ArrayList<T>();

				List<Individual> newIndiv = new ArrayList<Individual>();

				NodeIterator iterator = jenaUtility.getOntModel().listObjectsOfProperty(individ, op);

				while (iterator.hasNext()) {
					Individual individual = jenaUtility.getOntModel().getIndividual(iterator.next().toString());
					newIndiv.add(individual);
				}
				getIndividualsRecursive(cls, newIndiv, nObjects);
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

	}

	private <T> void populateDataPropertyFields(Class<?> cls, Individual individ, T clsInstance)
			throws IllegalArgumentException, IllegalAccessException {
		String propName;

		ExtendedIterator<DatatypeProperty> it = jenaUtility.getOntModel().listDatatypeProperties();

		while (it.hasNext()) {
			DatatypeProperty dp = it.next();
			if (individ.getProperty(dp) != null) {
				propName = dp.getURI().replace(JenaUtility.OWL_URI, "");
				propName = propName.replace("has", "");
				propName = WordUtils.uncapitalize(propName);

				Field field = getField(cls, propName);

				if (field != null) {
					field.setAccessible(true);
					Literal literal = individ.getProperty(dp).getLiteral();

					if (field.getType().getSimpleName().equals(String.class.getSimpleName())) {
						field.set(clsInstance, literal.getString());
					} else if (field.getType().getSimpleName().equals(Long.class.getSimpleName())) {
						field.set(clsInstance, literal.getLong());
					} else if (field.getType().getSimpleName().equals(Integer.class.getSimpleName())) {
						field.set(clsInstance, literal.getInt());
					} else if (field.getType().getSimpleName().equals(Float.class.getSimpleName())) {
						field.set(clsInstance, literal.getFloat());
					} else if (field.getType().getSimpleName().equals(Double.class.getSimpleName())) {
						field.set(clsInstance, literal.getDouble());
					} else if (field.getType().getSimpleName().equals(Boolean.class.getSimpleName())) {
						field.set(clsInstance, literal.getBoolean());
					} else if (field.getType().getSimpleName().equals(Date.class.getSimpleName())) {
						SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S a z");
						try {
							field.set(clsInstance, dt.parse(literal.getString()));
						} catch (ParseException e) {
							LOGGER.error(e);
						}
					} else if (field.getType().getSimpleName().equals(long.class.getSimpleName())) {
						field.setLong(clsInstance, literal.getLong());
					} else if (field.getType().getSimpleName().equals(double.class.getSimpleName())) {
						field.setDouble(clsInstance, literal.getDouble());
					} else if (field.getType().getSimpleName().equals(int.class.getSimpleName())) {
						field.setInt(clsInstance, literal.getInt());
					} else if (field.getType().getSimpleName().equals(float.class.getSimpleName())) {
						field.setFloat(clsInstance, literal.getFloat());
					} else if (field.getType().getSimpleName().equals(boolean.class.getSimpleName())) {
						field.setBoolean(clsInstance, literal.getBoolean());
					}

				}

			}

		}

	}

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

	public JenaUtility getOntologyJenaUtility() {
		return jenaUtility;
	}

	public void setOntologyJenaUtility(JenaUtility ontologyJenaUtility) {
		this.jenaUtility = ontologyJenaUtility;
	}

}
