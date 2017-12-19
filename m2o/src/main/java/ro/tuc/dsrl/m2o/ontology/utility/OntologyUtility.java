package ro.tuc.dsrl.m2o.ontology.utility;

import java.util.Date;

/**
 * @Author: Technical University of Cluj-Napoca, Romania Distributed Systems
 * Research Laboratory, http://dsrl.coned.utcluj.ro/
 */
public interface OntologyUtility {

    public void saveOntology();

    public void saveSnapshot(Date date);

    public void reset();

}
