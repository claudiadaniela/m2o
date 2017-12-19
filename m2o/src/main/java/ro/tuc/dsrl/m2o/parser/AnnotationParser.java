package ro.tuc.dsrl.m2o.parser;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ro.tuc.dsrl.m2o.annotations.InstanceIdentifier;
import ro.tuc.dsrl.m2o.datamodel.OntologyIndividual.ObjectPropertyData;

public class AnnotationParser {

	private static final Log LOGGER = LogFactory.getLog(AnnotationParser.class);

	private static final String VALUE = "value";
	private static final String RANGE = "range";

	private AnnotationParser() {
	}

	public static ObjectPropertyData parseObjectProperty(Field field) {
		final String dynamicProp = "ObjectProperty";
		ObjectPropertyData data = new ObjectPropertyData();
		for (Annotation annotation : field.getDeclaredAnnotations()) {
			Class<? extends Annotation> type = annotation.annotationType();
			if (dynamicProp.equals(type.getSimpleName())) {
				for (Method method : type.getDeclaredMethods()) {

					try {
						if (VALUE.equals(method.getName())) {
							data.setObjectProperty((String) method.invoke(
									annotation, (Object[]) null));
						}

						if (RANGE.equals(method.getName())) {
							data.setRange((Class<?>) method.invoke(annotation,
									(Object[]) null));
						}
					} catch (IllegalAccessException e) {
						LOGGER.error("", e);
					} catch (IllegalArgumentException e) {
						LOGGER.error("", e);
					} catch (InvocationTargetException e) {
						LOGGER.error("", e);
					}
				}
			}
		}
		return data;
	}

	public static boolean parseInstanceIdField(Field field) {
		final String instanceId = "InstanceIdentifier";
		Class<?> value = field.getType();

		for (Annotation annotation : field.getDeclaredAnnotations()) {
			Class<? extends Annotation> type = annotation.annotationType();
			if (instanceId.equals(type.getSimpleName())) {
				return true;
			}
		}
		return false;
	}

	public static boolean parseOwlIgnoreField(Field field) {
		final String owlIgnore = "OntologyIgnore";
		for (Annotation annotation : field.getDeclaredAnnotations()) {
			Class<? extends Annotation> type = annotation.annotationType();
			if (owlIgnore.equals(type.getSimpleName())) {
				return true;
			}
		}
		return false;
	}
}
