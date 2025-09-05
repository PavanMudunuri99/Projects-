package com.Spring.Service;

import com.Spring.Model.User;
import com.Spring.Repo.Repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final Repo repo;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(Repo repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    // Register new user
    public User register(User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        return repo.save(user);
    }

    // Load user for Spring Security
    @Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> userOpt = repo.findByUsername(username);
    if (userOpt.isEmpty()) {
        throw new UsernameNotFoundException("User not found with username: " + username);
    }

    User user = userOpt.get();

    return org.springframework.security.core.userdetails.User.builder()
            .username(user.getUsername())
            .password(user.getPassword())
            .build();
}


    // Check existence
    public boolean existsByUsername(Long username) {
        return repo.existsById(username);
    }


    // Get user
    public Optional<Optional<User>> getUserByUsername(String username) {
        return Optional.ofNullable(repo.findByUsername(username));
    }


    public Optional<User> findById(Long id) {
        return repo.findById(id);
    }


    public User save(User user) {
        return repo.save(user);
    }
}
