package ro.tuc.dsrl.m2o.datamodel;

/**
 * @Author: Technical University of Cluj-Napoca, Romania Distributed Systems
 * Research Laboratory, http://dsrl.coned.utcluj.ro/
 */
public class FieldValueType {

    private String field;
    private Object value;
    private String type;
    private boolean id;

    public FieldValueType(String field, Object value, String type, boolean id) {
        this.field = field;
        this.value = value;
        this.type = type;
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isId() {
        return id;
    }

    public void setId(boolean id) {
        this.id = id;
    }

}
