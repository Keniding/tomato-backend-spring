package com.keniding.backend.restaurant.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumenMesasResponse {
    private long total;
    private long libres;
    private long ocupadas;
    private long reservadas;
    private long pendientes;
}
