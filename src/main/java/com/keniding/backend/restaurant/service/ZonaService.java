package com.keniding.backend.restaurant.service;

import com.keniding.backend.exception.ResourceNotFoundException;
import com.keniding.backend.restaurant.dto.request.CrearZonaRequest;
import com.keniding.backend.restaurant.dto.response.ZonaResponse;
import com.keniding.backend.restaurant.mapper.RestaurantMapper;
import com.keniding.backend.restaurant.model.Zona;
import com.keniding.backend.restaurant.repository.ZonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ZonaService {

    private final ZonaRepository zonaRepository;
    private final RestaurantMapper mapper;

    public List<ZonaResponse> obtenerTodasLasZonas() {
        return zonaRepository.findAll()
                .stream()
                .map(mapper::toZonaResponse)
                .collect(Collectors.toList());
    }

    public ZonaResponse obtenerZonaPorId(Long id) {
        Zona zona = zonaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zona no encontrada con ID: " + id));
        return mapper.toZonaResponse(zona);
    }

    @Transactional
    public ZonaResponse crearZona(CrearZonaRequest request) {
        if (zonaRepository.findByCodigo(request.getCodigo()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una zona con el código: " + request.getCodigo());
        }

        Zona zona = Zona.builder()
                .codigo(request.getCodigo())
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .posicionX(request.getX())
                .posicionY(request.getY())
                .ancho(request.getAncho())
                .alto(request.getAlto())
                .color(request.getColor())
                .build();

        Zona zonaGuardada = zonaRepository.save(zona);
        return mapper.toZonaResponse(zonaGuardada);
    }

    @Transactional
    public ZonaResponse actualizarZona(Long id, CrearZonaRequest request) {
        Zona zona = zonaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zona no encontrada con ID: " + id));

        if (!zona.getCodigo().equals(request.getCodigo()) &&
                zonaRepository.findByCodigo(request.getCodigo()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una zona con el código: " + request.getCodigo());
        }

        zona.setCodigo(request.getCodigo());
        zona.setNombre(request.getNombre());
        zona.setDescripcion(request.getDescripcion());
        zona.setPosicionX(request.getX());
        zona.setPosicionY(request.getY());
        zona.setAncho(request.getAncho());
        zona.setAlto(request.getAlto());
        zona.setColor(request.getColor());

        Zona zonaActualizada = zonaRepository.save(zona);
        return mapper.toZonaResponse(zonaActualizada);
    }

    @Transactional
    public void eliminarZona(Long id) {
        Zona zona = zonaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zona no encontrada con ID: " + id));

        if (zona.getMesas() != null && !zona.getMesas().isEmpty()) {
            throw new IllegalStateException("No se puede eliminar la zona porque tiene mesas asociadas");
        }

        zonaRepository.delete(zona);
    }
}
