package exercise.controller;

import exercise.dto.RegisterUserDTO;
import exercise.dto.TokenDTO;
import exercise.dto.UserDTO;
import exercise.security.JwtUtil;
import exercise.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody UserDTO user) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        String s = jwtUtil.generateToken(authenticate.getName());
        return ResponseEntity.status(201).body(new TokenDTO(s, "Bearer"));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterUserDTO> registerUser(@RequestBody RegisterUserDTO user) {
        RegisterUserDTO registerUserDTO = userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(registerUserDTO);
    }
}
