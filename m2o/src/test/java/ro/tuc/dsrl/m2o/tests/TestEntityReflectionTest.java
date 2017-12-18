package ro.tuc.dsrl.m2o.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import ro.tuc.dsrl.m2o.datamodel.OntologyIndividual;
import ro.tuc.dsrl.m2o.examples.school.entities.Person;
import ro.tuc.dsrl.m2o.examples.school.entities.Student;
import ro.tuc.dsrl.m2o.examples.school.entities.Subject;
import ro.tuc.dsrl.m2o.examples.school.entities.Teacher;
import ro.tuc.dsrl.m2o.parser.EntityReflectionParser;
import ro.tuc.dsrl.m2o.utility.PropertiesLoader;

public class TestEntityReflectionTest {
	private static final String TEST_ENTITY = "Teacher";
	private static final String AGE = "hasAge";
	private static final String ADDRESS = "hasAddress";
	private static final String NAME = "hasName";
	private static final String MASTER = "hasHeadMaster";
	private static final int NO_FIELDS_TEACHER = 3;

	@BeforeClass
	public static void setup(){
		PropertiesLoader.setConfigFile("/src/test/resources/test-owl-config.config");
	}
	@Test
	public void testInsertTeacher() {
		Subject subject = new Subject(13L, "subject");
		Teacher entity = new Teacher(1L, "teacher", 35, "Cluj", false, subject);

		List<Person> students = new ArrayList<Person>();
		students.add(new Student(0L, "ion", 20, "mail2", 5.90, new Date()));
		students.add(new Student(1L, "maria", 21, "mail1", 6.90, new Date()));

		entity.setStudents(students);

		OntologyIndividual data = EntityReflectionParser.getOntologyData(entity);
		System.out.println(data);
		assertTrue(" id", entity.getId() == (long) data.getId());
		assertEquals(" class", TEST_ENTITY, data.getClassName());
		assertEquals("Field name", true, data.getParams().containsKey(NAME));
		assertEquals("Field age", true, data.getParams().containsKey(AGE));
		assertEquals("Field address", false, data.getParams().containsKey(ADDRESS));
		assertEquals("Field master", true, data.getParams().containsKey(MASTER));

		assertEquals("Field Number", NO_FIELDS_TEACHER, data.getParams().size());

		assertEquals("FK Number", 2, data.getForeignKeys().size());

	}

	@Test
	public void testInsertDirector() {
		Subject entity = new Subject(13L, "mainDirector");
		OntologyIndividual data = EntityReflectionParser.getOntologyData(entity);
		System.out.println(data);
	}

}
