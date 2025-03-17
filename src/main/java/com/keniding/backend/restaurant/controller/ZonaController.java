package com.keniding.backend.restaurant.controller;

import com.keniding.backend.restaurant.dto.request.CrearZonaRequest;
import com.keniding.backend.restaurant.dto.response.ApiResponse;
import com.keniding.backend.restaurant.dto.response.MesaResponse;
import com.keniding.backend.restaurant.dto.response.ZonaResponse;
import com.keniding.backend.restaurant.service.MesaService;
import com.keniding.backend.restaurant.service.ZonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zonas")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ZonaController {

    private final ZonaService zonaService;
    private final MesaService mesaService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ZonaResponse>>> obtenerTodasLasZonas() {
        List<ZonaResponse> zonas = zonaService.obtenerTodasLasZonas();
        return ResponseEntity.ok(new ApiResponse<>(true, "Zonas obtenidas con éxito", zonas));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ZonaResponse>> obtenerZonaPorId(@PathVariable Long id) {
        ZonaResponse zona = zonaService.obtenerZonaPorId(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Zona obtenida con éxito", zona));
    }

    @GetMapping("/{codigoZona}/mesas")
    public ResponseEntity<ApiResponse<List<MesaResponse>>> obtenerMesasPorZona(@PathVariable String codigoZona) {
        List<MesaResponse> mesas = mesaService.obtenerMesasPorZona(codigoZona);
        return ResponseEntity.ok(new ApiResponse<>(true, "Mesas de la zona obtenidas con éxito", mesas));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ZonaResponse>> crearZona(@RequestBody CrearZonaRequest request) {
        ZonaResponse zona = zonaService.crearZona(request);
        return new ResponseEntity<>(new ApiResponse<>(true, "Zona creada con éxito", zona), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ZonaResponse>> actualizarZona(@PathVariable Long id, @RequestBody CrearZonaRequest request) {
        ZonaResponse zona = zonaService.actualizarZona(id, request);
        return ResponseEntity.ok(new ApiResponse<>(true, "Zona actualizada con éxito", zona));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarZona(@PathVariable Long id) {
        zonaService.eliminarZona(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Zona eliminada con éxito", null));
    }
}
