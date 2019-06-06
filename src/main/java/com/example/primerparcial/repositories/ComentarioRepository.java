package com.example.primerparcial.repositories;

import com.example.primerparcial.models.Comentario;
import com.example.primerparcial.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario,Integer> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM Comentario where (timestampdiff(day,Fecha , now())) > 5;", nativeQuery = true) //en 5 minutos se borra
    void deleteComentarios();
}
