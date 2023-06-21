package med.helper.services;

import lombok.AllArgsConstructor;
import med.helper.dtos.NewTimetableDto;
import med.helper.dtos.PatientDto;
import med.helper.entitys.*;
import med.helper.enums.ElementStatus;
import med.helper.repository.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CalculateService {

    ActiveSubstanceInteractionRepository activeSubstanceInteractionRepository;

    DayTimetableRepository dayTimetableRepository;
    PatientRepository patientRepository;
    UserRepository userRepository;
    MedicineRepository medicineRepository;

    private final ModelMapper modelMapper;

    TimetableRepository timetableRepository;


    public Optional<PatientDto> addMedToPatient(Long patient_id, String medName, NewTimetableDto newTimetableDto) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        Optional<Patient> p = patientRepository.findById(patient_id);
        Patient patient = p.get();
        Optional<Medicine> med = medicineRepository.findByName(medName);
        ActiveSubstance activeSubstance = med.get().getActiveSubstance();
        Set<Timetable> timetables = timetableRepository.findAllByPatientId(patient_id);
        Timetable newTimetable = new Timetable();
        newTimetable.setPatient(patient);
        newTimetable.setPatient(patient);
        newTimetable.setDayDuration(newTimetableDto.getDayDuration());
        newTimetable.setStartTime(newTimetableDto.getStartTime());
        newTimetable.setEndTime(newTimetableDto.getEndTime());
        newTimetable.setDayDuration(newTimetableDto.getDayDuration());
        Timetable t = timetableRepository.save(newTimetable);

        Set<DayTimetable> dayTimetables = dayTimetableRepository.findByTimetableIdIn(timetables.stream().map(i -> i.getId()).collect(Collectors.toSet()));
        if (dayTimetables.isEmpty()) {
            addNewTimetable(activeSubstance, patient.getStart_day(), t);
            return Optional.ofNullable(modelMapper.map(patient, PatientDto.class));
        }

        for (Timetable timetable : timetables) {
            ActiveSubstance currentActiveSubstance = timetable.getMedicine().getActiveSubstance();
            if (!isConflict(activeSubstance, currentActiveSubstance)) {
                Date conflictTime = findTime(activeSubstance, currentActiveSubstance);
                if (conflictTime == null) {
                    Set<DayTimetable> dt = dayTimetableRepository.findByTimetable(timetable);
                    for (DayTimetable d : dt) {
                        Date newTime = addMinutesToJavaUtilDate(d.getMedTime(), 15);
                        addNewTimetable(d.getTimetable().getMedicine().getActiveSubstance(), newTime, timetable);
                    }
                }
            }
        }
        return Optional.ofNullable(modelMapper.map(patient, PatientDto.class));
    }

    private Date addMinutesToJavaUtilDate(Date date, int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, minutes);
        return calendar.getTime();
    }

    private void addNewTimetable(ActiveSubstance activeSubstance, Date time, Timetable t) {
        Medicine newMedicine = new Medicine();
        newMedicine.setActiveSubstance(activeSubstance);

        DayTimetable newDay = new DayTimetable();
        newDay.setMedTime(time);
        newDay.setStatus(ElementStatus.ACTIVE.getName());
        newDay.setTimetable(t);

        dayTimetableRepository.save(newDay);
    }

    private boolean isConflict(ActiveSubstance as1, ActiveSubstance as2) {
        Date conflictTime = findTime(as1, as2);
        return conflictTime != null;
    }

    private Date findTime(ActiveSubstance as1, ActiveSubstance as2) {
        List<ActiveSubstanceInteraction> list = activeSubstanceInteractionRepository.findByActiveSubstance1(as1);
        if (list.isEmpty()) {
            return null;
        }
        for (ActiveSubstanceInteraction l : list) {
            if (Objects.equals(l.getActiveSubstance2().getName(), as2.getName()))
                return l.getInteractionTime();
        }
        return null;
    }

}
