// src/main/java/com/notetakingapp/note_taking_app/service/NoteService.java
package com.notetakingapp.note_taking_app.service;

import com.notetakingapp.note_taking_app.model.Note;
import com.notetakingapp.note_taking_app.repository.NoteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NoteRepository repo;

    public NoteService(NoteRepository repo) {
        this.repo = repo;
    }

    public List<Note> findAll() {
        return repo.findAll();
    }

    public Note findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Note not found: " + id));
    }

    public Note create(Note note) {
        return repo.save(note);
    }

    public Note update(Long id, Note incoming) {
        Note existing = findById(id);
        existing.setTitle(incoming.getTitle());
        existing.setBody(incoming.getBody());
        return repo.save(existing);
    }

    public void delete(Long id) {
        Note existing = findById(id);
        repo.delete(existing);
    }
}
