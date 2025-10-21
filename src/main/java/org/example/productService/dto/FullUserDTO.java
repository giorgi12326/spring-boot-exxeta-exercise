package org.example.productService.dto;

import org.example.productService.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FullUserDTO {
    private String username;
    private String password;
    private Role role;
}
