package com.Spring.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.Spring.Model.AuthProvider;
import com.Spring.Model.Role;
import com.Spring.Model.User;
import com.Spring.Repo.Repo;
import com.Spring.Service.JwtUtil;
import com.Spring.Service.UserService;

import DTO.LoginRequest;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService ser;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private Repo repo;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (ser.existsByUsername(user.getUsername())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Username already exists."));
        }

        if (ser.existsByUsername(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Email already exists."));
        }

        user.setRole(Role.USER);
        user.setProvider(AuthProvider.LOCAL);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        ser.save(user);

        return ResponseEntity.ok(Map.of("message", "Registration successful"));
    }

    @PostMapping("/log")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> optionalUser = repo.findByUsername(loginRequest.getUsername());

        if (optionalUser.isEmpty() ||
                !passwordEncoder.matches(loginRequest.getPassword(), optionalUser.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid credentials"));
        }

        User user = optionalUser.get();
        String token = jwtUtil.generateToken(user.getUsername());

        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", Map.of(
                "id", user.getId(),
                "username", user.getUsername(),
                "email", user.getEmail()
        ));

        return ResponseEntity.ok(response);
    }
}
