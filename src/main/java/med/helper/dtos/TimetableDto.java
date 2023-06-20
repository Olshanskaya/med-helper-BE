package med.helper.dtos;

import lombok.Data;
import med.helper.entitys.Medicine;
import med.helper.entitys.Patient;

import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Data
public class TimetableDto {

    private Long id;

    private PatientDto patient;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long dayDuration;

    private Long timePerDay;


    private MedicineDto medicine;
}
