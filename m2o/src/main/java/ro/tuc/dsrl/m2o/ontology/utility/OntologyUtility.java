package ro.tuc.dsrl.m2o.ontology.utility;

import java.util.Date;

public interface OntologyUtility {

	public void saveOntology();

	public void saveSnapshot(Date date);

	public void reset();

}
