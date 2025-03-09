package uniandes.dse.examen1.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class StudentEntity {

    @PodamExclude
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The login of a student. It should be unique.
     */
    private String login;

    /**
     * The name of the student
     */
    private String name;

    
    @PodamExclude
    @OneToMany(mappedBy = "student")
    private List<RecordEntity> records = new ArrayList<>();

    
    @PodamExclude
    @ManyToMany(mappedBy = "students")
    private List<CourseEntity> course = new ArrayList<>();
    
}
