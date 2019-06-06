package com.example.primerparcial.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.util.Objects.isNull;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String descripcion;
    private String fecha;
    private String owner;
    @PrePersist
    public void setTime() {
        if (isNull(this.getFecha())) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-mmm-yyyy");
            LocalDateTime localDateTime = LocalDateTime.now();
            this.fecha = localDateTime.format(formatter);
        }
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publicacion_id", referencedColumnName = "id")
    @JsonIgnore
    private Publicacion publicacion;

}
