package ro.tuc.dsrl.m2o.datamodel;

import java.util.List;

public interface OntologyModel {

	public void addField(FieldValueType field);

	void addObjectProperty(List<ObjectPropertyValue> op);
}
