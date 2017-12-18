package ro.tuc.dsrl.m2o.datamodel;

import ro.tuc.dsrl.m2o.datamodel.OntologyIndividual.RangeData;

public class ObjectPropertyValue {
	private String objectProeperty;
	private RangeData value;

	public ObjectPropertyValue(String objectProeperty, RangeData value) {
		this.objectProeperty = objectProeperty;
		this.value = value;
	}

	public String getObjectProeperty() {
		return objectProeperty;
	}

	public void setObjectProeperty(String objectProeperty) {
		this.objectProeperty = objectProeperty;
	}

	public RangeData getValue() {
		return value;
	}

	public void setValue(RangeData value) {
		this.value = value;
	}

}
