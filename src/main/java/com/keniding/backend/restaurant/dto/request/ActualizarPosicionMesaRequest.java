package com.keniding.backend.restaurant.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActualizarPosicionMesaRequest {
    private Long mesaId;
    private Long zonaId;
    private Integer posicionX;
    private Integer posicionY;
}
