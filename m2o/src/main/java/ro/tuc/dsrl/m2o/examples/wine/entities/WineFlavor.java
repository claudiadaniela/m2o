package ro.tuc.dsrl.m2o.examples.wine.entities;

import ro.tuc.dsrl.m2o.annotations.OntologyEntity;

@OntologyEntity
public class WineFlavor extends WineTaste {
	public WineFlavor() {
	}

	public WineFlavor(long id) {
		super(id);
	}
}
