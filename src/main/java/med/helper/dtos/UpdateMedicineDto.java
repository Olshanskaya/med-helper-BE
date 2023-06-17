package med.helper.dtos;

import lombok.Data;

@Data
public class UpdateMedicineDto {
    String name;
    Long activeSubstanceId;
    Long id;
}
