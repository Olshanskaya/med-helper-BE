package med.helper.model;

import lombok.Data;

@Data
public class UserModel {
    private String email;
    private String password;
    private String role;
}
