package med.helper.controllers;

import lombok.AllArgsConstructor;
import med.helper.dtos.PatientDto;
import med.helper.repository.PatientRepository;
import med.helper.repository.UserRepository;
import med.helper.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/user/patient")
@AllArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @GetMapping("/{id}")
    public ResponseEntity<List<PatientDto>> getAllPatientsByUserId(@PathVariable(required = true, name = "id") String id) {
        return ResponseEntity.ok(patientService.getAllPatientsByUserID(Long.parseLong(id)));
    }
}


