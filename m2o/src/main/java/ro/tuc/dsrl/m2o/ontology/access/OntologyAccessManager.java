package ro.tuc.dsrl.m2o.ontology.access;

import java.util.List;

public interface OntologyAccessManager {
	
	public <T> void addIndividual(T t);

	public <T> void updateIndividual(T t);

	<T> List<T> getIndividuals(Class<T> cls);

	<T,V> T getIndividual(Class<T> cls, V  id);
	
	public <T,V> void deleteIndividual(Class<T> cls, V id);
}
