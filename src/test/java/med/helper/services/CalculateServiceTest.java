package med.helper.services;

import med.helper.dtos.NewTimetableDto;
import med.helper.dtos.PatientDto;
import med.helper.entitys.*;
import med.helper.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalculateServiceTest {

    @Mock
    private ActiveSubstanceInteractionRepository activeSubstanceInteractionRepository;
    @Mock
    private DayTimetableRepository dayTimetableRepository;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MedicineRepository medicineRepository;
    @Mock
    private ModelMapper modelMapper;
    @Mock
    private TimetableRepository timetableRepository;

    private CalculateService calculateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        calculateService = new CalculateService(activeSubstanceInteractionRepository, dayTimetableRepository,
                patientRepository, userRepository, medicineRepository, modelMapper, timetableRepository);
    }

    @Test
    void addMedToPatient_WhenPatientExistsAndMedicineExists_ShouldReturnPatientDto() {

        Long patientId = 1L;
        String medName = "Medicine A";
        NewTimetableDto newTimetableDto = new NewTimetableDto();
        Patient patient = new Patient();
        patient.setId(patientId);
        Optional<Patient> optionalPatient = Optional.of(patient);
        Medicine medicine = new Medicine();
        medicine.setName(medName);
        ActiveSubstance activeSubstance = new ActiveSubstance();
        medicine.setActiveSubstance(activeSubstance);
        Timetable newTimetable = new Timetable();
        when(patientRepository.findById(patientId)).thenReturn(optionalPatient);
        when(medicineRepository.findByName(medName)).thenReturn(Optional.of(medicine));
        when(timetableRepository.save(any(Timetable.class))).thenReturn(newTimetable);
        when(modelMapper.map(patient, PatientDto.class)).thenReturn(new PatientDto());

        Optional<PatientDto> result = calculateService.addMedToPatient(patientId, medName, newTimetableDto);
        assertTrue(result.isPresent());
        assertNotNull(result.get());
        verify(timetableRepository, times(1)).save(any(Timetable.class));
        verify(modelMapper, times(1)).map(patient, PatientDto.class);
    }

    @Test
    void addMedToPatient_WhenDayTimetablesEmpty_ShouldAddNewTimetable() {
        Long patientId = 1L;
        String medName = "Medicine A";
        NewTimetableDto newTimetableDto = new NewTimetableDto();
        Patient patient = new Patient();
        patient.setStart_day(new Date());
        Optional<Patient> optionalPatient = Optional.of(patient);
        Medicine medicine = new Medicine();
        medicine.setName(medName);
        ActiveSubstance activeSubstance = new ActiveSubstance();
        medicine.setActiveSubstance(activeSubstance);
        Timetable newTimetable = new Timetable();
        Timetable savedTimetable = new Timetable();
        Set<Timetable> timetables = new HashSet<>();
        timetables.add(savedTimetable);
        when(patientRepository.findById(patientId)).thenReturn(optionalPatient);
        when(medicineRepository.findByName(medName)).thenReturn(Optional.of(medicine));
        when(timetableRepository.save(any(Timetable.class))).thenReturn(savedTimetable);
        when(timetableRepository.findAllByPatientId(patientId)).thenReturn(timetables);

        Optional<PatientDto> result = calculateService.addMedToPatient(patientId, medName, newTimetableDto);

        assertTrue(result.isPresent());
        assertNotNull(result.get());
        verify(dayTimetableRepository, times(1)).save(any(DayTimetable.class));
    }

    @Test
    void addMedToPatient_WhenConflictExists_ShouldAddNewTimetable() {
        Long patientId = 1L;
        String medName = "Medicine A";
        NewTimetableDto newTimetableDto = new NewTimetableDto();
        Patient patient = new Patient();
        patient.setStart_day(new Date());
        Optional<Patient> optionalPatient = Optional.of(patient);
        Medicine medicine = new Medicine();
        medicine.setName(medName);
        ActiveSubstance activeSubstance = new ActiveSubstance();
        medicine.setActiveSubstance(activeSubstance);
        Timetable timetable = new Timetable();
        timetable.setMedicine(medicine);
        Set<Timetable> timetables = new HashSet<>();
        timetables.add(timetable);
        when(patientRepository.findById(patientId)).thenReturn(optionalPatient);
        when(medicineRepository.findByName(medName)).thenReturn(Optional.of(medicine));
        when(timetableRepository.findAllByPatientId(patientId)).thenReturn(timetables);
        when(activeSubstanceInteractionRepository.findByActiveSubstance1(activeSubstance))
                .thenReturn(Collections.singletonList(new ActiveSubstanceInteraction()));

        Optional<PatientDto> result = calculateService.addMedToPatient(patientId, medName, newTimetableDto);

        assertTrue(result.isPresent());
        assertNotNull(result.get());
        verify(dayTimetableRepository, times(1)).save(any(DayTimetable.class));
    }
}
