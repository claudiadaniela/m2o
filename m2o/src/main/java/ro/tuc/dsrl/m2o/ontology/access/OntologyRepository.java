package ro.tuc.dsrl.m2o.ontology.access;

import java.lang.reflect.ParameterizedType;
import java.util.List;

@SuppressWarnings("unchecked")
public class OntologyRepository<T> {
	private Class<T> persistentClass;
	private OntologyAccessManager accsessManager;

	public OntologyRepository() {

		this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		accsessManager = OntologyAccessManagerFactory.getInstance();
	}

	public T findByIdentifier(Long i) {
		return accsessManager.getIndividual(persistentClass, i);
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

	public void delete(Long id) {
		accsessManager.deleteIndividual(persistentClass, id);
	}
}
