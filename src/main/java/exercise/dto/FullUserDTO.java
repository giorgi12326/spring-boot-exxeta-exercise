package exercise.dto;

import exercise.entity.Role;
import lombok.Data;

@Data
public class FullUserDTO {
    private String username;
    private String password;
    private Role role;
}
