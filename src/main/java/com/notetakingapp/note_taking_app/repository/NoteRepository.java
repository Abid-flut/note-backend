package com.notetakingapp.note_taking_app.repository;

import com.notetakingapp.note_taking_app.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
