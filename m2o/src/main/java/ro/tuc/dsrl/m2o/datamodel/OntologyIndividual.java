package ro.tuc.dsrl.m2o.datamodel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @Author: Technical University of Cluj-Napoca, Romania Distributed Systems
 * Research Laboratory, http://dsrl.coned.utcluj.ro/
 */
public class OntologyIndividual implements OntologyModel {
    private String className;
    private Object id;
    private Map<String, Object> params;
    private Map<String, List<RangeData>> foreignKeys;

    public OntologyIndividual(String className, Object id) {
        this.className = className;
        this.id = id;
        this.params = new HashMap<String, Object>();
        this.foreignKeys = new HashMap<String, List<RangeData>>();
    }

    @Override
    public void addField(FieldValueType data) {
        if (data == null || data.getValue() == null) {
            return;
        }
        if (!data.isId()) {
            params.put(data.getField(), data.getValue());
        } else {

            id = data.getValue();
        }

    }

    @Override
    public void addObjectProperty(List<ObjectPropertyValue> op) {
        if (op == null) {
            return;
        }
        for (ObjectPropertyValue value : op) {
            if (value.getValue() != null && value.getValue().getId() != null) {
                putObjectPropertyValue(value);
            }
        }
    }

    private void putObjectPropertyValue(ObjectPropertyValue op) {
        List<RangeData> domains;
        if (foreignKeys.containsKey(op.getObjectProeperty())) {
            domains = foreignKeys.get(op.getObjectProeperty());
        } else {
            domains = new ArrayList<OntologyIndividual.RangeData>();
            foreignKeys.put(op.getObjectProeperty(), domains);
        }
        domains.add(op.getValue());

    }

    public String getClassName() {
        return className;
    }

    public Object getId() {
        return id;
    }

    public void setId(Object id) {
        this.id = id;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public Map<String, List<RangeData>> getForeignKeys() {
        return foreignKeys;
    }

    @Override
    public String toString() {

        StringBuilder paramString = new StringBuilder();
        for (Entry<String, Object> param : params.entrySet()) {
            paramString.append("\n		");
            paramString.append(param.getKey() + " = " + param.getValue());

        }

        StringBuilder fkString = new StringBuilder();
        for (Entry<String, List<RangeData>> fk : foreignKeys.entrySet()) {
            for (RangeData d : fk.getValue()) {
                fkString.append("\n		");
                fkString.append(fk.getKey() + " = " + d.getClassName()
                        + "  " + d.getId());
            }

        }

        return "\n	ClassName: " + className + "\n" + "	Id: " + id + "\n"
                + "	Params: " + paramString.toString() + "\n"
                + "	ForeignKeys: " + fkString.toString();
    }

    public static class RangeData {
        private String className;
        private Object id;

        public RangeData(String className, Object id) {
            this.className = className;
            this.id = id;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }
    }

    public static class ObjectPropertyData {
        private String objectProperty;
        private Class<?> range;

        public String getObjectProperty() {
            return objectProperty;
        }

        public void setObjectProperty(String objectProperty) {
            this.objectProperty = objectProperty;
        }

        public Class<?> getRange() {
            return range;
        }

        public void setRange(Class<?> range) {
            this.range = range;
        }

    }

}
