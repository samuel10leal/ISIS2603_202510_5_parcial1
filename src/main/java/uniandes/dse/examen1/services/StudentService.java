package uniandes.dse.examen1.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import uniandes.dse.examen1.entities.StudentEntity;
import uniandes.dse.examen1.exceptions.RepeatedStudentException;
import uniandes.dse.examen1.repositories.StudentRepository;

@Slf4j
@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Transactional
    public StudentEntity createStudent(StudentEntity newStudent) throws RepeatedStudentException {
        log.info("Creating student with login: {}", newStudent.getLogin());

        if (studentRepository.findByLogin(newStudent.getLogin()).isPresent()) {
            throw new RepeatedStudentException("A student with this login already exists.");
        }
        return studentRepository.save(newStudent);
    }
}
