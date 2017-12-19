package ro.tuc.dsrl.m2o.tests;

import org.junit.BeforeClass;
import org.junit.Test;


import ro.tuc.dsrl.m2o.examples.school.entities.Student;
import ro.tuc.dsrl.m2o.examples.school.repos.StudentRepository;
import ro.tuc.dsrl.m2o.examples.school.entities.Subject;
import ro.tuc.dsrl.m2o.examples.school.repos.SubjectRepository;
import ro.tuc.dsrl.m2o.utility.PropertiesLoader;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class CRUDTests {
    @BeforeClass
    public static void setup(){
        PropertiesLoader.setConfigFile("/src/test/resources/test-owl-config.config");
    }

    @Test
    public void crudSubject(){
        // TEST CRUD - DIRECTOR
        SubjectRepository dRepo = new SubjectRepository();

        // FIND ALL
        List<Subject> subjects = dRepo.findAll();
        assertEquals("Subject size before", 0, subjects.size());

        // CREATE
        Subject subject = new Subject("m", "Math");
        dRepo.create(subject);
        subjects = dRepo.findAll();
        assertEquals("Subject size after insert", 1, subjects.size());
        assertEquals("Subject  after insert", "Math", subjects.get(0).getName());

        // DELETE
        dRepo.delete("mt");
        subjects = dRepo.findAll();
        assertEquals("Subject size after delete", 1, subjects.size());

        // UPDATE
        subject = dRepo.findByIdentifier("m");
        subject.setName("Mathematics");
        dRepo.update(subject);

        subjects = dRepo.findAll();
        assertEquals("Subject size after update", 1, subjects.size());
        assertEquals("Subject  after update", "Mathematics", subjects.get(0).getName());

        // DELETE
        dRepo.delete("m");
        subjects = dRepo.findAll();
        assertEquals("Subject size after delete", 0, subjects.size());
    }


    @Test
    public void crudStudent(){
        // TEST CRUD - DIRECTOR
        StudentRepository dRepo = new StudentRepository();

        // FIND ALL
        List<Student> students = dRepo.findAll();
        assertEquals("Subject size before", 0, students.size());

        Date birthdate = new Date(2000, 12, 12);
        // CREATE
        Student student = new Student(3l, "Alex", 17, "alex@mail.com", 9.32, birthdate);
        dRepo.create(student);
        students = dRepo.findAll();
        assertEquals("students size after insert", 1, students.size());
        assertEquals("students  after insert", "Alex", students.get(0).getName());
        assertEquals("students  after insert - field ingored", null, students.get(0).getEmail());
        assertEquals("students  after insert", birthdate, students.get(0).getBirthdate());
        assertEquals("students  after insert", new Integer(17), students.get(0).getAge());


        // DELETE
        dRepo.delete(1L);
        students = dRepo.findAll();
        assertEquals("students size after delete", 1, students.size());

        // UPDATE
        student = dRepo.findByIdentifier(3L);
        student.setName("Filip");
        dRepo.update(student);

        students = dRepo.findAll();
        assertEquals("students size after update", 1, students.size());
        assertEquals("students  after update", "Filip", students.get(0).getName());

        // DELETE
        dRepo.delete(3L);
        students = dRepo.findAll();
        assertEquals("students size after delete", 0, students.size());
    }
}
