package med.helper.services;

import lombok.AllArgsConstructor;
import med.helper.dtos.MedicineDto;
import med.helper.dtos.NewPatientDto;
import med.helper.dtos.NewTimetableDto;
import med.helper.dtos.PatientDto;
import med.helper.entitys.*;
import med.helper.enums.ElementStatus;
import med.helper.repository.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PatientService {

    PatientRepository patientRepository;
    UserRepository userRepository;

    private final ModelMapper modelMapper;



    public List<PatientDto> getAllPatientsByUserID(Long id) {
        Optional<User> ou = userRepository.findById(id);
        if (ou.isEmpty()) {
            return Collections.emptyList();
        }

        Set<Patient> dtos = ou.get().getPatients();
        if (dtos.isEmpty()) {
            return Collections.emptyList();
        }

        return dtos
                .stream()
                .map(p -> modelMapper.map(p, PatientDto.class))
                .collect(Collectors.toList());
    }

    public Optional<PatientDto> newPatient(NewPatientDto newPatientDto, Long user_id) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        Optional<Patient> m = patientRepository.findByName(newPatientDto.getName());
        if (m.isPresent() && m.get().getStatus().equals(ElementStatus.ACTIVE.getName())) {
            return Optional.of(new PatientDto());
        }
        Optional<User> ou = userRepository.findById(user_id);
        if (ou.isEmpty()) {
            return Optional.of(new PatientDto());
        }

        Patient patient = new Patient();
        patient.setName(newPatientDto.getName());
        patient.setStatus(ElementStatus.ACTIVE.getName());
        patient.setBreakfast(newPatientDto.getBreakfast());
        patient.setDinner(newPatientDto.getDinner());
        patient.setLunch(newPatientDto.getLunch());
        patient.setStart_day(newPatientDto.getStart_day());
        patient.setEnd_day(newPatientDto.getEnd_day());
        Patient saved = patientRepository.save(patient);

        User u = ou.get();
        u.getPatients().add(saved);
        userRepository.save(u);

        return Optional.ofNullable(modelMapper.map(saved, PatientDto.class));
    }

}
