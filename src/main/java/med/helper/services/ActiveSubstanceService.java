package med.helper.services;

import lombok.AllArgsConstructor;
import med.helper.dtos.*;
import med.helper.entitys.*;
import med.helper.enums.ElementStatus;
import med.helper.repository.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ActiveSubstanceService {

    ActiveSubstanceRepository activeSubstanceRepository;
    ActiveSubstanceInteractionRepository activeSubstanceInteractionRepository;

    TimetableRepository timetableRepository;

    PatientRepository patientRepository;
    ModelMapper modelMapper;


    public Set<ActiveSubstanceDto> getAllActiveSubstance() {
        List<ActiveSubstance> dtos = activeSubstanceRepository.findAll();

        return dtos
                .stream()
                .filter(p -> p.getStatus().equals(ElementStatus.ACTIVE.getName()))
                .map(p -> modelMapper.map(p, ActiveSubstanceDto.class))
                .collect(Collectors.toSet());

    }

    public Set<ActiveSubstanceInteractionDto> getAllInteractionsWithSubstanceById(long subId) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        List<ActiveSubstanceInteraction> dtos = activeSubstanceInteractionRepository.findAll();

        return dtos
                .stream()
                .filter(p -> p.getStatus().equals(ElementStatus.ACTIVE.getName()))
                .filter(p -> p.getActiveSubstance1().getId().equals(subId))
                .map(p -> modelMapper.map(p, ActiveSubstanceInteractionDto.class))
                .collect(Collectors.toSet());

    }

    public Optional<ActiveSubstanceDto> addNewActiveSubstance(NewActiveSubstanceDto newActiveSubstanceDto) {
        Optional<ActiveSubstance> m = activeSubstanceRepository.findByName(newActiveSubstanceDto.getStatus());
        if (m.isPresent() && m.get().getStatus().equals(ElementStatus.ACTIVE.getName())) {
            return Optional.of(new ActiveSubstanceDto());
        }
        ActiveSubstance as = new ActiveSubstance();
        as.setName(newActiveSubstanceDto.getName());
        as.setStatus(ElementStatus.ACTIVE.getName());

        ActiveSubstance savedAS = activeSubstanceRepository.save(as);
        return Optional.of(modelMapper.map(savedAS, ActiveSubstanceDto.class));
    }

    public Optional<ActiveSubstanceDto> addNewSubInteraction(List<NewActiveSubstanceInteractionDto> dtos) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        for (NewActiveSubstanceInteractionDto i : dtos) {
            if (i.getActiveSubstanceName() == null || i.getActiveSubstanceName().equals(""))
                continue;
            Optional<ActiveSubstance> activeSubstance1 = activeSubstanceRepository.findById(i.getActiveSubstanceId1());
            Optional<ActiveSubstance> activeSubstance2 = activeSubstanceRepository.findByName(i.getActiveSubstanceName());
            if (activeSubstance1.isEmpty() || activeSubstance2.isEmpty())
                continue;

            ActiveSubstanceInteraction activeSubstanceInteraction = new ActiveSubstanceInteraction();
            activeSubstanceInteraction.setActiveSubstance1(activeSubstance1.get());
            activeSubstanceInteraction.setActiveSubstance2(activeSubstance2.get());
            activeSubstanceInteraction.setStatus(ElementStatus.ACTIVE.getName());
            activeSubstanceInteraction.setInteractionTime(i.getInteractionTime());
            activeSubstanceInteractionRepository.save(activeSubstanceInteraction);

            //если это не взаимодейтсвие вещества с самим собой то надо добавить вторую строку в бд
            if (!activeSubstance1.get().getId().equals(activeSubstance2.get().getId())) {
                ActiveSubstanceInteraction activeSubstanceInteraction2 = new ActiveSubstanceInteraction();
                activeSubstanceInteraction2.setActiveSubstance1(activeSubstance2.get());
                activeSubstanceInteraction2.setActiveSubstance2(activeSubstance1.get());
                activeSubstanceInteraction2.setStatus(ElementStatus.ACTIVE.getName());
                activeSubstanceInteraction2.setInteractionTime(i.getInteractionTime());
                activeSubstanceInteractionRepository.save(activeSubstanceInteraction2);
            }
        }

        return Optional.of(new ActiveSubstanceDto());
    }

    public boolean deactivateActiveSubstance(long id) {
        Optional<ActiveSubstance> ap = activeSubstanceRepository.findById(id);
        if (ap.isPresent()) {
            ActiveSubstance c = ap.get();
            c.setStatus(ElementStatus.DELETED.getName());
            activeSubstanceRepository.save(c);


            //удаление всех взаимодействий с этим веществом
            List<ActiveSubstanceInteraction> activeSubstanceList = activeSubstanceInteractionRepository.findAll();
            Set<Long> deleted = activeSubstanceList.stream()
                    .filter(p -> p.getActiveSubstance2().getId().equals(id) || p.getActiveSubstance1().getId().equals(id))
                    .map(p -> p.getId())
                    .collect(Collectors.toSet());


            deactivateActiveSubstanceInteraction(deleted);
            return true;
        }
        return false;
    }

    public boolean deactivateActiveSubstanceInteraction(Set<Long> ids) {
        for (Long i : ids) {
            Optional<ActiveSubstanceInteraction> ap = activeSubstanceInteractionRepository.findById(i);
            if (ap.isPresent()) {
                ActiveSubstanceInteraction c = ap.get();
                c.setStatus(ElementStatus.DELETED.getName());
                activeSubstanceInteractionRepository.save(c);
            }
        }
        return true;
    }


    public ResponseEntity<String> isMedsCombinationOk(Long patient_id, String as_name) {
        Optional<ActiveSubstance> as1 = activeSubstanceRepository.findByName(as_name);
        Set<Timetable> timetables = timetableRepository.findAllByPatientId(patient_id);
        if(timetables.isEmpty()) {
            return ResponseEntity.ok("ok");
        }
        //List<ActiveSubstanceInteraction> list = activeSubstanceInteractionRepository.findByActiveSubstance1(as1.get());
        for (Timetable t: timetables) {
            ActiveSubstance a = t.getMedicine().getActiveSubstance();
            List<ActiveSubstanceInteraction> list = activeSubstanceInteractionRepository.findByActiveSubstance1(a);
            if (list.isEmpty()) {
               continue;
            }
            for (ActiveSubstanceInteraction l: list) {
                if(Objects.equals(l.getActiveSubstance2().getName(), as_name) && l.getInteractionTime()==null)
                    return ResponseEntity.ok("not");
            }
        }
        return ResponseEntity.ok("ok");
    }
}
