package com.keniding.backend.restaurant.dto.response;

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
public class MesaResponse {
    private Long id;
    private String numero;
    private String estado;
    private Integer capacidad;
    private String tiempo;
    private String ubicacion;
    private String forma;
    private PosicionDTO posicion;
    private String zona;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PosicionDTO {
        private Integer x;
        private Integer y;
    }
}

