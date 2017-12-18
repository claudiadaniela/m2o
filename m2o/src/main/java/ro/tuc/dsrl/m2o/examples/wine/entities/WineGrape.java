package ro.tuc.dsrl.m2o.examples.wine.entities;

import ro.tuc.dsrl.m2o.annotations.OntologyEntity;

@OntologyEntity
public class WineGrape extends Grape {

	public WineGrape() {
	}

	public WineGrape(long id) {
		super(id);
	}
}
