package com.keniding.backend.restaurant.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ZonaDTO {
    private Long id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private Integer x;
    private Integer y;
    private Integer ancho;
    private Integer alto;
    private String color;
}
