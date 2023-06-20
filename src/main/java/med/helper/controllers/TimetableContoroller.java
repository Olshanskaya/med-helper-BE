package med.helper.controllers;

import med.helper.dtos.DayTimetableDto;
import med.helper.dtos.MedicineDto;
import med.helper.dtos.TimetableDto;
import med.helper.services.TimetableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class TimetableContoroller {

    @Autowired
    TimetableService timetableService;

    @GetMapping(value = "/user/timetable/{id}")
    public Set<TimetableDto> getTimetableDtoByPatientId(@PathVariable(required = true, name = "id") String id) {
        return timetableService.getTimetableDtoByPatientId(Long.parseLong(id));
    }

    @GetMapping(value = "/user/timetable/today/{id}")
    public Set<DayTimetableDto> getDayTimetableDtoByPatientId(@PathVariable(required = true, name = "id") String id) {
        return timetableService.getDayTimetableDtoByPatientId(Long.parseLong(id));
    }
}
