package uniandes.dse.examen1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import uniandes.dse.examen1.entities.CourseEntity;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;

import java.util.List;

@Slf4j
@Service
public class StatsService {

    @Autowired
    StudentRepository estudianteRepository;

    @Autowired
    CourseRepository cursoRepository;

    public Double calculateStudentAverage(String login) {
        log.info("Calculating average for student: {}", login);

        StudentEntity student = estudianteRepository.findByLogin(login).orElse(null);
        if (student == null) return 0.0;

        List<RecordEntity> records = student.getRecords();
        if (records.isEmpty()) return 0.0;

        double sum = 0;
        for (RecordEntity record : records) {
            sum += record.getFinalGrade();
        }

        return sum / records.size();
    }

    public Double calculateCourseAverage(String courseCode) {
        log.info("Calculating average for course: {}", courseCode);

        CourseEntity course = cursoRepository.findByCourseCode(courseCode).orElse(null);
        if (course == null) return 0.0;

        List<RecordEntity> records = course.getRecords();
        if (records.isEmpty()) return 0.0;

        double sum = 0;
        for (RecordEntity record : records) {
            sum += record.getFinalGrade();
        }

        return sum / records.size();
    }
}

