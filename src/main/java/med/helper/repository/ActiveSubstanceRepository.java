package med.helper.repository;

import med.helper.entitys.ActiveSubstance;
import med.helper.entitys.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActiveSubstanceRepository extends JpaRepository<ActiveSubstance, Long> {
    Optional<ActiveSubstance> findByName(String name);
}
