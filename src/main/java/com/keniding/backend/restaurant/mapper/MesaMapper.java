package com.keniding.backend.restaurant.mapper;

import com.keniding.backend.restaurant.dto.MesaDTO;
import com.keniding.backend.restaurant.model.Mesa;
import org.mapstruct.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MesaMapper {

    @Mapping(target = "estado", expression = "java(mesa.getEstado().name().toLowerCase())")
    @Mapping(target = "ubicacion", expression = "java(mesa.getUbicacion().name().toLowerCase())")
    @Mapping(target = "forma", expression = "java(mesa.getForma().name().toLowerCase())")
    @Mapping(target = "tiempo", expression = "java(calcularTiempo(mesa))")
    @Mapping(target = "posicion", expression = "java(new MesaDTO.PosicionDTO(mesa.getPosicionX(), mesa.getPosicionY()))")
    @Mapping(target = "zona", expression = "java(mesa.getZona() != null ? mesa.getZona().getCodigo() : null)")
    MesaDTO toDto(Mesa mesa);

    @Mapping(target = "estado", expression = "java(stringToEstadoMesa(dto.getEstado()))")
    @Mapping(target = "ubicacion", expression = "java(stringToUbicacionMesa(dto.getUbicacion()))")
    @Mapping(target = "forma", expression = "java(stringToFormaMesa(dto.getForma()))")
    @Mapping(target = "posicionX", expression = "java(dto.getPosicion() != null ? dto.getPosicion().getX() : null)")
    @Mapping(target = "posicionY", expression = "java(dto.getPosicion() != null ? dto.getPosicion().getY() : null)")
    @Mapping(target = "zona", ignore = true)
    Mesa toEntity(MesaDTO dto);

    default String calcularTiempo(Mesa mesa) {
        if (mesa.getEstado() == Mesa.EstadoMesa.OCUPADA && mesa.getHoraInicio() != null) {
            Duration duracion = Duration.between(mesa.getHoraInicio(), LocalDateTime.now());
            long minutos = duracion.toMinutes();
            long segundos = duracion.minusMinutes(minutos).getSeconds();
            return String.format("%02d:%02d", minutos, segundos);
        } else if (mesa.getEstado() == Mesa.EstadoMesa.RESERVADA && mesa.getHoraInicio() != null) {
            return mesa.getHoraInicio().format(DateTimeFormatter.ofPattern("HH:mm"));
        }
        return null;
    }

    default Mesa.EstadoMesa stringToEstadoMesa(String estado) {
        if (estado == null) return null;
        try {
            return Mesa.EstadoMesa.valueOf(estado.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    default Mesa.UbicacionMesa stringToUbicacionMesa(String ubicacion) {
        if (ubicacion == null) return null;
        try {
            return Mesa.UbicacionMesa.valueOf(ubicacion.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    default Mesa.FormaMesa stringToFormaMesa(String forma) {
        if (forma == null) return null;
        try {
            return Mesa.FormaMesa.valueOf(forma.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}

