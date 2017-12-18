package ro.tuc.dsrl.m2o.examples.wine.entities;

import ro.tuc.dsrl.m2o.annotations.InstanceIdentifier;
import ro.tuc.dsrl.m2o.annotations.OntologyEntity;

@OntologyEntity
public class Winery {
	@InstanceIdentifier
	private long id;
	
	public Winery() {
	}

	public Winery(long id) {
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
