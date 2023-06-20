package med.helper.services;

import med.helper.services.MedicineService;
import med.helper.dtos.NewMedicineDto;
import med.helper.entitys.ActiveSubstance;
import med.helper.entitys.Medicine;
import med.helper.repository.ActiveSubstanceRepository;
import med.helper.repository.MedicineRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MedicineServiceTest {

    @InjectMocks
    private MedicineService medicineService;

    @Mock
    private MedicineRepository medicineRepository;

    @Mock
    private ActiveSubstanceRepository activeSubstanceRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);

        // Mocking for the modelMapper and Configuration
        Configuration configuration = Mockito.mock(Configuration.class);
        Mockito.when(modelMapper.getConfiguration()).thenReturn(configuration);

        // Mocking for the medicineRepository
        Mockito.when(medicineRepository.findAll()).thenReturn(Arrays.asList(new Medicine(), new Medicine()));
    }


    @Test
    public void getAllMedsTest() {
        List<Medicine> medicines = new ArrayList<>();
        Medicine medicine = new Medicine();
        medicine.setStatus("ACTIVE");
        medicines.add(medicine);

        when(medicineRepository.findAll()).thenReturn(medicines);

        assertEquals(1, medicineService.getAllMeds().size());
    }

    @Test
    public void addNewMedTest() {
        NewMedicineDto newMedicineDto = new NewMedicineDto();
        newMedicineDto.setName("Med1");
        newMedicineDto.setActiveSubstanceId(1L);

        Medicine medicine = new Medicine();
        medicine.setName(newMedicineDto.getName());
        medicine.setStatus("ACTIVE");

        ActiveSubstance activeSubstance = new ActiveSubstance();
        activeSubstance.setId(newMedicineDto.getActiveSubstanceId());

        when(medicineRepository.findByName(newMedicineDto.getName())).thenReturn(Optional.empty());
        when(activeSubstanceRepository.findById(newMedicineDto.getActiveSubstanceId())).thenReturn(Optional.ofNullable(activeSubstance));
        when(medicineRepository.save(any(Medicine.class))).thenReturn(medicine);

        assertFalse(medicineService.addNewMed(newMedicineDto).isPresent());
    }

    @Test
    public void deactivateMedTest() {
        Medicine medicine = new Medicine();
        medicine.setId(1L);
        medicine.setStatus("ACTIVE");

        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine));

        assertTrue(medicineService.deactivateMed(1L));
    }

    @Test
    public void editNewMedTest() {
        NewMedicineDto newMedicineDto = new NewMedicineDto();
        newMedicineDto.setName("Med1");
        newMedicineDto.setActiveSubstanceId(1L);

        Medicine medicine = new Medicine();
        medicine.setName(newMedicineDto.getName());
        medicine.setStatus("ACTIVE");

        ActiveSubstance activeSubstance = new ActiveSubstance();
        activeSubstance.setId(newMedicineDto.getActiveSubstanceId());

        when(medicineRepository.findById(1L)).thenReturn(Optional.of(medicine));
        when(activeSubstanceRepository.findById(newMedicineDto.getActiveSubstanceId())).thenReturn(Optional.of(activeSubstance));
        when(medicineRepository.save(medicine)).thenReturn(medicine);

        assertTrue(medicineService.editNewMed(newMedicineDto, 1L).isEmpty());
    }
}
