package ro.tuc.dsrl.m2o.datamodel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @Author: Technical University of Cluj-Napoca, Romania Distributed Systems
 * Research Laboratory, http://dsrl.coned.utcluj.ro/
 */
public class OntologyClass implements OntologyModel {

    private static final String THING = "Thing";
    private static final String OBJECT = "Object";
    private String className;
    private String superClass;
    private Map<String, String> fields;
    private Map<String, String> objectProperties;

    public OntologyClass(String className) {
        this.className = className;
        this.fields = new HashMap<String, String>();
        this.objectProperties = new HashMap<String, String>();
    }

    public String getClassName() {
        return className;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public Map<String, String> getObjectProperties() {
        return objectProperties;
    }

    public String getSuperClass() {
        return superClass;
    }

    public void setSuperClass(String superClass) {
        if (OBJECT.equals(superClass)) {
            this.superClass = THING;
            return;
        }
        this.superClass = superClass;
    }

    @Override
    public void addField(FieldValueType field) {
        if (field == null || field.isId()) {
            return;
        }
        fields.put(field.getField(), field.getType());

    }

    @Override
    public void addObjectProperty(List<ObjectPropertyValue> op) {
        if (op == null) {
            return;
        }
        for (ObjectPropertyValue value : op) {
            objectProperties.put(value.getObjectProeperty(), value.getValue()
                    .getClassName());
        }
    }

    @Override
    public String toString() {

        StringBuilder paramString = new StringBuilder();
        for (Entry<String, String> param : fields.entrySet()) {
            paramString.append("\n		");
            paramString.append(param.getKey() + " = " + param.getValue());

        }

        StringBuilder fkString = new StringBuilder();
        for (Entry<String, String> fk : objectProperties.entrySet()) {
            fkString.append("\n		");
            fkString.append(fk.getKey() + " = " + fk.getValue());

        }

        return "\n	ClassName: " + className + "\n	Super Class: " + superClass
                + "\n" + "	Fields: " + paramString.toString() + "\n"
                + "	Object Properties: " + fkString.toString() + "\n";
    }

}
