package med.helper.dtos;

import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
public class PatientDto {
    private Long id;
    private String name;
    private String status;
    Date start_day;
    Date end_day;
    Date breakfast;
    Date lunch;
    Date dinner;
}
