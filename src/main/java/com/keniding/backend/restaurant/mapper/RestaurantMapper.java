package com.keniding.backend.restaurant.mapper;

import com.keniding.backend.restaurant.dto.response.MesaResponse;
import com.keniding.backend.restaurant.dto.response.ZonaResponse;
import com.keniding.backend.restaurant.model.Mesa;
import com.keniding.backend.restaurant.model.Zona;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class RestaurantMapper {

    public MesaResponse toMesaResponse(Mesa mesa) {
        if (mesa == null) return null;

        String tiempo = null;
        if (mesa.getEstado() == Mesa.EstadoMesa.OCUPADA && mesa.getHoraInicio() != null) {
            Duration duracion = Duration.between(mesa.getHoraInicio(), LocalDateTime.now());
            long minutos = duracion.toMinutes();
            long segundos = duracion.minusMinutes(minutos).getSeconds();
            tiempo = String.format("%02d:%02d", minutos, segundos);
        } else if (mesa.getEstado() == Mesa.EstadoMesa.RESERVADA && mesa.getHoraInicio() != null) {
            tiempo = mesa.getHoraInicio().format(DateTimeFormatter.ofPattern("HH:mm"));
        }

        return MesaResponse.builder()
                .id(mesa.getId())
                .numero(mesa.getNumero())
                .estado(mesa.getEstado().name().toLowerCase())
                .capacidad(mesa.getCapacidad())
                .tiempo(tiempo)
                .ubicacion(mesa.getUbicacion().name().toLowerCase())
                .forma(mesa.getForma().name().toLowerCase())
                .posicion(new MesaResponse.PosicionDTO(mesa.getPosicionX(), mesa.getPosicionY()))
                .zona(mesa.getZona() != null ? mesa.getZona().getCodigo() : null)
                .build();
    }

    public ZonaResponse toZonaResponse(Zona zona) {
        if (zona == null) return null;

        return ZonaResponse.builder()
                .id(zona.getId())
                .codigo(zona.getCodigo())
                .nombre(zona.getNombre())
                .descripcion(zona.getDescripcion())
                .x(zona.getPosicionX())
                .y(zona.getPosicionY())
                .ancho(zona.getAncho())
                .alto(zona.getAlto())
                .color(zona.getColor())
                .build();
    }

    public Mesa.EstadoMesa stringToEstadoMesa(String estado) {
        if (estado == null) return null;
        try {
            return Mesa.EstadoMesa.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Mesa.UbicacionMesa stringToUbicacionMesa(String ubicacion) {
        if (ubicacion == null) return null;
        try {
            return Mesa.UbicacionMesa.valueOf(ubicacion.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Mesa.FormaMesa stringToFormaMesa(String forma) {
        if (forma == null) return null;
        try {
            return Mesa.FormaMesa.valueOf(forma.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
