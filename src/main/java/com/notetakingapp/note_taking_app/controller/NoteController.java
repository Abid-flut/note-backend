// src/main/java/com/notetakingapp/note_taking_app/controller/NoteController.java
package com.notetakingapp.note_taking_app.controller;

import com.notetakingapp.note_taking_app.model.Note;
import com.notetakingapp.note_taking_app.service.NoteService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService service;

    public NoteController(NoteService service) {
        this.service = service;
    }

    @GetMapping
    public List<Note> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Note> getOne(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.findById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Note> create(@RequestBody Note n) {
        Note saved = service.create(n);
        return ResponseEntity
                .created(URI.create("/api/notes/" + saved.getId()))
                .body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Note> update(@PathVariable Long id, @RequestBody Note n) {
        try {
            return ResponseEntity.ok(service.update(id, n));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
