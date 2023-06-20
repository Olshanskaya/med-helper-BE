package med.helper.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class NewActiveSubstanceInteractionDto {
    Long activeSubstanceId1;
    String activeSubstanceName;

    Date interactionTime;

}
