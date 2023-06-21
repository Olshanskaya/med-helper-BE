package med.helper.controllers;

import lombok.AllArgsConstructor;
import med.helper.dtos.*;
import med.helper.repository.PatientRepository;
import med.helper.repository.UserRepository;
import med.helper.services.CalculateService;
import med.helper.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/user/patient")
@AllArgsConstructor
public class PatientController {

    private final PatientService patientService;
    private final CalculateService calculateService;

    @GetMapping("/{id}")
    public ResponseEntity<List<PatientDto>> getAllPatientsByUserId(@PathVariable(required = true, name = "id") String id) {
        return ResponseEntity.ok(patientService.getAllPatientsByUserID(Long.parseLong(id)));
    }

    @PostMapping(value = "/new/{user_id}")
    public ResponseEntity<PatientDto> addNewPatient(@PathVariable(required = true, name = "id") String user_id, @RequestBody NewPatientDto newPatientDto) {
        return ResponseEntity.of(patientService.newPatient(newPatientDto, Long.parseLong(user_id)));
    }

    @PostMapping(value = "/{patient_id}/new/{med_name}")
    public ResponseEntity<PatientDto> addMedToPatient(@PathVariable(required = true, name = "patient_id") String patient_id,
                                                      @PathVariable(required = true, name = "med_name") String medName,
                                                      @RequestBody NewTimetableDto newTimetableDto) {
        return ResponseEntity.of(calculateService.addMedToPatient(Long.parseLong(patient_id), medName, newTimetableDto));
    }


}


