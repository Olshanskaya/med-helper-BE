package med.helper.controllers;

import med.helper.dtos.MedicineDto;
import med.helper.entitys.User;
import med.helper.model.UserModel;
import med.helper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
public class SimpleController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping(value = "/register")
    public User register(@RequestBody UserModel userModel) {
        User newUser = new User();
        newUser.setEmail(userModel.getEmail());
        newUser.setAuthority(userModel.getAuthority());
        newUser.setPassword(bcryptEncoder.encode(userModel.getPassword()));
        return userRepository.save(newUser);
    }

    @PostMapping(value = "/login")
    public User login(@RequestBody UserModel userModel) throws Exception {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userModel.getEmail(), userModel.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (BadCredentialsException e) {
            throw new Exception("invalid creds");
        }
        return userRepository.findByEmail(userModel.getEmail()).get();
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
