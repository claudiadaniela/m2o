package ro.tuc.dsrl.m2o.parser;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ro.tuc.dsrl.m2o.datamodel.FieldValueType;
import ro.tuc.dsrl.m2o.datamodel.ObjectPropertyValue;
import ro.tuc.dsrl.m2o.datamodel.OntologyClass;
import ro.tuc.dsrl.m2o.datamodel.OntologyIndividual;
import ro.tuc.dsrl.m2o.datamodel.OntologyIndividual.ObjectPropertyData;

public class EntityReflectionParser {
	private static final Log LOGGER = LogFactory
			.getLog(EntityReflectionParser.class);

	private EntityReflectionParser() {
	}

	public static OntologyIndividual getOntologyData(Object o) {
		if (o == null) {
			LOGGER.info("Null entity in EntityReflectionParser");
			return new OntologyIndividual("", (long) 0);
		}
		Class<?> clazz = o.getClass();
		String name = clazz.getSimpleName();
		OntologyIndividual data = new OntologyIndividual(name, (long) 0);
		while (clazz != null) {
			for (Field field : clazz.getDeclaredFields()) {
				field.setAccessible(true);
				if (AnnotationParser.parseOwlIgnoreField(field)) {
					continue;
				}

				ObjectPropertyData objectProperty = AnnotationParser
						.parseObjectProperty(field);
				if (objectProperty.getObjectProperty() == null) {
					FieldValueType found = FieldParser.parse(field,
							getFieldValue(field, o));
					data.addField(found);
				} else {
					List<ObjectPropertyValue> op = ObjectPropertyParser
							.parseCollectionForeignKey(getFieldValue(field, o),
									objectProperty);

					data.addObjectProperty(op);
				}

			}
			clazz = clazz.getSuperclass();
		}
		return data;
	}

	public static OntologyClass getOntologyClass(Class<?> clazz) {
		if (clazz == null) {
			LOGGER.info("Null class in EntityReflectionParser");
			return null;
		}

		String name = clazz.getSimpleName();
		OntologyClass ontologyClass = new OntologyClass(name);

		for (Field field : clazz.getDeclaredFields()) {
			field.setAccessible(true);
			if (AnnotationParser.parseOwlIgnoreField(field)) {
				continue;
			}

			ObjectPropertyData objectProperty = AnnotationParser
					.parseObjectProperty(field);
			if (objectProperty.getObjectProperty() == null) {
				FieldValueType found = FieldParser.parse(field, null);
				ontologyClass.addField(found);
			} else {
				List<ObjectPropertyValue> op = ObjectPropertyParser
						.parseCollectionForeignKey(null, objectProperty);

				ontologyClass.addObjectProperty(op);
			}

		}
		String superClazz = clazz.getSuperclass().getSimpleName();
		ontologyClass.setSuperClass(superClazz);
		return ontologyClass;
	}

	private static Object getFieldValue(Field field, Object o) {
		try {
			if (o != null) {
				return field.get(o);
			}
		} catch (IllegalArgumentException e) {
			LOGGER.error("", e);
		} catch (IllegalAccessException e) {
			LOGGER.error("", e);
		}
		return null;
	}

}
