package med.helper.services;

import med.helper.dtos.PatientDto;
import med.helper.entitys.Patient;
import med.helper.entitys.User;
import med.helper.repository.PatientRepository;
import med.helper.repository.UserRepository;
import med.helper.services.PatientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PatientService patientService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllPatientsByUserId() {
        // Create test data
        User user = new User();
        user.setId(1L);

        Patient patient1 = createTestPatient(1L, "Patient 1", user, new Date(), new Date(), new Date(), new Date(), new Date());
        Patient patient2 = createTestPatient(2L, "Patient 2", user, new Date(), new Date(), new Date(), new Date(), new Date());

        Set<Patient> patients = new HashSet<>();
        patients.add(patient1);
        patients.add(patient2);

        user.setPatients(patients);

        // Set up the mock repositories
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        PatientDto patientDto1 = createTestPatientDto(1L, "Patient 1", user.getId(), new Date(), new Date(), new Date(), new Date(), new Date());
        PatientDto patientDto2 = createTestPatientDto(2L, "Patient 2", user.getId(), new Date(), new Date(), new Date(), new Date(), new Date());

        when(modelMapper.map(patient1, PatientDto.class)).thenReturn(patientDto1);
        when(modelMapper.map(patient2, PatientDto.class)).thenReturn(patientDto2);

        // Call the method under test
        List<PatientDto> result = patientService.getAllPatientsByUserID(1L);

        // Assert the result
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    private Patient createTestPatient(Long id, String name, User user, Date startDay, Date endDay, Date breakfast, Date lunch, Date dinner) {
        Patient patient = new Patient();
        patient.setId(id);
        patient.setName(name);
        patient.setStatus("ACTIVE");
        patient.setStart_day(startDay);
        patient.setEnd_day(endDay);
        patient.setBreakfast(breakfast);
        patient.setLunch(lunch);
        patient.setDinner(dinner);
        return patient;
    }

    private PatientDto createTestPatientDto(Long id, String name, Long userId, Date startDay, Date endDay, Date breakfast, Date lunch, Date dinner) {
        PatientDto patientDto = new PatientDto();
        patientDto.setId(id);
        patientDto.setName(name);
        patientDto.setStatus("ACTIVE");
        patientDto.setStart_day(startDay);
        patientDto.setEnd_day(endDay);
        patientDto.setBreakfast(breakfast);
        patientDto.setLunch(lunch);
        patientDto.setDinner(dinner);
        return patientDto;
    }
}
