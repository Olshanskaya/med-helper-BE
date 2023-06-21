package med.helper.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewTimetableDto {

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long dayDuration;

    private Long timePerDay;

}
