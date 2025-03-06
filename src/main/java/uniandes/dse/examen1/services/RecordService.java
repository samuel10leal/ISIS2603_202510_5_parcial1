package uniandes.dse.examen1.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.exceptions.InvalidRecordException;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.repositories.RecordRepository;

@Slf4j
@Service
public class RecordService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    RecordRepository recordRepository;

    @Transactional
    public RecordEntity createRecord(String loginStudent, String courseCode, Double grade, String semester)
            throws InvalidRecordException {
        log.info("Creacion de record para el estudiante  en un curso ");

        
        Optional<StudentEntity> studentOpt = studentRepository.findByLogin(loginStudent);
        if (!studentOpt.isPresent()) {
            throw new InvalidRecordException("No existe el estudiante");
        }
        StudentEntity student = studentOpt.get();

       
        if (grade < 1.5 || grade > 5.0) {
            throw new InvalidRecordException("La nota debe estar entre 1.5 y 5");
        }
         
         Optional<CourseEntity> courseOpt = courseRepository.findByCourseCode(courseCode);
         if (!courseOpt.isPresent()) {
             throw new InvalidRecordException("No existe el curso");
         }
         CourseEntity course = courseOpt.get();
 
         for (RecordEntity record : student.getRecords()) {
             if (record.getCourse().equals(course) && record.getFinalGrade() >= 3.0) {
                 throw new InvalidRecordException("El estudiante ya ha aprobado el curso");
             }
         }
 
        
         RecordEntity newRecord = new RecordEntity();
         newRecord.setStudent(student);
         newRecord.setCourse(course);
         newRecord.setFinalGrade(grade);
         newRecord.setSemester(semester);

         
        return recordRepository.save(newRecord);
    }
 
    
}
