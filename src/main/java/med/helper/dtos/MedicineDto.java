package med.helper.dtos;

import lombok.Data;

@Data
public class MedicineDto {
    String name;
    Integer id;

    ActiveSubstanceDto activeSubstanceDto;

    private String status;
}
