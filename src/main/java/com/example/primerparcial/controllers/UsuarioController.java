package com.example.primerparcial.controllers;

import com.example.primerparcial.models.Publicacion;
import com.example.primerparcial.models.Usuario;
import com.example.primerparcial.models.UsuarioDto;
import com.example.primerparcial.repositories.PublicacionRepository;
import com.example.primerparcial.repositories.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.stream.Collectors;

import static sun.audio.AudioPlayer.player;

@RequestMapping("/usuario")
@RestController
public class UsuarioController {
    private static final String PERSON_NOT_FOUND = "User id not found: %s";

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    PublicacionRepository publicacionRepository;

    ModelMapper modelMapper = new ModelMapper();

    // ------------------- add a usuario-----------------------------------------
    @PostMapping("")
    public void addPublicacion(@RequestBody final Usuario p) {//averiguar q hace esto
        usuarioRepository.save(p);
    }

    // ------------------- add a publicacion-----------------------------------------
    @PostMapping("/{idUsuario}/{idPublicacion}")
    public void addPublicacion(@PathVariable final Integer idUsuario, @PathVariable final Integer idPublicacion){
        Publicacion p = publicacionRepository.findById(idPublicacion)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, String.format(PERSON_NOT_FOUND,idUsuario)));

        p.setUsuario(usuario);
        usuario.getPublicaciones().add(p);
        publicacionRepository.save(p);
        usuarioRepository.save(usuario);
    }

    // ------------------- get a usuario-----------------------------------------

    @GetMapping("")
    public List<UsuarioDto> getAll() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(player -> convertToDto(player))
                .collect(Collectors.toList());
    }

    private UsuarioDto convertToDto(Usuario usuario) {
        UsuarioDto usuarioDto = modelMapper.map(usuario, UsuarioDto.class);
        return usuarioDto;
    }

    @GetMapping("/{id}")
    public UsuarioDto getById(@PathVariable final Integer id){
        return convertToDto(usuarioRepository
                .findById(id).orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, String.format(PERSON_NOT_FOUND,id))));
        // return playerRepository.findById(id)
        //       .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, String.format(PERSON_NOT_FOUND,id)));
    }

    // ------------------- Update a usuario -----------------------------------------

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateUsuario(@PathVariable("id") Integer id, @RequestBody Usuario usuario) {
        Usuario currentUsuario = usuarioRepository.findById(id)
              .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, String.format(PERSON_NOT_FOUND,id)));

        if (currentUsuario == null) {
            return new ResponseEntity<>("Unable to upate. Usuario with id " + id + " not found.",
                    HttpStatus.NOT_FOUND);
        }

        currentUsuario.setNombre(usuario.getNombre());
        currentUsuario.setApellido(usuario.getApellido());
        currentUsuario.setBrowser(usuario.getBrowser());
        usuarioRepository.save(currentUsuario);
        return new ResponseEntity<Usuario>(currentUsuario, HttpStatus.NO_CONTENT);
    }

    // ------------------- Delete a usuario-----------------------------------------

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUsuario(@PathVariable("id") Integer id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, String.format(PERSON_NOT_FOUND,id)));

        if (usuario == null) {
            return new ResponseEntity<>("Unable to delete. Usuario with id " + id + " not found.",
                    HttpStatus.NOT_FOUND);
        }
        usuarioRepository.deleteById(id);
        return new ResponseEntity<Usuario>(HttpStatus.NO_CONTENT);
    }

    // ------------------- Delete All usuarios-----------------------------

    @DeleteMapping(value = "")
    public void deleteAllUsuarios() {
        usuarioRepository.deleteAll();
    }
}
