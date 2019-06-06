package com.example.primerparcial.controllers;

import com.example.primerparcial.models.Comentario;
import com.example.primerparcial.repositories.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/comentario")
@RestController
public class ComentarioController {
    private static final String PERSON_NOT_FOUND = "Comentario id not found: %s";

    @Autowired
    ComentarioRepository comentarioRepository;

    // ------------------- Add a comentario -----------------------------------------

    @PostMapping("")
    public void addComentario(@RequestBody final Comentario p) {
        comentarioRepository.save(p);
    }

    // ------------------- Get a comentario -----------------------------------------

    @GetMapping("")
    public List<Comentario> getAll() {
        return comentarioRepository.findAll();
    }

    @GetMapping("/{id}")
    public Comentario getById(@PathVariable final Integer id){
         return comentarioRepository.findById(id)
              .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, String.format(PERSON_NOT_FOUND,id))); //ACA es mejor usar el @responsebody?
    }

    // ------------------- Delete a comentario -----------------------------------------

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable final Integer id){
        Comentario comentario = comentarioRepository.findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, String.format(PERSON_NOT_FOUND,id)));
        if (comentario == null) {
            return new ResponseEntity<>("Unable to delete Comentario, id " + id + " not found.", //revisar esto dudas con el response entity
                    HttpStatus.NOT_FOUND);
        }
        comentarioRepository.deleteById(id);
        return new ResponseEntity<Comentario>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "")
    public void deleteAllComentarios() {
        comentarioRepository.deleteAll();
    }

}
