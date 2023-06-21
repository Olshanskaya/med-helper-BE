package med.helper.repository;

import med.helper.entitys.ActiveSubstance;
import med.helper.entitys.ActiveSubstanceInteraction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActiveSubstanceInteractionRepository extends JpaRepository<ActiveSubstanceInteraction, Long> {

    List<ActiveSubstanceInteraction> findByActiveSubstance1(ActiveSubstance as1);
}
