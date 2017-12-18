package ro.tuc.dsrl.m2o.examples.school.entities;

import ro.tuc.dsrl.m2o.annotations.InstanceIdentifier;
import ro.tuc.dsrl.m2o.annotations.OntologyEntity;

@OntologyEntity
public class Subject {

	@InstanceIdentifier
	private Long id;
	private String name;

	public Subject() {
	}

	public Subject(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
