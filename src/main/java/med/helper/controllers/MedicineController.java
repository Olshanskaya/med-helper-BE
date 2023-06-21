package med.helper.controllers;

import med.helper.dtos.MedicineDto;
import med.helper.dtos.NewMedicineDto;
import med.helper.dtos.UpdateMedicineDto;
import med.helper.entitys.User;
import med.helper.model.UserModel;
import med.helper.services.MedicineService;
import med.helper.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class MedicineController {

    @Autowired
    private MedicineService medicineService;


    @GetMapping(value = "/admin/meds/all")
    public Set<MedicineDto> getAllMeds() {
        return medicineService.getAllMeds();
    }

    @PostMapping(value = "/admin/meds/new")
    public ResponseEntity<MedicineDto> addNewMed(@RequestBody NewMedicineDto newMedicineDto) {
        return ResponseEntity.of(medicineService.addNewMed(newMedicineDto));
    }

    @GetMapping(value = "/admin/meds/delete/{id}")
    public ResponseEntity<String> deleteNonActiveMeds(@PathVariable(required = true, name = "id") String id) {
        if( medicineService.deactivateMed(Long.parseLong(id)))
            return ResponseEntity.ok("med deactivated");

        return ResponseEntity.badRequest().body("bad id");
    }

    @PutMapping(value = "/admin/meds/edit/{id}")
    public ResponseEntity<MedicineDto> editNewMed(@RequestBody NewMedicineDto newMedicineDto, @PathVariable(required = true, name = "id") String id) {
        return ResponseEntity.of(medicineService.editNewMed(newMedicineDto, Long.parseLong(id)));
    }

}
