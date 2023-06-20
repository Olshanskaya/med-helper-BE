package med.helper.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class DayTimetableDto {
    private Long id;

    private TimetableDto timetable;

    Date medTime;

    String status;
}
