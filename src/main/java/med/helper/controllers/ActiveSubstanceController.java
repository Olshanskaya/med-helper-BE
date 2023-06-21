package med.helper.controllers;

import med.helper.dtos.*;
import med.helper.repository.ActiveSubstanceRepository;
import med.helper.repository.UserRepository;
import med.helper.services.ActiveSubstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class ActiveSubstanceController {

    @Autowired
    private ActiveSubstanceService activeSubstanceService;

    @GetMapping(value = "/admin/sub/all")
    public Set<ActiveSubstanceDto> getAllActiveSubstance() {
        return activeSubstanceService.getAllActiveSubstance();
    }

    @GetMapping(value = "/admin/sub/interaction/{id}")
    public Set<ActiveSubstanceInteractionDto> getAllInteractionsWithSubstanceById(@PathVariable(required = true, name = "id") String id) {
        return activeSubstanceService.getAllInteractionsWithSubstanceById(Long.parseLong(id));
    }

    @GetMapping(value = "/admin/sub/interaction/delete/{id}")
    public ResponseEntity<String> deleteActiveSubstance(@PathVariable(required = true, name = "id") String id) {
        if(activeSubstanceService.deactivateActiveSubstance(Long.parseLong(id)))
            return ResponseEntity.ok("med deactivated");

        return ResponseEntity.badRequest().body("bad id");
    }

    @PostMapping(value = "/admin/sub/new")
    public ResponseEntity<ActiveSubstanceDto> addNewSub(@RequestBody NewActiveSubstanceDto newActiveSubstanceDto) {
        return ResponseEntity.of(activeSubstanceService.addNewActiveSubstance(newActiveSubstanceDto));
    }
    @PostMapping(value = "/admin/sub/interaction/new")
    public ResponseEntity<ActiveSubstanceDto> addNewSubInteractions(@RequestBody List<NewActiveSubstanceInteractionDto> dtos) {
        return ResponseEntity.of(activeSubstanceService.addNewSubInteraction(dtos));
    }

    @GetMapping(value = "/user/activsub/conflict/{patient_id}/{as_name}")
    public ResponseEntity<String> isCombinationOk(@PathVariable(required = true, name = "patient_id") String patient_id, @PathVariable(required = true, name = "as_name") String as_name) {
        return activeSubstanceService.isMedsCombinationOk(Long.parseLong(patient_id), as_name);
    }


}
