package med.helper.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class NewActiveSubstanceInteractionDto {
    Long activeSubstanceId1;
    String activeSubstanceName;

    Date interactionTime;

}
