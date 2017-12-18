package ro.tuc.dsrl.m2o.ontology.access;

import java.util.List;

public interface OntologyAccessManager {
	
	public <T> void addIndividual(T t);

	public <T> void updateIndividual(T t);

	<T> List<T> getIndividuals(Class<T> cls);

	<T> T getIndividual(Class<T> cls, Long id);
	
	public <T> void deleteIndividual(Class<T> cls, Long id);
}
