package med.helper.dtos;

import lombok.Data;
import med.helper.entitys.ActiveSubstance;

import java.util.Date;

@Data
public class ActiveSubstanceInteractionDto {

    private ActiveSubstance activeSubstance1;
    private ActiveSubstance activeSubstance2;

    Date interactionTime;

    private String status;

}
