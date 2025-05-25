package com.notetakingapp.note_taking_app.service;

import com.notetakingapp.note_taking_app.model.Note;
import com.notetakingapp.note_taking_app.repository.NoteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NoteServiceTest {

    @Mock
    private NoteRepository repo;

    @InjectMocks
    private NoteService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_returnsAllNotes() {
        List<Note> dummy = List.of(new Note(), new Note());
        when(repo.findAll()).thenReturn(dummy);

        List<Note> result = service.findAll();

        assertEquals(2, result.size());
        verify(repo).findAll();
    }

    @Test
    void findById_found() {
        Note n = new Note(); n.setTitle("T"); n.setBody("B");
        when(repo.findById(1L)).thenReturn(Optional.of(n));

        Note result = service.findById(1L);

        assertEquals("T", result.getTitle());
        assertEquals("B", result.getBody());
        verify(repo).findById(1L);
    }

    @Test
    void findById_notFound_throws() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.findById(99L));
        verify(repo).findById(99L);
    }

    @Test
    void create_savesAndReturns() {
        Note in = new Note(); in.setTitle("X"); in.setBody("Y");
        when(repo.save(in)).thenReturn(in);

        Note result = service.create(in);

        assertSame(in, result);
        verify(repo).save(in);
    }

    @Test
    void update_found_updatesAndReturns() {
        Note existing = new Note(); existing.setTitle("A"); existing.setBody("B");
        Note updates  = new Note(); updates.setTitle("C"); updates.setBody("D");
        when(repo.findById(5L)).thenReturn(Optional.of(existing));
        when(repo.save(existing)).thenReturn(existing);

        Note result = service.update(5L, updates);

        assertEquals("C", existing.getTitle());
        assertEquals("D", existing.getBody());
        assertSame(existing, result);
        verify(repo).findById(5L);
        verify(repo).save(existing);
    }

    @Test
    void update_notFound_throws() {
        when(repo.findById(6L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.update(6L, new Note()));
        verify(repo).findById(6L);
    }

    @Test
    void delete_existing_deletes() {
        Note n = new Note();
        when(repo.findById(7L)).thenReturn(Optional.of(n));

        service.delete(7L);

        verify(repo).findById(7L);
        verify(repo).delete(n);
    }

    @Test
    void delete_notFound_throws() {
        when(repo.findById(8L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> service.delete(8L));
        verify(repo).findById(8L);
        verify(repo, never()).delete(any());
    }
}
