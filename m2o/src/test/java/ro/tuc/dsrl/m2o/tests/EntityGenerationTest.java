package ro.tuc.dsrl.m2o.tests;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import ro.tuc.dsrl.m2o.datamodel.OntologyClass;
import ro.tuc.dsrl.m2o.examples.school.entities.Subject;
import ro.tuc.dsrl.m2o.examples.school.entities.Person;
import ro.tuc.dsrl.m2o.examples.school.entities.Student;
import ro.tuc.dsrl.m2o.examples.school.entities.Teacher;
import ro.tuc.dsrl.m2o.parser.EntityReflectionParser;
import ro.tuc.dsrl.m2o.parser.OntologyClassGeneration;
import ro.tuc.dsrl.m2o.utility.PropertiesLoader;

public class EntityGenerationTest {
	private static final String PERSON = "Person";
	private static final String STUDENT = "Student";
	private static final String TEACHER = "Teacher";
	private static final String SUBJECT = "Subject";
	private static final String THING = "Thing";
	private static final String FINAL_GRADE = "hasFinalGrade";
	private static final String AGE = "hasAge";
	private static final String NAME = "hasName";
	private static final String BIRTHDATE = "hasBirthdate";
	private static final String STUDENTS_OP = "hasStudents";
	private static final String SUBJECT_OP = "hasSubject";
	private static final String ADDRESS = "hasAddress";
	private static final String MASTER = "hasHeadMaster";

	@BeforeClass
	public static void setup(){
		PropertiesLoader.setConfigFile("/src/test/resources/test-owl-config.config");
	}

	@Test
	public void testGeneration() {
		List<OntologyClass> classes = OntologyClassGeneration.generateOwlClasses();

		for (OntologyClass c : classes) {
			System.out.println(c);
		}
		assertEquals(" nr.", 4, classes.size());

	}

	@Test
	public void testPersonClass() {

		OntologyClass clazzData = EntityReflectionParser
				.getOntologyClass(Person.class);

		assertEquals("classname", PERSON, clazzData.getClassName());
		assertEquals("superclass", THING, clazzData.getSuperClass());

		// Fields
		assertEquals("Fields no", 2, clazzData.getFields().size());
		assertEquals("Field Age", true, clazzData.getFields().containsKey(AGE));
		assertEquals("Field Name", true, clazzData.getFields()
				.containsKey(NAME));

		// Object Prop
		assertEquals("Obj P no", 0, clazzData.getObjectProperties().size());

	}

	@Test
	public void testTeacherClass() {

		OntologyClass clazzData = EntityReflectionParser
				.getOntologyClass(Teacher.class);

		assertEquals("classname", TEACHER, clazzData.getClassName());
		assertEquals("superclass", PERSON, clazzData.getSuperClass());

		// Fields
		assertEquals("Fields no", 1, clazzData.getFields().size());
		assertEquals("Field Age", false, clazzData.getFields().containsKey(AGE));
		assertEquals("Field Name", false,
				clazzData.getFields().containsKey(NAME));
		assertEquals("Field Master", true, clazzData.getFields().containsKey(MASTER));
		assertEquals("Field Address", false,
				clazzData.getFields().containsKey(ADDRESS));

		// Object Prop
		assertEquals("Obj P no", 2, clazzData.getObjectProperties().size());
		assertEquals("OP Students", true, clazzData.getObjectProperties().containsKey(STUDENTS_OP));
		assertEquals("OP Director", true, clazzData.getObjectProperties().containsKey(SUBJECT_OP));

	}
	
	
	@Test
	public void testSubjectClass() {

		OntologyClass clazzData = EntityReflectionParser
				.getOntologyClass(Subject.class);

		assertEquals("classname", SUBJECT, clazzData.getClassName());
		assertEquals("superclass", THING, clazzData.getSuperClass());

		// Fields
		assertEquals("Fields no", 1, clazzData.getFields().size());
		assertEquals("Field Name", true,
				clazzData.getFields().containsKey(NAME));

		// Object Prop
		assertEquals("Obj P no", 0, clazzData.getObjectProperties().size());

	}
	
	@Test
	public void testStudentClass() {

		OntologyClass clazzData = EntityReflectionParser
				.getOntologyClass(Student.class);

		assertEquals("classname", STUDENT, clazzData.getClassName());
		assertEquals("superclass", PERSON, clazzData.getSuperClass());

		// Fields
		assertEquals("Fields no", 2, clazzData.getFields().size());
		assertEquals("Field Birthdate", true, clazzData.getFields().containsKey(BIRTHDATE));
		assertEquals("Field Final Grade", true, clazzData.getFields().containsKey(FINAL_GRADE));
		assertEquals("Field Age", false, clazzData.getFields().containsKey(AGE));
		assertEquals("Field Name", false,
				clazzData.getFields().containsKey(NAME));

		// Object Prop
		assertEquals("Obj P no", 0, clazzData.getObjectProperties().size());

	}
}
