package com.example.primerparcial.repositories;

import com.example.primerparcial.models.Publicacion;
import com.example.primerparcial.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion,Integer> {
}
