package med.helper.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class NewPatientDto {
    private String name;
    private String status;
    Date start_day;
    Date end_day;
    Date breakfast;
    Date lunch;
    Date dinner;
}
