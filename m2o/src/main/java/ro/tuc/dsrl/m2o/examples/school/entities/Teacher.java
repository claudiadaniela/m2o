package ro.tuc.dsrl.m2o.examples.school.entities;

import java.util.ArrayList;
import java.util.List;

import ro.tuc.dsrl.m2o.annotations.ObjectProperty;
import ro.tuc.dsrl.m2o.annotations.OntologyEntity;
import ro.tuc.dsrl.m2o.annotations.OntologyIgnore;
import ro.tuc.dsrl.m2o.examples.school.entities.Person;
import ro.tuc.dsrl.m2o.examples.school.entities.Subject;

/**
 * @Author: Technical University of Cluj-Napoca, Romania Distributed Systems
 * Research Laboratory, http://dsrl.coned.utcluj.ro/
 */
@OntologyEntity
public class Teacher extends Person {

    @OntologyIgnore
    private String address;
    private Boolean headMaster;
    @ObjectProperty(value = "hasStudents", range = Person.class)
    private List<Person> students;
    @ObjectProperty(value = "hasSubject", range = Subject.class)
    private Subject subject;

    public Teacher() {
    }

    public Teacher(Long id, String name, Integer age, String address, Boolean headMaster, Subject subject) {
        super(id, name, age);
        this.address = address;
        this.headMaster = headMaster;
        this.students = new ArrayList<Person>();
        this.subject = subject;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getHeadMaster() {
        return headMaster;
    }

    public void setHeadMaster(Boolean headMaster) {
        this.headMaster = headMaster;
    }

    public List<Person> getStudents() {
        return students;
    }

    public void setStudents(List<Person> students) {
        this.students = students;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

}
