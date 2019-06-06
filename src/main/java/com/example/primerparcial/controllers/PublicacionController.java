package com.example.primerparcial.controllers;

import com.example.primerparcial.models.Comentario;
import com.example.primerparcial.models.Publicacion;
import com.example.primerparcial.repositories.ComentarioRepository;
import com.example.primerparcial.repositories.PublicacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Stream;

@RequestMapping("/publicacion")
@RestController
@EnableScheduling
public class PublicacionController {
    private static final String PERSON_NOT_FOUND = "Publicacion id not found: %s";

    @Autowired
    PublicacionRepository publicacionRepository;

    @Autowired
    ComentarioRepository comentarioRepository;

    // ------------------- add a publicacion-----------------------------------------
    @PostMapping("")
    public void addPublicacion(@RequestBody final Publicacion p) {//averiguar q hace esto
        publicacionRepository.save(p);
    }

    // ------------------- add a comentario to publicacion-----------------------------------------
    @PostMapping("/{idUsuario}/{idPublicacion}")
    public void addComentario(@PathVariable final Integer idPublicacion, @PathVariable final Integer idComentario){
        Comentario p = comentarioRepository.findById(idPublicacion)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        Publicacion publicacion = getById(idPublicacion);
        p.setPublicacion(publicacion);
        publicacion.getComentarios().add(p);
        comentarioRepository.save(p);
        publicacionRepository.save(publicacion);
    }

    // ------------------- get a publicacion-----------------------------------------

    @GetMapping("")
    public List<Publicacion> getAll() {
        return publicacionRepository.findAll();
    }

    @GetMapping("/{id}")
    public Publicacion getById(@PathVariable final Integer id){
        return publicacionRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, String.format(PERSON_NOT_FOUND,id))); //ACA es mejor usar el @responsebody?
    }

    // ------------------- Scheduled publicacion-----------------------------------------


    @Scheduled(fixedRate = ("{$nValue}")) // no se pudo remplazar por long pa que ande
    private void deleteComentarios(){
        //voteRepository.deleteAll();
        comentarioRepository.deleteComentarios();
    }

}
