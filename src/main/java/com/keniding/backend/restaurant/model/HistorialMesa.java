package com.keniding.backend.restaurant.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_mesas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistorialMesa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mesa_id", nullable = false)
    private Mesa mesa;

    @Column(nullable = false)
    private String accion;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Mesa.EstadoMesa estadoAnterior;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Mesa.EstadoMesa estadoNuevo;

    @Column(nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "usuario_id")
    private Long usuarioId;

    @Column(name = "usuario_nombre")
    private String usuarioNombre;

    private String observaciones;

    @PrePersist
    public void prePersist() {
        this.fechaHora = LocalDateTime.now();
    }
}
