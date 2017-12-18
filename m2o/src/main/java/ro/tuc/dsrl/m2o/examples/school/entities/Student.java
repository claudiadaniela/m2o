package ro.tuc.dsrl.m2o.examples.school.entities;

import java.util.Date;

import ro.tuc.dsrl.m2o.annotations.OntologyEntity;
import ro.tuc.dsrl.m2o.annotations.OntologyIgnore;
import ro.tuc.dsrl.m2o.examples.school.entities.Person;

@OntologyEntity
public class Student extends Person {

	@OntologyIgnore
	private String email;
	private double finalGrade;
	private Date birthdate;

	public Student() {
	}

	public Student(Long id, String name, Integer age, String email, double finalGrade, Date birthdate) {
		super(id, name, age);
		this.email = email;
		this.finalGrade = finalGrade;
		this.birthdate = birthdate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public double getFinalGrade() {
		return finalGrade;
	}

	public void setFinalGrade(double finalGrade) {
		this.finalGrade = finalGrade;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

}
