package com.keniding.backend.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarPosicionMesaDTO {
    private Long mesaId;
    private Long zonaId;
    private Integer posicionX;
    private Integer posicionY;
}
