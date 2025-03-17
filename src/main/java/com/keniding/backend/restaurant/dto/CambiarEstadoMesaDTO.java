package com.keniding.backend.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CambiarEstadoMesaDTO {
    private Long mesaId;
    private String nuevoEstado;
    private String observaciones;
}

