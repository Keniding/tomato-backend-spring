package com.keniding.backend.restaurant.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "mesas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Mesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String numero;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoMesa estado;

    @Column(nullable = false)
    private Integer capacidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UbicacionMesa ubicacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FormaMesa forma;

    @Column(name = "hora_inicio")
    private LocalDateTime horaInicio;

    @ManyToOne
    @JoinColumn(name = "zona_id")
    private Zona zona;

    @Column(name = "posicion_x")
    private Integer posicionX;

    @Column(name = "posicion_y")
    private Integer posicionY;

    @Column(name = "ultima_actualizacion")
    private LocalDateTime ultimaActualizacion;

    public enum EstadoMesa {
        LIBRE, OCUPADA, RESERVADA, PENDIENTE
    }

    public enum UbicacionMesa {
        INTERIOR, TERRAZA
    }

    public enum FormaMesa {
        REDONDA, CUADRADA, RECTANGULAR
    }

    @PrePersist
    @PreUpdate
    public void actualizarFecha() {
        this.ultimaActualizacion = LocalDateTime.now();
    }
}
