package com.notetakingapp.note_taking_app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.notetakingapp.note_taking_app.controller.NoteController;
import com.notetakingapp.note_taking_app.model.Note;
import com.notetakingapp.note_taking_app.service.NoteService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class NoteControllerTest {

    @Mock
    private NoteService service;

    @InjectMocks
    private NoteController controller;

    private MockMvc mvc;
    private ObjectMapper json = new ObjectMapper();

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getAll_returnsOk() throws Exception {
        when(service.findAll()).thenReturn(List.of(new Note()));

        mvc.perform(get("/api/notes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(service).findAll();
    }

    @Test
    void getOne_found() throws Exception {
        Note n = new Note(); n.setTitle("h"); n.setBody("b");
        when(service.findById(1L)).thenReturn(n);

        mvc.perform(get("/api/notes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("h"))
                .andExpect(jsonPath("$.body").value("b"));
    }

    @Test
    void getOne_notFound() throws Exception {
        when(service.findById(99L)).thenThrow(new EntityNotFoundException());

        mvc.perform(get("/api/notes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_returnsCreated() throws Exception {
        Note in = new Note(); in.setTitle("t"); in.setBody("b");
        when(service.create(any())).thenReturn(in);

        mvc.perform(post("/api/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(in)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("t"));
    }

    @Test
    void update_found() throws Exception {
        Note in = new Note(); in.setTitle("X");
        when(service.update(eq(1L), any())).thenReturn(in);

        mvc.perform(put("/api/notes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(in)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("X"));
    }

    @Test
    void update_notFound() throws Exception {
        when(service.update(eq(2L), any()))
                .thenThrow(new EntityNotFoundException());

        mvc.perform(put("/api/notes/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.writeValueAsString(new Note())))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_found() throws Exception {
        mvc.perform(delete("/api/notes/3"))
                .andExpect(status().isNoContent());

        verify(service).delete(3L);
    }

    @Test
    void delete_notFound() throws Exception {
        doThrow(new EntityNotFoundException())
                .when(service).delete(5L);

        mvc.perform(delete("/api/notes/5"))
                .andExpect(status().isNotFound());
    }
}
