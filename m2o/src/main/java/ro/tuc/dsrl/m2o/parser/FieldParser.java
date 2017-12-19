package ro.tuc.dsrl.m2o.parser;

import java.lang.reflect.Field;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.text.WordUtils;

import ro.tuc.dsrl.m2o.datamodel.FieldValueType;

/**
 * @Author: Technical University of Cluj-Napoca, Romania Distributed Systems
 * Research Laboratory, http://dsrl.coned.utcluj.ro/
 */
public class FieldParser {
    private static final String SERIAL_VERSION = "serialVersionUID";
    private static final String STRING_CLASS = "java.lang.String";
    private static final String DATE_CLASS = "java.util.Date";

    private FieldParser() {
    }

    public static FieldValueType parse(Field field, Object value) {

        String name = field.getName();
        String classFullQualifiedName = field.getType().getName();

        if (SERIAL_VERSION.equals(name)) {
            return null;
        }

        if (AnnotationParser.parseInstanceIdField(field)) {
            return new FieldValueType(name, value, classFullQualifiedName, true);
        }

        boolean isPrimitiveOrWrapped = field.getType().isPrimitive()
                || ClassUtils.wrapperToPrimitive(field.getType()) != null;

        if (isPrimitiveOrWrapped || STRING_CLASS.equals(classFullQualifiedName)
                || DATE_CLASS.equals(classFullQualifiedName)) {
            name = WordUtils.capitalize(name);
            name = "has" + name;
            return new FieldValueType(name, value, classFullQualifiedName,
                    false);
        }

        return null;
    }

}
