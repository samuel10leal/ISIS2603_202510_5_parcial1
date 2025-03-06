package uniandes.dse.examen1.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import uniandes.dse.examen1.entities.RecordEntity;
import uniandes.dse.examen1.repositories.CourseRepository;
import uniandes.dse.examen1.repositories.StudentRepository;
import uniandes.dse.examen1.repositories.RecordRepository;

import java.util.List;

@Slf4j
@Service
public class StatsService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    RecordRepository recordRepository;

    public Double calculateStudentAverage(String login) {
        List<RecordEntity> records = recordRepository.findByStudentLogin(login);
        if (records.isEmpty()) {
            return null; // or return 0.0 if you prefer
        }

        double sum = 0;
        for (RecordEntity record : records) {
            sum += record.getFinalGrade();
        }
        return sum / records.size();
    }

    public Double calculateCourseAverage(String courseCode) {
        List<RecordEntity> records = recordRepository.findByCourseCode(courseCode);
        if (records.isEmpty()) {
            return null; // or return 0.0 if you prefer
        }

        double sum = 0;
        for (RecordEntity record : records) {
            sum += record.getFinalGrade();
        }
        return sum / records.size();
    }
}
