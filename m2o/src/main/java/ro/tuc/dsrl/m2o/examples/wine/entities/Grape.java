package ro.tuc.dsrl.m2o.examples.wine.entities;

import ro.tuc.dsrl.m2o.annotations.InstanceIdentifier;
import ro.tuc.dsrl.m2o.annotations.OntologyEntity;

@OntologyEntity
public class Grape {
	@InstanceIdentifier
	private long id;

	public Grape() {
	}

	public Grape(long id) {
		super();
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
