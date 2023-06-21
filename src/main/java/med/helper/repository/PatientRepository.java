package med.helper.repository;

import med.helper.entitys.Medicine;
import med.helper.entitys.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

//    @Query("SELECT p FROM Patient p INNER JOIN p.usersPatients up WHERE up.user.id = :userId")
//    List<Patient> findAllByUserId(@Param("userId") Long userId);

    Optional<Patient> findByName(String name);
}
