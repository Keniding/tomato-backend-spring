package com.keniding.backend.restaurant.service;

import com.keniding.backend.exception.ResourceNotFoundException;
import com.keniding.backend.restaurant.dto.request.ActualizarPosicionMesaRequest;
import com.keniding.backend.restaurant.dto.request.CambiarEstadoMesaRequest;
import com.keniding.backend.restaurant.dto.request.CrearMesaRequest;
import com.keniding.backend.restaurant.dto.response.MesaResponse;
import com.keniding.backend.restaurant.dto.response.ResumenMesasResponse;
import com.keniding.backend.restaurant.mapper.RestaurantMapper;
import com.keniding.backend.restaurant.model.HistorialMesa;
import com.keniding.backend.restaurant.model.Mesa;
import com.keniding.backend.restaurant.model.Zona;
import com.keniding.backend.restaurant.repository.HistorialMesaRepository;
import com.keniding.backend.restaurant.repository.MesaRepository;
import com.keniding.backend.restaurant.repository.ZonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MesaService {

    private final MesaRepository mesaRepository;
    private final ZonaRepository zonaRepository;
    private final HistorialMesaRepository historialRepository;
    private final RestaurantMapper mapper;

    public List<MesaResponse> obtenerTodasLasMesas() {
        return mesaRepository.findAll()
                .stream()
                .map(mapper::toMesaResponse)
                .collect(Collectors.toList());
    }

    public MesaResponse obtenerMesaPorId(Long id) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada con ID: " + id));
        return mapper.toMesaResponse(mesa);
    }

    public List<MesaResponse> obtenerMesasPorEstado(String estado) {
        Mesa.EstadoMesa estadoMesa = mapper.stringToEstadoMesa(estado);
        if (estadoMesa == null) {
            throw new IllegalArgumentException("Estado de mesa no válido: " + estado);
        }

        return mesaRepository.findByEstado(estadoMesa)
                .stream()
                .map(mapper::toMesaResponse)
                .collect(Collectors.toList());
    }

    public List<MesaResponse> obtenerMesasPorUbicacion(String ubicacion) {
        Mesa.UbicacionMesa ubicacionMesa = mapper.stringToUbicacionMesa(ubicacion);
        if (ubicacionMesa == null) {
            throw new IllegalArgumentException("Ubicación no válida: " + ubicacion);
        }

        return mesaRepository.findByUbicacion(ubicacionMesa)
                .stream()
                .map(mapper::toMesaResponse)
                .collect(Collectors.toList());
    }

    public List<MesaResponse> obtenerMesasPorZona(String codigoZona) {
        Zona zona = zonaRepository.findByCodigo(codigoZona)
                .orElseThrow(() -> new ResourceNotFoundException("Zona no encontrada: " + codigoZona));

        return mesaRepository.findByZona(zona)
                .stream()
                .map(mapper::toMesaResponse)
                .collect(Collectors.toList());
    }

    public List<MesaResponse> obtenerMesasFiltradas(String estado, Integer capacidad) {
        Mesa.EstadoMesa estadoMesa = estado != null && !estado.equals("todos")
                ? mapper.stringToEstadoMesa(estado)
                : null;

        Integer capacidadFiltro = capacidad != null && capacidad > 0 ? capacidad : null;

        return mesaRepository.findByFilters(estadoMesa, capacidadFiltro)
                .stream()
                .map(mapper::toMesaResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public MesaResponse crearMesa(CrearMesaRequest request) {
        // Validar que el número de mesa no exista
        if (mesaRepository.findByNumero(request.getNumero()).isPresent()) {
            throw new IllegalArgumentException("Ya existe una mesa con el número: " + request.getNumero());
        }

        // Obtener la zona
        Zona zona = null;
        if (request.getZonaId() != null) {
            zona = zonaRepository.findById(request.getZonaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Zona no encontrada con ID: " + request.getZonaId()));
        }

        Mesa mesa = Mesa.builder()
                .numero(request.getNumero())
                .estado(mapper.stringToEstadoMesa(request.getEstado()))
                .capacidad(request.getCapacidad())
                .ubicacion(mapper.stringToUbicacionMesa(request.getUbicacion()))
                .forma(mapper.stringToFormaMesa(request.getForma()))
                .zona(zona)
                .posicionX(request.getPosicionX())
                .posicionY(request.getPosicionY())
                .build();

        if (mesa.getEstado() == Mesa.EstadoMesa.OCUPADA) {
            mesa.setHoraInicio(LocalDateTime.now());
        } else if (mesa.getEstado() == Mesa.EstadoMesa.RESERVADA) {
            mesa.setHoraInicio(LocalDateTime.now().plusHours(1)); // Por defecto 1 hora después
        }

        Mesa mesaGuardada = mesaRepository.save(mesa);

        // Registrar en historial
        registrarHistorial(mesaGuardada, "CREACIÓN", null, mesaGuardada.getEstado(), null);

        return mapper.toMesaResponse(mesaGuardada);
    }

    @Transactional
    public MesaResponse cambiarEstadoMesa(CambiarEstadoMesaRequest request) {
        Mesa mesa = mesaRepository.findById(request.getMesaId())
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada con ID: " + request.getMesaId()));

        Mesa.EstadoMesa estadoAnterior = mesa.getEstado();
        Mesa.EstadoMesa nuevoEstado = mapper.stringToEstadoMesa(request.getNuevoEstado());

        if (nuevoEstado == null) {
            throw new IllegalArgumentException("Estado de mesa no válido: " + request.getNuevoEstado());
        }

        mesa.setEstado(nuevoEstado);

        if (nuevoEstado == Mesa.EstadoMesa.OCUPADA) {
            mesa.setHoraInicio(LocalDateTime.now());
        } else if (nuevoEstado == Mesa.EstadoMesa.LIBRE) {
            mesa.setHoraInicio(null);
        }

        Mesa mesaActualizada = mesaRepository.save(mesa);

        registrarHistorial(mesaActualizada, "CAMBIO_ESTADO", estadoAnterior, nuevoEstado, request.getObservaciones());

        return mapper.toMesaResponse(mesaActualizada);
    }

    @Transactional
    public MesaResponse actualizarPosicionMesa(ActualizarPosicionMesaRequest request) {
        Mesa mesa = mesaRepository.findById(request.getMesaId())
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada con ID: " + request.getMesaId()));

        Zona nuevaZona = null;
        if (request.getZonaId() != null) {
            nuevaZona = zonaRepository.findById(request.getZonaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Zona no encontrada con ID: " + request.getZonaId()));
        }

        mesa.setZona(nuevaZona);
        mesa.setPosicionX(request.getPosicionX());
        mesa.setPosicionY(request.getPosicionY());

        Mesa mesaActualizada = mesaRepository.save(mesa);
        return mapper.toMesaResponse(mesaActualizada);
    }

    @Transactional
    public void eliminarMesa(Long id) {
        Mesa mesa = mesaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada con ID: " + id));

        // Registrar en historial antes de eliminar
        registrarHistorial(mesa, "ELIMINACIÓN", mesa.getEstado(), null, "Mesa eliminada del sistema");

        mesaRepository.delete(mesa);
    }

    private void registrarHistorial(Mesa mesa, String accion, Mesa.EstadoMesa estadoAnterior,
                                    Mesa.EstadoMesa estadoNuevo, String observaciones) {
        HistorialMesa historial = HistorialMesa.builder()
                .mesa(mesa)
                .accion(accion)
                .estadoAnterior(estadoAnterior != null ? estadoAnterior : Mesa.EstadoMesa.LIBRE)
                .estadoNuevo(estadoNuevo != null ? estadoNuevo : Mesa.EstadoMesa.LIBRE)
                .observaciones(observaciones)
                .usuarioId(1L)
                .usuarioNombre("Sistema")
                .build();

        historialRepository.save(historial);
    }

    public List<HistorialMesa> obtenerHistorialMesa(Long mesaId) {
        Mesa mesa = mesaRepository.findById(mesaId)
                .orElseThrow(() -> new ResourceNotFoundException("Mesa no encontrada con ID: " + mesaId));

        return historialRepository.findByMesaOrderByFechaHoraDesc(mesa);
    }

    public ResumenMesasResponse obtenerResumenMesas() {
        long totalLibres = mesaRepository.countByEstado(Mesa.EstadoMesa.LIBRE);
        long totalOcupadas = mesaRepository.countByEstado(Mesa.EstadoMesa.OCUPADA);
        long totalReservadas = mesaRepository.countByEstado(Mesa.EstadoMesa.RESERVADA);
        long totalPendientes = mesaRepository.countByEstado(Mesa.EstadoMesa.PENDIENTE);
        long total = totalLibres + totalOcupadas + totalReservadas + totalPendientes;

        return ResumenMesasResponse.builder()
                .total(total)
                .libres(totalLibres)
                .ocupadas(totalOcupadas)
                .reservadas(totalReservadas)
                .pendientes(totalPendientes)
                .build();
    }
}
