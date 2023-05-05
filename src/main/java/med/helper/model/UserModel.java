package med.helper.model;

import lombok.Data;
import med.helper.entitys.Authority;

@Data
public class UserModel {
    private String email;
    private String password;
    private Authority authority;
}
