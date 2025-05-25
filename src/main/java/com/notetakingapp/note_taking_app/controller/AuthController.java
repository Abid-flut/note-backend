package com.notetakingapp.note_taking_app.controller;

import com.notetakingapp.note_taking_app.model.AppUser;
import com.notetakingapp.note_taking_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  private UserRepository userRepo;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @PostMapping("/signup")
  public ResponseEntity<Void> signup(@RequestBody AppUser u) {
    if (userRepo.existsByEmail(u.getEmail())) {
      return ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
    u.setPassword(passwordEncoder.encode(u.getPassword()));
    userRepo.save(u);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody AppUser u) {
    return userRepo.findByEmail(u.getEmail())
            .filter(db -> passwordEncoder.matches(u.getPassword(), db.getPassword()))
            .map(user -> {
              Map<String, String> response = new HashMap<>();
              response.put("message", "Login successful");
              return ResponseEntity.ok(response);
            })
            .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
  }
}
