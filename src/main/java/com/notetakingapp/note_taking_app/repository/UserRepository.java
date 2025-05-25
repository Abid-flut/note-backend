package com.notetakingapp.note_taking_app.repository;

import com.notetakingapp.note_taking_app.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
  boolean existsByEmail(String email);
  Optional<AppUser> findByEmail(String email);
}
