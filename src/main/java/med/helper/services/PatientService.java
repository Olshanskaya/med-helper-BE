package med.helper.services;

import lombok.AllArgsConstructor;
import med.helper.dtos.PatientDto;
import med.helper.entitys.Patient;
import med.helper.entitys.User;
import med.helper.repository.PatientRepository;
import med.helper.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
}
