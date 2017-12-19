package ro.tuc.dsrl.m2o.examples.school.entities;

import ro.tuc.dsrl.m2o.annotations.InstanceIdentifier;
import ro.tuc.dsrl.m2o.annotations.OntologyEntity;

/**
 * @Author: Technical University of Cluj-Napoca, Romania Distributed Systems
 * Research Laboratory, http://dsrl.coned.utcluj.ro/
 */
@OntologyEntity
public class Person {

    @InstanceIdentifier
    private Long id;
    private String name;
    private Integer age;

    public Person() {
    }

    public Person(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
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

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

}
