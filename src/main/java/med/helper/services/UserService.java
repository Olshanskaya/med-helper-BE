package med.helper.services;

import lombok.AllArgsConstructor;
import med.helper.entitys.User;
import med.helper.enums.ElementStatus;
import med.helper.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    UserRepository userRepository;

    public Set<User> getAllAdmins() {
        return userRepository.findAll().stream()
                .filter(u -> Objects.equals(u.getStatus(), ElementStatus.ACTIVE.getName()))
                .filter(u -> u.getAuthority().getAuthority().equals("ADMIN"))
                .collect(Collectors.toSet());
    }

    public boolean deactivateAdmin(Long id) {
        Optional<User> ap = userRepository.findById(id);
        if (ap.isPresent()) {
            User c = ap.get();
            c.setStatus(ElementStatus.DELETED.getName());
            userRepository.save(c);
            return true;
        }
        return false;
    }

}
