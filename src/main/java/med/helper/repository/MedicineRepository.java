package med.helper.repository;

import med.helper.entitys.ActiveSubstance;
import med.helper.entitys.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    Optional<Medicine> findByName(String name);
}
