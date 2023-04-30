package med.helper.controllers;

import med.helper.dtos.MedicineDto;
import med.helper.entitys.User;
import med.helper.model.UserModel;
import med.helper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class SimpleController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @PostMapping(value = "/register")
    public User register(@RequestBody UserModel userModel) {
        User newUser = new User();
        newUser.setEmail(userModel.getEmail());
        newUser.setRole(userModel.getRole());
        newUser.setPassword(bcryptEncoder.encode(userModel.getPassword()));
        return userRepository.save(newUser);
    }

    @GetMapping(value = "/login/{id}")
    public MedicineDto getTestData(@PathVariable Integer id) {
        MedicineDto m = new MedicineDto();
        m.setName("login");
        m.setId(id);
        return m;
    }

    @GetMapping(value = "/admin/{id}")
    public MedicineDto getTestDataAdmin(@PathVariable Integer id) {
        MedicineDto m = new MedicineDto();
        m.setName("admin");
        m.setId(id);
        return m;
    }

    @GetMapping(value = "/user/{id}")
    public MedicineDto getTestDataUser(@PathVariable Integer id) {
        MedicineDto m = new MedicineDto();
        m.setName("user");
        m.setId(id);
        return m;
    }
}
