package med.helper.services;

import lombok.AllArgsConstructor;
import med.helper.dtos.DayTimetableDto;
import med.helper.dtos.PatientDto;
import med.helper.dtos.TimetableDto;
import med.helper.entitys.DayTimetable;
import med.helper.entitys.Patient;
import med.helper.entitys.Timetable;
import med.helper.entitys.User;
import med.helper.repository.DayTimetableRepository;
import med.helper.repository.TimetableRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TimetableService {

    TimetableRepository timetableRepository;

    DayTimetableRepository dayTimetableRepository;

    private final ModelMapper modelMapper;


    public Set<TimetableDto> getTimetableDtoByPatientId(Long patientId) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        Set<Timetable> tt = timetableRepository.findAllByPatientId(patientId);

        if (tt.isEmpty()) {
            return Collections.emptySet();
        }

        return tt
                .stream()
                .map(p -> modelMapper.map(p, TimetableDto.class))
                .collect(Collectors.toSet());

    }

    public Set<DayTimetableDto> getDayTimetableDtoByPatientId(Long patientId) {
        Set<TimetableDto> t = getTimetableDtoByPatientId(patientId);

        Set<Long> ids = t.stream()
                .map(TimetableDto::getId)
                .collect(Collectors.toSet());
        ;

        Set<DayTimetable>  dayTimetables =  dayTimetableRepository.findByTimetableIdIn(ids);
        return dayTimetables.stream()
                .map(p -> modelMapper.map(p, DayTimetableDto.class))
                .collect(Collectors.toSet());
    }
}
