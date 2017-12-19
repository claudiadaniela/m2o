package ro.tuc.dsrl.m2o.ontology.access;

import java.lang.reflect.ParameterizedType;
import java.util.List;

@SuppressWarnings("unchecked")
public class OntologyRepository<T, V> {
	private Class<T> persistentClass;
	private Class<V> identifierClass;
	private OntologyAccessManager accsessManager;

	public OntologyRepository() {

		this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		this.identifierClass = (Class<V>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[1];
		accsessManager = OntologyAccessManagerFactory.getInstance();
	}

	public T findByIdentifier(V id) {
		return accsessManager.getIndividual(persistentClass, id);
	}

	public List<T> findAll() {
		return accsessManager.getIndividuals(persistentClass);
	}

	public void create(T t) {
		accsessManager.addIndividual(t);
	}

	public void update(T t) {
		accsessManager.updateIndividual(t);
	}

	public void delete(V id) {
		accsessManager.deleteIndividual(persistentClass, id);
	}
}
