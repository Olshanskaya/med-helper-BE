package med.helper.controllers;

import med.helper.entitys.User;
import med.helper.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping(value = "/admin/admins/all")
    public Set<User> getAllAdmins() {
        return userService.getAllAdmins();
    }

    @GetMapping(value = "/admin/admins/delete/{id}")
    public ResponseEntity<String> deleteNonActiveAdmin(@PathVariable(required = true, name = "id") String id) {
        if( userService.deactivateAdmin(Long.parseLong(id)))
            return ResponseEntity.ok("admin deactivated");

        return ResponseEntity.badRequest().body("bad id");
    }

}
