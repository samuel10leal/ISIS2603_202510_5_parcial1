package uniandes.dse.examen1.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import uniandes.dse.examen1.entities.RecordEntity;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<RecordEntity, Long> {
    List<RecordEntity> findByStudentLogin(String login);
    List<RecordEntity> findByCourseCode(String courseCode);

}
