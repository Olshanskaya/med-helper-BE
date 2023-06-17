package med.helper.services;

import lombok.AllArgsConstructor;
import med.helper.dtos.MedicineDto;
import med.helper.dtos.NewMedicineDto;
import med.helper.dtos.UpdateMedicineDto;
import med.helper.entitys.ActiveSubstance;
import med.helper.entitys.Medicine;
import med.helper.entitys.User;
import med.helper.enums.ElementStatus;
import med.helper.repository.ActiveSubstanceRepository;
import med.helper.repository.MedicineRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MedicineService {

    MedicineRepository medicineRepository;
    ModelMapper modelMapper;

    ActiveSubstanceRepository activeSubstanceRepository;


    public Set<MedicineDto> getAllMeds() {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        List<Medicine> dtos = medicineRepository.findAll();

        return dtos
                .stream()
                .filter(p -> p.getStatus().equals(ElementStatus.ACTIVE.getName()))
                .map(p -> modelMapper.map(p, MedicineDto.class))
                .collect(Collectors.toSet());
    }

    public Optional<MedicineDto> addNewMed(NewMedicineDto newMedicineDto) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);
        //medicineRepository.save(modelMapper.map(newMedicineDto, Medicine.class));
        Optional<Medicine> m = medicineRepository.findByName(newMedicineDto.getName());
        if (m.isPresent() && m.get().getStatus().equals(ElementStatus.ACTIVE.getName())) {
            return Optional.of(new MedicineDto());
        }
        Medicine medicine = new Medicine();
        medicine.setName(newMedicineDto.getName());
        medicine.setStatus(ElementStatus.ACTIVE.getName());
        Optional<ActiveSubstance> as = activeSubstanceRepository.findById(newMedicineDto.getActiveSubstanceId());
        medicine.setActiveSubstance(as.get());
        Medicine savedMed = medicineRepository.save(medicine);
        return Optional.of(modelMapper.map(savedMed, MedicineDto.class));
    }

    public boolean deactivateMed(long medID) {
        Optional<Medicine> ap = medicineRepository.findById(medID);
        if (ap.isPresent()) {
            Medicine c = ap.get();
            c.setStatus(ElementStatus.DELETED.getName());
            medicineRepository.save(c);
            return true;
        }
        return false;
    }

    public Optional<MedicineDto> editNewMed(NewMedicineDto newMedicineDto, Long id) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        Optional<Medicine> m = medicineRepository.findById(id);
        if (m.isEmpty()) {
            return Optional.of(new MedicineDto());
        }
        Medicine medicine = m.get();
        medicine.setName(newMedicineDto.getName());
        Optional<ActiveSubstance> as = activeSubstanceRepository.findById(newMedicineDto.getActiveSubstanceId());
        medicine.setActiveSubstance(as.get());
        Medicine savedMed = medicineRepository.save(medicine);
        return Optional.of(modelMapper.map(savedMed, MedicineDto.class));
    }
}
