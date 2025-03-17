package com.keniding.backend.restaurant.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrearZonaRequest {
    private String codigo;
    private String nombre;
    private String descripcion;
    private Integer x;
    private Integer y;
    private Integer ancho;
    private Integer alto;
    private String color;
}
