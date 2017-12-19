package ro.tuc.dsrl.m2o.examples.school;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ro.tuc.dsrl.m2o.examples.school.entities.Student;
import ro.tuc.dsrl.m2o.examples.school.entities.Subject;
import ro.tuc.dsrl.m2o.examples.school.repos.StudentRepository;
import ro.tuc.dsrl.m2o.examples.school.repos.SubjectRepository;
import ro.tuc.dsrl.m2o.ontology.utility.OntologyUtilityFactory;

/**
 * @Author: Technical University of Cluj-Napoca, Romania Distributed Systems
 * Research Laboratory, http://dsrl.coned.utcluj.ro/
 */
public class Start {

    private static final Log LOGGER = LogFactory.getLog(Start.class);

    private Start() {
    }

    public static void main(String[] args) {

        // TEST CRUD - DIRECTOR
        SubjectRepository dRepo = new SubjectRepository();

        // FIND ALL
        List<Subject> subjects = dRepo.findAll();
        LOGGER.info(" First size: " + subjects.size());

        // CREATE
        Subject subject = new Subject("m", "Math");
        dRepo.create(subject);
        subjects = dRepo.findAll();
        LOGGER.info(" Created size: " + subjects.size());

        // DELETE
        dRepo.delete("mt");
        subjects = dRepo.findAll();
        LOGGER.info(" Deleted size: " + subjects.size());

        // UPDATE
        subject = dRepo.findByIdentifier("m");
        subject.setName("Mathematics");
        dRepo.update(subject);

        // TEST - STUDENT
        StudentRepository sRepo = new StudentRepository();

        // FIND ALL
        List<Student> students = sRepo.findAll();
        LOGGER.info(" First size: " + students.size());

        // CREATE
        Student student = new Student(10L, "Pop Andrei", 24, "pop@gmail.com", 7.0, new Date());
        sRepo.create(student);
        students = sRepo.findAll();
        LOGGER.info(" Created size: " + students.size());

        // SAVE A SNAPSHOT
        OntologyUtilityFactory.getInstance().saveSnapshot(new Date());

    }

}
