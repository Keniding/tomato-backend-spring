package com.keniding.backend.restaurant.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearMesaDTO {
    private String numero;
    private String estado;
    private Integer capacidad;
    private String ubicacion;
    private String forma;
    private Long zonaId;
    private Integer posicionX;
    private Integer posicionY;
}
