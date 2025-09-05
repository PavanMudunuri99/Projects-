package com.Spring.Controller;

import com.Spring.Model.User;
import com.Spring.Repo.Repo;
import com.Spring.Service.JwtUtil;
import DTO.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private Repo repo;

    @Autowired
    private PasswordEncoder passwordEncoder;
@PostMapping("/login")
public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
    Map<String, Object> response = new HashMap<>();

    // Use Optional to fetch user
    Optional<User> userOpt = Optional.ofNullable(repo.findByUsername(loginRequest.getUsername()));
    if (userOpt.isEmpty()) {
        response.put("error", "Invalid username or password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    User user = userOpt.get();

    // Verify password
    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
        response.put("error", "Invalid username or password");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    // Generate JWT
    String token = jwtUtil.generateToken(user.getUsername());

    response.put("token", token);
    response.put("username", user.getUsername());
    response.put("email", user.getEmail());
    response.put("id", user.getId());

    return ResponseEntity.ok(response);
}
}
