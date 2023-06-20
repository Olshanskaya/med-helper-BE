package med.helper.repository;

import med.helper.entitys.ActiveSubstance;
import med.helper.entitys.DayTimetable;
import med.helper.entitys.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DayTimetableRepository extends JpaRepository<DayTimetable, Long> {

    Set<DayTimetable> findByTimetableIdIn(Set<Long> ids);
}
