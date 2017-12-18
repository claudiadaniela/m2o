package ro.tuc.dsrl.m2o.examples.wine.entities;

import ro.tuc.dsrl.m2o.annotations.InstanceIdentifier;
import ro.tuc.dsrl.m2o.annotations.OntologyEntity;

@OntologyEntity
public class PortableLiquid {
	@InstanceIdentifier
	private long id;

	public PortableLiquid() {
	}

	public PortableLiquid(long name) {
		super();
		this.id = name;
	}

	public long getName() {
		return id;
	}

	public void setName(long name) {
		this.id = name;
	}

}
