package med.helper.repository;

import med.helper.entitys.DayTimetable;
import med.helper.entitys.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface TimetableRepository  extends JpaRepository<Timetable, Long> {
    Set<Timetable> findAllByPatientId(Long id);
}
