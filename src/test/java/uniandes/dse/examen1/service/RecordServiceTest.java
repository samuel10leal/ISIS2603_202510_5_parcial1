package uniandes.dse.examen1.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.exceptions.RepeatedCourseException;
import uniandes.dse.examen1.exceptions.RepeatedStudentException;
import uniandes.dse.examen1.exceptions.InvalidRecordException;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.services.CourseService;
import uniandes.dse.examen1.services.StudentService;
import uniandes.dse.examen1.services.RecordService;

@DataJpaTest
@Transactional
@Import({ RecordService.class, CourseService.class, StudentService.class })
public class RecordServiceTest {

    @Autowired
    private RecordService recordService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    private PodamFactory factory = new PodamFactoryImpl();

    private String login;
    private String courseCode;

    @BeforeEach
    void setUp() throws RepeatedCourseException, RepeatedStudentException {
        CourseEntity newCourse = factory.manufacturePojo(CourseEntity.class);
        newCourse = courseService.createCourse(newCourse);
        courseCode = newCourse.getCourseCode();

        StudentEntity newStudent = factory.manufacturePojo(StudentEntity.class);
        newStudent = studentService.createStudent(newStudent);
        login = newStudent.getLogin();
    }

    @Test
void testCreateRecord() {
    try {
        RecordEntity record = recordService.createRecord(login, courseCode, 4.0, "2025-1");
        assertEquals(4.0, record.getFinalGrade(), "Debe tener la misma nota");
    } catch (InvalidRecordException e) {
        fail("No deberia lanzarse ninguna excepcion " + e.getMessage());
    }
}

@Test
void testCreateRecordMissingStudent() {
    try {
        recordService.createRecord("login incorrecto", courseCode, 4.0, "2025-1");
        fail("Deberia haber una excepcion");
    } catch (InvalidRecordException e) {
        assertEquals("No existe el estudiante", e.getMessage(), "El mensaje de la excepcion no es correcto");
    }
}

@Test
void testCreateInscripcionMissingCourse() {
    try {
        recordService.createRecord(login, "curso incorrecto", 4.0, "2025-1");
        fail("Deberia haber una excepcion");
    } catch (InvalidRecordException e) {
        assertEquals("No existe el curso", e.getMessage(), "El mensaje de la excepcion no es correcto");
    }
}

@Test
void testCreateInscripcionWrongGrade() {
    try {
        recordService.createRecord(login, courseCode, 1.0, "2025-1"); // Nota inválida
        fail("Deberia haber una excepcion");
    } catch (InvalidRecordException e) {
        assertEquals("La nota debe estar entre 1.5 y 5", e.getMessage(), "El mensaje de la excepcion no es correcto");
    }
}

@Test
void testCreateInscripcionRepetida1() {
    try {
        recordService.createRecord(login, courseCode, 3.5, "2025-1"); // Nota aprobatoria
        recordService.createRecord(login, courseCode, 4.0, "2025-2");
        fail("An exception should be thrown");
    } catch (InvalidRecordException e) {
        assertEquals("El estudiante ya ha aprobado el curso", e.getMessage(), "The exception message should match");
    }
}

@Test
void testCreateInscripcionRepetida2() {
    try {
        recordService.createRecord(login, courseCode, 2.5, "2025-1"); // No aprobó
        RecordEntity record = recordService.createRecord(login, courseCode, 3.0, "2025-2");
        assertEquals(3.0, record.getFinalGrade(), "La nota debe ser 3.0");
    } catch (InvalidRecordException e) {
        fail("No exception should be thrown: " + e.getMessage());
    }
}
}