package ro.tuc.dsrl.m2o.examples.school.entities;

import ro.tuc.dsrl.m2o.annotations.InstanceIdentifier;
import ro.tuc.dsrl.m2o.annotations.OntologyEntity;

/**
 * @Author: Technical University of Cluj-Napoca, Romania Distributed Systems
 * Research Laboratory, http://dsrl.coned.utcluj.ro/
 */
@OntologyEntity
public class Subject {

    @InstanceIdentifier
    private String id;
    private String name;

    public Subject() {
    }

    public Subject(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
